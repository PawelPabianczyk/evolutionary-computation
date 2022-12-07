package pl.pk.evolutionarycomputation.dto;

import java.util.Map;
import java.util.Objects;

public final class ResultsDTO {
    private double calculationTime;
    private Map<Integer, Double> bestFunctionValues;
    private Map<Integer, Double> avgFunctionValues;
    private Map<Integer, Double> standardDeviations;

    public ResultsDTO() {
    }

    public ResultsDTO(double calculationTime, Map<Integer, Double> bestFunctionValues, Map<Integer, Double> avgFunctionValues, Map<Integer, Double> standardDeviations) {
        this.calculationTime = calculationTime;
        this.bestFunctionValues = bestFunctionValues;
        this.avgFunctionValues = avgFunctionValues;
        this.standardDeviations = standardDeviations;
    }

    public double calculationTime() {
        return calculationTime;
    }

    public Map<Integer, Double> bestFunctionValues() {
        return bestFunctionValues;
    }

    public Map<Integer, Double> avgFunctionValues() {
        return avgFunctionValues;
    }

    public Map<Integer, Double> standardDeviations() {
        return standardDeviations;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ResultsDTO) obj;
        return Double.doubleToLongBits(this.calculationTime) == Double.doubleToLongBits(that.calculationTime) &&
                Objects.equals(this.bestFunctionValues, that.bestFunctionValues) &&
                Objects.equals(this.avgFunctionValues, that.avgFunctionValues) &&
                Objects.equals(this.standardDeviations, that.standardDeviations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calculationTime, bestFunctionValues, avgFunctionValues, standardDeviations);
    }

    @Override
    public String toString() {
        return "ResultsDTO[" +
                "calculationTime=" + calculationTime + ", " +
                "bestFunctionValues=" + bestFunctionValues + ", " +
                "avgFunctionValues=" + avgFunctionValues + ", " +
                "standardDeviations=" + standardDeviations + ']';
    }

}
