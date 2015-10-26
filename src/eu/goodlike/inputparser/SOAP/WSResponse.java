package eu.goodlike.inputparser.SOAP;

/**
 * Turns a string from web service method back into an object
 */
@FunctionalInterface
public interface WSResponse {

    Object responseToObject(String response);

}
