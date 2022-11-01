package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {
    private Document testDoc;

    @BeforeEach
    void runBefore() {
        testDoc = new Document("type", "test","issue", "expiry");
    }

    @Test
    void testConstructor() {
        assertEquals("type", testDoc.getCountryCode());
        assertEquals("test" , testDoc.getDocumentType());
        assertEquals("issue" , testDoc.getIssueDate());
        assertEquals("expiry" , testDoc.getExpiryDate());
    }

    @Test
    void testDisplayDoc() {
        assertTrue(testDoc.toString().contains("type issue expiry"));
    }
}
