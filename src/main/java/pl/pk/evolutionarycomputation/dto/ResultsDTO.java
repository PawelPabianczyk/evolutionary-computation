package pl.pk.evolutionarycomputation.dto;

import java.util.Map;

public record ResultsDTO(double calculationTime, Map<Integer, Double> bestFunctionValues, Map<Integer, Double> avgFunctionValues, Map<Integer, Double> standardDeviations) {
}
