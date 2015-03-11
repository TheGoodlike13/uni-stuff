package com.goodlike.inputparser;

public class ParserFactory {

    private static final Parser DEFAULT_PARSER = new DefaultParser();

    public static Parser getParser() {
        return DEFAULT_PARSER;
    }

}
