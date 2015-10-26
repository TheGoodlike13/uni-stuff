package eu.goodlike.chaining;

import eu.goodlike.utils.StringUtils;

import java.util.LinkedHashSet;
import java.util.List;

final class Facts {

    private LinkedHashSet<String> facts;

    static Facts makeFacts(String factString) {
        List<String> facts = StringUtils.spaceSplit(factString);
        if (facts.isEmpty())
            throw new IllegalArgumentException("Empty/null arguments not allowed.");
        LinkedHashSet<String> newFacts = new LinkedHashSet<>(facts);
        if (facts.size() != newFacts.size())
            throw new IllegalArgumentException("There cannot be duplicate facts: " + facts);
        return new Facts(newFacts);
    }

    static Facts saveFacts(Facts facts) {
        return new Facts(new LinkedHashSet<>(facts.facts));
    }

    private Facts(LinkedHashSet<String> facts) {
        this.facts = facts;
    }

    boolean isFact(String fact) {
        return facts.contains(fact);
    }

    boolean add(String fact) {
        return facts.add(fact);
    }

    @Override
    public String toString() {
        return facts.toString();
    }

}
