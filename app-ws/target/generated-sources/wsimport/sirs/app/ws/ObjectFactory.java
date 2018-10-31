
package sirs.app.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sirs.app.ws package. 
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

    private final static QName _TestClearResponse_QNAME = new QName("http://ws.app.sirs/", "test_clear_response");
    private final static QName _TestClear_QNAME = new QName("http://ws.app.sirs/", "test_clear");
    private final static QName _TestPing_QNAME = new QName("http://ws.app.sirs/", "test_ping");
    private final static QName _TestPingResponse_QNAME = new QName("http://ws.app.sirs/", "test_ping_response");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sirs.app.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TestClearResponse }
     * 
     */
    public TestClearResponse createTestClearResponse() {
        return new TestClearResponse();
    }

    /**
     * Create an instance of {@link TestClear }
     * 
     */
    public TestClear createTestClear() {
        return new TestClear();
    }

    /**
     * Create an instance of {@link TestPing }
     * 
     */
    public TestPing createTestPing() {
        return new TestPing();
    }

    /**
     * Create an instance of {@link TestPingResponse }
     * 
     */
    public TestPingResponse createTestPingResponse() {
        return new TestPingResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestClearResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "test_clear_response")
    public JAXBElement<TestClearResponse> createTestClearResponse(TestClearResponse value) {
        return new JAXBElement<TestClearResponse>(_TestClearResponse_QNAME, TestClearResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestClear }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "test_clear")
    public JAXBElement<TestClear> createTestClear(TestClear value) {
        return new JAXBElement<TestClear>(_TestClear_QNAME, TestClear.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestPing }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "test_ping")
    public JAXBElement<TestPing> createTestPing(TestPing value) {
        return new JAXBElement<TestPing>(_TestPing_QNAME, TestPing.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestPingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "test_ping_response")
    public JAXBElement<TestPingResponse> createTestPingResponse(TestPingResponse value) {
        return new JAXBElement<TestPingResponse>(_TestPingResponse_QNAME, TestPingResponse.class, null, value);
    }

}
