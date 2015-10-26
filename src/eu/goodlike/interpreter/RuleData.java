package com.goodlike.interpreter;

import java.util.Arrays;
import java.util.List;

public class RuleData {

    /*
	 * Rule representation in form: C A (A A...), where C - consequent, A - antecedents;
	 * Must be assigned in constructor.
	 */
    private final String ruleString;
    public String getRuleString() {
        return ruleString;
    }

    /*
     * Description of rule's functions. Optional.
     */
    private String description;
    String getDescription() {
        return description;
    }
    @SuppressWarnings("unused")
    public void setDescription(String description) {
        this.description = description;
    }

    /*
     * Holds the implementation of the rule's execution
     */
    private final List<Function> functions;
    Function getFunction() {
        return functions.get(position);
    }
    public List<Function> getFunctions() {
        return functions;
    }
    private int position = 0;
    public void setPosition(int position) {
        this.position = position;
    }

    private final int ruleCount;
    int getRuleCount() {
        return ruleCount;
    }

    public RuleData(String ruleString, int ruleCount, Function... functions) {
        if (functions == null || functions.length == 0)
            throw new NullPointerException("A rule function has not been provided.");

        this.ruleString = ruleString;
        this.ruleCount = ruleCount;
        this.functions = Arrays.asList(functions);
    }

}
