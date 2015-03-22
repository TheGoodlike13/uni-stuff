package com.goodlike.inputparser.quality;

/**
 * Handles a particular quality of a type
 */
public class Quality {

    private final String qualityName;
    public String name() {
        return qualityName;
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
        System.out.println(qualityName + " weigthing: " + quantifier + " * " + coefficient + " = " + result);
        return result;
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

    public Quality(String qualityName, QualityType qualityType, QualityMode qualityMode, double requirement) {
        this.qualityName = qualityName;
        this.qualityType = qualityType;
        this.qualityMode = qualityMode;
        this.requirement = requirement;
    }

    @Override
    public String toString() {
        return qualityType + "; " + qualityMode + "; " + requirement + "; ";
    }

}
