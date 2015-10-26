
package com.goodlike.plan.chaining;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for chainingResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="chainingResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="executionSteps" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="goalReached" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ruleSequences" type="{http://chaining.mif.vu.lt/}chainingResultRuleSequences" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "chainingResult", propOrder = {
    "executionSteps",
    "goalReached",
    "ruleSequences"
})
public class ChainingResult {

    protected String executionSteps;
    protected Boolean goalReached;
    protected ChainingResultRuleSequences ruleSequences;

    /**
     * Gets the value of the executionSteps property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @SuppressWarnings("unused")
    public String getExecutionSteps() {
        return executionSteps;
    }

    /**
     * Sets the value of the executionSteps property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @SuppressWarnings("unused")
    public void setExecutionSteps(String value) {
        this.executionSteps = value;
    }

    /**
     * Gets the value of the goalReached property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @SuppressWarnings("unused")
    public Boolean isGoalReached() {
        return goalReached;
    }

    /**
     * Sets the value of the goalReached property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @SuppressWarnings("unused")
    public void setGoalReached(Boolean value) {
        this.goalReached = value;
    }

    /**
     * Gets the value of the ruleSequences property.
     * 
     * @return
     *     possible object is
     *     {@link ChainingResultRuleSequences }
     *     
     */
    public ChainingResultRuleSequences getRuleSequences() {
        return ruleSequences;
    }

    /**
     * Sets the value of the ruleSequences property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChainingResultRuleSequences }
     *     
     */
    @SuppressWarnings("unused")
    public void setRuleSequences(ChainingResultRuleSequences value) {
        this.ruleSequences = value;
    }

}
