package com.goodlike.plan;

import com.goodlike.chaining.ChainingType;
import com.goodlike.inputparser.InputData;
import com.goodlike.inputparser.quality.Quality;
import com.goodlike.interpretator.Function;
import com.goodlike.interpretator.Rule;
import com.goodlike.interpretator.RuleData;
import com.goodlike.interpretator.plan.PlanFinder;
import com.goodlike.plan.chaining.*;
import com.goodlike.utils.Permutations;

import java.util.*;
import java.util.stream.Collectors;

public class JuliusPlanFinder implements PlanFinder {

    private static final int RULE_NUM_START = 1;

    private List<Rule> plan;

    /*
     * String representation of path: Rn,( Rn, Rn,...) where n - rule number;
     */
    private String pathString;
    private final ChainingType method;

    public JuliusPlanFinder(InputData input) {
        pathString = getPathString(input.ruleData(), input.factStrings(), input.goalString(), input.method());
        this.method = input.method();
        establishQuality(input);
    }

    private void establishQuality(InputData input) {
        List<RuleData> relevantRules = filterByPlan(input.ruleData());
        List<Quality> qualities = input.qualities();
        List<List<Function>> allFunctions =
                relevantRules.stream()
                .map(RuleData::getFunctions)
                .collect(Collectors.toList());
        Permutations<Function> permutations = new Permutations<>(allFunctions);
        Map<List<Integer>, Map<Quality, Double>> indexToQuality = new HashMap<>();
        List<List<Integer>> relevantIndexes = new ArrayList<>();
        while (permutations.hasNext()) {
            List<Map<Object, Object>> next = permutations.next().stream()
                    .map(Function::getMetaData)
                    .collect(Collectors.toList());
            System.out.println(permutations);
            Map<Quality, Double> qualityValues = new HashMap<>();
            if (qualities.stream()
                    .map(quality ->  {
                                double qualityValue = next.stream()
                                        .map(data -> data.get(quality.name()))
                                        .map(String::valueOf)
                                        .mapToDouble(string -> "null".equals(string) ? Double.valueOf("NaN") : Double.valueOf(string))
                                        .reduce(quality.getBaseQuantifier(), quality::calculate);
                                qualityValues.put(quality, qualityValue);
                                return quality.isHigh(qualityValue);
                            }
                    )
                    .reduce(true, (previousQualities, nextQuality) -> previousQualities && nextQuality)) {
                relevantIndexes.add(permutations.indexes());
                indexToQuality.put(permutations.indexes(), qualityValues);
            }
        }
        List<List<Integer>> allThemIndexes = relevantIndexes.stream()
                .sorted((list1, list2) -> {
                    Iterator<List<Function>> rules1 = allFunctions.iterator();
                    Iterator<List<Function>> rules2 = allFunctions.iterator();
                    System.out.println("Comparing instaces " +
                            list1.stream().map(i -> rules1.next().get(i)).collect(Collectors.toList())
                            + " vs " +
                            list2.stream().map(i -> rules2.next().get(i)).collect(Collectors.toList()));
                    return (int)
                            (indexToQuality.get(list2).keySet().stream()
                                    .mapToDouble(quality -> quality.weight(indexToQuality.get(list2).get(quality)))
                                    .sum()
                                    -
                                    indexToQuality.get(list1).keySet().stream()
                                            .mapToDouble(quality -> quality.weight(indexToQuality.get(list1).get(quality)))
                                            .sum());
                })
                .collect(Collectors.toList());
        allThemIndexes.stream().forEach(o -> {
            Iterator<List<Function>> rules = allFunctions.iterator();
            System.out.println(o.stream().map(i -> rules.next().get(i)).collect(Collectors.toList()));
        });
        Optional<List<Integer>> bestIndexes = allThemIndexes.stream()
                .findFirst();
        if (bestIndexes.isPresent()) {
            Iterator<Integer> indexes = bestIndexes.get().iterator();
            relevantRules.stream()
                    .forEach(rule -> rule.setPosition(indexes.next()));
            return;
        }
        pathString = "";
    }

    private String getPathString(List<RuleData> rules, List<String> factStrings, String goalString, ChainingType method) {
        NameMap nameMap = new NameMap();

        ChainingServiceService css = new ChainingServiceService();
        ChainingService cs = css.getChainingServicePort();
        ChainingMode mode = ChainingMode.fromValue(method.name());
        ChainingServiceRules serviceRules = new ChainingServiceRules();
        List<ChainingServiceRule> serviceRulesList = serviceRules.getRule();

        rules.stream()
                .map(rule -> {
                    ChainingServiceRule csr = new ChainingServiceRule();
                    ChainingServiceAntecedents csa = new ChainingServiceAntecedents();
                    List<String> antecedents = csa.getAntecedent();

                    getAntecedents(rule).stream()
                            .map(nameMap::getKey)
                            .forEach(antecedents::add);

                    csr.setAntecedents(csa);
                    String c = getConsequent(rule);
                    String key = nameMap.getKey(c);
                    csr.setConsequent(key);
                    return csr;
                })
                .forEach(serviceRulesList::add);

        ChainingServiceAssertions assertions = new ChainingServiceAssertions();
        List<String> assertionsList = assertions.getAssertion();

        factStrings.stream()
                .map(nameMap::getKey)
                .forEach(assertionsList::add);

        String mappedGoalString = nameMap.getKey(goalString);
        ChainingResult result = cs.clientQuery(mode, serviceRules, assertions, mappedGoalString);
        ChainingResultRuleSequences crrs = result.getRuleSequences();
        List<String> ruleSequence = crrs.getRuleSequence();

        String pathString = ruleSequence.stream()
                .collect(Collectors.joining(", "));

        System.out.println(pathString);
        return pathString;
    }

    private String getConsequent(RuleData rule) {
        String ruleString = rule.getRuleString();
        return ruleString.substring(0, ruleString.indexOf(' '));
    }

    private LinkedHashSet<String> getAntecedents(RuleData rule) {
        String ruleString = rule.getRuleString();
        String[] facts = ruleString.substring(ruleString.indexOf(' ') + 1).split(" ");
        LinkedHashSet<String> factSet = new LinkedHashSet<>();
        Arrays.stream(facts)
                .forEach(factSet::add);
        return factSet;
    }

    @Override
    public void makePlan(List<Rule> rules) {
        plan = pathString.isEmpty()
                ? Collections.emptyList()
                : filterByPlan(rules);
    }

    private <T> List<T> filterByPlan(List<T> list) {
        return Arrays.stream(pathString.split(", "))
                .map(ruleString -> list.get(Integer.valueOf(ruleString.substring(RULE_NUM_START)) - 1))
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
        return method;
    }
}
