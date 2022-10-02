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
    private double rangeBegin = 3.0;
    private double rangeEnd = 5.0;
    private int populationAmount = 1000;
    private int epochsAmount = 20;
    private int chromosomeAmount = 10;
    private int eliteStrategyAmount = 12;
    private double crossProbability = 0.3;
    private double mutationProbability = 0.6;
    private double inversionProbability = 0.2;
    private String selectionMethod = "best";
    private String crossMethod = "cross-one";
    private String mutationMethod = "mutation-two";
    private boolean maximization = true;
}
