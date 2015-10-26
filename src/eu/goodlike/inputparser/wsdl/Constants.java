package com.goodlike.inputparser.wsdl;

/**
 * Convenience class for WSDL XML parser
 */
public class Constants {

    public final static String DEF_STR = "definitions";
    public final static String OP_STR = "operation";
    public final static String SRVC_STR = "service";
    public final static String PORT_STR = "port";
    public final static String ADDR_STR = "address";
    public final static String BIND_STR = "binding";

    public final static String DEF_ID = "targetNamespace";
    public final static String NAME_ID = "name";
    public final static String ACTION_ID = "soapAction";
    public final static String ADDR_ID = "location";

    private Constants() {
        throw new AssertionError();
    }

}
