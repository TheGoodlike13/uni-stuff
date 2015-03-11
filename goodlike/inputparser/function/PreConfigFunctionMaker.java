package com.goodlike.inputparser.function;

import com.goodlike.config.Config;
import com.goodlike.interpretator.Function;

import java.util.ArrayList;
import java.util.List;

public class PreConfigFunctionMaker implements FunctionMaker {

    @Override
    public List<Function> makeFunctions(List<String> definitions, Config config) {
        List<String> newDefinitions = new ArrayList<>();
        definitions.stream()
                .map(config::method)
                .forEach(newDefinitions::addAll);
        return new WebServiceFunctionMaker().makeFunctions(newDefinitions, config);
    }
}
