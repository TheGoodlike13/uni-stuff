package com.goodlike.interpreter.plan;

import com.goodlike.chaining.ChainingType;
import com.goodlike.interpreter.Rule;
import com.goodlike.interpreter.RuleData;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.goodlike.chaining.ChainingType.BACKWARD;

public class DefaultPlanFinder implements PlanFinder {

    private static final int RULE_NUM_START = 1;

    private List<Rule> plan;

    /*
     * String representation of path: Rn,( Rn, Rn,...) where n - rule number;
     */
    private final String pathString;

    /*
     * Method which returns a list of rules, which represents the synthesized path;
     * This path will be taken by executing the rules one by one;
     *
     * Parameters:
     * rules - non-empty list of non-null Rule objects;
     * factString - F( F F...), where F - fact;
     * goalChar - G, where G - goal;
     */
    public DefaultPlanFinder(List<RuleData> rules, List<String> factStrings, String goalString) {
        pathString = getPathString(rules, factStrings, goalString);
    }

    private static String getPathString(List<RuleData> rules, List<String> factStrings, String goalString) {
        List<String> ruleStrings = rules.stream()
                .map(RuleData::getRuleString)
                .collect(Collectors.toList());

        String factString = factStrings.stream()
                .collect(Collectors.joining(" "));

        return BACKWARD.getChainer(ruleStrings, factString, goalString).path();
    }

    @Override
    public void makePlan(List<Rule> rules) {
        plan = pathString.isEmpty()
                ? Collections.emptyList()
                : Arrays.stream(pathString.split(", "))
                .map(ruleString -> rules.get(Integer.valueOf(ruleString.substring(RULE_NUM_START)) - 1))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return pathString;
    }

    @Override
    public Iterator<Rule> iterator() {
        return plan.iterator();
    }

    @Override
    public boolean isEmpty() {
        return plan.isEmpty();
    }

    @Override
    public ChainingType method() {
        return BACKWARD;
    }

}
