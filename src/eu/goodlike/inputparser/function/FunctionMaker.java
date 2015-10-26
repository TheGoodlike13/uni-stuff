package eu.goodlike.inputparser.function;

import eu.goodlike.config.Config;
import eu.goodlike.interpreter.Function;

import java.util.List;

/**
 * Returns a list of functions
 */
@FunctionalInterface
public interface FunctionMaker {

    List<Function> makeFunctions(List<String> definitions, Config config);

}
