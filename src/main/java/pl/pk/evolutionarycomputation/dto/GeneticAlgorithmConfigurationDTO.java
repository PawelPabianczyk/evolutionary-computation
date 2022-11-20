package pl.pk.evolutionarycomputation.dto;

import pl.pk.evolutionarycomputation.enums.Crossover;
import pl.pk.evolutionarycomputation.enums.Mutation;
import pl.pk.evolutionarycomputation.enums.Selection;
import pl.pk.evolutionarycomputation.enums.Tournament;

public class GeneticAlgorithmConfigurationDTO {
    private final int rangeBegin = -5;
    private final int rangeEnd = 5;
    private final int populationAmount = 100;
    private final int epochsAmount = 100;
    private final int chromosomeAmount = 10;
    private final int eliteStrategyAmount = 10;
    private final int crossProbability = 60;
    private final int mutationProbability = 40;
    private final int inversionProbability = 10;
    private final Selection selectionMethod = Selection.ROULETTE;
    private final Crossover crossMethod = Crossover.ONE_POINT;
    private final Mutation mutationMethod = Mutation.ONE_POINT;
    private final Tournament tournament = Tournament.SINGLE;
    private final boolean maximization = true;

    private final int percentageOfBestElements = 30;
    private final int tournamentSize = 10;
    private final int spinNumber = 30;

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

    public Tournament getTournament() {
        return tournament;
    }

    public int getPercentageOfBestElements() {
        return percentageOfBestElements;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }

    public int getSpinNumber() {
        return spinNumber;
    }
}
