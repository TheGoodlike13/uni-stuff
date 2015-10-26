
package eu.goodlike.plan.chaining;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for chainingMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="chainingMode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FORWARD"/>
 *     &lt;enumeration value="BACKWARD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "chainingMode")
@XmlEnum
public enum ChainingMode {

    @SuppressWarnings("unused")
    FORWARD,
    BACKWARD;

    public String value() {
        return name();
    }

    public static ChainingMode fromValue(String v) {
        return valueOf(v);
    }

}
