package com.goodlike.chaining;

import com.goodlike.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@code BackwardChainer} klasė naudoja <b>backward chaining</b> algortimą
 * taisyklių keliui surasti.
 *
 * Šiai klasei reikia sąrašo taisyklių, kurių formatas:
 * <pre>C A1 A2...</pre>
 * Čia {@code C} - konsekventas, {@code A1}, {@code A2} ir t.t. - antecedentai.
 * Privalomas <b>vienas</b> konsekventas, ir <b>bent vienas</b> antecedentas.
 * Taisyklę {@code C1 C2 A1 A2}... galima užrašyti taip:<pre>
 * C1 A1 A2...
 * C2 A1 A2...</pre>
 * Dar reikalingi faktai {@code F1 F2}... ir tikslas {@code G}.<br>
 * Būtinas <b>bent vienas</b> faktas, bei <b>vienas ir tik vienas</b> tikslas.<br>
 * Taisyklės bei faktų aprašuose negali būti besidubliuojančių elementų, t.y.<pre>
 * A C D C
 * A B B A</pre>
 * yra neleistini taisyklių ar faktų aprašimai.
 *
 * @author Goodlike
 */
public class BackwardChainer implements Chainer {

    /** Konstanta - eilutės pabaiga. */
    private final static String ENDLINE = "\n";

    /** Konstanta - tuščia eilutė. */
    private final static String EMPTY_LINE = "";

    /** Konstanta - tabuliacija. */
    private final static String TABS = "  ";

    /** Konstanta - kablelis su tarpu. */
    private final static String COMMA = ", ";

    /** Saugo taisyklių sąrašą. */
    private final List<Rule> rules;

    /** Saugo faktų vardų aibę. */
    private Facts facts;

    /** Saugo tikslo vardą. */
    private final String goal;

    /** Registruoja algoritmo vykdomus veiksmus. */
    private final StringBuilder log;

    /** Saugo taisyklių kelią rekursijos metu. */
    private StringBuilder path;

    /** Saugo tabuliacijas kiekvienam ciklui. */
    private final StringBuilder tabulations;

    /** Saugo ėjimų skaičių. */
    private int stepCount;

    /** Saugo ieškomų tikslų eilutę. */
    private final Set<String> goalLine;

    /** Saugo taisyklių kelią. */
    private final String pathString;

    /** Saugo ar pavyko surasti tikslą */
    private boolean goalFound;

    /**
     * {@code Static factory} metodas.
     * Šiuo metodu sukuriami {@code BackwardChainer} objektai.
     *
     * @param ruleStrings - taisyklių aprašimų sąrašas.
     * @param factString - faktų vardai.
     * @param goalString - tikslo vardas.
     * @return naują {@code ForwardChainer} objektą, kuriam perduodami parametrai.
     * @throws IllegalArgumentException jei parametrai tušti, trūksta taisyklių elementų,
     * daugiau nei vienas tikslas, arba dubliuojasi taisyklių ar faktų elementai.
     * @throws NullPointerException jei kuris nors parametras ar taisyklė lygi {@code null}.
     */
    public static BackwardChainer makeBWC(List<String> ruleStrings,
                                          CharSequence factString,
                                          CharSequence goalString) {

        List<Rule> rules = makeRules(ruleStrings);
        Facts facts = Facts.makeFacts(factString.toString());
        String goal = goalString.toString();
        if ((rules.isEmpty()) || (goal.trim().isEmpty()))
            throw new IllegalArgumentException("Empty arguments not allowed.");
        if (StringUtils.spaceSplitCount(goal) > 1)
            throw new IllegalArgumentException("There can only be one goal: " + goal);
        return new BackwardChainer(rules, facts, goal);
    }

    /**
     * Sukuria taisyklių sąrašą iš taisyklių aprašimų.
     *
     * @param ruleStrings - taisyklių aprašymų sąrašas.
     * @return taisyklių sąrašą {@code List<Rule>}.
     */
    private static List<Rule> makeRules(List<String> ruleStrings) {
        List<Rule> rules = new ArrayList<>();
        int i = 1;
        for (String rule : ruleStrings)
            rules.add(Rule.makeRule(rule, i++));
        return rules;
    }

    /**
     * Inicializuoja {@code BackwardChainer} objektą. Kviečiamas <b>tik</b> per
     * {@code static factory} metodą.
     *
     * @param rules - taisyklių sąrašas.
     * @param facts - faktų aibė.
     * @param goal - tikslo vardas.
     */
    private BackwardChainer(List<Rule> rules, Facts facts, String goal) {
        this.rules = rules;
        this.facts = facts;
        this.goal = goal;
        path = new StringBuilder();
        log = new StringBuilder();
        tabulations = new StringBuilder("	|");
        goalLine = new HashSet<>();
        pathString = makePath();
    }

    /**
     * Grąžina kelią, kuriuo taisyklės pasiekia tikslą.<br>
     * Kelio formatas:<pre>
     * Ra, Rb, Rc...</pre>
     * kur a, b, c - natūralieji skaičiai, kurie nurodo taisyklių numerį.<br>
     * <i>Kelias gali būti tuščias</i> (jei tikslas faktuose arba nepasiekiamas).
     *
     * @return taisyklių kelią {@code String}.
     */
    @Override
    public String path() {
        return pathString;
    }

    /**
     * Sugrąžina tikslo paieškos pasisekimo reikšmę.
     *
     * @return {@code true} jei tikslą pavyko surasti, {@code false} priešingu atveju.
     */
    @Override
    public boolean success() {
        return goalFound;
    }

    /**
     * Suranda ir grąžina kelią, kuriuo taisyklės pasiekia tikslą.
     *
     * @return taisyklių kelią {@code String}.
     */
    private String makePath() {
        path.setLength(0);
        stepCount = 0;
        Set<String> goals = new HashSet<>();
        goals.add(goal);
        goalFound = backwardChain(goals);
        if (goalFound) {
            logln(EMPTY_LINE);
            if (path.length() > 0) {
                logln("Success!");
                path.setLength(path.length() - COMMA.length());
            }
            else
                logln("Goal is in facts.");
            return path.toString();
        }
        logln(EMPTY_LINE);
        logln("Goal cannot be derived.");
        return EMPTY_LINE;
		/*
		 * Jei rekursija pavyks, grąžinsime kelią (be kablelio pradžioje), jei ne,
		 * tai grąžinsime tuščią eilutę.
		 */
    }

    /**
     * Rekursyviai ieško taisyklių kelio pagal tikslų sąrašą.
     *
     * @param goals - tam tikrų tikslų sąrašas.
     * @return {@code true} jei visi tikslai sąraše surasti, {@code false} priešingu atveju.
     */
    private boolean backwardChain(Set<String> goals) {
        StringBuilder savedPath = new StringBuilder(path);	// Išsaugome kelią
        Facts savedFacts = Facts.saveFacts(facts);			// Išsaugome faktus
        for (String goal : goals) {							// Einame per kiekvieną tikslą
            String initial = tabulations + "Goal " + goal;	// Šis string bus kiekvienoje
            // išvestoje eilutėje, todėl
            // jį išsaugojame
            if (facts.isFact(goal))							// Jei tikslas faktuose
                logln(++stepCount + initial + ". Fact.");	// Taip ir parašom
            else {											// O jei jo faktuose nėra
                if (goalLine.add(goal)) {			         // Tada ieškome tikslų eilutėje
                    // Šiuo atveju tikslas yra naujas
                    boolean goalFound = false;
                    for (Rule rule : rules) {				// Einame per kiekvieną taisyklę
                        // Bent kol neradome tikslo
                        if (!goalFound && rule.isConsequentGoal(goal)) {
                            // Jei taisyklės konsekventas
                            // ir yra mūsų ieškomas tikslas
                            Set<String> newGoals = rule.getAntecedents();
                            // Tai paimkime tos taisyklės
                            // antecedentus
                            logln(++stepCount + initial + ". Take " + rule + ". New goals: "
                                    + newGoals + ".");
                            tabulations.append(TABS);
                            goalFound = backwardChain(newGoals);
                            // Ir rekursyviai ieškokime jų
                            // kaip tikslų
                            tabulations.setLength(tabulations.length() - TABS.length());
                            if (goalFound) {				// Jei ši rekursija pavyks
                                facts.add(goal);			// Pridėkime tikslą prie faktų
                                path.append("R").append(rule.getNum()).append(COMMA);
                                // Pridėkime taisyklę prie kelio
                                logln(++stepCount + initial + ". New fact: " + goal
                                        + ". Facts: " + facts + ".");
                            }
                        }
                    }
                    goalLine.remove(goal);					// Kai baigiame su visom taisyklėm
                    // šito tikslo nebeieškome, todėl
                    // išimkime jį iš eilutės
                    if (!goalFound) {						// Jei po visų taisyklių tikslo
                        // nepavyko surasti
                        log(++stepCount + initial + ". Fact cannot be derived.");
                        restoreData(savedPath, savedFacts);	// Atstatom kelią ir faktus
                        return false;						// Rekursinis etapas nepavyko
                    }
                }
                else {										// Čia ateisime jei ieškomas
                    // tikslas jau yra eilutėje
                    log(++stepCount + initial + ". Loop.");	// Iš to seka, kad užsiciklinom
                    restoreData(savedPath, savedFacts);		// Atstatom kelią ir faktus
                    return false;							// Rekursinis etapas nepavyko
                }
            }
        }
        return true;										// Čia ateisime tik jei visi
        // tikslai yra faktai, arba
        // šių tikslų sukeltos rekursijos
        // pavyko - todėl šis rekursinis
        // etapas pavyko
    }

    /**
     * Sugrąžina kelio bei faktų būsenas. Kviečiamas <b>tik</b> esant ciklui,
     * arba kai negalima gauti kažkurio tikslo
     *
     * @param savedPath - rekursinio etapo pradžioje išsaugotas kelias.
     * @param savedFacts - rekursinio etapo pradžioje išsaugoti faktai.
     */
    private void restoreData(StringBuilder savedPath, Facts savedFacts) {
        if (!(path.toString().equals(savedPath.toString()))) {
            path = savedPath;
            facts = savedFacts;
            log(" Facts restored: " + facts +".");
        }
        logln(EMPTY_LINE);
    }

    /**
     * Registruoja objektą (kaip simbolių eilutę).<br>
     * Tam kviečiamas {@code obj.toString()} metodas.<br>
     * Po šios eilutės ar objekto <b>nėra įterpiamas</b> eilutės pabaigos simbolis.
     *
     * @param obj - bet koks objektas ({@code String}, {@code Integer} ir t.t.).
     */
    private void log(Object obj) {
        log.append(obj);
    }

    /**
     * Registruoja objektą (kaip simbolių eilutę).<br>
     * Tam kviečiamas {@code obj.toString()} metodas.<br>
     * Po šios eilutės ar objekto <b>yra įterpiamas</b> eilutės pabaigos simbolis.
     *
     * @param obj - bet koks objektas ({@code String}, {@code Integer} ir t.t.).
     */
    private void logln(Object obj) {
        log.append(obj).append(ENDLINE);
    }

    /**
     * Grąžina kelio paieškos registrą.
     *
     * @return registras {@code String} pavidalu.
     */
    @Override
    public String toString() {
        return log.toString();
    }

}
