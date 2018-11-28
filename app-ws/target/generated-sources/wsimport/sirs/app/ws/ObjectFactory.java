
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

    private final static QName _NoteNotFound_QNAME = new QName("http://ws.app.sirs/", "NoteNotFound");
    private final static QName _UpdateNote_QNAME = new QName("http://ws.app.sirs/", "updateNote");
    private final static QName _UpdateNoteResponse_QNAME = new QName("http://ws.app.sirs/", "updateNoteResponse");
    private final static QName _TestPingResponse_QNAME = new QName("http://ws.app.sirs/", "testPingResponse");
    private final static QName _TestPing_QNAME = new QName("http://ws.app.sirs/", "testPing");
    private final static QName _NoteView_QNAME = new QName("http://ws.app.sirs/", "noteView");
    private final static QName _GetNoteByName_QNAME = new QName("http://ws.app.sirs/", "getNoteByName");
    private final static QName _GetNoteByNameResponse_QNAME = new QName("http://ws.app.sirs/", "getNoteByNameResponse");
    private final static QName _NotAllowed_QNAME = new QName("http://ws.app.sirs/", "NotAllowed");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sirs.app.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NoteNotFound }
     * 
     */
    public NoteNotFound createNoteNotFound() {
        return new NoteNotFound();
    }

    /**
     * Create an instance of {@link UpdateNote }
     * 
     */
    public UpdateNote createUpdateNote() {
        return new UpdateNote();
    }

    /**
     * Create an instance of {@link UpdateNoteResponse }
     * 
     */
    public UpdateNoteResponse createUpdateNoteResponse() {
        return new UpdateNoteResponse();
    }

    /**
     * Create an instance of {@link TestPingResponse }
     * 
     */
    public TestPingResponse createTestPingResponse() {
        return new TestPingResponse();
    }

    /**
     * Create an instance of {@link TestPing }
     * 
     */
    public TestPing createTestPing() {
        return new TestPing();
    }

    /**
     * Create an instance of {@link NoteView }
     * 
     */
    public NoteView createNoteView() {
        return new NoteView();
    }

    /**
     * Create an instance of {@link GetNoteByName }
     * 
     */
    public GetNoteByName createGetNoteByName() {
        return new GetNoteByName();
    }

    /**
     * Create an instance of {@link GetNoteByNameResponse }
     * 
     */
    public GetNoteByNameResponse createGetNoteByNameResponse() {
        return new GetNoteByNameResponse();
    }

    /**
     * Create an instance of {@link NotAllowed }
     * 
     */
    public NotAllowed createNotAllowed() {
        return new NotAllowed();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoteNotFound }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "NoteNotFound")
    public JAXBElement<NoteNotFound> createNoteNotFound(NoteNotFound value) {
        return new JAXBElement<NoteNotFound>(_NoteNotFound_QNAME, NoteNotFound.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateNote }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "updateNote")
    public JAXBElement<UpdateNote> createUpdateNote(UpdateNote value) {
        return new JAXBElement<UpdateNote>(_UpdateNote_QNAME, UpdateNote.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateNoteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "updateNoteResponse")
    public JAXBElement<UpdateNoteResponse> createUpdateNoteResponse(UpdateNoteResponse value) {
        return new JAXBElement<UpdateNoteResponse>(_UpdateNoteResponse_QNAME, UpdateNoteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestPingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "testPingResponse")
    public JAXBElement<TestPingResponse> createTestPingResponse(TestPingResponse value) {
        return new JAXBElement<TestPingResponse>(_TestPingResponse_QNAME, TestPingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestPing }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "testPing")
    public JAXBElement<TestPing> createTestPing(TestPing value) {
        return new JAXBElement<TestPing>(_TestPing_QNAME, TestPing.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoteView }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "noteView")
    public JAXBElement<NoteView> createNoteView(NoteView value) {
        return new JAXBElement<NoteView>(_NoteView_QNAME, NoteView.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNoteByName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "getNoteByName")
    public JAXBElement<GetNoteByName> createGetNoteByName(GetNoteByName value) {
        return new JAXBElement<GetNoteByName>(_GetNoteByName_QNAME, GetNoteByName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNoteByNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "getNoteByNameResponse")
    public JAXBElement<GetNoteByNameResponse> createGetNoteByNameResponse(GetNoteByNameResponse value) {
        return new JAXBElement<GetNoteByNameResponse>(_GetNoteByNameResponse_QNAME, GetNoteByNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotAllowed }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.app.sirs/", name = "NotAllowed")
    public JAXBElement<NotAllowed> createNotAllowed(NotAllowed value) {
        return new JAXBElement<NotAllowed>(_NotAllowed_QNAME, NotAllowed.class, null, value);
    }

}
