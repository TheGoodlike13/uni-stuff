package com.goodlike.chaining;

import com.goodlike.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code ForwardChainer} klasė naudoja <b>forward chaining</b> algortimą
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
public class ForwardChainer implements Chainer {

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
    private final Facts facts;

    /** Saugo tikslo vardą. */
    private final String goal;

    /** Registruoja algoritmo vykdomus veiksmus. */
    private final StringBuilder log;

    /** Saugo taisyklių kelią. */
    private final String pathString;

    /** Saugo ar pavyko surasti tikslą */
    private boolean success;

    /**
     * {@code Static factory} metodas.
     * Šiuo metodu sukuriami {@code ForwardChainer} objektai.
     *
     * @param ruleStrings - taisyklių aprašimų sąrašas.
     * @param factString - faktų vardai.
     * @param goalString - tikslo vardas.
     * @return naują {@code ForwardChainer} objektą, kuriam perduodami parametrai.
     * @throws IllegalArgumentException jei parametrai tušti, trūksta taisyklių elementų,
     * daugiau nei vienas tikslas, arba dubliuojasi taisyklių ar faktų elementai.
     * @throws NullPointerException jei kuris nors parametras ar taisyklė lygi {@code null}.
     */
    public static ForwardChainer makeFWC(List<String> ruleStrings,
                                         CharSequence factString,
                                         CharSequence goalString) {

        List<Rule> rules = makeRules(ruleStrings);
        Facts facts = Facts.makeFacts(factString.toString());
        String goal = goalString.toString();
        if ((rules.isEmpty()) || (goal.trim().isEmpty()))
            throw new IllegalArgumentException("Empty arguments not allowed.");
        if (StringUtils.spaceSplitCount(goal) > 1)
            throw new IllegalArgumentException("There can only be one goal: " + goal);
        return new ForwardChainer(rules, facts, goal);
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
     * Inicializuoja {@code ForwardChainer} objektą. Kviečiamas <b>tik</b> per
     * {@code static factory} metodą.
     *
     * @param rules - taisyklių sąrašas.
     * @param facts - faktų aibė.
     * @param goal - tikslo vardas.
     */
    private ForwardChainer(List<Rule> rules, Facts facts, String goal) {
        this.rules = rules;
        this.facts = facts;
        this.goal = goal;
        log = new StringBuilder();
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
        return success;
    }

    /**
     * Suranda ir grąžina kelią, kuriuo taisyklės pasiekia tikslą.<br>
     *
     * @return taisyklių kelią {@code String}.
     */
    private String makePath() {
        int stepCount = 0;
        int iterationCount = 0;
        StringBuilder path = new StringBuilder();

        boolean goalFound = facts.isFact(goal);
        if (goalFound) {							// Jei tikslas faktuose
            logln("Goal is in facts.");
            success = true;
            return EMPTY_LINE;						// Gražiname tuščią kelią
        }
        while (!goalFound) {						// Kol surasime kelią, iteruojame
            boolean continueFlag = true;
            logln("Iteration " + ++iterationCount);
            for (Rule rule : rules) {				// Einame per kiekvieną taisyklę
                if (continueFlag) {					// Bent kol nepasisekė surasti
                    // įvykdomos taisyklės
                    log(++stepCount + TABS + rule);
                    switch (rule.getFlag()) {		// Tikriname taisyklės vėliavėlę
                        default: 					// Ši dalis turėtų būti neįmanoma
                            throw new AssertionError("Something has gone terribly wrong.");
                        case FAILED:				// flag2
                            logln(" was not applied because its " +
                                    "consequent was found among facts. (flag2)");
                            break;
                        case USED:					// flag1
                            logln(" was not applied because it's already " +
                                    "been applied once. (flag1)");
                            break;
                        case NOT_USED:				// Taisyklė dar nevykdyta
                            String missingAntecedent = rule.testAntecedents(facts);
                            // Tikriname ar taisyklė įvykdoma
                            if (missingAntecedent != null) {
                                // Nepavyko dėl antecedentų trūkumo
                                logln(" was not applied because fact " +
                                        missingAntecedent + " is missing.");
                            }
                            else {
                                if (rule.isConsequentFact(facts)) {
                                    // Antecedentai kaip ir yra,
                                    // bet konsekventas faktuose
                                    rule.setFlag(RuleUsageFlag.FAILED);
                                    // Nustatom flag2
                                    logln(" was not applied because"
                                            + " consequent is already a fact.");
                                }
                                else {
                                    // Taisyklė įvykdoma, tai ir įvykdykim
                                    continueFlag = false;
                                    // Kitų taisyklių šioje iteracijoje
                                    // nebejudinsim
                                    rule.setFlag(RuleUsageFlag.USED);
                                    // Nustatom flag1
                                    rule.apply(facts);
                                    // Pridedam konsekventą prie faktų
                                    path.append("R").append(rule.getNum()).append(COMMA);
                                    // Pridedam taisyklę prie kelio
                                    logln(" has been applied. Facts: " + facts + ".");
                                }
                            }
                            break;
                    }
                }
            }
            if (continueFlag) {						// Jei praėjom pro visas taisykles
                // o vykdomų taisyklių neradom
                logln("Iteration unsuccesful.");
                logln(EMPTY_LINE);
                logln("Goal cannot be derived.");
                success = false;
                return EMPTY_LINE;					// Grąžinam tuščią kelią
            }
            logln(EMPTY_LINE);
            goalFound = facts.isFact(goal);			// Prieš naują iteraciją, patikrinam,
            // gal jau suradom kelią
        }
        logln("Success!");
        success = true;
        path.setLength(path.length() - COMMA.length());
        // Nutrinam kablelį ant kelio galo
        return path.toString();						// Grąžinam surastą kelią
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
