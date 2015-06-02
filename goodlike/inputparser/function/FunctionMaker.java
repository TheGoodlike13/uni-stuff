package com.goodlike.inputparser.function;

import com.goodlike.config.Config;
import com.goodlike.interpreter.Function;

import java.util.List;

/**
 * Returns a list of functions
 */
@FunctionalInterface
public interface FunctionMaker {

    List<Function> makeFunctions(List<String> definitions, Config config);

}
