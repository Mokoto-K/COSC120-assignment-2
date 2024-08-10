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

    private static final String filePath = "./inventory_v2.txt";
    private static final Icon icon = new ImageIcon("./the_greenie_geek.png");
    private static Inventory inventory;
    private static final String appName = "Greenie Geek";

    public static void main(String[] args) {
        inventory = loadInventory(filePath);
        DreamFruitingPlant dreamFruitingPlant = getFilters();
        processSearchResults(dreamFruitingPlant);
        System.exit(0);
    }

    /**
     *
     * @param filePath
     * @return
     */
    private static Inventory loadInventory(String filePath) {
        Inventory inventory = new Inventory();
        Path path = Path.of(filePath);
        List<String> fileContents = null;
        try {
            fileContents = Files.readAllLines(path);
        } catch (IOException io) {
            System.out.println("File could not be found");
            System.exit(0);
        }
        // TODO some form of simple try catch around all this data in-case of data type reading error
        for (int i = 1; i < fileContents.size(); i++) {
            String[] info = fileContents.get(i).split("\\[");
            String[] singularInfo = info[0].split(",");

            String pollinatorsRaw = info[1].replace("],", "");
            String pricesRaw = info[2].replace("],","");
            String potSizesRaw = info[3].replace("],", "");
            String description = info[4].replace("]","");

            // Parse in the category of plant from the database
            // TODO- Fix the error message
            String category = singularInfo[0].toUpperCase().replace(" ", "_");
            try {
                category = CategoryOfFruit.valueOf(category).toString();
            } catch (Exception e) {
                System.out.println("Plant not support in CategoryOfFruits enum, check database " + e);
            }

            String productName = singularInfo[1];
            String productCode = singularInfo[2];
            String type = singularInfo[3].trim();

            String dwarf = singularInfo[4].trim();
            String trainingSystem = singularInfo[5];

            // Create a list for our pollinators
            List<String> pollinatorList = new ArrayList<>();
            // TODO perhaps add a try catch for this iteration specifically look at price below for example
            if (!pollinatorsRaw.isEmpty()) {
                String[] pollinatorOptions = pollinatorsRaw.split(",");
                for (String p : pollinatorOptions) {
                    pollinatorList.add(p.replace("[", "").replace("]", "").trim());
                }
            }

            Map<Integer,Float> potSizeToPrice = new LinkedHashMap<>();
            if(potSizesRaw.length()>0) {
                String[] optionsPotSizes = potSizesRaw.split(",");

                String[] optionsPrices = pricesRaw.split(",");
                for (int j=0;j<optionsPrices.length;j++){
                    int potSize = 0;
                    float price = 0f;
                    try {
                        potSize = Integer.parseInt(optionsPotSizes[j].trim());
                        price = Float.parseFloat(optionsPrices[j].trim());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error in file. Pot size/price option could not be parsed for item on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                        System.exit(0);
                    }
                    potSizeToPrice.put(potSize,price);
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
            // TODO we might not need this, but putting it in for now just incase, everything to to with just the pot
            //  sizes, not the price
            List<Integer> potSizeList = new ArrayList<>();
            if(potSizesRaw.length()>0) {
                String[] optionsPotSizes = potSizesRaw.split(",");
                for (String pSize : optionsPotSizes) {
                    int potS = Integer.parseInt(pSize.trim());
                    potSizeList.add(potS);
                }
            }
            filterMap.put(Filters.POT_SIZE, potSizeList);
            filterMap.put(Filters.POT_SIZE_PRICE, potSizeToPrice);

            // Create a dreamFruituingPlant object passing it our map full of goodies
            DreamFruitingPlant dreamFruitingPlant = new DreamFruitingPlant(filterMap);

//            DreamFruitingPlant dreamFruitingPlant = new DreamFruitingPlant(type, dwarf, 0, potSizeToPrice, 0, 0);
            FruitingPlant fruitingPlant = new FruitingPlant(productName, productCode, description, dreamFruitingPlant);
            inventory.addItem(fruitingPlant);
        }
        return inventory;
    }

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

        // TODO - Potential change to "I don't mind" and not NA
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

        int potSize = 7;
        while (potSize < 8) {
            String userInput = (String) JOptionPane.showInputDialog(null, "What size pot would" +
                    " you like? Options are even numbers from 8 - 16inch", appName, JOptionPane.QUESTION_MESSAGE, icon,
                    null, null);

            if (userInput == null) System.exit(0);
            try {
                potSize = Integer.parseInt((userInput));
                if (potSize < 7)
                    JOptionPane.showMessageDialog(null, "Pot size must be at least 8", appName, JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.", appName, JOptionPane.ERROR_MESSAGE);
            }
        }

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

    private static void processSearchResults(DreamFruitingPlant dreamFruitingPlant){
        List<FruitingPlant> matching = inventory.findMatch(dreamFruitingPlant);
        if(matching.size() > 0) {
            Map<String, FruitingPlant> options = new HashMap<>();
            StringBuilder infoToShow = new StringBuilder("Matches found!! The following citrus trees meet your criteria: \n");
            for (FruitingPlant match : matching) {
                // TODO - this was meant to just put a map in a tostring method, I dont know if this is a solution yet
                infoToShow.append(match.toString(dreamFruitingPlant.getAllFilters()));
                options.put(match.productName(), match);
            }
            String choice = (String) JOptionPane.showInputDialog(null, infoToShow + "\n\nPlease select which item you'd like to order:", appName, JOptionPane.INFORMATION_MESSAGE, icon, options.keySet().toArray(), "");
            if(choice == null) System.exit(0);
            FruitingPlant chosenTree = options.get(choice);
            // this used to want to get dreamfruitingplants.getpotsize, I changed it to this, not sure if it works or not
            submitOrder(getContactInfo(),chosenTree, Integer.parseInt(dreamFruitingPlant.getFilter(Filters.POT_SIZE).toString()));
            JOptionPane.showMessageDialog(null,"Thank you! Your order has been submitted. Please head to your local Greenie Geek to pay and pick up!",appName, JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null,"Unfortunately none of our citrus trees meet your criteria :("+
                    "\n\tTo exit, click OK.",appName, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static Customer getContactInfo(){
        String name;
        do{
            name = (String) JOptionPane.showInputDialog(null,"Please enter your full name (in format firstname surname): ",appName,JOptionPane.QUESTION_MESSAGE, icon, null,null);
            if(name==null) System.exit(0);
        } while(!isValidFullName(name));
        String phoneNumber;
        do{
            phoneNumber = (String) JOptionPane.showInputDialog(null,"Please enter your phone number (10-digit number in the format 0412345678): ",appName,JOptionPane.QUESTION_MESSAGE, icon, null,null);
            if(phoneNumber==null) System.exit(0);}
        while(!isValidPhoneNumber(phoneNumber));
        return new Customer(name,phoneNumber);
    }

    private static void submitOrder(Customer customer, FruitingPlant fruitingPlant, int potSize) {
        String filePath = customer.name().replace(" ", "_") + "_" + fruitingPlant.productCode() + ".txt";
        Path path = Path.of(filePath);
        String lineToWrite = "Order details:" +
                "\n\tName: " + customer.name() + " ("+customer.phoneNumber() +")"+
                "\n\tItem: " + fruitingPlant.productName() + " (" + fruitingPlant.productCode() + ")" +
                "\n\tPot size (inch): "+potSize;

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
