/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.parser;

import goscope.main.XMLTools;
import goscope.model.RepoDeviceModel;
import goscope.model.RepoModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author egilhasting
 */
public class RepoHandler extends GoScopeXMLHandler {

    int counter = 0;

    enum tag {

        NON, CATEGORY, FILE, TAG, SITE, DISPLAYNAME, DESCRIPTION, RELEASENOTE, DEVICE
    };
    tag detectedTag = tag.NON;
    RepoDeviceModel dv = new RepoDeviceModel();
    RepoModel rm = new RepoModel();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String currentTag = "";

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        currentTag = qName;
        switch (currentTag) {
            case "devices":
                rm.length = Integer.parseInt(attributes.getValue("length"));
                rm.generation = attributes.getValue("generation");
                break;

            case "base":
                dv.baseID = attributes.getValue("id");
                dv.baseAuthor = attributes.getValue("author");
                break;

            case "version":
                dv._tmpVersion.versionNumber = Float.parseFloat(attributes.getValue("name"));
                dv._tmpVersion.filename = attributes.getValue("filename");
                dv._tmpVersion.hash = attributes.getValue("hash");
                try {
                    String dateValue = attributes.getValue("added").replace('T', ' ');
                    dv._tmpVersion.added = format.parse(dateValue);
                } catch (ParseException ex) {
                    Logger.getLogger(RepoHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "release-notes":
                detectedTag = tag.RELEASENOTE;
                break;

            case "file":
                dv._tmpFile.mime = attributes.getValue("mime");
                dv._tmpFile.type = attributes.getValue("type");
                detectedTag = tag.FILE;
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
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "device":
                rm.devices.add(dv);
                dv = new RepoDeviceModel();
                break;
            case "devices":
                Output = rm;
                System.out.println("[Debug] Parsing Done");
                break;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String value = new String(ch, start, length);

        if (!value.trim().equals("")) {

            switch (currentTag) {
                case "file":
                    dv._tmpFile.value = new String(ch, start, length);
                    dv.media.add(dv._tmpFile);
                    dv.cleanTmpFile();
                    detectedTag = tag.NON;
                    break;
                case "displayname":
                    dv.displayname = new String(ch, start, length);
                    detectedTag = tag.NON;
                    break;
                case "description":
                    dv.description = new String(ch, start, length);
                    detectedTag = tag.NON;
                    break;
                case "release-notes":
                    dv._tmpVersion.releasenote = new String(ch, start, length);
                    dv.versions.add(dv._tmpVersion);
                    dv.cleanTmpVersion();
                    detectedTag = tag.NON;
                    break;
                case "tag":
                    dv._tmpTag.value = new String(ch, start, length);
                    dv.tags.add(dv._tmpTag);
                    dv.cleanTmpTag();
                    detectedTag = tag.NON;
                    break;
                case "category":
                    dv.category = new String(ch, start, length);
                    detectedTag = tag.NON;
                    break;
                case "site":
                    dv.site.append(new String(ch, start, length));
                    detectedTag = tag.NON;
                    break;
            }
        }
    }
}
