package task3.parsers;

import task3.generated.KnifeDesc;
import task3.generated.KnifeDesc.KnifeVisual.*;
import task3.generated.KnifeDesc.KnifeVisual.KnifeHandle.Wood;
import task3.generated.MetalType;
import task3.generated.WoodType;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This is a DOM parser for the XML document containing a collection of knifes.
 *
 * @author Viacheslav Chichin
 * @version 1.0  June 22, 2015.
 */

public class KnifesDomParser {

    /**
     * Creates DOM model of the XML file.
     *
     * @param filename the input XML document
     * @return corresponding DOM document
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public Document getDocument(String filename) throws
            ParserConfigurationException, IOException, SAXException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File(filename));

        return doc;
    }

    /**
     * Parses the XML document and creates the list of records.
     *
     * @param filename the input XML document
     * @return the list of knifes (records)
     */
    public List<KnifeDesc> parseDocument(String filename) {
        List<KnifeDesc> knifes = new ArrayList<>();
        KnifeDesc knife = null;
        Node currentNode = null;

        try {
            Document doc = getDocument(filename);
            NodeList records = doc.getDocumentElement().getElementsByTagName("knife");

            for (int i = 0; i < records.getLength(); ++i) {
                currentNode = records.item(i);

                // create new knife instance
                knife = new KnifeDesc();

                // get name
                knife.setName(((Element) currentNode).getAttribute("name"));

                // get child tags
                NodeList children = currentNode.getChildNodes();

                for (int j = 0; j < children.getLength(); ++j) {
                    Node node = children.item(j);

                    if (node instanceof Element) {
                        switch (node.getNodeName()) {
                            case "knifeType":
                                knife.setKnifeType(node.getTextContent());
                                break;

                            case "knifeHandy":
                                knife.setKnifeHandy(Integer.parseInt(node.getTextContent()));
                                break;

                            case "knifeOrigin":
                                knife.setKnifeOrigin(node.getTextContent());
                                break;

                            case "knifeVisual":
                                knife.setKnifeVisual(new KnifeDesc.KnifeVisual());
                                NodeList visualAttrs = node.getChildNodes();
                                for (int k = 0; k < visualAttrs.getLength(); ++k) {
                                    Node visualNode = visualAttrs.item(k);

                                    if (visualNode instanceof Element) {
                                        switch (visualNode.getNodeName()) {

                                            case "knifeBlade":

                                                KnifeBlade blade = new KnifeBlade();

                                                blade.setLength(new BigInteger(((Element) visualNode).getAttribute("length")));
                                                blade.setWidth(new BigInteger(((Element) visualNode).getAttribute("width")));
                                                blade.setMetal(MetalType.fromValue(((Element) visualNode).getAttribute("metal")));
                                                knife.getKnifeVisual().setKnifeBlade(blade);
                                                break;

                                            case "knifeHandle":
                                                knife.getKnifeVisual().setKnifeHandle(new KnifeHandle());

                                                // check wood
                                                NodeList woodNode = ((Element) visualNode).getElementsByTagName("wood");

                                                if (woodNode.getLength() > 0) {
                                                    Wood wood = new Wood();
                                                    wood.setType(WoodType.fromValue(((Element) woodNode.item(0)).getAttribute("type")));
                                                    knife.getKnifeVisual().getKnifeHandle().setWood(wood);
                                                    break;
                                                }

                                                // check leatherCoated
                                                if (((Element) visualNode).getElementsByTagName("leatherCoated").getLength() > 0) {
                                                    knife.getKnifeVisual().getKnifeHandle().setLeatherCoated(new Object());
                                                    break;
                                                }

                                                // check plastic
                                                if (((Element) visualNode).getElementsByTagName("plastic").getLength() > 0) {
                                                    knife.getKnifeVisual().getKnifeHandle().setPlastic(new Object());
                                                    break;
                                                }

                                            default:
                                                knife = null;
                                                throw new IllegalArgumentException();
                                        }
                                    }
                                }

                                break;


                            case "collection":
                                knife.setCollection(Boolean.parseBoolean(node.getTextContent()));
                                break;

                            default:
                                knife = null;
                                throw new IllegalArgumentException();
                        }
                    }
                }

                knifes.add(knife);
            }

        } catch (Exception e) {
            System.out.println("DOM parser: error while parsing document");
        }

        return knifes;
    }
}