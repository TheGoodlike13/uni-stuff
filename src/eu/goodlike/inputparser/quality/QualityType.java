package eu.goodlike.inputparser.quality;

/**
 * Quality modes:
 * MULTI - multiplies the qualities to get the result
 * ADD - adds the qualities to get the result
 */
public enum QualityType {
    MULTI {
        @Override
        public double baseQuantifier() {
            return 1;
        }

        @Override
        public double calculate(double quantifier, double quality) {
            double result = quantifier * quality;
            System.out.println(String.format("%.2f * %.2f = %.2f", quantifier, quality, result));
            return result;
        }
    },

    ADD {
        @Override
        public double baseQuantifier() {
            return 0;
        }

        @Override
        public double calculate(double quantifier, double quality) {
            double result = quantifier + quality;
            System.out.println(String.format("%.2f + %.2f = %.2f", quantifier, quality, result));
            return result;
        }
    };

    public abstract double baseQuantifier();

    public abstract double calculate(double quantifier, double quality);

    public static QualityType getType(String string) {
        return valueOf(string);
    }
}
