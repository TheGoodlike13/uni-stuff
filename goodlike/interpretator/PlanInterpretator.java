package com.goodlike.interpretator;

import com.goodlike.interpretator.plan.DefaultPlanFinder;
import com.goodlike.interpretator.plan.PlanFinder;
import com.goodlike.utils.StringUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PlanInterpretator {

    private final static String END_LINE = "\n";
    private final static String EMPTY_LINE = "";

    private final StringBuilder log;

    private final LinkedHashSet<String> factStrings;
    private final String goalString;

    private final PlanFinder path;

    /*
     * Throws NullPointer & IllegalArgument
     */
    //Static factory method for PlanInterpretator
    public static PlanInterpretator makePI(List<RuleData> ruleData, List<String> facts, String goal, PlanFinder planFinder) {
        List<Rule> rules = buildRules(ruleData);
        LinkedHashSet<String> factStrings = buildFactsIntoSet(facts);
        if (facts.size() != factStrings.size())
            throw new IllegalArgumentException("There cannot be duplicate facts: " + facts);

        if ((rules.isEmpty()) || (factStrings.isEmpty()) || (goal.trim().isEmpty()))
            throw new IllegalArgumentException("Empty/null arguments not allowed.");

        if (StringUtils.spaceSplitCount(goal) > 1)
            throw new IllegalArgumentException("There can only be one goal: " + goal);

        planFinder.makePlan(rules);

        return new PlanInterpretator(rules, factStrings, goal, planFinder);
    }

    @SuppressWarnings("unused")
    public static PlanInterpretator makePI(List<RuleData> ruleData, List<String> facts, String goal) {
        return makePI(ruleData, facts, goal, new DefaultPlanFinder(ruleData, facts, goal));
    }

    private static List<Rule> buildRules(List<RuleData> ruleData) {
        return ruleData.stream()
                .map(rule -> new Rule.RuleBuilder(rule).build())
                .collect(Collectors.toList());
    }

    private static LinkedHashSet<String> buildFactsIntoSet(List<String> facts) {
        return new LinkedHashSet<>(facts);
    }

    private PlanInterpretator(List<Rule> rules, LinkedHashSet<String> factStrings, String goalString, PlanFinder planFinder) {
        this.factStrings = factStrings;
        this.goalString = goalString;
        path = planFinder;
        log = new StringBuilder();

        log("Initial data:");
        log(EMPTY_LINE);
        log("Rules:");
        for (Rule rule : rules)
            log(rule);
        log(EMPTY_LINE);
        log("Facts:");
        log(factStrings);
        log(EMPTY_LINE);
        log("Goal: " + goalString);
        log(EMPTY_LINE);
        log("Chaining method: " + planFinder.method());
        log(EMPTY_LINE);

        if (path.isEmpty())
            log("The path is empty.");
        else {
            log("Path found: " + planFinder + ";");
            log(EMPTY_LINE);
        }
    }

    //Returns the result (final object) of executing the path using given fact values
    public Object execute(Object... factValues) {
        if (factStrings.size() != factValues.length)
            throw new IllegalArgumentException("Incorrect amount of fact values specified. "
                    + "Needed: " + factStrings.size() + "; got " + factValues.length);

        Facts facts = new Facts();
        int i = 0;
        for (String fact : factStrings)
            facts.put(fact, factValues[i++]);

        if (path.isEmpty())
            log("Path is empty, nothing has been executed.");
        else {
            log("Attempting to execute path: " + path);
            log(EMPTY_LINE);
            for (Rule currentRule : path) {
                log("	Applying rule " + currentRule + ";");
                if (currentRule.getDescription() != null)
                    log("	Rule description: " + currentRule.getDescription());
                currentRule.apply(facts);
                log(currentRule.getExecutionStr());
                log(EMPTY_LINE);
            }
        }
        return facts.get(goalString);
    }

    @Override
    public String toString() {
        String lastLog = log.toString();
        log.setLength(0);
        return lastLog;
    }

    private void log(Object o) {
        log.append(o).append(END_LINE);
    }

}
