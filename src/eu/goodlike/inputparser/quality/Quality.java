package eu.goodlike.inputparser.quality;

/**
 * Handles a particular quality of a type
 */
public class Quality {

    private final String configName;
    private final String qualityName;
    public String wsdlName() {
        return qualityName;
    }
    public String name() {
        return configName + " (" + qualityName + ")";
    }

    private final QualityType qualityType;
    private final QualityMode qualityMode;

    private final double requirement;

    private double coefficient = 0;
    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }
    public double weight(double quantifier) {
        double result = coefficient * quantifier;
        System.out.print(name() + String.format(" weight: %.2f * %.2f = %.2f", quantifier, coefficient, result));
        if (qualityMode == QualityMode.MIN) {
            System.out.println("; adding to total weight");
            return result;
        }

        System.out.println("; removing from total weight");
        return -result;
    }

    public double getBaseQuantifier() {
        return qualityType.baseQuantifier();
    }

    public double calculate(double quantifier, double quality) {
        if (Double.valueOf("NaN").equals(quality))
            return quantifier;

        System.out.print(this);
        return qualityType.calculate(quantifier, quality);
    }

    public boolean isHigh(double quantifier) {
        return qualityMode.compare(quantifier, requirement);
    }

    public Quality(String configName, String qualityName, QualityType qualityType, QualityMode qualityMode, double requirement) {
        this.configName = configName;
        this.qualityName = qualityName;
        this.qualityType = qualityType;
        this.qualityMode = qualityMode;
        this.requirement = requirement;
    }

    @Override
    public String toString() {
        return qualityType + "; " + qualityMode + String.format("; %.2f; ", requirement);
    }

}
