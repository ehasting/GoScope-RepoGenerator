/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.model;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author egilhasting
 */
public class RepoDeviceModel extends DeviceModel {

    public RepoDeviceModel() {
        super();
    }
    public ArrayList<version> versions = new ArrayList<>();
    // <version name="1.0" filename="anotherDevice-1.0.zip" added="09:02:04 04-01-2012" hash="md5 or sha1" />
    public class version {

        public version() {
        }
        

        public Float versionNumber = new Float(0.0f);
        public String filename = "";
        public Date added = new Date();
        public String releasenote = "";
        public String hash = "";

        @Override
        public String toString() {
            return "version{" + "versionNumber=" + versionNumber + ", filename=" + filename + ", added=" + added + ", releasenote=" + releasenote + ", hash=" + hash + '}';
        }
        
    }
    public version _tmpVersion = new version();

    public void cleanTmpVersion() {
        _tmpVersion = new version();
    }

    @Override
    public String toString() {
        return  baseID + baseAuthor +  category + displayname + description  + tags +  site  + media +  versions;
    }


}
