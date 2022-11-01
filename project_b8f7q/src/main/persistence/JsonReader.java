package persistence;

import model.*;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
// Code from Professor Paul Carter https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/tree/master/lib
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads documents from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ArrayList<Document> read() throws IOException {
        String jsonData = readFile(source);
        JSONArray documentsAsJson = new JSONArray(jsonData);
        return parseDocuments(documentsAsJson);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses document from JSON object and returns it
    private ArrayList<Document> parseDocuments(JSONArray documentsAsJson) {
        ArrayList<Document> parsedDocs = new ArrayList<Document>();

        for (int i = 0; i < documentsAsJson.length(); ++i) {
            JSONObject documentAsJson = documentsAsJson.getJSONObject(i);
            String countryCode = documentAsJson.getString("countryCode");
            String docType = documentAsJson.getString("documentType");
            String issueDate = documentAsJson.getString("issueDate");
            String expiryDate = documentAsJson.getString("expiryDate");

            Document document = new Document(countryCode, docType, issueDate, expiryDate);
            parsedDocs.add(document);
        }

        return parsedDocs;
    }

}
