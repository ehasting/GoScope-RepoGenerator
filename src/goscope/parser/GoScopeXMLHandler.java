/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.parser;

import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author egilhasting
 */
public class GoScopeXMLHandler extends DefaultHandler {

    protected Object Output;
    public String fileName = "";

    public Object getOutput() {
        return Output;
    }
}
