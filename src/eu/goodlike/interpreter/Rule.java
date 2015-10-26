package eu.goodlike.interpreter;

import eu.goodlike.utils.StringUtils;

import java.util.LinkedHashSet;
import java.util.List;

public class Rule {

    static class RuleBuilder {

        private static final int CONSEQUENT_POSITION = 0;

        private final String ruleString;
        private final String description;
        private final Function function;

        private final int ruleCount;

        private final LinkedHashSet<String> antecedents;
        private final String consequent;

        RuleBuilder(RuleData ruleData) {
            ruleString = ruleData.getRuleString();
            description = ruleData.getDescription();
            function = ruleData.getFunction();

            this.ruleCount = ruleData.getRuleCount();

            List<String> items = StringUtils.spaceSplit(ruleString);
            if (items.size() < 2)
                throw new IllegalArgumentException("Each rule has to have a consequent and at least one " +
                        "antecedent: " + items);

            LinkedHashSet<String> ruleItems = new LinkedHashSet<>(items);
            if (items.size() != ruleItems.size())
                throw new IllegalArgumentException("Rule's elements cannot be duplicate :" + items);

            consequent = items.remove(CONSEQUENT_POSITION);
            ruleItems.remove(consequent);
            antecedents = ruleItems;
        }

        Rule build() {
            return new Rule(this);
        }

    }

    /*
	 * Rule representation in form: C A (A A...), where C - consequent, A - antecedents;
	 * Must be assigned in constructor.
	 */
    @SuppressWarnings("unused")
    private final String ruleString;

    /*
     * Description of rule's functions. Optional.
     */
    private final String description;
    String getDescription() {
        return description;
    }

    /*
     * Holds the implementation of the rule's execution
     */
    private final Function function;

    private final int ruleCount;
    int ruleCount() {
        return ruleCount;
    }

    private final LinkedHashSet<String> antecedents;
    private final String consequent;

    private String executionString;
    String getExecutionStr() {
        return executionString;
    }

    private Rule(RuleBuilder ruleBuilder) {
        ruleString = ruleBuilder.ruleString;
        description = ruleBuilder.description;
        function = ruleBuilder.function;

        ruleCount = ruleBuilder.ruleCount;
        antecedents = ruleBuilder.antecedents;
        consequent = ruleBuilder.consequent;
    }

    //Method for rule execution
    void apply(Facts facts) {
        StringBuilder logBuilder = new StringBuilder();
        for (String antecedent : antecedents)
            logBuilder.append("		We take antecedent ")
                    .append(antecedent).append(": ")
                    .append(facts.get(antecedent))
                    .append("\n");

        int expectedDataSize = facts.getSize() + 1;
        Object[] args = new Object[antecedents.size()];
        int i = 0;
        for (String antecedent : antecedents) {
            args[i++] = facts.get(antecedent);
        }

        Object result;
        try {
            //Creates an object which represents the consequent's value
            result = function.execute(args);
        } catch (NumberFormatException|ClassCastException e) {
            throw new RuntimeException("Data type mismatch while executing " + this);
		}

        facts.put(consequent, result);
        if ((facts.getSize() > expectedDataSize) || (facts.get(consequent) == null))
            throw new RuntimeException("Illegal function in rule " + this);

        logBuilder.append("		We make consequent ")
                .append(consequent)
                .append(": ")
                .append(facts.get(consequent));
        executionString = logBuilder.toString();
    }

    @Override
    public String toString() {
        StringBuilder ruleStringBuilder = new StringBuilder("R" + ruleCount + ": ");
        for (String antecedent : antecedents)
            ruleStringBuilder.append(antecedent).append(", ");
        ruleStringBuilder.setLength(ruleStringBuilder.length() - 2);
        ruleStringBuilder.append(" -> ").append(consequent);
        return ruleStringBuilder.toString();
    }

}
