
package sirs.app.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateNote complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateNote">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="noteView" type="{http://ws.app.sirs/}noteView"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateNote", propOrder = {
    "noteView"
})
public class UpdateNote {

    @XmlElement(required = true)
    protected NoteView noteView;

    /**
     * Gets the value of the noteView property.
     * 
     * @return
     *     possible object is
     *     {@link NoteView }
     *     
     */
    public NoteView getNoteView() {
        return noteView;
    }

    /**
     * Sets the value of the noteView property.
     * 
     * @param value
     *     allowed object is
     *     {@link NoteView }
     *     
     */
    public void setNoteView(NoteView value) {
        this.noteView = value;
    }

}
