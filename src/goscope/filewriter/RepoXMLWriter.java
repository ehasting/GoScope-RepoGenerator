/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.filewriter;

import goscope.main.XMLTools;
import goscope.model.RepoDeviceModel;
import goscope.model.RepoModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author egilhasting
 */
public class RepoXMLWriter {

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String toXMLFile(RepoModel data, String outputFilename) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(outputFilename));
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(outputStream, "utf-8"));
            out.writeStartDocument("utf-8", "1.0");
            out.writeStartElement("repository");
            out.writeStartElement("devices");
            out.writeAttribute("length", String.valueOf(data.length));
            out.writeAttribute("generation", XMLTools.CalculateCheckSumString(data.toString()));
            for (Iterator<RepoDeviceModel> it = data.devices.iterator(); it.hasNext();) {
                RepoDeviceModel rdm = it.next();
                // device
                out.writeStartElement("device");
                // base
                out.writeStartElement("base");
                out.writeAttribute("id", rdm.baseID);
                out.writeAttribute("author", rdm.baseAuthor);
                out.writeEndElement();
                // displayname
                out.writeStartElement("displayname");
                out.writeCharacters(rdm.displayname);
                out.writeEndElement();
                // description
                out.writeStartElement("description");
                out.writeCharacters(rdm.description);
                out.writeEndElement();
                // category
                out.writeStartElement("category");
                out.writeCharacters(rdm.category);
                out.writeEndElement();
                // tags
                out.writeStartElement("tags");
                for (RepoDeviceModel.tag tag : rdm.tags) {
                    out.writeStartElement("tag");
                    out.writeAttribute("type", tag.type);
                    out.writeCharacters(tag.value);
                    out.writeEndElement();
                }
                //end Tags
                out.writeEndElement();
                //Site
                out.writeStartElement("site");
                out.writeCharacters(rdm.site.toString());
                out.writeEndElement();
                //media
                out.writeStartElement("media");
                for (RepoDeviceModel.file file : rdm.media) {
                    out.writeStartElement("file");
                    out.writeAttribute("mime", file.mime);
                    out.writeAttribute("type", file.type);
                    out.writeCharacters(file.value);
                    out.writeEndElement();
                }
                //end media
                out.writeEndElement();
                //versions
                out.writeStartElement("versions");
                for (RepoDeviceModel.version file : rdm.versions) {
                    out.writeStartElement("version");
                    out.writeAttribute("name", String.valueOf(file.versionNumber));
                    out.writeAttribute("filename", file.filename);
                    String textDate = format.format(file.added);
                    textDate = textDate.replace(' ', 'T');
                    out.writeAttribute("added", textDate);
                    out.writeAttribute("hash", file.hash);
                    out.writeStartElement("release-notes");
                    out.writeCharacters(file.releasenote);
                    out.writeEndElement();
                    out.writeEndElement();
                }
                //end versions
                out.writeEndElement();
                //end Device
                out.writeEndElement();

            }
            out.writeEndElement();
            out.writeEndElement();
            out.writeEndDocument();
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RepoXMLWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(RepoXMLWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;

        }
    }
}
