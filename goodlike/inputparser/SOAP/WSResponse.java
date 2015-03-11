package com.goodlike.inputparser.SOAP;

@FunctionalInterface
public interface WSResponse {

    Object responseToObject(String response);

}
