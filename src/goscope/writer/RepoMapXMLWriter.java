/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.writer;

import goscope.main.XMLTools;
import goscope.model.WordpressMapModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author egilhasting
 */
public class RepoMapXMLWriter {

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String toXMLFile(ArrayList<WordpressMapModel> data, String outputFilename) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(outputFilename));
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(outputStream, "utf-8"));
            out.writeStartDocument("utf-8", "1.0");
            out.writeStartElement("mapping");
            out.writeAttribute("version", "1.0");
            out.writeAttribute("length", String.valueOf(data.size()));
            out.writeAttribute("generation", XMLTools.CalculateCheckSumString(data.toString()));
            for (WordpressMapModel rdm : data) {
                // device
                out.writeStartElement("map");
                out.writeAttribute("deviceId", rdm.deviceId);
                out.writeAttribute("wpId", rdm.wordpressId);
                out.writeAttribute("thumbId", rdm.wordpressThumbId);
                out.writeEndElement(); // End map
            }
            out.writeEndElement(); // End mapping
            out.writeEndDocument();
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RepoMapXMLWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(RepoMapXMLWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;

        }
    }
}
