/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.main;

import goscope.filewriter.RepoXMLWriter;
import goscope.model.DeviceModel;
import goscope.model.RepoModel;
import goscope.parser.DeviceHandler;
import goscope.parser.GenerateRepo;
import goscope.parser.RepoHandler;
import goscope.parser.XMLParser;
import java.util.ArrayList;

/**
 *
 * @author egilhasting
 */
public class GoScopeRepoGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        /// 1. Read existing repo.xml file (if not found, nothing to do.), and populate
        /// hashIndex and DeviceRepoModel arrayList
        /// 2. Read directory and device.xml into device array, validate against 
        /// hashIndex, to avoid injecting devices allready in repo.
        /// 3. Add device array into repo array, sorted.
        /// 4. Ouput new repo.xml.

        ArrayList<String> dir = ListFiles.loadFilenameIntoArray(".");
        GenerateRepo genrep = new GenerateRepo("repo.xml");

        for (String d : dir) {
            System.out.println("Filename: " + d);
            RepoHandler rh = new RepoHandler();
            DeviceHandler dh = new DeviceHandler();
            ZipExtractor zipme = new ZipExtractor(d, null);
            String e = zipme.unzip("device.xml");

            XMLParser singleParse = new XMLParser(dh);
            //dp.readFromFile("device.xsd", "device.xml");
            singleParse.readFromString("device.xsd",e);
            genrep.addDeviceModel( (DeviceModel)singleParse.parserHandler.getOutput(), d );

        }
        System.out.println(genrep.repo.toString());
        RepoXMLWriter xw = new RepoXMLWriter();
        xw.toXMLFile(genrep.repo, "testOutput.xml");
    }
}