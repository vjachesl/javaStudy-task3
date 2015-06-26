package task3.parsers.exception;

/**
 * The exception is thrown when the parser cannot find the attribute by the
 * given name.
 * @author Viacheslav Chichin
 * @version 1.0  June 22, 2015.
 */

public class NoSuchXmlAttributeException extends Exception {
    private static final long serialVersionUID = 1L;

    public NoSuchXmlAttributeException(String attribute) {
        super(attribute);
    }

    @Override
    public String getMessage() {
        return "Attribute \'" + super.getMessage() + "\' not found";
    }
}