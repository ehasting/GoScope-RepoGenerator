/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.parser;

import goscope.main.XMLTools;
import goscope.model.DeviceModel;
import goscope.model.RepoDeviceModel;
import goscope.model.RepoModel;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author egilhasting
 */
public class GenerateRepo {

    public RepoModel repo = new RepoModel();
    private String fileName;

    public GenerateRepo(String filename) {
        // load existing repo
        File f = new File(filename);
        if (f.exists()) {
            RepoHandler rh = new RepoHandler();
            XMLParser repoFile = new XMLParser(rh);
            repoFile.readFromFile_nonValidate(filename);
            repo = (RepoModel) repoFile.parserHandler.Output;
        } else {
            System.out.println("[DEBUG] Existing repo not found, creating new!");
            repo = new RepoModel();
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

    public void addDeviceModel(DeviceModel device, String fileName) {
        this.fileName = fileName;
        boolean deviceFound = false;
        float highestVersion = 0.0f;
        int fondCount = -1;
        for (RepoDeviceModel drm : repo.devices) {
            fondCount++;
            if (drm.baseID.equalsIgnoreCase(device.baseID)) {
                deviceFound = true;
                break;
            }

        }
        if (deviceFound) {
            System.out.println("[DEBUG] Device already in repo.");
            boolean newVersion = true;

            for (RepoDeviceModel.version ver : repo.devices.get(fondCount).versions) {
                if (compareFloat(device.versionNumber, ver.versionNumber)) {
                    newVersion = false;
                    System.out.println("[DEBUG] Device version already in database (" + ver.versionNumber + ")");
                }
                if (ver.versionNumber > highestVersion) {
                    highestVersion = ver.versionNumber;
                    //System.out.println("[DEBUG] Searching for higest version (" + highestVersion + ")");
                }
            }
            if (newVersion) {
                RepoDeviceModel rdm = new RepoDeviceModel();
                if (highestVersion < device.versionNumber) {
                    System.out.println("[DEBUG] Updating main text, higher version found (" + device.versionNumber + ")");
                    repo.devices.get(fondCount).baseAuthor = device.baseAuthor;
                    repo.devices.get(fondCount).baseID = device.baseID;
                    repo.devices.get(fondCount).category = device.category;
                    repo.devices.get(fondCount).description = device.description;
                    repo.devices.get(fondCount).displayname = device.displayname;
                    repo.devices.get(fondCount).media = device.media;
                    repo.devices.get(fondCount).site = device.site;
                    repo.devices.get(fondCount).tags = device.tags;
                }
                rdm._tmpVersion.added = device.versionAdded;
                rdm._tmpVersion.filename = device.versionFilename;
                rdm._tmpVersion.releasenote = device.releasenote;
                rdm._tmpVersion.versionNumber = device.versionNumber;
                rdm._tmpVersion.hash = XMLTools.CalculateCheckSumString(fileName);
                repo.devices.get(fondCount).versions.add(rdm._tmpVersion);
                //rdm.cleanTmpVersion();
                System.out.println("[DEBUG] New Version added to Database.");
            }
        } else {
            try {
                System.out.println("[DEBUG] Device newly added into repo.");
                // copy deviceModel to an repoDeviceModel instance.
                RepoDeviceModel rdm = new RepoDeviceModel();
                rdm.baseAuthor = device.baseAuthor;
                rdm.baseID = device.baseID;
                rdm.category = device.category;
                rdm.description = device.description;
                rdm.displayname = device.displayname;
                rdm.media = device.media;
                rdm.site = device.site;
                rdm.tags = device.tags;
                rdm._tmpVersion.added = device.versionAdded;
                rdm._tmpVersion.filename = device.versionFilename;
                rdm._tmpVersion.releasenote = device.releasenote;
                rdm._tmpVersion.versionNumber = device.versionNumber;
                rdm._tmpVersion.hash = XMLTools.CalculateCheckSumFile(fileName);
                rdm.versions.add(rdm._tmpVersion);
                //rdm.cleanTmpVersion();
                repo.devices.add(rdm);
            } catch (Exception ex) {
                Logger.getLogger(GenerateRepo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
