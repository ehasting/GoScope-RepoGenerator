/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author egilhasting
 */
public class DeviceModel {

    public String baseID = "";
    public String baseAuthor = "";
    public String category = "";
    public Float versionNumber = new Float(0.0f);
    public String versionFilename = "";
    public Date versionAdded = new Date();
    public String releasenote = "";
    public String displayname = "";
    public String description = "";
    public ArrayList<tag> tags = new ArrayList<>();
    public StringBuilder site = new StringBuilder();
    public ArrayList<file> media = new ArrayList<>();

    public class tag {

        public String type = "";
        public String value = "";

        @Override
        public String toString() {
            return "tag{" + "type=" + type + ", value=" + value + '}';
        }
    }

    public class file {

        public String mime = "";
        public String type = "";
        public String value = "";

        @Override
        public String toString() {
            return "file{" + "mime=" + mime + ", type=" + type + ", value=" + value + '}';
        }
    }
    public file _tmpFile = new file();
    public tag _tmpTag = new tag();

    public void cleanTmpFile() {
        _tmpFile = new file();
    }

    public void cleanTmpTag() {
        _tmpTag = new tag();
    }

    public String getBaseID() {
        return baseID;
    }

    public void setBaseID(String baseID) {
        this.baseID = baseID;
    }

    public String getBaseAuthor() {
        return baseAuthor;
    }

    public void setBaseAuthor(String baseAuthor) {
        this.baseAuthor = baseAuthor;
    }

    public Float getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Float versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionFilename() {
        return versionFilename;
    }

    public void setVersionFilename(String versionFilename) {
        this.versionFilename = versionFilename;
    }

    public Date getVersionAdded() {
        return versionAdded;
    }

    public void setVersionAdded(Date versionAdded) {
        this.versionAdded = versionAdded;
    }

    public String getReleasenote() {
        return releasenote;
    }

    public void setReleasenote(String releasenote) {
        this.releasenote = releasenote;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<tag> tags) {
        this.tags = tags;
    }

    public StringBuilder getSite() {
        return site;
    }

    public void setSite(StringBuilder site) {
        this.site = site;
    }

   
    public ArrayList<file> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<file> media) {
        this.media = media;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "DeviceModel{" + "baseID=" + baseID + ", baseAuthor=" + baseAuthor + ", category=" + category + ", versionNumber=" + versionNumber + ", versionFilename=" + versionFilename + ", versionAdded=" + versionAdded + ", releasenote=" + releasenote + ", displayname=" + displayname + ", description=" + description + ", tags=" + tags + ", site=" + site + ", media=" + media +'}';
    }

    
}
