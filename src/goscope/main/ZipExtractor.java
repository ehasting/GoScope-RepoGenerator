/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author egilhasting
 */
public class ZipExtractor {

    public ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private String _zipFile;
    private String _location;

    public ZipExtractor() {
    }

    public ZipExtractor(String zipFile, String location) {
        _zipFile = zipFile;
        _location = location;

        _dirChecker("");
    }

    public byte[] unzipToByteArray(String zipArchive, String desiredFile) {
        bos = new ByteArrayOutputStream();
        byte[] op = null;
        try {
            File _f = new File(zipArchive);
            _f.canRead();
            FileInputStream fin = new FileInputStream(_f);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze;


            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                } else {
                    bos = new ByteArrayOutputStream();
                    //FileOutputStream fout = new FileOutputStream(_location + ze.getName());
                    /**
                     * ** Changes made below ***
                     */
                    int c;
                    if (ze.getName().toString().equalsIgnoreCase(desiredFile)) {

                        byte[] buf = new byte[1];
                        while ((c = zin.read(buf)) != -1) {
                            bos.write(buf);
                        }
                        op = bos.toByteArray();

                    }
                }
            }
            zin.closeEntry();
            fin.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return op;
    }

    public String unzip(String desiredFile) {
        String strUnzipped = "";
        try {
            File _f = new File(_zipFile);
            _f.canRead();
            FileInputStream fin = new FileInputStream(_f);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {

                    //FileOutputStream fout = new FileOutputStream(_location + ze.getName());

                    /**
                     * ** Changes made below ***
                     */
                    if (ze.getName().toString().equalsIgnoreCase(desiredFile)) {
                        long fsize = ze.getSize();
                        byte[] bytes = new byte[0];
                        int cnt = 0;
                        int _c;
                        while ((_c = zin.read()) != -1) {
                            cnt++;
                            strUnzipped += (char) _c;
                        }



                        /**
                         * * REMOVED if (ze.getName() == desiredFile) { for
                         * (int c = zin.read(); c != -1; c = zin.read()) {
                         * strUnzipped += c; //fout.write(c); }
                         */
                    }

                    zin.closeEntry();
                    //fout.close();
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return strUnzipped;
    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }
}
