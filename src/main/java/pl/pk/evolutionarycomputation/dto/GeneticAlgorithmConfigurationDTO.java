package pl.pk.evolutionarycomputation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pk.evolutionarycomputation.enums.Mode;
import pl.pk.evolutionarycomputation.enums.Selection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneticAlgorithmConfigurationDTO {
    //TODO - add default values
    private int rangeBegin = 3;
    private int rangeEnd = 5;
    private int populationAmount = 1000;
    private int epochsAmount = 20;
    private int chromosomeAmount = 10;
    private int eliteStrategyAmount = 12;
    private double crossProbability = 0.3;
    private double mutationProbability = 0.6;
    private double inversionProbability = 0.2;
    private Selection selectionMethod = Selection.BEST_ELEMENTS;
    private String crossMethod = "cross-one";
    private String mutationMethod = "mutation-two";
    private boolean maximization = true;
}
