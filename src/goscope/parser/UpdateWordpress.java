/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.parser;

import goscope.main.ZipExtractor;
import goscope.model.RepoDeviceModel;
import goscope.model.RepoModel;
import goscope.model.WordpressMapModel;
import goscope.writer.RepoMapXMLWriter;
import goscope.writer.RepoWPRPCWriter;
import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author egilhasting
 */
public class UpdateWordpress {

    public RepoModel repo = new RepoModel();
    private String fileName;
    RepoWPRPCWriter wpWriter = new RepoWPRPCWriter();
    RepoMapXMLWriter repoMapW = new RepoMapXMLWriter();
    private ZipExtractor zip;
    private ArrayList<WordpressMapModel> wordpressMap;

    public UpdateWordpress(String filename) {
        fileName = filename;
        // load existing repo
        File f = new File(filename);
        if (f.exists()) {

            MapperHandler mh = new MapperHandler();
            XMLParser singleParse = new XMLParser(mh);
            singleParse.readFromFile_nonValidate(filename);
            wordpressMap = (ArrayList<WordpressMapModel>) singleParse.parserHandler.getOutput();
        } else {
            System.out.println("[DEBUG] Existing repo_map not found, creating new!");
            wordpressMap = new ArrayList<>();
        }
    }

    private boolean compareFloat(Float number1, Float number2) {
        int number1i = (int) (number1 * 1000000);
        int number2i = (int) (number2 * 1000000);

        if (number1i == number2i) {
            return true;
        } else {
            return false;
        }
    }

    public void execute(RepoModel device) {
        zip = new ZipExtractor();
        ArrayList<WordpressMapModel> outputList = wordpressMap;
        boolean deviceFound = false;
        for (RepoDeviceModel d : device.devices) {
            for (WordpressMapModel wp : wordpressMap) {
                if (wp.deviceId.equalsIgnoreCase(d.baseID)) {
                    deviceFound = true;
                    System.out.println("[DEBUG] Post found, updating existing!");
                    if (!wp.wordpressThumbId.equals("0")) {
                        wpWriter.WP_UpdatePost(d, wp.wordpressId, wp.wordpressThumbId);
                    } else {
                        HashMap<String, Object> upPict = wpWriter.WP_UploadPictureFromByte(d, "image/jpg", zip.unzipToByteArray(d.versionFilename, d.baseID + ".jpg"));
                        wpWriter.WP_UpdatePost(d, wp.wordpressId, (String) upPict.get("id"));
                    }
                    break;
                }
            }
            if (!deviceFound) {
                HashMap<String, Object> upPict = wpWriter.WP_UploadPictureFromByte(d, "image/jpg", zip.unzipToByteArray(d.versions.get(0).filename, d.baseID + ".jpg"));
                System.out.println("[DEBUG] " + upPict);
                int wpId = Integer.parseInt(wpWriter.WP_NewPost(d, (String) upPict.get("id"))); // = create new wordpress and fetch id
                System.out.println("[DEBUG] Post not found, creating new!");
                outputList.add(new WordpressMapModel(d.baseID, String.valueOf(wpId), (String) upPict.get("id")));
                deviceFound = false;

            }
        }
        repoMapW.toXMLFile(outputList, fileName);
    }
}
