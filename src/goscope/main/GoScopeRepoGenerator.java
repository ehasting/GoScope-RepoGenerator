/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.main;

import goscope.writer.RepoXMLWriter;
import goscope.model.DeviceModel;
import goscope.model.RepoDeviceModel;
import goscope.parser.DeviceHandler;
import goscope.parser.GenerateRepo;
import goscope.parser.RepoHandler;
import goscope.parser.UpdateWordpress;
import goscope.parser.XMLParser;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ws.commons.util.Base64;

/**
 *
 * @author egilhasting
 */
public class GoScopeRepoGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*RepoWPRPCWriter wpr = new RepoWPRPCWriter();
         ArrayList<String> pict = ListFiles.loadFilenameIntoArray("./");
         System.out.println();
         //wpr.UpdateWordpress();
         
         *
         * wpr.UploadPictures();
         *
         *
         */
        //   MapperHandler mh = new MapperHandler();
        //           XMLParser singleParse1 = new XMLParser(mh);
        //dp.readFromFile("device.xsd", "device.xml");
        //    singleParse1.readFromFile_nonValidate("repo_map.xml");
        //    System.out.println((ArrayList< WordpressMapModel>)singleParse1.parserHandler.getOutput());
        //   System.exit(0);
        String path = ".";
        if (args.length < 1) {
            System.out.println("Please use <repo-path> to start the generation of repo");
            System.exit(2);
        }

        /// 1. Read existing repo.xml file (if not found, nothing to do.), and populate
        /// hashIndex and DeviceRepoModel arrayList
        /// 2. Read directory and device.xml into device array, validate against 
        /// hashIndex, to avoid injecting devices allready in repo.
        /// 3. Add device array into repo array, sorted.
        /// 4. Ouput new repo.xml.
        if (args.length != 0) {
            path = args[0];
        }

        if (!path.endsWith("/")) {
            path = path + "/";
        }

        ArrayList<String> dir = ListFiles.loadFilenameIntoArray(path);


        GenerateRepo genrep = new GenerateRepo(path + "repo.xml");
        UpdateWordpress wpGen = new UpdateWordpress("repo_map.xml");

        for (String d : dir) {
            System.out.println("Filename: " + d);
            RepoHandler rh = new RepoHandler();
            DeviceHandler dh = new DeviceHandler();
            ZipExtractor zipme = new ZipExtractor(path + d, null);
            String e = zipme.unzip("device.xml");

            XMLParser singleParse = new XMLParser(dh);
            //dp.readFromFile("device.xsd", "device.xml");
            singleParse.readFromString("device.xsd", e);
            genrep.addDeviceModel((DeviceModel) singleParse.parserHandler.getOutput(), d);


        }
        for (RepoDeviceModel r : genrep.repo.devices) {
            r.SortVersions();
        }
        RepoXMLWriter xw = new RepoXMLWriter();
        xw.toXMLFile(genrep.repo, path + "repo.xml");
        wpGen.execute(genrep.repo);
        System.exit(0);
    }
}
