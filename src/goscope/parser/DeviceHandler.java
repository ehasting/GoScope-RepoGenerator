/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.parser;

import goscope.model.DeviceModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author egilhasting
 */
public class DeviceHandler extends GoScopeXMLHandler {

    int counter = 0;

    enum tag {

        NON, CATEGORY, FILE, TAG, SITE, DISPLAYNAME, DESCRIPTION, RELEASENOTE
    };
    tag detectedTag = tag.NON;
    DeviceModel dv = new DeviceModel();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    boolean mediaTag = false;

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        switch (qName) {
            case "base":
                dv.baseID = attributes.getValue("id");
                dv.baseAuthor = attributes.getValue("author");
                break;

            case "version":
                dv.versionNumber = Float.parseFloat(attributes.getValue("number"));
                dv.versionFilename = attributes.getValue("filename");
                try {
                    String dateValue = attributes.getValue("added").replace('T', ' ');
                    dv.versionAdded = format.parse(dateValue);
                } catch (ParseException ex) {
                    Logger.getLogger(DeviceHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "release-notes":
                detectedTag = tag.RELEASENOTE;
                break;

            case "media":
                mediaTag = true;
                break;

            case "file":
                if (mediaTag) {
                    dv._tmpFile.mime = attributes.getValue("mime");
                    dv._tmpFile.type = attributes.getValue("type");
                    detectedTag = tag.FILE;
                }
                break;

            case "displayname":
                detectedTag = tag.DISPLAYNAME;
                break;

            case "description":
                detectedTag = tag.DESCRIPTION;
                break;

            case "category":
                detectedTag = tag.CATEGORY;
                break;

            case "tag":
                dv._tmpTag.type = attributes.getValue("type");
                detectedTag = tag.TAG;
                break;

            case "site":
                detectedTag = tag.SITE;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName,
            String qName) throws SAXException {
        switch (qName) {
            case "device":
                Output = dv;
                System.out.println("[Debug] Parsing Done");
                break;
            case "media":
                mediaTag = false;
                break;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        switch (detectedTag) {
            case FILE:
                dv._tmpFile.value = new String(ch, start, length);
                dv.media.add(dv._tmpFile);
                dv.cleanTmpFile();
                detectedTag = tag.NON;
                break;
            case DISPLAYNAME:
                dv.displayname = new String(ch, start, length);
                detectedTag = tag.NON;
                break;
            case DESCRIPTION:
                dv.description = new String(ch, start, length);
                detectedTag = tag.NON;
                break;
            case RELEASENOTE:
                dv.releasenote = new String(ch, start, length);
                detectedTag = tag.NON;
                break;
            case TAG:
                dv._tmpTag.value = new String(ch, start, length);
                dv.tags.add(dv._tmpTag);
                dv.cleanTmpTag();
                detectedTag = tag.NON;
                break;
            case CATEGORY:
                dv.category = new String(ch, start, length);
                detectedTag = tag.NON;
                break;
            case SITE:
                dv.site.append(new String(ch, start, length));
                detectedTag = tag.NON;
                break;
        }
    }
}
