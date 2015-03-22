package com.goodlike.inputparser.SOAP;

import com.goodlike.interpretator.Function;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.stream.Collectors;

import static com.goodlike.inputparser.wsdl.Constants.*;

/**
 * Function, which uses web service methods to send requests and retrieve responses
 */
public class SOAPMessager implements Function {

    private static final String XML_START_1 = "<soapenv:Envelope xmlns:soapenv=\"http://"
            + "schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"";
    private static final String XML_START_2 = "\"><soapenv:Header/><soapenv:Body><web:";
    private static final String XML_MID_1 = ">";
    private static final String XML_MID_2 = "</web:";
    private static final String XML_END = "></soapenv:Body></soapenv:Envelope>";

    private final String methodName;
    private final String nameSpace;
    private final QName serviceName;
    private final QName portName;
    private final String endPointUrl;
    private final String soapAction;
    private final Map<Object, Object> metaData;
    private final WSResponse wsReply;
    private final WSRequest wsRequest;

    private SOAPMessage invoke(Object... args) throws SOAPException {
        /** Create a service and add at least one port to it. **/
        Service service = Service.create(serviceName);
        service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endPointUrl);

        /** Create a Dispatch instance from a service.**/
        Dispatch<SOAPMessage> dispatch = service.createDispatch(portName,
                SOAPMessage.class, Service.Mode.MESSAGE);

        // The soapActionUri is set here. otherwise we get a error on .net based services.
        dispatch.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY, true);
        dispatch.getRequestContext().put(Dispatch.SOAPACTION_URI_PROPERTY, soapAction);

        /** Create SOAPMessage request. **/
        // compose a request message
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();

        //Create objects for the message parts
        SOAPPart soapPart = message.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        @SuppressWarnings("unused")
        SOAPBody body = envelope.getBody();

        //Populate the Message
        String messageString = XML_START_1 + nameSpace + XML_START_2 + methodName + XML_MID_1
                + wsRequest.objectsToRequest(args) + XML_MID_2 + methodName + XML_END;

        StreamSource prepMsgSrc = new StreamSource(new StringReader(messageString));
        soapPart.setContent(prepMsgSrc);

        //Save the message
        message.saveChanges();

        System.out.println("Some debug: ");
        try {
            message.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("");

        System.out.println("Waiting for answer...");
        SOAPMessage response = dispatch.invoke(message);
        try {
            response.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("");
        System.out.println("If we get here, exception didn't get caught!");

        return response;
    }

    @Override
    public Object execute(Object... args) {
        try {
            return SOAPtoObject(invoke(args));
        } catch (SOAPException e) {
            throw new RuntimeException("SOAP connection has failed.");
        }
    }

    private Object SOAPtoObject(SOAPMessage message) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(
                    new DOMSource(message.getSOAPPart()),
                    new StreamResult(sw));
            return wsReply.responseToObject(sw.toString());
        } catch (TransformerException e) {
            throw new RuntimeException("Failed to transform received SOAP message.");
        }

    }

    public static SOAPMessager newInstance(Map<String, String> data, String port, Map<String, String> qualities,
                                           WSRequest wsRequest, WSResponse wsResponse) {

        if (data == null || port == null || wsResponse == null || wsRequest == null)
            throw new IllegalArgumentException("Data required to create SOAPMessager not provided.");

        String methodName = data.get("methodName");
        String nameSpace = data.get(DEF_ID);
        String serviceName = data.get(NAME_ID);
        String endPointUrl = data.get(ADDR_ID);
        String soapAction = data.get(ACTION_ID);
        if (nameSpace == null || serviceName == null
                || endPointUrl == null || soapAction == null)
            throw new IllegalArgumentException("Provided WSDL does not have sufficient information.");

        return new SOAPMessager(methodName, nameSpace, serviceName, port,
                endPointUrl, soapAction, qualities, wsRequest, wsResponse);
    }

    private SOAPMessager(String methodName, String nameSpace, String serviceName,
                         String portName, String endPointUrl, String soapAction,
                         Map<String, String> qualities, WSRequest wsRequest, WSResponse wsReply) {

        this.methodName = methodName;
        this.nameSpace = nameSpace;
        this.serviceName = new QName(nameSpace, serviceName);
        this.portName = new QName(nameSpace, portName);
        this.endPointUrl = endPointUrl;
        this.soapAction = soapAction;
        this.metaData = qualities.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.wsReply = wsReply;
        this.wsRequest = wsRequest;
    }

    @Override
    public Map<Object, Object> getMetaData() {
        return metaData;
    }

    @Override
    public String toString() {
        return methodName;
    }
}
