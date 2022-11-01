package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a document having a documentType, an issueDate, and an expiryDate
// Date represented as YYYY-MM-DD
public class Document implements Writable {
    String countryCode;
    String documentType;    //Type of document
    String issueDate;       //Date document was issued
    String expiryDate;      //Date document expires

    /*
        REQUIRES: documentType has a non-zero length
                  issueDate has a non-zero length
                  expiryDate has a non-zero length

        EFFECTS:  documentType is set to documentType input by user
                  issueDate is set to issueDate input by user
                  expiryDate is set to expiryDate input by user
     */

    public Document(String countryCode,String documentType, String issueDate, String expiryDate) {
        this.countryCode = countryCode;
        this.documentType = documentType;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    /*
        EFFECTS: returns a string representation of the document
     */
    public String toString() {
        return (documentType + " " + issueDate + " " + expiryDate);

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("countryCode", countryCode);
        json.put("docType", documentType);
        json.put("issueDate", issueDate);
        json.put("expiryDate", expiryDate);
        return json;
    }

}
