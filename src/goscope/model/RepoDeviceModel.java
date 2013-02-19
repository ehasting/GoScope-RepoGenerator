/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 *
 * @author egilhasting
 */
public class RepoDeviceModel extends DeviceModel {

    static public int multiplyer = 1000000;

    public RepoDeviceModel() {
        super();
    }

    public static int convertVersion(Float Number) {
        return (int) (Number * multiplyer);
    }

    public static boolean compareFloat(Float number1, Float number2) {
        int number1i = (int) (number1 * multiplyer);
        int number2i = (int) (number2 * multiplyer);

        if (number1i == number2i) {
            return true;
        } else {
            return false;
        }
    }
    public ArrayList<version> versions = new ArrayList<>();
    // <version name="1.0" filename="anotherDevice-1.0.zip" added="09:02:04 04-01-2012" hash="md5 or sha1" />

    public void SortVersions() {
        ArrayList<version> unsortedVersions = versions;
        ArrayList<version> _sortedVersions = new ArrayList<>();
        int hiVersion = 0;
        int hiVersionIndex = 0;
        while (unsortedVersions.size() > 0) {
            for (int cnt = 0; unsortedVersions.size() > cnt; cnt++) {
                if (hiVersion < convertVersion(unsortedVersions.get(cnt).versionNumber)) {
                    hiVersion = convertVersion(unsortedVersions.get(cnt).versionNumber);
                    hiVersionIndex = cnt;

                }
            }
            System.out.println("Version/ID: " +hiVersionIndex);
            _sortedVersions.add(unsortedVersions.get(hiVersionIndex));
            unsortedVersions.remove(hiVersionIndex);
            hiVersion = 0;
            hiVersionIndex = 0;
        }
        versions =  _sortedVersions;
    }

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
        return baseID + baseAuthor + category + displayname + description + tags + site + media + versions;
    }
}
