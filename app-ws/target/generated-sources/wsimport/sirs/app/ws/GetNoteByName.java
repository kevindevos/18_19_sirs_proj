
package sirs.app.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getNoteByName complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getNoteByName">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="input_message" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getNoteByName", propOrder = {
    "inputMessage"
})
public class GetNoteByName {

    @XmlElement(name = "input_message", required = true)
    protected String inputMessage;

    /**
     * Gets the value of the inputMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInputMessage() {
        return inputMessage;
    }

    /**
     * Sets the value of the inputMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInputMessage(String value) {
        this.inputMessage = value;
    }

}
