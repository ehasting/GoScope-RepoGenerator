/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.main;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author egilhasting
 */
public class ListFiles {

    public static ArrayList<String> loadFilenameIntoArray(String directory) {
        // Directory path here
        ArrayList<String> _output = new ArrayList<>();
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (File f : listOfFiles) {
            if (f.isFile()) {
                if (f.getName().toLowerCase().endsWith(".zip")) {
                    _output.add(f.getName());
                }

            }
        }
        return _output;
    }
}
