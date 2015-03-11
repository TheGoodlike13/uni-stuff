package com.goodlike.inputparser.SOAP;

@FunctionalInterface
public interface WSRequest {

    String objectsToRequest(Object... objs);

}
