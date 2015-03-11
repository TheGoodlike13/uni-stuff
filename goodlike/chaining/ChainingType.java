package com.goodlike.chaining;

import java.util.List;

public enum ChainingType {
    BACKWARD {
        @Override
        public Chainer getChainer(List<String> rules, CharSequence facts, CharSequence goal) {
            return BackwardChainer.makeBWC(rules, facts, goal);
        }
    },
    @SuppressWarnings("unused")
    FORWARD {
        @Override
        public Chainer getChainer(List<String> rules, CharSequence facts, CharSequence goal) {
            return ForwardChainer.makeFWC(rules, facts, goal);
        }
    };

    public static ChainingType getMethod(String methodName) {
        return valueOf(methodName.toUpperCase());
    }

    public abstract Chainer getChainer(List<String> rules, CharSequence facts, CharSequence goal);

}