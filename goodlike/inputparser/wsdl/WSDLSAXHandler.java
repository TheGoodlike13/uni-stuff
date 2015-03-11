package com.goodlike.inputparser.wsdl;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.goodlike.inputparser.wsdl.Constants.*;

class WSDLSAXHandler extends DefaultHandler {

    private final static char NAMESPACE_CH = ':';

    private final String operationName;
    private final String portName;
    private String bindingName;
    private String parsedName;
    private String parsedBindingName;

    private String qualityName = "";
    private final List<String> qualityNames;

    private Map<String, Map<String, String>> bindings = new HashMap<>();

    private Map<String, String> serviceInfo = new HashMap<>();

    public WSDLSAXHandler(String operationName, String portName, List<String> qualityNames) {
        super();
        serviceInfo.put("methodName", operationName);
        this.operationName = operationName;
        this.portName = portName;
        this.qualityNames = qualityNames;
    }

    private String noNameSpace(String qName) {
        String justName = qName;
        int nameSpaceEnd = justName.indexOf(NAMESPACE_CH);
        while (nameSpaceEnd >= 0) {
            justName = justName.substring(nameSpaceEnd + 1);
            nameSpaceEnd = justName.indexOf(NAMESPACE_CH);
        }
        return justName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!"".equals(qualityName) && operationName.equals(parsedName)) {
            StringBuilder sb = new StringBuilder();
            for (int i = start; i < start + length; i++)
                sb.append(ch[i]);
            String s = sb.toString().trim();
            if (!s.isEmpty())
                serviceInfo.put(qualityName, s);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        String justName = noNameSpace(qName);
        if (justName.equals(DEF_STR)) {
            String def = attributes.getValue(DEF_ID);
            if (def.charAt(def.length() - 1) != '/')
                def += '/';
            serviceInfo.put(DEF_ID, def);
        }
        else if (justName.equals(BIND_STR)) {
            String bindingName = attributes.getValue(NAME_ID);
            if (bindingName != null) {
                parsedBindingName = bindingName;
                Map<String, String> bindingInfo = new HashMap<>();
                bindings.put(bindingName, bindingInfo);
            }
        }
        else if (justName.equals(OP_STR)) {
            String operation = attributes.getValue(NAME_ID);
            if (operation != null) {
                parsedName = operation;
            }
            else {
                String soapAction = attributes.getValue(ACTION_ID);
                bindings.get(parsedBindingName).put(parsedName, soapAction);
            }
        }
        else if (justName.equals(SRVC_STR)) {
            String service = attributes.getValue(NAME_ID);
            serviceInfo.put(NAME_ID, service);
        }
        else if (justName.equals(PORT_STR)) {
            parsedName = attributes.getValue(NAME_ID);
            if (parsedName.equals(portName)) {
                String binding = attributes.getValue(BIND_STR);
                bindingName = noNameSpace(binding);
            }
        }
        else if ((justName.equals(ADDR_STR)) && (parsedName.equals(portName))) {
            String address = attributes.getValue(ADDR_ID);
            serviceInfo.put(ADDR_ID, address);
        }
        else if (qualityNames.contains(justName)) {
            qualityName = justName;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String justName = noNameSpace(qName);
        if (qualityNames.contains(justName)) {
            qualityName = "";
        }
    }

    public void getBind() {
        String action = bindings.get(bindingName).get(operationName);
        serviceInfo.put(ACTION_ID, action);
    }

    public Map<String, String> getResults() {
        return serviceInfo;
    }

    @Override
    public String toString() {
        StringBuilder full = new StringBuilder();
        for (Map.Entry<String, String> s : serviceInfo.entrySet())
            full.append(s).append("\n");
        return full.toString();
    }


}
