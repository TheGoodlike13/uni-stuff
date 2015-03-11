package com.goodlike.inputparser.quality;

public enum QualityMode {
    MIN {
        @Override
        public boolean compare(double quantifier, double requirement) {
            return quantifier >= requirement;
        }
    },

    MAX {
        @Override
        public boolean compare(double quantifier, double requirement) {
            return quantifier <= requirement;
        }
    };

    public abstract boolean compare(double quantifier, double requirement);

    public static QualityMode getMode(String string) {
        return valueOf(string);
    }
}
