/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.model;

import java.util.ArrayList;

/**
 *
 * @author egilhasting
 */
public class RepoModel {

    public ArrayList<RepoDeviceModel> devices = new ArrayList<>();
    public String generation = "";
    public int length = 0;
    public String version_build = "1.0";

    public boolean checkGeneration(String hash) {
        if (generation.equalsIgnoreCase(generation)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "length="+length+" " +devices.toString();
    }
}
