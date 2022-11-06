package com.elcom.plus.auth.controller;

import java.io.*;

import com.elcom.plus.auth.dto.response.ResultResponse;
import com.elcom.plus.common.util.soap.SoapConstant;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class  test {
    public static void main(String[] args) {
        ResultResponse response = getMSISDNResponse("192.168.10.2");
        System.out.println("======= code ==== "+response.getCode());
        System.out.println("======= desc ==== "+response.getDesc());
    }
    private static ResultResponse getMSISDNResponse(String ip) {
        ResultResponse result = new ResultResponse();
        try {
            String xml = getXml("mecall", "Gm\\#2022\\$Yk", ip);
            System.out.println("======== xml ==== "+xml);
            HttpPost post = new HttpPost("http://localhost:18280/RadiusGW/Radius");
            post.setEntity(new StringEntity(xml));
            post.setHeader("SOAPAction", "");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            String res_xml = EntityUtils.toString(response.getEntity());
            result.setCode(Integer.parseInt(getFullNameFromXml(res_xml,"code")));
            result.setDesc(getFullNameFromXml(res_xml,"desc"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String getXml(String user, String pass, String ip) {
        String xml = SoapConstant.xmlSoapVaaa;
        xml = xml.replaceAll("USER", user).replaceAll("PASS", pass).replaceAll("IP", ip);
        return xml;
    }

    public static String getFullNameFromXml(String response, String tagName) throws Exception {
        Document xmlDoc = loadXMLString(response);
        NodeList nodeList = xmlDoc.getElementsByTagName(tagName);
        List<String> ids = new ArrayList<String>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node x = nodeList.item(i);
            ids.add(x.getFirstChild().getNodeValue());
            System.out.println(nodeList.item(i).getFirstChild().getNodeValue());
        }
        return ids.get(0);
    }

    public static Document loadXMLString(String response) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(response));

        return db.parse(is);
    }

}
