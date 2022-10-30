package pl.pk.evolutionarycomputation.dto;

import pl.pk.evolutionarycomputation.enums.Crossover;
import pl.pk.evolutionarycomputation.enums.Mutation;
import pl.pk.evolutionarycomputation.enums.Selection;

public class GeneticAlgorithmConfigurationDTO {
    private final int rangeBegin = 3;
    private final int rangeEnd = 5;
    private final int populationAmount = 1000;
    private final int epochsAmount = 20;
    private final int chromosomeAmount = 10;
    private final int eliteStrategyAmount = 12;
    private final int crossProbability = 30;
    private final int mutationProbability = 60;
    private final int inversionProbability = 20;
    private final Selection selectionMethod = Selection.BEST_ELEMENTS;
    private final Crossover crossMethod = Crossover.ONE_POINT;
    private final Mutation mutationMethod = Mutation.ONE_POINT;
    private final boolean maximization = true;

    public int getRangeBegin() {
        return rangeBegin;
    }

    public int getRangeEnd() {
        return rangeEnd;
    }

    public int getPopulationAmount() {
        return populationAmount;
    }

    public int getEpochsAmount() {
        return epochsAmount;
    }

    public int getChromosomeAmount() {
        return chromosomeAmount;
    }

    public int getEliteStrategyAmount() {
        return eliteStrategyAmount;
    }

    public int getCrossProbability() {
        return crossProbability;
    }

    public int getMutationProbability() {
        return mutationProbability;
    }

    public int getInversionProbability() {
        return inversionProbability;
    }

    public Selection getSelectionMethod() {
        return selectionMethod;
    }

    public Crossover getCrossMethod() {
        return crossMethod;
    }

    public Mutation getMutationMethod() {
        return mutationMethod;
    }

    public boolean isMaximization() {
        return maximization;
    }
}
