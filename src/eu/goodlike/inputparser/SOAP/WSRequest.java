package eu.goodlike.inputparser.SOAP;

/**
 * Turns objects into a string the web service method can understand
 */
@FunctionalInterface
public interface WSRequest {

    String objectsToRequest(Object... objs);

}
