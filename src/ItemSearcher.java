/**
 * @author -
 * Email -
 * created for COSC120 Assignment 2
 * Add descriptor
 */

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemSearcher {
    // TODO - Load inventory try catch statements for parsing in data
    // TODO - Null pointer errors in getFilters
    // TODO - Null pointer error in getContact info
    // TODO - parameter testing to break the program
    private static final String filePath = "./inventory_v2.txt";
    private static final Icon icon = new ImageIcon("./the_greenie_geek.png");
    private static Inventory inventory;
    private static final String appName = "Greenie Geek";

    public static void main(String[] args) {
        // initialising an inventory object with the return value from the load inventory method
        inventory = loadInventory(filePath);
        // initialising a dream fruiting plant object with the return value from the getFilters method
        DreamFruitingPlant dreamFruitingPlant = getFilters();
        // Passing the user's dream fruiting plant into the method for processing results
        processSearchResults(dreamFruitingPlant);
        // exiting when finished
        System.exit(0);
    }

    /**
     * Opens a file which is our database of plants and proceeds to split and clean
     * the data, going through each attribute and assigning it to it's designated
     * variable, then placing all variables into a map as values with the enum
     * "Filters" as the key. Finally creating a dream fruiting plant object, passing
     * it the map and then creating a fruit plant object, passing it the dream fruit
     * plant and key plant identifiers and adding it all to the set of all plants
     * @param filePath a string containing the file path to our database file
     * @return inventory object from the Inventory class, used to control the class methods and database
     */
    private static Inventory loadInventory(String filePath) {
        // Create an inventory object
        Inventory inventory = new Inventory();
        // Creating our path object
        Path path = Path.of(filePath);
        // Creating a list to load our database into
        List<String> fileContents = null;
        // Try to read all lines from the database and exit if there is a problem
        try {
            fileContents = Files.readAllLines(path);
        } catch (IOException io) {
            System.out.println("File could not be found");
            System.exit(0);
        }

        // Iterate through each line of the database, sorting and cleaning the data
        for (int i = 1; i < fileContents.size(); i++) {
            // Split the data up, remove the unwanted characters
            String[] info = fileContents.get(i).split("\\[");
            String[] singularInfo = info[0].split(",");

            // Create subset strings from the above splits that will form collections
            String pollinatorsRaw = info[1].replace("],", "");
            String pricesRaw = info[2].replace("],", "");
            String potSizesRaw = info[3].replace("],", "");

            // parse the description
            String description = info[4].replace("]", "");

            // Parse in the category of plant from the database
            String category = singularInfo[0].toUpperCase().replace(" ", "_");
            try {
                category = CategoryOfFruit.valueOf(category).toString();
            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Plant not supported by enum CategoryOfPlany" + (i + 1) + ". " +
                        "Terminating. \nError message: " + e.getMessage());
            }

            // Parse the product name, id, type, dwarf status and training system
            String productName = singularInfo[1];
            String productCode = singularInfo[2];
            String type = singularInfo[3].trim();
            String dwarf = singularInfo[4].trim();
            String trainingSystem = singularInfo[5];

            // Initialise a list to hold all pollinators
            List<String> pollinatorList = new ArrayList<>();
            // Create a string array for the pollinators by splitting on the comma, if the pollinator String isn't empty
            if (!pollinatorsRaw.isEmpty()) {
                String[] pollinatorOptions = pollinatorsRaw.split(",");
                // For each string in the array, clean the data and store it in the pollinator List
                for (String p : pollinatorOptions) {
                    pollinatorList.add(p.replace("[", "").replace("]", "").trim());
                }
            }

            // Initialising a map to hold our pot sizes to price mapping, using a LinkedHashmap to preserve order for pricing
            Map<Integer,Float> potSizeToPrice = new LinkedHashMap<>();
            // If the strings not empty, split both the pot sizes data and the prices data to two arrays
            if(potSizesRaw.length()>0) {
                String[] optionsPotSizes = potSizesRaw.split(",");
                String[] optionsPrices = pricesRaw.split(",");
                // For each element in the prices array, parse the price as a float and the pot size as an int
                for (int j=0;j<optionsPrices.length;j++){
                    int potSize = 0;
                    float price = 0f;
                    try {
                        potSize = Integer.parseInt(optionsPotSizes[j].trim());
                        price = Float.parseFloat(optionsPrices[j].trim());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error in file. Pot size/price option could not be parsed for item on line "
                                + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                        System.exit(0);
                    }
                    // Use the pot size as the key and the prices as the values in the map
                    potSizeToPrice.put(potSize,price);
                }
            }

            // Initialise a list for the pot sizes
            List<Integer> potSizeList = new ArrayList<>();
            // If the string isn't empty, split it on the commas, clean the data and parse the data in as an int into our list
            if(potSizesRaw.length()>0) {
                String[] tempPotSize = potSizesRaw.split(",");
                for (String potSizeOption : tempPotSize) {
                    int potSize = Integer.parseInt(potSizeOption.trim());
                    potSizeList.add(potSize);
                }
            }

            // Create a map to hold all of our databases features via keys from an enum
            Map<Filters, Object> filterMap = new LinkedHashMap<>();
            // Put all of our database features into the map
            filterMap.put(Filters.CATEGORY, category);
            filterMap.put(Filters.TYPE, type);
            filterMap.put(Filters.DWARF, dwarf);
            filterMap.put(Filters.TRAINING_SYSTEM, trainingSystem);
            filterMap.put(Filters.POLLINATORS, pollinatorList);
            filterMap.put(Filters.POT_SIZE, potSizeList);
            filterMap.put(Filters.POT_SIZE_PRICE, potSizeToPrice);

            // Create a dreamFruitingPlant object passing it our map full of goodies
            DreamFruitingPlant dreamFruitingPlant = new DreamFruitingPlant(filterMap);
            // Create a fruiting plant passing in the dream fruiting plant as well as the other pieces of data collected
            FruitingPlant fruitingPlant = new FruitingPlant(productName, productCode, description, dreamFruitingPlant);
            // Add the fruiting plant to the inventory set
            inventory.addItem(fruitingPlant);

        }
        return inventory;
    }

    /**
     * Asks the user a series of questions about their dream fruiting plant, parsing all of their answers into
     * a map that will represent their dream plant
     * @return a dreamFruitingPlant containing a map of all the users choices and two variables for prices
     */
    private static DreamFruitingPlant getFilters() {

        // Creating a map to hold all the users choices, using Linked to preserve order
        Map<Filters, Object> usersDreamPlant = new LinkedHashMap<>();

        // First asking the user what type of plant they are looking to buy
        CategoryOfFruit category = (CategoryOfFruit) JOptionPane.showInputDialog(null, "Please select the type " +
                "of plant you'd like to purchase", appName, JOptionPane.QUESTION_MESSAGE, icon, CategoryOfFruit.values(), CategoryOfFruit.CITRUS);
        // adding their selection to the map
        usersDreamPlant.put(Filters.CATEGORY, category.toString());

        // Getting the type of fruit the user wants and adding it to the map if their choice is not "NA"
        String type = (String) JOptionPane.showInputDialog(null, "Please select your preferred " +
                "type", appName, JOptionPane.QUESTION_MESSAGE, icon, inventory.getAllTypes(category.toString()).toArray(), null);
        if (type == null) System.exit(0);

        // Control structure for all types, if user wants a specific type, add it to the map
        if (!type.equalsIgnoreCase("SKIP (any will do)")) {
            usersDreamPlant.put(Filters.TYPE, type);
        } else {
            // If the user doesn't mind what type, add all types to the map
            List<String> allTypes = new ArrayList<>(inventory.getAllTypes(category.toString()));
            usersDreamPlant.put(Filters.TYPE, allTypes);
        }

        // Filtering questions for if the customer did not choose vine for their plant
        if (!(category==CategoryOfFruit.VINE)) {
            Dwarf dwarf = (Dwarf) JOptionPane.showInputDialog(null, "Would you like a dwarf plant?",
                    appName, JOptionPane.QUESTION_MESSAGE, icon, Dwarf.values(), Dwarf.YES);

            if (dwarf == null) System.exit(0);

            // Control structure for dwarf choice, if the user selects yes or no, just add that string
            if (!(dwarf==Dwarf.NA)) {
                usersDreamPlant.put(Filters.DWARF, dwarf.toString().toLowerCase());
            }
            // If the user doesn't care about the tree being dwarf, just add all options to the dreamFruitPlant
            else {
                // Declare a list to hold the options
                List<String> allDwarfs = new ArrayList<>();
                // Iterate through all values of the Dwarf enum adding each value to the list
                for (Dwarf d : Dwarf.values()){
                    allDwarfs.add(d.toString().toLowerCase());
                }
                // Put the collection into the map
                usersDreamPlant.put(Filters.DWARF, allDwarfs);
            }

            // if the category also wasn't citrus
            if (!(category==CategoryOfFruit.CITRUS)) {
                // Create a Set to store all pollinators, we are using a set to stop duplicates from appearing
                Set<String> listOfPollinators = new HashSet<>();

                // Create a loop to continue asking the customer to choose a pollinator until they skip or reply no to add another
                int decision = 0;
                while (decision == 0) {
                    // Obtaining the users choice of pollinator
                    String pollinator = (String) JOptionPane.showInputDialog(null, "Would you like to add any pollinators",
                            appName, JOptionPane.QUESTION_MESSAGE, icon, inventory.getAllPollinators(category.toString()).toArray(), null);

                    // Handling the null pointer error
                    if (pollinator == null) System.exit(0);

                    // if the customer selects a pollinator, add it to the list
                    if (!pollinator.equalsIgnoreCase("SKIP (any will do)")) {
                        listOfPollinators.add(pollinator);
                    } else {
                        // If the user doesn't mind what pollinator, add all pollinators to the map and break out of the loop
                        Set<String> allPollinators = new HashSet<>(inventory.getAllPollinators(category.toString()));
                        usersDreamPlant.put(Filters.POLLINATORS, allPollinators);
                        // clearing the list of Pollinators list in-case the user selected a pollinator and wanted to choose a
                        // second, then changed their mind on the second go around, prevents two sets getting added to the map.
                        listOfPollinators.clear();
                        break;
                    }
                    // Ask the customer if they would like to add another pollinator, then run through it all again
                    decision = JOptionPane.showConfirmDialog(null, "Would you like to add another pollinator",
                            appName, JOptionPane.YES_NO_OPTION);
                }
                // If they chose to add at least one pollinator, add it to the map
                if (!listOfPollinators.isEmpty()) {
                    usersDreamPlant.put(Filters.POLLINATORS, listOfPollinators);
                }
            }
        } else {
            // Only asking the customer what training system they would like if they chose vine as their category of plant
            String trainingSystem = (String) JOptionPane.showInputDialog(null, "What type of training system would you like for your vine?",
                    appName, JOptionPane.QUESTION_MESSAGE, icon, inventory.getAllTrellis(category.toString()).toArray(), null);

            // Handling the null pointer error
            if (trainingSystem == null) System.exit(0);

            // Control structure for all trellis, if user wants a specific trellis, add it to the map
            if (!type.equalsIgnoreCase("SKIP (any will do)")) {
                usersDreamPlant.put(Filters.TRAINING_SYSTEM, type);
            } else {
                // If the user doesn't mind what trellis, add all types to the map
                List<String> allTrellis = new ArrayList<>(inventory.getAllTrellis(category.toString()));
                usersDreamPlant.put(Filters.TRAINING_SYSTEM, allTrellis);
            }
        }

        // Initialise the pot size to below the threshold and continue to loop through asking the user to enter a size
        // they desired until it is within a possible range depending on what is in stock
        int potSize = 7;
        while (potSize < 8 || potSize > 17) {
            String userInput = (String) JOptionPane.showInputDialog(null, "Enter the size pot would" +
                    " you like as a number. Options range from 8, 10, 12, 14, and 16inch", appName, JOptionPane.QUESTION_MESSAGE, icon,
                    null, null);

            if (userInput == null) System.exit(0);

            try {
                // Try to parse the user input
                potSize = Integer.parseInt((userInput));
                // Control structure to help guide the user with potential solutions if they are having problems
                if (potSize < 7)
                    JOptionPane.showMessageDialog(null, "Pot size must be at least 8", appName, JOptionPane.ERROR_MESSAGE);
                if (potSize > 16)
                    JOptionPane.showMessageDialog(null, "Pot size must be at most 16", appName, JOptionPane.ERROR_MESSAGE);
                if (!(potSize % 2 == 0)) {
                    JOptionPane.showMessageDialog(null, "We only stock 8, 10, 12, 14, and 16 inch sizes", appName, JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.", appName, JOptionPane.ERROR_MESSAGE);
            }
        }
        // add the pot size choice to the map
        usersDreamPlant.put(Filters.POT_SIZE, potSize);

        // Call the price values function to get the customers minimum and maximum prices they want
        int[] price = priceValues();

        return new DreamFruitingPlant(usersDreamPlant, price[0], price[1]);
    }

    /**
     * Asks the customer what the minimium and maximum prices they want to pay for a plant, it then
     * assigns these two values to an int array
     * @return an int array containing the maximum and minimum prices the customer has selected to pay
     */
    private static int[] priceValues() {
        // Set the two variables
        int minPrice = -1, maxPrice = -1;
        // Continue to ask the user to enter a min price until they enter a value ≥ 0
        while (minPrice < 0) {
            String userInput = (String) JOptionPane.showInputDialog(null, "Enter min price range value:", appName, JOptionPane.QUESTION_MESSAGE, icon, null, null);
            // Handle null pointer error
            if (userInput == null) System.exit(0);
            // Try to parse the string as an int
            try {
                minPrice = Integer.parseInt(userInput);
                // Check the int doesn't meet our expectations
                if (minPrice < 0)
                    JOptionPane.showMessageDialog(null, "Price must be >= 0.", appName, JOptionPane.ERROR_MESSAGE);
                // Catch any non number inputs
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.", appName, JOptionPane.ERROR_MESSAGE);
            }
        }
        // Continue to ask the user to enter a max price until they enter a value > the min price
        while (maxPrice < minPrice) {
            String userInput = (String) JOptionPane.showInputDialog(null, "Enter max price range value:", appName, JOptionPane.QUESTION_MESSAGE, icon, null, null);
            // Handle null pointer error
            if (userInput == null) System.exit(0);
            // Try to parse the string as an int
            try {
                maxPrice = Integer.parseInt(userInput);
                // Check the int doesn't meet our expectations
                if (maxPrice < minPrice)
                    JOptionPane.showMessageDialog(null, "Price must be >= " + minPrice, appName, JOptionPane.ERROR_MESSAGE);
                // Catch any non number inputs
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.", appName, JOptionPane.ERROR_MESSAGE);
            }
        }
        // Return the two values as an int array
        return new int[]{minPrice, maxPrice};
    }

    /**
     * Takes the users Dream plant, looks for any matches in the database related to the users choices
     * if none are found, it informs the customer then exits, otherwise It will show the user all the
     * possible choices that fit their critera and prompt them to make a selection, if a user does
     * make a selection, it will call the submitOrder method passing in the users choice.
     * @param dreamFruitingPlant a users dream plant made up from their choices
     */
    private static void processSearchResults(DreamFruitingPlant dreamFruitingPlant){
        // Fill with a list of matching fruit plants (if any) from the find match method, passing in the users plant
        List<FruitingPlant> matching = inventory.findMatch(dreamFruitingPlant);

        // Check for at least a single match
        if(matching.size() > 0) {
            // Create a map to hold matching fruit plants names as keys and their object as the value
            Map<String, FruitingPlant> options = new HashMap<>();

            // Initialising a string builder that is used to popular a J option pane to show the user the returned results
            StringBuilder infoToShow = new StringBuilder("Matches found!! The following citrus trees meet your criteria: \n");
            for (FruitingPlant match : matching) {
                // Pass each match into the fruiting plants to-string method, passing into that the users dream plant
                // calling the get all filters method to get a list of features to return information on (Only those
                // relevant to the customer)
                infoToShow.append(match.toString(dreamFruitingPlant.getAllFilters()));
                // Put the match into the map of user choices
                options.put(match.productName(), match);
            }
            // Ask the user which tree they would like to order
            String choice = (String) JOptionPane.showInputDialog(null, infoToShow + "\n\n" +
                    "Please select which item you'd like to order:", appName, JOptionPane.INFORMATION_MESSAGE, icon,
                    options.keySet().toArray(), "");
            // Exit on null pointer
            if(choice == null) System.exit(0);
            // Add the users choice to the options map
            FruitingPlant chosenTree = options.get(choice);

           // call submit order passing in all relevant information
            submitOrder(getContactInfo(),chosenTree, Integer.parseInt(dreamFruitingPlant.getFilter(Filters.POT_SIZE).toString()));
            JOptionPane.showMessageDialog(null,"Thank you! Your order has been submitted. " +
                    "Please head to your local Greenie Geek to pay and pick up!",appName, JOptionPane.INFORMATION_MESSAGE);
        }
        // Let the user know there were no matches
        else{
            JOptionPane.showMessageDialog(null,"Unfortunately none of our citrus trees meet your criteria :("+
                    "\n\tTo exit, click OK.",appName, JOptionPane.INFORMATION_MESSAGE);
        }
    }
    /**
     * Asks the customer for their name and phone number and creates a Customer
     * object using this information
     * @return a Customer object containing our customers information
     */
    private static Customer getContactInfo(){
        // Ask the user to enter their first and last name and check if it is correct.
        String name;
        do{
            name = (String) JOptionPane.showInputDialog(null,"Please enter your full name (in format firstname surname): ",appName,JOptionPane.QUESTION_MESSAGE, icon, null,null);
            if(name==null) System.exit(0);
        } while(!isValidFullName(name));

        // Ask the user to enter their phone number and check if it is correct.
        String phoneNumber;
        do{
            phoneNumber = (String) JOptionPane.showInputDialog(null,"Please enter your phone number (10-digit number in the format 0412345678): ",appName,JOptionPane.QUESTION_MESSAGE, icon, null,null);
            if(phoneNumber==null) System.exit(0);}

        // Send the users input to the isValidPhoneNumber function to check if it is correctly formatted
        while(!isValidPhoneNumber(phoneNumber));

        // Return the created geek object from the users input
        return new Customer(name,phoneNumber);
    }

    /**
     * Takes the user's information, the fruiting tree they wish to order, and the requested pot Size
     * and systematically adds all relevant information to a String that is then used to write a txt
     * file of the user's order.
     * @param customer - A Customer object of with the users name and phone number
     * @param fruitingPlant - fruiting plant object the user has selected
     * @param potSize - The users chosen potsize
     */
    private static void submitOrder(Customer customer, FruitingPlant fruitingPlant, int potSize) {
        // Set the name of our file
        String filePath = customer.name().replace(" ", "_") + "_" + fruitingPlant.productCode() + ".txt";
        Path path = Path.of(filePath);

        // Concatenate out the string to be appended to the message
        String lineToWrite = "Order details:" +
                "\n\tName: " + customer.name() + " ("+customer.phoneNumber() +")"+
                "\n\tItem: " + fruitingPlant.productName() + " (" + fruitingPlant.productCode() + ")" +
                "\n\tPot size (inch): "+potSize;

        // Write the string to a file
        try {
            Files.writeString(path, lineToWrite);
        } catch (IOException io) {
            System.out.println("Order could not be placed. \nError message: " + io.getMessage());
            System.exit(0);
        }
    }

    /**
     * Compares a given string against a predetermined sequence of charters to determine if
     * customer input is correct. In this case the format of the users first and last name
     * @param fullName - User input of their first and last name
     * @return boolean True of False whether the input matched the required format
     */
    private static boolean isValidFullName(String fullName) {
        // TODO this might not be good enough, re check before submission to see what happened with ass1 with this regex
        Pattern pattern = Pattern.compile("^([A-Z][a-z '.-]*(\\s))+[A-Z][a-z '.-]*$");

        // Match the users input against the required format
        Matcher matcher = pattern.matcher(fullName);

        // Return the result
        return matcher.matches();
    }

    /**
     * Compares a given string against a predetermined sequence of charters to determine if
     * customer input is correct. In this case the format of a phone number
     * @param phoneNumber - customer phone number asked to be input
     * @return boolean True of False whether the input matched the required format
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Create a pattern object containing the required format
        // is 10 digits starting with 04
        Pattern pattern = Pattern.compile("^04\\d{8}$");

        // Match the users input against the required format
        Matcher matcher = pattern.matcher(phoneNumber);

        // Return the result
        return matcher.matches();
    }
}
