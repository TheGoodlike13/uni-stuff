package com.goodlike.inputparser;

import com.goodlike.chaining.ChainingType;
import com.goodlike.inputparser.quality.Quality;
import com.goodlike.interpretator.RuleData;

import java.util.List;

/**
 * Holds the data from the input file
 */
public class InputData {

    private final List<RuleData> ruleData;
    public List<RuleData> ruleData() {
        return ruleData;
    }

    private final List<String> factStrings;
    public List<String> factStrings() {
        return factStrings;
    }

    private final String goalString;
    public String goalString() {
        return goalString;
    }

    private final ChainingType method;
    public ChainingType method() {
        return method;
    }

    private final List<Quality> qualities;
    public List<Quality> qualities() {
        return qualities;
    }

    public InputData(List<RuleData> ruleData, List<String> factStrings, String goalString,
                     ChainingType method, List<Quality> qualities) {

        this.ruleData = ruleData;
        this.factStrings = factStrings;
        this.goalString = goalString;
        this.method = method;
        this.qualities = qualities;
    }

}
