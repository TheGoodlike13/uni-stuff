package com.goodlike.inputparser.SOAP;

@SuppressWarnings("unused")
public class DefaultWSRequest implements WSRequest {

    private final static String XML_START = "<web:execute>";
    private final static String ARG_START = "<arg0>";
    private final static String ARG_END = "</arg0>";
    private final static String XML_END = "</web:execute>";

    @Override
    public String objectsToRequest(Object... objs) {
        if ((objs == null) || (objs.length < 1))
            throw new IllegalArgumentException("Default service takes at least 1 parameter.");

        StringBuilder sb = new StringBuilder(XML_START);
        for (Object o : objs)
            sb.append(ARG_START).append(o).append(ARG_END);

        sb.append(XML_END);
        return sb.toString();
    }

}
