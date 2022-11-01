package model;

import model.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import persistence.JsonReader;
import persistence.JsonWriter;

//International document sorter application
public class DocSort {
    private static final String JSON_STORE = "./data";
    private String countryCode;
    private String currentCountry;

    private HashMap<String, ArrayList<Document>> countryList;
    private ArrayList<String> countries;

    /*
        EFFECTS: runs the document sorting application and initializes HashMap
     */
    public DocSort() {
        countryList = new HashMap<String, ArrayList<Document>>();
        currentCountry = null;

        // Loading all existing country data from disk
        countries = new ArrayList<>();
        this.loadFiles();
        runDocSort();
    }

    /*
        REQUIRES: user inputs a valid country code
        MODIFIES: this
        EFFECTS: processes user input
     */
    private void runDocSort() {
        currentCountry = "";
        countryCode = "";

        if (currentCountry != null && currentCountry != countryCode) {
            this.saveCountry(currentCountry);
        }

        if (!countryList.containsKey(countryCode)) {
            countryList.put(countryCode, new ArrayList<Document>());
        }

    }

    public void loadFiles() {
        File data = new File(JSON_STORE);

        for (final File file : data.listFiles()) {
            if (file.isFile()) {
                String fileName = file.getName();
                if (fileName.contains(".json")) {
                    // Here we assume any json file in data must be a country

                    String country = fileName.substring(0, fileName.indexOf(".json"));
                    countries.add(country);
                    JsonReader reader = new JsonReader(file.getAbsolutePath());
                    ArrayList<Document> docs = null;

                    try {
                        docs = reader.read();
                        EventLog.getInstance().logEvent(new Event("Loaded saved documents "));
                    } catch (IOException e) {
                        e.printStackTrace();
                        // TODO: Handle later
                    }
                    countryList.put(country, docs);
                }
            }
        }
    }

    /* REQUIRES: user inputs a valid option (Add, Remove, View, End)
       MODIFIES: this
       EFFECTS: processes user command
     */
    public void menu(String option) {
        switch (option.toUpperCase()) {
            case ("ADD"):
                runDocSort();

            case ("REMOVE"):
                runDocSort();

            case ("END"):
                this.saveAll();
                System.out.println(EventLog.getInstance());
                break;

            default:
                System.out.println("Improper input");
                runDocSort();
        }
    }

    /*
        REQUIRES: docType, issueDate, and expiryDate have non-zero lengths
        MODIFIES: this, countryList,
        EFFECTS: processes user input and adds document created to corresponding list
     */
    public void menuAdd(String cc, String docType, String issueDate, String expiryDate) {
        if (!countryList.containsKey(cc)) {
            countryList.put(cc, new ArrayList<Document>());
        }
        Document doc0 = new Document(cc, docType, issueDate, expiryDate);
        countryList.get(cc).add(doc0);
        EventLog.getInstance().logEvent(new Event("Document added to " + countryCode));
    }

    /*
       REQUIRES: index >= 0
       MODIFIES: this, countryList
       EFFECTS: processes user input and removes document from corresponding list
     */
    public void menuRemove(int n) {
        try {
            if (countryList.get(countryCode).size() > 0) {
                countryList.get(countryCode).remove(n);
                EventLog.getInstance().logEvent(new Event("Document removed from " + countryCode));
            } else {
                menu(countryCode);
            }

        } catch (Exception e) {
            System.out.println("Couldn't remove");
        }
    }

    // EFFECTS: saves the workroom to file
    public void saveCountry(String selectedCountry) {
        try {
            String path = JSON_STORE + "/" + selectedCountry + ".json";
            JsonWriter writer = new JsonWriter(path);
            writer.open();
            writer.write(countryList.get(selectedCountry));
            writer.close();
            EventLog.getInstance().logEvent(new Event("Saved " + selectedCountry + " to disc"));
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    public void saveAll() {
        for (String country : countryList.keySet()) {
            if (!countryList.get(country).isEmpty()) {
                saveCountry(country);
            }
        }
    }

    public void setCountryCode(String country) {
        this.countryCode = country;
        EventLog.getInstance().logEvent(new Event("Opened menu for " + country));

    }

    public ArrayList<String> getCountries() {
        return countries;
    }

    public ArrayList<Document> getDocsFromCode(String cc) {
        return countryList.get(cc);
    }

}
