package com.goodlike.inputparser.wsdl;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Parses the WSDL of a web service
 */
public class WSDLParser {

    private final SAXParser parser;

    public WSDLParser() throws ParserConfigurationException, SAXException {
        SAXParserFactory parserFactor = SAXParserFactory.newInstance();
        parser = parserFactor.newSAXParser();
    }

    public Map<String, String> parse(String url, String operation, String port, List<String> qualityNames) {
        WSDLSAXHandler handler = new WSDLSAXHandler(operation, port, qualityNames);
        InputStream xmlStream;
        try {
            xmlStream = new URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load WSDL from " + url);
        }
        System.out.println("Stream open.");
        try {
            parser.parse(xmlStream, handler);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed parse WSDL at " + url);
        }
        handler.getBind();
        Map<String, String> serviceInfo = handler.getResults();
        System.out.println(serviceInfo);
        return serviceInfo;
    }

}
