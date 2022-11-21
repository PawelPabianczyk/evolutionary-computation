package pl.pk.evolutionarycomputation.dto;

import pl.pk.evolutionarycomputation.enums.Crossover;
import pl.pk.evolutionarycomputation.enums.Mutation;
import pl.pk.evolutionarycomputation.enums.Selection;
import pl.pk.evolutionarycomputation.enums.Tournament;

public class GeneticAlgorithmConfigurationDTO {
    private int rangeBegin = -5;
    private int rangeEnd = 5;
    private int populationAmount = 100;
    private int epochsAmount = 100;
    private int chromosomeAmount = 10;
    private int eliteStrategyAmount = 10;
    private int crossProbability = 60;
    private int mutationProbability = 40;
    private int inversionProbability = 10;
    private Selection selectionMethod = Selection.ROULETTE;
    private Crossover crossMethod = Crossover.ONE_POINT;
    private Mutation mutationMethod = Mutation.ONE_POINT;
    private Tournament tournament = Tournament.SINGLE;
    private boolean maximization = true;

    private int percentageOfBestElements = 30;
    private int tournamentSize = 10;
    private int spinNumber = 30;


    public GeneticAlgorithmConfigurationDTO(int rangeBegin, int rangeEnd, int populationAmount, int epochsAmount, int chromosomeAmount, int eliteStrategyAmount, int crossProbability, int mutationProbability, int inversionProbability, Selection selectionMethod, Crossover crossMethod, Mutation mutationMethod, Tournament tournament, boolean maximization, int percentageOfBestElements, int tournamentSize, int spinNumber) {
        this.rangeBegin = rangeBegin;
        this.rangeEnd = rangeEnd;
        this.populationAmount = populationAmount;
        this.epochsAmount = epochsAmount;
        this.chromosomeAmount = chromosomeAmount;
        this.eliteStrategyAmount = eliteStrategyAmount;
        this.crossProbability = crossProbability;
        this.mutationProbability = mutationProbability;
        this.inversionProbability = inversionProbability;
        this.selectionMethod = selectionMethod;
        this.crossMethod = crossMethod;
        this.mutationMethod = mutationMethod;
        this.tournament = tournament;
        this.maximization = maximization;
        this.percentageOfBestElements = percentageOfBestElements;
        this.tournamentSize = tournamentSize;
        this.spinNumber = spinNumber;
    }

    public GeneticAlgorithmConfigurationDTO() {

    }

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
