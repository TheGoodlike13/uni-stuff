package com.goodlike.inputparser.quality;

public enum QualityType {
    MULTI {
        @Override
        public double baseQuantifier() {
            return 1;
        }

        @Override
        public double calculate(double quantifier, double quality) {
            System.out.print(quantifier + " * " + quality + " = ");
            return quantifier * quality;
        }
    },

    ADD {
        @Override
        public double baseQuantifier() {
            return 0;
        }

        @Override
        public double calculate(double quantifier, double quality) {
            System.out.print(quantifier + " + " + quality + " = ");
            return quantifier + quality;
        }
    };

    public abstract double baseQuantifier();

    public abstract double calculate(double quantifier, double quality);

    public static QualityType getType(String string) {
        return valueOf(string);
    }
}
