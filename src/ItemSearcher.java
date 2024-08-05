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

    private static final String filePath = "./inventory_v1.txt";
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

            String category = singularInfo[0];
            String productName = singularInfo[1];
            String productCode = singularInfo[2];
            String type = singularInfo[3].trim();

            boolean dwarf = singularInfo[4].equalsIgnoreCase("yes");
            String trainingSystem = singularInfo[5];

            // Create a list for our pollinators
            List<String> pollinatorList = new ArrayList<>();
            // TODO perhaps add a try catch for this iteration specifically look at price below for example
            if (!pollinatorsRaw.isEmpty()) {
                String[] pollinatorOptions = pollinatorsRaw.split(",");
                pollinatorList.addAll(Arrays.asList(pollinatorOptions));
            }

            Map<Integer,Float> potSizeToPrice = new LinkedHashMap<>();
            if(potSizesRaw.length()>0) {
                String[] optionsPotSizes = potSizesRaw.split(",");
                String[] optionsPrices = pricesRaw.split(",");
                for (int j=0;j<optionsPrices.length;j++){
                    int potSize = 0;
                    float price = 0f;
                    try {
                        potSize = Integer.parseInt(optionsPotSizes[j]);
                        price = Float.parseFloat(optionsPrices[j]);
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
            List<String> potSizeList = new ArrayList<>();
            if(potSizesRaw.length()>0) {
                String[] optionsPotSizes = potSizesRaw.split(",");
                potSizeList.addAll(Arrays.asList(optionsPotSizes));
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

    private static DreamFruitingPlant getFilters(){

        String type = (String) JOptionPane.showInputDialog(null, "Please select your preferred type", appName, JOptionPane.QUESTION_MESSAGE, icon, inventory.getAllTypes().toArray(new String[0]), null);
        if (type == null) System.exit(0);

        boolean dwarf=false;
        int chooseDwarf = JOptionPane.showConfirmDialog(null,"Would you like a dwarf tree?",appName, JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,icon);
        if(chooseDwarf==-1) System.exit(0);
        else if(chooseDwarf==0) dwarf=true;

        int potSize = Integer.parseInt((String) JOptionPane.showInputDialog(null,"Pot size (inch)? **min 8 inch",appName, JOptionPane.QUESTION_MESSAGE,icon,null,null));
        if(potSize < 8) {
            JOptionPane.showMessageDialog(null,"Invalid input. Please enter a positive number greater than 8.",appName, JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        int minPrice=-1,maxPrice = -1;
        while(minPrice<0) {
            String userInput = (String) JOptionPane.showInputDialog(null, "Enter min price range value:", appName, JOptionPane.QUESTION_MESSAGE, icon, null, null);
            if(userInput==null)System.exit(0);
            try {
                minPrice = Integer.parseInt(userInput);
                if(minPrice<0) JOptionPane.showMessageDialog(null,"Price must be >= 0.",appName, JOptionPane.ERROR_MESSAGE);
            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid input. Please try again.", appName, JOptionPane.ERROR_MESSAGE);
            }
        }
        while(maxPrice<minPrice) {
            String userInput = (String) JOptionPane.showInputDialog(null, "Enter max price range value:", appName, JOptionPane.QUESTION_MESSAGE,icon,null, null);
            if(userInput==null)System.exit(0);
            try {
                maxPrice = Integer.parseInt(userInput);
                if(maxPrice<minPrice) JOptionPane.showMessageDialog(null,"Price must be >= "+minPrice,appName, JOptionPane.ERROR_MESSAGE);
            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid input. Please try again.", appName, JOptionPane.ERROR_MESSAGE);
            }
        }

//        FruitingPlant dreamFruitingPlant = new FruitingPlant("", "", "", type, dwarf, potSize, new HashMap<>());
//        dreamFruitingPlant.setMaxPrice(maxPrice);
//        dreamFruitingPlant.setMinPrice(minPrice);
        DreamFruitingPlant dreamFruitingPlant = new DreamFruitingPlant(type, dwarf, potSize, new HashMap<>(), maxPrice, minPrice);
        return dreamFruitingPlant;
    }

    private static void processSearchResults(DreamFruitingPlant dreamFruitingPlant){
        List<FruitingPlant> matching = inventory.findMatch(dreamFruitingPlant);
        if(matching.size() > 0) {
            Map<String, FruitingPlant> options = new HashMap<>();
            StringBuilder infoToShow = new StringBuilder("Matches found!! The following citrus trees meet your criteria: \n");
            for (FruitingPlant match : matching) {
                infoToShow.append(match.getItemInformation());
                options.put(match.getProductName(), match);
            }
            String choice = (String) JOptionPane.showInputDialog(null, infoToShow + "\n\nPlease select which item you'd like to order:", appName, JOptionPane.INFORMATION_MESSAGE, icon, options.keySet().toArray(), "");
            if(choice == null) System.exit(0);
            FruitingPlant chosenTree = options.get(choice);
            submitOrder(getContactInfo(),chosenTree, dreamFruitingPlant.getPotSize());
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
        String filePath = customer.name().replace(" ", "_") + "_" + fruitingPlant.getProductCode() + ".txt";
        Path path = Path.of(filePath);
        String lineToWrite = "Order details:" +
                "\n\tName: " + customer.name() + " ("+customer.phoneNumber() +")"+
                "\n\tItem: " + fruitingPlant.getProductName() + " (" + fruitingPlant.getProductCode() + ")" +
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
