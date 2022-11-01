package ui;

//Contains countryCode requisites
public class CountryCode {
    public static final int NUM_DIGITS = 3;
    private String theCode;

    /*
    REQUIRES: code is a valid String
    MODIFIES:theCode
    EFFECTS: creates countryCode with input code and max number of digits being 3
     */
    public CountryCode(String code) {
        theCode = code;
    }

    /*
    EFFECTS: verifies that an object is equal to theCode
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other.getClass() != this.getClass()) {
            return false;
        }

        CountryCode otherCode = (CountryCode) other;
        return theCode.equals(otherCode.theCode);
    }

    @Override
    public int hashCode() {
        return theCode.hashCode();
    }

    @Override
    public String toString() {
        if (theCode.length() == 0) {
            return "<empty>";
        } else {
            return theCode;
        }
    }

    /**
     * Determines if code is valid (4 digit, numeric)
     * @return  true if code is valid, false otherwise
     */
    public boolean isValid() {
        return theCode != null && theCode.length() == 4 && isNumeric();
    }

    /**
     * Determines if code is numeric.
     * @return  true if code is numeric, false otherwise
     */
    private boolean isNumeric() {
        try {
            Integer.parseInt(theCode);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }



}
