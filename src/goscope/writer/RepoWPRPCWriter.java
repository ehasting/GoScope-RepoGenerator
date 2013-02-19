/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goscope.writer;

import com.sun.xml.internal.messaging.saaj.util.Base64;
import goscope.model.DeviceModel;
import goscope.model.RepoDeviceModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.ws.commons.util.Base64;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;

/**
 *
 * @author egilhasting
 */
public class RepoWPRPCWriter {

    private String rpcUrl = "http://www.scopeportal.com/xmlrpc.php";
    //private String rpcUrl = "http://www.higen.org/xmlrpc.php";
    private String username = "admin";
    private String password = "rmyd4me6";
    private HashMap<String, Object> result;

    public static class wpImageReturn {

        String id;
        String file;
        String url;
        String type;
    }

    public HashMap<String, Object> UploadPicture(RepoDeviceModel d) {
        FileInputStream pict = null;
        try {
            String fileToUse = "";
            String extToUse = "";
            File f;
            String[] supportedFiles = new String[]{"jpg", "jpeg", "png", "gif"};
            for (String ext : supportedFiles) {
                f = new File(d.baseID.toLowerCase() + "." + ext);
                if (f.exists()) {
                    fileToUse = d.baseID.toLowerCase() + "." + ext;
                    extToUse = ext;
                    break;
                }
            }
            pict = new FileInputStream(fileToUse);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int c;
            byte[] buf = new byte[128];
            while ((c = pict.read(buf)) != -1) {
                bos.write(buf);
            }
            //String picFile = Base64.encodeBase64(pict.);

            XmlRpcClient client = new XmlRpcClient(rpcUrl, Boolean.FALSE);
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", d.baseID);
            data.put("type", extToUse);
            data.put("bits", bos.toByteArray());
            data.put("overwrite", "true");
            //[] {, "description=Test også", "mt_allow_comments=1", "mt_allow_pings=0", "post_type=post", "mt_keywords=tag34","categories={Mixer,Synth}"};
            Object[] params = new Object[]{new Integer(389), username, password, data, 0};
            result = (HashMap<String, Object>) client.invoke("wp.uploadFile", params);
            System.out.println(result.toString());


        } catch (XmlRpcException | IOException ex) {
            Logger.getLogger(RepoWPRPCWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pict.close();
            } catch (IOException ex) {
                Logger.getLogger(RepoWPRPCWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
        }
    }

    public HashMap<String, Object> WP_UploadPictureFromByte(RepoDeviceModel d, String fileType, byte[] image) {
        try {
            //String picFile = Base64.encodeBase64();

            XmlRpcClient client = new XmlRpcClient(rpcUrl, Boolean.FALSE);
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", d.baseID + ".jpg");
            data.put("type", "image/jpeg");
            data.put("bits", image);
            data.put("overwrite", "false");
            //[] {, "description=Test også", "mt_allow_comments=1", "mt_allow_pings=0", "post_type=post", "mt_keywords=tag34","categories={Mixer,Synth}"};
            Object[] params = new Object[]{new Integer(389), username, password, data, 0};
            result = (HashMap<String, Object>) client.invoke("wp.uploadFile", params);
        } catch (XmlRpcException | IOException ex) {
            Logger.getLogger(RepoWPRPCWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return result;
        }
    }

    public void WP_UpdatePost(RepoDeviceModel d, String postId, String thumbId) {
        int cnt;
        try {
            XmlRpcClient client = new XmlRpcClient(rpcUrl, Boolean.TRUE);
            //[] {, "description=Test også", "mt_allow_comments=1", "mt_allow_pings=0", "post_type=post", "mt_keywords=tag34","categories={Mixer,Synth}"};
            Object[] params = new Object[]{0, username, password, Integer.parseInt(postId), generateList(d, thumbId), true};
            try {
                boolean postUpdateOk = (boolean) client.invoke("wp.editPost", params);
            } catch (XmlRpcException | XmlRpcFault ex) {
                Logger.getLogger(RepoWPRPCWriter.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(RepoWPRPCWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String WP_NewPost(RepoDeviceModel d, String thumbId) {
        int cnt;
        String postId;
        try {

            XmlRpcClient client = new XmlRpcClient(rpcUrl, Boolean.TRUE);

            Object[] params = new Object[]{0, username, password, generateList(d, thumbId)};
            try {
                postId = (String) client.invoke("wp.newPost", params);
                if (postId != null) {
                    return postId;
                } else {
                    return "fail";
                }
            } catch (XmlRpcException | XmlRpcFault ex) {
                Logger.getLogger(RepoWPRPCWriter.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(RepoWPRPCWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Map<String, Object> generateList(RepoDeviceModel d, String thumbId) {
        int cnt;
        Map<String, String[]> terms_names = new HashMap<>();
        Map<String, Object> list = new HashMap<>();

        String[] catSplit = d.category.split("/");

        ArrayList<String> tagListArray = new ArrayList<>();
        cnt = 0;
        for (String c : catSplit) {
            if (cnt == 0) {
                terms_names.put("category", new String[]{c});
            } else {
                tagListArray.add(c);
            }
            cnt++;
        }
        //

        cnt = 0;
        for (DeviceModel.tag t : d.tags) {
            tagListArray.add(t.type + ": " + t.value);
            cnt++;
        }
        String[] tagOut = new String[tagListArray.size()];
        tagOut = tagListArray.toArray(tagOut);
        terms_names.put("post_tag", tagListArray.toArray(tagOut));

        // list of media ids.

        StringBuilder postContent = new StringBuilder();
        postContent.append("[device_info author=\"").append(d.baseAuthor).
                append("\" device_name=\"").append(d.displayname).append("\" url=\"").
                append(d.site).append("\"]");
        postContent.append("\n\n");
        postContent.append("[gallery size=\"medium\" link=\"file\"]");
        postContent.append("<a href=\"http://repo.scopeportal.com/" + d.versions.get(0).filename + "\">Download Latest Version</a>");
        postContent.append("\n\n\n");
        postContent.append(d.description);
        postContent.append("\n\n\n\n\n\n");
        String urlprefix = "";
        for (RepoDeviceModel.file rV : d.media) {
            switch (rV.type) {
                case "repo-path":
                    urlprefix = "http://repo.scopeportal.com/";
                    break;
                case "url":
                    urlprefix = "";
                    break;
            }
            if (rV.mime.contains("image")) {
                // <img src="http://www.google.com" alt="asd" />
                postContent.append("<img src=\"" + urlprefix + "" + rV.value + "\"/>\n");
            } else if (rV.mime.contains("audio")) {
                postContent.append("Sound Example: [audio:" + urlprefix + "" + rV.value + "\"]\n");
            } else {
                postContent.append("<a href=\"" + urlprefix + "" + rV.value + "\">" + rV.value + " Download</a>\n");
            }
        }

        postContent.append("\n\n\n\n\n\n");
        postContent.append("<h2>Download and change notes</h2><ul>");
        for (RepoDeviceModel.version rV : d.versions) {
            postContent.append("<li>").append(rV.versionNumber).
                    append(" - ").append(rV.releasenote).append(" (").
                    append(rV.added).append(") - ").append("<a href=\"http://repo.scopeportal.com/" + rV.filename + "\">Download</a>");
        }
        postContent.append("</ul>");
        list.put("post_title", d.baseAuthor + ": " + d.displayname);
        list.put("post_content", postContent.toString());
        list.put("comment_status", "open");
        list.put("post_type", "post");
        if (thumbId != null) {
            list.put("post_thumbnail", thumbId);
        }
        list.put("terms_names", terms_names);
        list.put("post_name", d.displayname);
        list.put("post_status", "publish");
        return list;
    }

    public void SearchWordpressPosts() {
        try {
            XmlRpcClient client = new XmlRpcClient(rpcUrl, Boolean.TRUE);
            Map<String, Object> list = new HashMap<>();

            Map<String, String[]> catList = new HashMap<>();
            catList.put("category", new String[]{"16", "52"});
            Map<String, String[]> tagList = new HashMap<>();
            tagList.put("post_tag", new String[]{"testTag1", "testTag2"});

            UUID id = UUID.randomUUID();

            list.put("post_title", "Test Title RPC " + new Date());
            list.put("post_content", "Test Også");
            list.put("comment_status", "open");
            list.put("post_type", "post");
            list.put("post_thumbnail", 439);
            list.put("terms", catList);
            list.put("terms_names", tagList);
            list.put("post_name", id.toString());
            list.put("post_status", "pending");
            //[] {, "description=Test også", "mt_allow_comments=1", "mt_allow_pings=0", "post_type=post", "mt_keywords=tag34","categories={Mixer,Synth}"};
            Object[] params = new Object[]{0, username, password, list, true};
            String PostId = (String) client.invoke("wp.getPosts", params);

        } catch (XmlRpcException | MalformedURLException | XmlRpcFault ex) {
            Logger.getLogger(RepoWPRPCWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }
}
