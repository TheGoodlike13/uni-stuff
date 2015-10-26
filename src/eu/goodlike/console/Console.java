package eu.goodlike.console;

import eu.goodlike.config.Config;
import eu.goodlike.inputparser.InputData;
import eu.goodlike.inputparser.Parser;
import eu.goodlike.inputparser.ParserFactory;
import eu.goodlike.interpreter.PlanInterpreter;
import eu.goodlike.interpreter.plan.PlanFinder;
import eu.goodlike.plan.JuliusPlanFinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Deals with output and input to system console
 */
public class Console implements Runnable {

    private final static BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));

    private final InputData input;

    public Console(Config config, String file) {
        Parser parser = ParserFactory.getParser();
        this.input = parser.parse(file, config);
    }

    @Override
    public void run() {
        PlanInterpreter planInterpreter = makePI();

        String command;
        do {
            Object[] data = input.factStrings().stream()
                    .map(fact -> {
                        System.out.print("Input " + fact + ": ");
                        return readLine();
                    })
                    .toArray();

            Object goal = planInterpreter.execute(data);
            System.out.print(planInterpreter);
            System.out.println("Goal achieved!");
            System.out.println(input.goalString() + ": " + goal);

            System.out.print("Input 'y' to continue...");
            command = readLine();
        } while ("Y".equals(command.toUpperCase()));
    }

    private PlanInterpreter makePI() {
        PlanFinder planFinder = new JuliusPlanFinder(input);
        return PlanInterpreter.makePI(input.ruleData(), input.factStrings(), input.goalString(), planFinder);
    }

    private static String readLine() {
        try {
            return INPUT.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to read from console for some reason.");
        }
    }

}
