package com.goodlike.inputparser.SOAP;

@SuppressWarnings("unused")
public class DefaultWSResponse implements WSResponse {

    private final static String PATTERN_START = "<return>";
    private final static String PATTERN_END = "</return>";

    @Override
    public Object responseToObject(String response) {
        int responseStart = response.indexOf(PATTERN_START);
        if (responseStart < 0)
            throw new IllegalArgumentException("Specified response does not have the necessary values.");

        responseStart += PATTERN_START.length();
        int responseEnd = response.indexOf(PATTERN_END);
        return response.substring(responseStart, responseEnd);
    }

}
