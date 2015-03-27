package com.goodlike.config;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * XML parsing configuration for config.xml file
 */
public class ConfigSAXHandler extends DefaultHandler {

    private Map<String, String> services = new HashMap<>();
    private Map<String, ServiceMethod> methods = new HashMap<>();
    private Map<String, String> directories = new HashMap<>();
    private Map<String, String> qualityNames = new HashMap<>();

    private String id;
    private String xmlRawString;
    private ServiceMethod serviceMethod;

    public ConfigSAXHandler() {
        super();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "service":
            case "directory":
                id = attributes.getValue("id").trim();
                break;
            case "method":
                id = attributes.getValue("id");
                serviceMethod = new ServiceMethod();
                break;
            case "quality":
                id = attributes.getValue("id");
                break;
        }
    }

    @Override
    public void characters (char[] ch, int start, int length) throws SAXException {
        if (length > 0)
            xmlRawString = new String(ch, start, length).trim();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "URL":
                services.put(id, xmlRawString);
                break;
            case "name":
                serviceMethod.setName(xmlRawString);
                break;
            case "port":
                serviceMethod.setPort(xmlRawString);
                break;
            case "request":
                serviceMethod.setRequest(xmlRawString);
                break;
            case "response":
                serviceMethod.setResponse(xmlRawString);
                break;
            case "service-name":
                serviceMethod.setServiceName(xmlRawString);
                methods.put(id, serviceMethod);
                break;
            case "methodID":
                directories.put(xmlRawString, id);
                break;
            case "quality":
                qualityNames.put(id, xmlRawString);
                break;
        }
    }

    public void getResults(Map<String, String> services, Map<String, ServiceMethod> methods,
                           Map<String, String> directories, Map<String, String> qualityNames) {

        services.putAll(this.services);
        methods.putAll(this.methods);
        directories.putAll(this.directories);
        qualityNames.putAll(this.qualityNames);
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder full = new StringBuilder();
        for (Map.Entry<String, String> s : services.entrySet())
            full.append(s).append("\n");
        for (Map.Entry<String, ServiceMethod> s : methods.entrySet())
            full.append(s).append("\n");
        for (Map.Entry<String, String> s : directories.entrySet())
            full.append(s).append("\n");
        for (Map.Entry<String, String> s : qualityNames.entrySet())
            full.append(s).append("\n");
        return full.toString();
    }

}
