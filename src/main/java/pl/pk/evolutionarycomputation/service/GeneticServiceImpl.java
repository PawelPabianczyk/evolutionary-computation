package pl.pk.evolutionarycomputation.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pk.evolutionarycomputation.dto.GeneticAlgorithmConfigurationDTO;
import pl.pk.evolutionarycomputation.dto.ResultsDTO;
import pl.pk.evolutionarycomputation.enums.Mode;
import pl.pk.evolutionarycomputation.enums.Rank;
import pl.pk.evolutionarycomputation.enums.Selection;
import pl.pk.evolutionarycomputation.enums.Tournament;
import pl.pk.evolutionarycomputation.model.Chromosome;
import pl.pk.evolutionarycomputation.model.FunctionResult;
import pl.pk.evolutionarycomputation.util.generator.ChromosomeGenerator;
import pl.pk.evolutionarycomputation.util.selection.ISelection;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GeneticServiceImpl implements GeneticService{

    private final ChromosomeGenerator chromosomeGenerator;
    private final ISelection selection;


    @Override
    public ResultsDTO perform(GeneticAlgorithmConfigurationDTO dto) {
        List<Chromosome> chromosomes = chromosomeGenerator
                .generateChromosomesBinary(dto.getPopulationAmount(), dto.getRangeBegin(), dto.getRangeEnd());

        //TODO for loop

        List<FunctionResult> functionResults = chromosomes.stream()
                .map(chromosome -> new FunctionResult(chromosome, /*TODO*/ (x) -> (2 * Math.pow(x, 2)) + 5))
                .collect(Collectors.toList());

        List<FunctionResult> selectionResultsList;
        Map<FunctionResult, Double> selectionResultsMap;

        if(Selection.ROULETTE.equals(dto.getSelectionMethod())) {
            selectionResultsMap = selection.rouletteMethod(functionResults, /*TODO*/ Mode.MINIMIZATION);
        } else {
            selectionResultsList = switch (dto.getSelectionMethod()) {
                case BEST_ELEMENTS -> selection.bestElementsMethod(functionResults, /*TODO*/ 0.3f, /*TODO*/Mode.MINIMIZATION);
                case TOURNAMENT -> selection.tournamentMethod(functionResults, /*TODO*/ 10, /*TODO*/ Tournament.SINGLE, /*TODO*/Mode.MINIMIZATION);
                case RANKING -> selection.rankingMethod(functionResults, /*TODO*/ Rank.MINIMUM_VALUE);
                default -> throw new IllegalStateException("Unexpected value: " + dto.getSelectionMethod());
            };
        }

        //TODO -> ...

        return new ResultsDTO(); //TODO
    }
}
