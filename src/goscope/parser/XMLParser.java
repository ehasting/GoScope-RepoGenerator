/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.parser;

import goscope.main.XMLTools;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author egilhasting
 */
public class XMLParser {

    public GoScopeXMLHandler parserHandler;

    public XMLParser(GoScopeXMLHandler parserHandler)
    {
        this.parserHandler = parserHandler;
    }

    public void readFromFile_nonValidate(String xmlFile) {
        try {
            File f = new File(xmlFile);
            // validate the xml before reading.
            XMLTools xt = new XMLTools();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(f, this.parserHandler);
            parserHandler.fileName =  xmlFile;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Big badabooom!!! " + e.getMessage());
        }
    }

    public void readFromFile(String xsdFile, String xmlFile) {
        try {
            // validate the xml before reading.
            XMLTools xt = new XMLTools();
            if (xt.ValidateXMLFile(xsdFile, xmlFile)) {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                saxParser.parse(xt.getFile(), this.parserHandler);
                parserHandler.fileName =  xmlFile;
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Big badabooom!!! " + e.getMessage());
        }
    }

    public void readFromString(String xsdFile, String xmlString) {
        try {
            // validate the xml before reading.
            XMLTools xt = new XMLTools();
            if (xt.ValidateXMLString(xsdFile, xmlString)) {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                saxParser.parse(xt.getXmlString(), this.parserHandler);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Big badabooom!!! " + e.getMessage());
        }
    }
}
