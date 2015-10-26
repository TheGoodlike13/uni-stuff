
package com.goodlike.plan.chaining;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for clientQuery complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clientQuery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mode" type="{http://chaining.mif.vu.lt/}chainingMode" minOccurs="0"/>
 *         &lt;element name="rules" type="{http://chaining.mif.vu.lt/}chainingServiceRules" minOccurs="0"/>
 *         &lt;element name="assertions" type="{http://chaining.mif.vu.lt/}chainingServiceAssertions" minOccurs="0"/>
 *         &lt;element name="goal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clientQuery", propOrder = {
    "mode",
    "rules",
    "assertions",
    "goal"
})
public class ClientQuery {

    @XmlSchemaType(name = "string")
    protected ChainingMode mode;
    protected ChainingServiceRules rules;
    protected ChainingServiceAssertions assertions;
    protected String goal;

    /**
     * Gets the value of the mode property.
     * 
     * @return
     *     possible object is
     *     {@link ChainingMode }
     *     
     */
    @SuppressWarnings("unused")
    public ChainingMode getMode() {
        return mode;
    }

    /**
     * Sets the value of the mode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChainingMode }
     *     
     */
    @SuppressWarnings("unused")
    public void setMode(ChainingMode value) {
        this.mode = value;
    }

    /**
     * Gets the value of the rules property.
     * 
     * @return
     *     possible object is
     *     {@link ChainingServiceRules }
     *     
     */
    public ChainingServiceRules getRules() {
        return rules;
    }

    /**
     * Sets the value of the rules property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChainingServiceRules }
     *     
     */
    public void setRules(ChainingServiceRules value) {
        this.rules = value;
    }

    /**
     * Gets the value of the assertions property.
     * 
     * @return
     *     possible object is
     *     {@link ChainingServiceAssertions }
     *     
     */
    @SuppressWarnings("unused")
    public ChainingServiceAssertions getAssertions() {
        return assertions;
    }

    /**
     * Sets the value of the assertions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChainingServiceAssertions }
     *     
     */
    @SuppressWarnings("unused")
    public void setAssertions(ChainingServiceAssertions value) {
        this.assertions = value;
    }

    /**
     * Gets the value of the goal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGoal() {
        return goal;
    }

    /**
     * Sets the value of the goal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGoal(String value) {
        this.goal = value;
    }

}
