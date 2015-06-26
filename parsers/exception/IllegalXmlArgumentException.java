package task3.parsers.exception;

/**
 * The exception is thrown when a value of a tag or an attribute is invalid.
 * @author Viacheslav Chichin
 * @version 1.0  June 22, 2015.
 */


public class IllegalXmlArgumentException extends Exception {
    private static final long serialVersionUID = 1L;
    private String element;
    private boolean isAttribute;

    public IllegalXmlArgumentException(String name, String value, boolean isAttr) {
        super(value);
        element = name;
        isAttribute = isAttr;
    }

    @Override
    public String getMessage() {
        return "Value " + super.getMessage() + " is not allowed for " +
                (isAttribute ? "attribute" : "element") + element;
    }
}
