package eu.goodlike.inputparser.function;

import eu.goodlike.config.Config;
import eu.goodlike.interpreter.Function;

import java.util.List;

/**
 * Retrieves the appropriate function list creation implementation
 */
public enum Modifier {

    F(new FileFunctionMaker()),
    @SuppressWarnings("unused")
    S(new WebServiceFunctionMaker()),
    C(new PreConfigFunctionMaker());

    private final FunctionMaker functionMaker;

    Modifier(FunctionMaker functionMaker) {
        this.functionMaker = functionMaker;
    }

    public static Modifier getInstance(String modifier) {
        return valueOf(modifier.substring(1).toUpperCase());
    }

    public List<Function> makeFunctions(List<String> definitions, Config config) {
        return functionMaker.makeFunctions(definitions, config);
    }

}