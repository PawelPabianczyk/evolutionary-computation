package pl.pk.evolutionarycomputation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneticAlgorithmConfiguration {
    //TODO - add default values
    private double rangeBegin;
    private double rangeEnd;
    private int populationAmount;
    private int epochsAmount;
    private int chromosomeAmount;
    private int eliteStrategyAmount;
    private double crossProbability;
    private double mutationProbability;
    private double inversionProbability;
    private String selectionMethod;
    private String crossMethod;
    private String mutationMethod;
    private boolean maximization;
}
