/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.parser;

import goscope.model.DeviceModel;
import goscope.model.WordpressMapModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author egilhasting
 */
public class MapperHandler extends GoScopeXMLHandler {

    int counter = 0;

    enum tag {

        NON, CATEGORY, FILE, TAG, SITE, DISPLAYNAME, DESCRIPTION, RELEASENOTE
    };
    tag detectedTag = tag.NON;
    ArrayList<WordpressMapModel> map = new ArrayList<>();
    boolean mediaTag = false;
    WordpressMapModel _map = new WordpressMapModel();

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        switch (qName) {
            case "map":

                _map.deviceId = attributes.getValue("deviceId");
                _map.wordpressId = attributes.getValue("wpId");
                _map.wordpressThumbId = attributes.getValue("thumbId");
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName,
            String qName) throws SAXException {
        switch (qName) {
            case "mapping":
                Output = map;
                break;
            case "map":
                map.add(_map);
                _map = new WordpressMapModel();
                break;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
    }
}
