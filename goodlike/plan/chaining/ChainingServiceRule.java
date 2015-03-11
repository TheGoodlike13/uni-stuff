
package com.goodlike.plan.chaining;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for chainingServiceRule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="chainingServiceRule">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="antecedents" type="{http://chaining.mif.vu.lt/}chainingServiceAntecedents" minOccurs="0"/>
 *         &lt;element name="consequent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "chainingServiceRule", propOrder = {
    "antecedents",
    "consequent"
})
public class ChainingServiceRule {

    protected ChainingServiceAntecedents antecedents;
    protected String consequent;

    /**
     * Gets the value of the antecedents property.
     * 
     * @return
     *     possible object is
     *     {@link ChainingServiceAntecedents }
     *     
     */
    @SuppressWarnings("unused")
    public ChainingServiceAntecedents getAntecedents() {
        return antecedents;
    }

    /**
     * Sets the value of the antecedents property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChainingServiceAntecedents }
     *     
     */
    public void setAntecedents(ChainingServiceAntecedents value) {
        this.antecedents = value;
    }

    /**
     * Gets the value of the consequent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @SuppressWarnings("unused")
    public String getConsequent() {
        return consequent;
    }

    /**
     * Sets the value of the consequent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsequent(String value) {
        this.consequent = value;
    }

}
