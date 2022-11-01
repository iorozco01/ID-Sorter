package persistence;

import model.Document;
import org.json.JSONObject;
import org.json.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

// Represents a writer that writes JSON representation of workroom to file
// Code from Professor Paul Carter https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/tree/master/lib
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of country to file
    public void write(ArrayList<Document> documents) {
        JSONArray array = new JSONArray();

        for (Document doc : documents) {
            JSONObject docAsJson = new JSONObject();
            docAsJson.put("countryCode", doc.getCountryCode());
            docAsJson.put("documentType", doc.getDocumentType());
            docAsJson.put("issueDate", doc.getIssueDate());
            docAsJson.put("expiryDate", doc.getExpiryDate());
            array.put(docAsJson);
        }

        saveToFile(array.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.write(json);
    }
}
