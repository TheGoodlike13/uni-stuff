package com.goodlike.plan;

import com.goodlike.interpreter.Function;

import java.util.Map;
import java.util.stream.Collectors;

public class FunctionWrapper {

    public double quality(String name) {
        return qualities.get(name);
    }

    public int index() {
        return index;
    }

    public Function function() {
        return function;
    }

    public Map<String, Double> qualities() {
        return qualities;
    }

    @Override
    public String toString() {
        return function.toString();
    }

    // CONSTRUCTORS

    public FunctionWrapper(int index, Function function) {
        this.index = index;
        this.function = function;
        this.qualities = function.getMetaData().entrySet().stream()
            .collect(Collectors.toMap(FunctionWrapper::keyToString, FunctionWrapper::valueToDouble));
    }

    // PRIVATE

    private final int index;
    private final Function function;
    private final Map<String, Double> qualities;

    private static <K, V> String keyToString(Map.Entry<K, V> entry) {
        return String.valueOf(entry.getKey());
    }

    private static <K, V> Double valueToDouble(Map.Entry<K, V> entry) {
        String result = String.valueOf(entry.getValue());
        result = result == null ? "NaN" : result;
        return Double.parseDouble(result);
    }

}
