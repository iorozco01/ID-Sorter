package persistence;

import model.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Partial code from Professor Paul Carter https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/tree/master/lib
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ArrayList<Document> doc = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyDocumentList.json");
        try {
            ArrayList<Document> doc = reader.read();
            assertEquals("My document", doc.get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralDocument() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralDocumentList.json");
        try {
            ArrayList<Document> doc = reader.read();
            Document document = new Document("country", "type", "issue", "expiry");
            doc.add(document);
            assertEquals(document, doc.get(0));
            assertEquals(1, doc.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}