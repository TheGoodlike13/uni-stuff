package eu.goodlike;

import eu.goodlike.config.Config;
import eu.goodlike.console.Console;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1 || args[0].toUpperCase().equals("HELP")) {
            printHelpDialogue();
            return;
        }

        List<String> files = Arrays.asList(args);
        Config config;
        try {
            config = new Config();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to find the configuration file.");
        }

        files.stream().forEach(file -> new Thread(new Console(config, file)).start());
    }

    private static void printHelpDialogue() {
        System.out.println("This program asks for 1 parameter:");
        System.out.println("A file containing the data to interpret.");
    }
}
