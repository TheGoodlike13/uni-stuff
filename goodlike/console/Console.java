package com.goodlike.console;

import com.goodlike.config.Config;
import com.goodlike.inputparser.InputData;
import com.goodlike.inputparser.Parser;
import com.goodlike.inputparser.ParserFactory;
import com.goodlike.interpretator.PlanInterpretator;
import com.goodlike.interpretator.plan.PlanFinder;
import com.goodlike.plan.JuliusPlanFinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console implements Runnable {

    private final static BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));

    private final InputData input;

    public Console(Config config, String file) {
        Parser parser = ParserFactory.getParser();
        this.input = parser.parse(file, config);
    }

    @Override
    public void run() {
        PlanInterpretator planInterpretator = makePI();

        String command;
        do {
            Object[] data = input.factStrings().stream()
                    .map(fact -> {
                        System.out.print("Input " + fact + ": ");
                        return readLine();
                    })
                    .toArray();

            Object goal = planInterpretator.execute(data);
            System.out.print(planInterpretator);
            System.out.print("Goal achieved!");
            System.out.print(input.goalString() + ": " + goal);

            System.out.print("Input 'y' to continue...");
            command = readLine();
        } while ("Y".equals(command.toUpperCase()));
    }

    private PlanInterpretator makePI() {
        PlanFinder planFinder = new JuliusPlanFinder(input);
        return PlanInterpretator.makePI(input.ruleData(), input.factStrings(), input.goalString(), planFinder);
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
