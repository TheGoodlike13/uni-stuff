
package com.goodlike.plan.chaining;

import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ChainingServiceService", targetNamespace = "http://chaining.mif.vu.lt/", wsdlLocation = "http://juliuschainingexample.appspot.com/ChainingServiceService.wsdl")
public class ChainingServiceService
    extends Service
{

    private final static URL CHAININGSERVICESERVICE_WSDL_LOCATION;
    //private final static WebServiceException CHAININGSERVICESERVICE_EXCEPTION;
    private final static QName CHAININGSERVICESERVICE_QNAME = new QName("http://chaining.mif.vu.lt/", "ChainingServiceService");

    static {
        URL url;
        //WebServiceException e = null;
        try {
            url = new URL("http://juliuschainingexample.appspot.com/ChainingServiceService.wsdl");
        } catch (MalformedURLException ex) {
            throw new WebServiceException(ex);
        }
        CHAININGSERVICESERVICE_WSDL_LOCATION = url;
        //CHAININGSERVICESERVICE_EXCEPTION = e;
    }

    public ChainingServiceService() {
        super(__getWsdlLocation(), CHAININGSERVICESERVICE_QNAME);
    }

    @SuppressWarnings("unused")
    public ChainingServiceService(WebServiceFeature... features) {
        super(__getWsdlLocation(), CHAININGSERVICESERVICE_QNAME, features);
    }

    @SuppressWarnings("unused")
    public ChainingServiceService(URL wsdlLocation) {
        super(wsdlLocation, CHAININGSERVICESERVICE_QNAME);
    }

    @SuppressWarnings("unused")
    public ChainingServiceService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CHAININGSERVICESERVICE_QNAME, features);
    }

    @SuppressWarnings("unused")
    public ChainingServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    @SuppressWarnings("unused")
    public ChainingServiceService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ChainingService
     */
    @WebEndpoint(name = "ChainingServicePort")
    public ChainingService getChainingServicePort() {
        return super.getPort(new QName("http://chaining.mif.vu.lt/", "ChainingServicePort"), ChainingService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ChainingService
     */
    @WebEndpoint(name = "ChainingServicePort")
    @SuppressWarnings("unused")
    public ChainingService getChainingServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://chaining.mif.vu.lt/", "ChainingServicePort"), ChainingService.class, features);
    }

    private static URL __getWsdlLocation() {
        //if (CHAININGSERVICESERVICE_EXCEPTION!= null) {
        //    throw CHAININGSERVICESERVICE_EXCEPTION;
        //}
        return CHAININGSERVICESERVICE_WSDL_LOCATION;
    }

}