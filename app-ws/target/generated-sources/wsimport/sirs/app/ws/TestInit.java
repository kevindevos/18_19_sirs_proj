
package sirs.app.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for test_init complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="test_init">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="user_initial_points" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "test_init", propOrder = {
    "userInitialPoints"
})
public class TestInit {

    @XmlElement(name = "user_initial_points")
    protected int userInitialPoints;

    /**
     * Gets the value of the userInitialPoints property.
     * 
     */
    public int getUserInitialPoints() {
        return userInitialPoints;
    }

    /**
     * Sets the value of the userInitialPoints property.
     * 
     */
    public void setUserInitialPoints(int value) {
        this.userInitialPoints = value;
    }

}
