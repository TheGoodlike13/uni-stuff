package com.goodlike.inputparser.wsdl;

/**
 * Initializes the WSDL parser
 */
public class WSDLParserFactory {

    private static final WSDLParser parser;
    static {
        try {
            parser = new WSDLParser();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize parser.");
        }
    }

    public static WSDLParser getParser() {
        return parser;
    }

}
