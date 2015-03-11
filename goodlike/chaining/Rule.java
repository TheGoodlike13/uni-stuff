package com.goodlike.chaining;

import com.goodlike.utils.StringUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

final class Rule {

    private static final int CONSEQUENT_POSITION = 0;

    private final LinkedHashSet<String> antecedents;
    private final String consequent;

    private final int ruleNum;

    private RuleUsageFlag flag;

    static Rule makeRule(String ruleString, int ruleNum) {
        List<String> items = StringUtils.spaceSplit(ruleString);
        if (items.size() < 2)
            throw new IllegalArgumentException("Each rule has to have a consequent and at"
                    + "least one antecedent: " + items);
        LinkedHashSet<String> ruleItems = new LinkedHashSet<>(items);
        if (items.size() != ruleItems.size())
            throw new IllegalArgumentException("Rule's elements cannot be duplicate: " +
                    items);
        String consequent = items.remove(CONSEQUENT_POSITION);
        ruleItems.remove(consequent);

        return new Rule(consequent, ruleItems, ruleNum);
    }

    private Rule(String consequent, LinkedHashSet<String> antecedents, int ruleNum) {
        this.consequent = consequent;
        this.antecedents = antecedents;
        this.ruleNum = ruleNum;
        flag = RuleUsageFlag.NOT_USED;
    }

    /*
     * Checks if all the antecedents are facts;
     * If yes, returns null;
     * If no, returns the first antecedent, which was missing.
     */
    String testAntecedents(Facts f) {
        for (String antecedent : antecedents)
            if (!f.isFact(antecedent))
                return antecedent;
        return null;
    }

    /*
     * Checks if consequent is a fact;
     */
    boolean isConsequentFact(Facts f) {
        return f.isFact(consequent);
    }

    //Checks whether consequent is amongst goals.
    boolean isConsequentGoal(String goal) {
        return goal.equals(consequent);
    }

    Set<String> getAntecedents() {
        return antecedents;
    }

    void apply(Facts f) {
        f.add(consequent);
    }

    void setFlag(RuleUsageFlag flag){
        this.flag = flag;
    }

    RuleUsageFlag getFlag() {
        return flag;
    }

    int getNum() {
        return ruleNum;
    }

    @Override
    public String toString() {
        StringBuilder ruleStringBuilder = new StringBuilder();
        for (String antecedent : antecedents)
            ruleStringBuilder.append(antecedent).append(", ");
        ruleStringBuilder.setLength(ruleStringBuilder.length() - 2);
        return "R" + ruleNum + ": " + ruleStringBuilder + " -> " + consequent;
    }

}
