package task3.parsers;

import task3.generated.KnifeDesc;
import task3.generated.KnifeDesc.KnifeVisual.*;
import task3.generated.KnifeDesc.KnifeVisual.KnifeHandle.Wood;
import task3.generated.Knifes;
import task3.generated.MetalType;
import task3.generated.WoodType;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import task3.parsers.exception.IllegalXmlArgumentException;
import task3.parsers.exception.NoSuchXmlAttributeException;

/**
 * Class - document handler for SAX parser.
 *
 * @author Viacheslav Chichin
 * @version 1.0  June 22, 2015.
 */


class KnifesDocHandler extends DefaultHandler {
    private String currentElem;
    private KnifeDesc currentKnife;
    private List<KnifeDesc> knifes;
    private StringBuffer buffer = new StringBuffer();

    public List<KnifeDesc> getKnifes() {
        return knifes;
    }

    @Override
    public void startDocument() {
        System.out.println("SAX parser start...");
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        String data = null;
        currentElem = qName;
        try {
            switch (currentElem) {
                case "knifes":
                    knifes = new Knifes().getKnife();
                    break;

                case "knife":
                    currentKnife = new KnifeDesc();

                    data = attributes.getValue("name");

                    if (data == null) {
                        throw new NoSuchXmlAttributeException("name");
                    } else {
                        currentKnife.setName(data);
                    }
                    break;

                case "knifeType":
                case "knifeHandy":
                case "knifeOrigin":
                    break;

                case "knifeVisual":
                    currentKnife.setKnifeVisual(new KnifeDesc.KnifeVisual());
                    break;

                case "knifeBlade":
                    KnifeBlade blade = new KnifeBlade();

                    // set blade attributes
                    data = attributes.getValue("length");
                    if (data == null) {
                        throw new NoSuchXmlAttributeException("length");
                    } else {
                        try {
                            blade.setLength(new BigInteger(data));
                        } catch (NumberFormatException e) {
                            throw new IllegalXmlArgumentException("length", data, true);
                        }
                    }

                    data = attributes.getValue("width");
                    if (data == null) {
                        throw new NoSuchXmlAttributeException("width");
                    } else {
                        try {
                            blade.setWidth(new BigInteger(data));
                        } catch (NumberFormatException e) {
                            throw new IllegalXmlArgumentException("width", data, true);
                        }
                    }

                    data = attributes.getValue("metal");
                    if (data == null) {
                        throw new NoSuchXmlAttributeException("metal");
                    } else {
                        try {
                            blade.setMetal(MetalType.fromValue(data));
                        } catch (IllegalArgumentException e) {
                            throw new IllegalXmlArgumentException("metal", data, true);
                        }
                    }

                    currentKnife.getKnifeVisual().setKnifeBlade(blade);
                    break;

                case "knifeHandle":
                    currentKnife.getKnifeVisual().setKnifeHandle(new KnifeHandle());
                    break;

                case "wood":
                    Wood wood = new Wood();

                    data = attributes.getValue("type");
                    if (data == null) {
                        throw new NoSuchXmlAttributeException("type");
                    } else {
                        try {
                            wood.setType(WoodType.fromValue(data));
                        } catch (IllegalArgumentException e) {
                            throw new IllegalXmlArgumentException("type", data, true);
                        }
                    }

                    currentKnife.getKnifeVisual().getKnifeHandle().setWood(wood);
                    break;

                case "leatherCoated":
                    currentKnife.getKnifeVisual().getKnifeHandle().setLeatherCoated(new Object());
                    break;

                case "plastic":
                    currentKnife.getKnifeVisual().getKnifeHandle().setPlastic(new Object());
                    break;

                case "collection":
                    break;

                default:

                    // element not found
                    currentKnife = null;
                    throw new SAXException("Unknown element " + qName);
            }
        } catch (NoSuchXmlAttributeException e) {

            // required attribute is missing
            throw new SAXException(e.getMessage());
        } catch (IllegalXmlArgumentException e) {

            // wrong attribute value
            throw new SAXException(e.getMessage());
        } catch (NullPointerException e) {

            // illegal order of construction of a knife
            throw new SAXException("Illegal order of elements");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (currentElem.equals("knifeType") ||
                currentElem.equals("knifeHandy") ||
                currentElem.equals("knifeOrigin") ||
                currentElem.equals("collection")) {

            buffer.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        String data = buffer.toString().trim();

        switch (qName) {

            case "knife":
                knifes.add(currentKnife);
                break;

            case "knifeType":
                currentKnife.setKnifeType(data);
                break;

            case "knifeHandy":
                currentKnife.setKnifeHandy(Integer.parseInt(data));
                break;

            case "knifeOrigin":
                currentKnife.setKnifeOrigin(data);
                break;

            case "collection":
                currentKnife.setCollection(Boolean.parseBoolean(data));
                break;

            default:
                break;
        }

        buffer.setLength(0);
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("End of document");
    }
}

/**
 * This is a SAX parser for the XML document containing a collection of knifes.
 *
 * @author Viacheslav Chichin
 * @version 1.0  June 22, 2015.
 */


public class KnifesSaxParser {

    /**
     * Parses the XML document and creates the list of records.
     *
     * @param filename the input XML document
     * @return the list of knifes (records)
     */
    public List<KnifeDesc> parseDocument(String filename) {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            KnifesDocHandler docHandler = new KnifesDocHandler();

            if (docHandler != null) {
                parser.parse(new File(filename), docHandler);

                return docHandler.getKnifes();
            }
        } catch (Exception e) {
            System.out.println("SAX parser: error while parsing document");
        }

        return null;
    }
}