package com.goodlike.config;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigSAXHandler extends DefaultHandler {

    private Map<String, String> services = new HashMap<>();
    private Map<String, ServiceMethod> methods = new HashMap<>();
    private Map<String, String> directories = new HashMap<>();
    private List<String> qualityNames = new ArrayList<>();

    private String id;
    private String xmlRawString;
    private ServiceMethod serviceMethod;

    public ConfigSAXHandler() {
        super();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("service") || qName.equals("directory")) {
            id = attributes.getValue("id").trim();
        }
        else if (qName.equals("method")) {
            id = attributes.getValue("id");
            serviceMethod = new ServiceMethod();
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
                qualityNames.add(xmlRawString);
                break;
        }
    }

    public void getResults(Map<String, String> services, Map<String, ServiceMethod> methods,
                           Map<String, String> directories, List<String> qualityNames) {

        services.putAll(this.services);
        methods.putAll(this.methods);
        directories.putAll(this.directories);
        qualityNames.addAll(this.qualityNames);
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
        return full.toString();
    }

}
