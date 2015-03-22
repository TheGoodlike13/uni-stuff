package com.goodlike.inputparser;

import com.goodlike.config.Config;

/**
 * Parses the input file using the config.xml if needed
 */
@FunctionalInterface
public interface Parser {

    InputData parse(String file, Config config);

}
