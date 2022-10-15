package pl.pk.evolutionarycomputation.service;

import pl.pk.evolutionarycomputation.dto.GeneticAlgorithmConfigurationDTO;
import pl.pk.evolutionarycomputation.dto.ResultsDTO;

public interface GeneticService {
    ResultsDTO perform(GeneticAlgorithmConfigurationDTO geneticAlgorithmConfigurationDTO);
}
