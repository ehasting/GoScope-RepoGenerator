/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.model;

/**
 *
 * @author egilhasting
 */
public class WordpressMapModel {

    public String deviceId;
    public String wordpressId;
    public String wordpressThumbId;

    public WordpressMapModel() {
    }

    public WordpressMapModel(String deviceId, String wordpressId, String wordpressThumbId) {
        this.deviceId = deviceId;
        this.wordpressId = wordpressId;
        this.wordpressThumbId = wordpressThumbId;
    }

    @Override
    public String toString() {
        return "WordpressMapModel{" + "deviceId=" + deviceId + ", wordpressId=" + wordpressId + ", wordpressThumbId=" + wordpressThumbId + '}';
    }
}
