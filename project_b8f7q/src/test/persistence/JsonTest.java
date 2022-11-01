package persistence;

import model.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Partial code from Professor Paul Carter https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/tree/master/lib
public class JsonTest {
    protected void checkDocument(String countryCode, String type, String issue, String expiry) {
        Document doc = new Document(countryCode, type, issue, expiry);
        assertEquals(countryCode, doc.getCountryCode());
        assertEquals(type, doc.getDocumentType());
        assertEquals(issue, doc.getIssueDate());
        assertEquals(expiry, doc.getExpiryDate());
    }
}
