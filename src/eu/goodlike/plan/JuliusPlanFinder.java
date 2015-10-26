package eu.goodlike.plan;

import eu.goodlike.chaining.ChainingType;
import eu.goodlike.inputparser.InputData;
import eu.goodlike.inputparser.quality.Quality;
import eu.goodlike.interpreter.Function;
import eu.goodlike.interpreter.Rule;
import eu.goodlike.interpreter.RuleData;
import eu.goodlike.interpreter.plan.PlanFinder;
import eu.goodlike.plan.chaining.*;
import eu.goodlike.utils.permutations.Permutations;

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

        List<List<FunctionWrapper>> allFunctions = new ArrayList<>();
        for (RuleData rule : relevantRules) {
            List<FunctionWrapper> functions = new ArrayList<>();
            int i = 0;
            for (Function function : rule.getFunctions())
                functions.add(new FunctionWrapper(i++, function));

            allFunctions.add(functions);
        }

        List<FunctionWrapper> best = Collections.emptyList();
        double highestQuality = 0;

        Permutations<FunctionWrapper> permutations = new Permutations<>(allFunctions);
        for (List<FunctionWrapper> permutation : permutations) {
            System.out.println("Calculating quality for chain: " + permutation);
            List<Double> qualityLevels = qualityLevels(qualities, permutation);
            if (qualityLevels.isEmpty()) {
                System.out.println("Chain dropped due to low quality.");
                System.out.println();
                continue;
            }

            if (best.isEmpty()) {
                System.out.println("First sufficient chain found.");
                System.out.println();
                best = permutation;
                highestQuality = weightedQuality(qualities, qualityLevels);
                System.out.println(String.format("Weighted value: %.2f", highestQuality));
                continue;
            }

            System.out.println("New sufficient chain found.");
            System.out.println();
            double newQuality = weightedQuality(qualities, qualityLevels);
            System.out.println(String.format("Weighted value: %.2f", newQuality));
            System.out.println();
            if (newQuality <= highestQuality) {
                System.out.println(String.format("%.2f >= %.2f, chain is not better than current best, ignoring...", highestQuality, newQuality));
                System.out.println();
                continue;
            }

            System.out.println(String.format("%.2f < %.2f, chain is better than current best, updating...", highestQuality, newQuality));
            System.out.println();
            best = permutation;
            highestQuality = newQuality;
        }

        if (best.isEmpty()) {
            System.out.println("No sufficiently good chain found.");
            System.out.println();
            pathString = "";
            return;
        }
        System.out.println();

        List<Integer> indexes = best.stream()
                .map(FunctionWrapper::index)
                .collect(Collectors.toList());
        for (int i = 0; i < indexes.size(); i++)
            relevantRules.get(i).setPosition(indexes.get(i));
    }

    private double weightedQuality(List<Quality> qualities, List<Double> qualityLevels) {
        double weight = 0;
        for (int i = 0; i < qualities.size(); i++)
            weight += qualities.get(i).weight(qualityLevels.get(i));

        return weight;
    }

    private List<Double> qualityLevels(List<Quality> qualities, List<FunctionWrapper> functions) {
        List<Double> qualityLevels = new ArrayList<>();
        for (Quality quality : qualities) {
            System.out.println();
            System.out.println("Calculating characteristic '" + quality.name() + "'");
            System.out.println();
            double qualityValue = quality.getBaseQuantifier();
            for (FunctionWrapper function : functions) {
                qualityValue = quality.calculate(qualityValue, function.quality(quality.wsdlName()));

                if (!quality.isHigh(qualityValue))
                    return Collections.emptyList();
            }
            qualityLevels.add(qualityValue);
        }
        return qualityLevels;
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
