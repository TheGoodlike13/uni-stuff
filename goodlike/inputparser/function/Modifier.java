package com.goodlike.inputparser.function;

import com.goodlike.config.Config;
import com.goodlike.interpretator.Function;

import java.util.List;

public enum Modifier {

    F(new FileFunctionMaker()),
    @SuppressWarnings("unused")
    S(new WebServiceFunctionMaker()),
    C(new PreConfigFunctionMaker());

    private final FunctionMaker functionMaker;

    private Modifier(FunctionMaker functionMaker) {
        this.functionMaker = functionMaker;
    }

    public static Modifier getInstance(String modifier) {
        return valueOf(modifier.substring(1).toUpperCase());
    }

    public List<Function> makeFunctions(List<String> definitions, Config config) {
        return functionMaker.makeFunctions(definitions, config);
    }

}