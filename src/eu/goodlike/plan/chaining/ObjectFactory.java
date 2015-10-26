
package eu.goodlike.plan.chaining;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.goodlike.plan.chaining package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ClientQueryResponse_QNAME = new QName("http://chaining.mif.vu.lt/", "clientQueryResponse");
    private final static QName _ClientQuery_QNAME = new QName("http://chaining.mif.vu.lt/", "clientQuery");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.goodlike.plan.chaining
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ClientQueryResponse }
     * 
     */
    @SuppressWarnings("unused")
    public ClientQueryResponse createClientQueryResponse() {
        return new ClientQueryResponse();
    }

    /**
     * Create an instance of {@link ClientQuery }
     * 
     */
    @SuppressWarnings("unused")
    public ClientQuery createClientQuery() {
        return new ClientQuery();
    }

    /**
     * Create an instance of {@link ChainingResultRuleSequences }
     * 
     */
    @SuppressWarnings("unused")
    public ChainingResultRuleSequences createChainingResultRuleSequences() {
        return new ChainingResultRuleSequences();
    }

    /**
     * Create an instance of {@link ChainingServiceRule }
     * 
     */
    @SuppressWarnings("unused")
    public ChainingServiceRule createChainingServiceRule() {
        return new ChainingServiceRule();
    }

    /**
     * Create an instance of {@link ChainingServiceRules }
     * 
     */
    @SuppressWarnings("unused")
    public ChainingServiceRules createChainingServiceRules() {
        return new ChainingServiceRules();
    }

    /**
     * Create an instance of {@link ChainingServiceAntecedents }
     * 
     */
    @SuppressWarnings("unused")
    public ChainingServiceAntecedents createChainingServiceAntecedents() {
        return new ChainingServiceAntecedents();
    }

    /**
     * Create an instance of {@link ChainingServiceAssertions }
     * 
     */
    @SuppressWarnings("unused")
    public ChainingServiceAssertions createChainingServiceAssertions() {
        return new ChainingServiceAssertions();
    }

    /**
     * Create an instance of {@link ChainingResult }
     * 
     */
    @SuppressWarnings("unused")
    public ChainingResult createChainingResult() {
        return new ChainingResult();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClientQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://chaining.mif.vu.lt/", name = "clientQueryResponse")
    @SuppressWarnings("unused")
    public JAXBElement<ClientQueryResponse> createClientQueryResponse(ClientQueryResponse value) {
        return new JAXBElement<>(_ClientQueryResponse_QNAME, ClientQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClientQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://chaining.mif.vu.lt/", name = "clientQuery")
    @SuppressWarnings("unused")
    public JAXBElement<ClientQuery> createClientQuery(ClientQuery value) {
        return new JAXBElement<>(_ClientQuery_QNAME, ClientQuery.class, null, value);
    }

}
