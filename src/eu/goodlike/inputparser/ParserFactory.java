package eu.goodlike.inputparser;

/**
 * Initializes the parser
 */
public class ParserFactory {

    private static final Parser DEFAULT_PARSER = new DefaultParser();

    public static Parser getParser() {
        return DEFAULT_PARSER;
    }

}
