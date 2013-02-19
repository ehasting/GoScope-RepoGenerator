/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author egilhasting
 */
public class XMLTools {

    private InputSource xmlfile = null;
    private SchemaFactory factory = null;
    private Schema schema = null;
    private File file;
    private InputStream xmlString;

    public boolean ValidateXMLFile(String XSDFile, String XMLFile) {
        try {
            file = new File(XMLFile);
            factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schema = factory.newSchema(new File(XSDFile));
            InputStream inputStream = new FileInputStream(file);
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            xmlfile = new InputSource(reader);
            xmlfile.setEncoding("UTF-8");
            Validator validator = schema.newValidator();
            SAXSource source = new SAXSource(xmlfile);
            validator.validate(source);
        } catch (IOException | SAXException ex) {
            Logger.getLogger(XMLTools.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean ValidateXMLString(String XSDFile, String XMLString) {
        try {
            factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schema = factory.newSchema(new File(XSDFile));
            Reader reader = new StringReader(XMLString);
            xmlfile = new InputSource(reader);
            xmlfile.setEncoding("UTF-8");
            Validator validator = schema.newValidator();
            SAXSource source = new SAXSource(xmlfile);
            validator.validate(source);
            xmlString = new ByteArrayInputStream(XMLString.getBytes());
        } catch (IOException | SAXException ex) {
            Logger.getLogger(XMLTools.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public InputStream getXmlString() {
        return xmlString;
    }

    public void setXmlString(InputStream xmlString) {
        this.xmlString = xmlString;
    }

    public static String CalculateCheckSumFile(String datafile) throws Exception {


    MessageDigest md = MessageDigest.getInstance("SHA-256");
    FileInputStream fis = new FileInputStream(datafile);
    byte[] dataBytes = new byte[1024];

    int nread = 0;

    while ((nread = fis.read(dataBytes)) != -1) {
      md.update(dataBytes, 0, nread);
    };

    byte[] mdbytes = md.digest();

    //convert the byte to hex format
    StringBuilder sb = new StringBuilder("");
    for (int i = 0; i < mdbytes.length; i++) {
    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();

  }
    static public String CalculateCheckSumString(String input) {
        String hashCode;
        MessageDigest algorithm = null;

        try {
            algorithm = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println("Cannot find digest algorithm");
            System.exit(1);
        }

        byte[] defaultBytes = input.getBytes();
        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        hashCode = hexString.toString();
        /// DEBUG: System.out.println("SHA-256 (" + input + ") = " + hashCode);
        return hashCode;
    }

}
