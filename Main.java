package task3;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import task3.parsers.KnifesDomParser;
import task3.parsers.KnifesSaxParser;
import task3.parsers.KnifesStaxParser;
import task3.generated.KnifeDesc;
import task3.parsers.xmlValidator.KnivesXmlValidator;

/**
 * This is runner.
 * It's run all commands, to demonstrate, that is works properly:
 * 1. XML validation
 * 2. XML parsing with DOM
 * 3. XML parsing with SAX
 * 4. XML parsing with STAX
 *
 * @author Viacheslav Chichin
 * @version 1.0  June 22, 2015.
 */

public class Main {

    /**
     * Runs all tasks.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        String xmlFile = "src/task3/xml/knifes.xml";
        String xsdFile = "src/task3/xml/knifes2.xsd";

        System.out.println("--------------Running XML Validation----------------------");
        System.out.println(new KnivesXmlValidator().validate(xsdFile, xmlFile));
        System.out.println("--------------Running DOM parsing ------------------------");
        List<KnifeDesc> knifesDom = new KnifesDomParser().parseDocument(xmlFile);
        sortKnifes(knifesDom);
        System.out.println("----print result ------------------------");
        printKnifes(knifesDom);
        System.out.println("--------------Running SAX parsing ------------------------");
        List<KnifeDesc> knifesSax = new KnifesSaxParser().parseDocument(xmlFile);
        sortKnifes(knifesSax);
        System.out.println("----print result ------------------------");
        printKnifes(knifesSax);
        System.out.println("--------------Running STAX parsing -----------------------");
        List<KnifeDesc> knifesStax = new KnifesStaxParser().parseDocument(xmlFile);
        sortKnifes(knifesStax);
        System.out.println("----print result ------------------------");
        printKnifes(knifesStax);

    }


    /**
     * Method is for sorting knives by name.
     * @param knifes the list of knives
     */
    private static void sortKnifes(List<KnifeDesc> knifes) {
        Collections.sort(knifes, new Comparator<KnifeDesc>() {
            @Override
            public int compare(KnifeDesc o1, KnifeDesc o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    /**
     * Method prints knifes collection.
     * @param knifes the list of knives
     */
    private static void printKnifes(List<KnifeDesc> knifes) {
        for (KnifeDesc knife : knifes) {
            System.out.println("Name: " + knife.getName());
            System.out.println("Type: " + knife.getKnifeType());
            System.out.println("Handy: " + knife.getKnifeHandy());
            System.out.println("Origin: " + knife.getKnifeOrigin());
            System.out.println("Blade: " + knife.getKnifeVisual().getKnifeBlade().getLength() +
                    " " + knife.getKnifeVisual().getKnifeBlade().getWidth() + " " +
                    knife.getKnifeVisual().getKnifeBlade().getMetal());
            System.out.println("Collection: " + knife.isCollection());
            System.out.println("");
        }
    }
}