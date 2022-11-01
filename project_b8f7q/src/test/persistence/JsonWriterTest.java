package persistence;

import model.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Partial code from Professor Paul Carter https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/tree/master/lib
class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            Document document = new Document("country", "document", "issue", "expiry");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyDocumentList() {
        try {
            Document document = new Document("country", "document", "issue", "expiry");
            ArrayList<Document> documents = new ArrayList<Document>();
            JsonWriter writer = new JsonWriter("./data/testWriterDocumentList.json");
            writer.open();
            writer.write(documents);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterDocumentList.json");
            documents = reader.read();
            assertEquals(0, documents.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralDocumentList() {
        try {
            Document document = new Document("country", "document", "issue", "expiry");
            ArrayList<Document> documents = new ArrayList<Document>();
            documents.add(document);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralDocumentList.json");
            writer.open();
            writer.write(documents);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterDocumentList.json");
            documents = reader.read();

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}