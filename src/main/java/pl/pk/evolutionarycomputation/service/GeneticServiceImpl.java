package pl.pk.evolutionarycomputation.service;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pk.evolutionarycomputation.dto.GeneticAlgorithmConfigurationDTO;
import pl.pk.evolutionarycomputation.dto.ResultsDTO;
import pl.pk.evolutionarycomputation.enums.*;
import pl.pk.evolutionarycomputation.model.Candidate;
import pl.pk.evolutionarycomputation.model.Chromosome;
import pl.pk.evolutionarycomputation.model.FunctionResult;
import pl.pk.evolutionarycomputation.model.FunctionResult2;
import pl.pk.evolutionarycomputation.util.generator.ChromosomeGenerator;
import pl.pk.evolutionarycomputation.util.selection.ISelection;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service
public class GeneticServiceImpl implements GeneticService {

    private final ChromosomeGenerator chromosomeGenerator;
    private final ISelection selection;

    public GeneticServiceImpl(ChromosomeGenerator chromosomeGenerator, ISelection selection) {
        this.chromosomeGenerator = chromosomeGenerator;
        this.selection = selection;
    }

    @Override
    public ResultsDTO perform(GeneticAlgorithmConfigurationDTO dto) {
        List<Chromosome> chromosomes = chromosomeGenerator
                .generateChromosomesBinary(dto.getPopulationAmount(), dto.getRangeBegin(), dto.getRangeEnd());


        List<FunctionResult> functionResults = chromosomes.stream()
                .map(chromosome -> new FunctionResult(chromosome, /*TODO*/ (x) -> (2 * Math.pow(x, 2)) + 5))
                .collect(Collectors.toList());

        List<FunctionResult> selectionResultsList;
        Map<FunctionResult, Double> selectionResultsMap;

        if (Selection.ROULETTE.equals(dto.getSelectionMethod())) {
            selectionResultsMap = selection.rouletteMethod(functionResults, /*TODO*/ Mode.MINIMIZATION);
        } else {
            selectionResultsList = switch (dto.getSelectionMethod()) {
                case BEST_ELEMENTS ->
                        selection.bestElementsMethod(functionResults, /*TODO*/ 0.3f, /*TODO*/Mode.MINIMIZATION);
                case TOURNAMENT ->
                        selection.tournamentMethod(functionResults, /*TODO*/ 10, /*TODO*/ Tournament.SINGLE, /*TODO*/Mode.MINIMIZATION);
                case RANKING -> selection.rankingMethod(functionResults, /*TODO*/ Rank.MINIMUM_VALUE);
                default -> throw new IllegalStateException("Unexpected value: " + dto.getSelectionMethod());
            };
        }

        //TODO -> ...

        return new ResultsDTO(); //TODO
    }


    @Override
    public ResultsDTO perform2(GeneticAlgorithmConfigurationDTO dto) {

        // TODO: 30/10/2022 add elite strategy

        BinaryOperator<Double> bealeFunction = (x1, x2) ->
                Math.pow((1.5 - x1 + x1 * x2), 2) +
                        Math.pow((2.25 - x1 + x1 * Math.pow(x2, 2)), 2) +
                        Math.pow((2.625 - x1 + x1 * Math.pow(x2, 3)), 2);


        List<Candidate> candidates = initializePopulation(dto.getPopulationAmount(), dto.getRangeBegin(), dto.getRangeEnd());


        List<FunctionResult2> functionResults;

        List<Candidate> eliteCandidates;

        for (int i = 0; i < dto.getEpochsAmount(); i++) {

            functionResults = evaluation(candidates, bealeFunction);

            eliteCandidates = eliteStrategy(functionResults, dto.getEliteStrategyAmount(), Mode.MINIMIZATION);

            functionResults = selection(dto.getSelectionMethod(), functionResults);

            candidates = crossover(dto.getCrossMethod(), functionResults.stream().map(FunctionResult2::getCandidate).collect(Collectors.toList()), dto.getPopulationAmount(), dto.getCrossProbability());

            candidates = mutation(dto.getMutationMethod(), candidates, dto.getMutationProbability());

            candidates = inversion(candidates, dto.getInversionProbability());

            candidates.addAll(eliteCandidates);
        }


        return new ResultsDTO(); //TODO
    }

    private List<Candidate> eliteStrategy(List<FunctionResult2> functionResults, int eliteStrategyAmount, Mode mode) {
        // TODO: 30/10/2022 it should be always even number
        return switch (mode) {
            case MINIMIZATION -> functionResults.stream()
                    .sorted()
                    .map(FunctionResult2::getCandidate)
                    .limit(eliteStrategyAmount)
                    .collect(Collectors.toList());

            case MAXIMIZATION -> functionResults.stream()
                    .sorted(Comparator.reverseOrder())
                    .map(FunctionResult2::getCandidate)
                    .limit(eliteStrategyAmount)
                    .collect(Collectors.toList());
        };
    }

    private List<Candidate> initializePopulation(int populationAmount, int rangeBegin, int rangeEnd) {
        List<Chromosome> chromosomesA = chromosomeGenerator
                .generateChromosomesBinary(populationAmount, rangeBegin, rangeEnd);
        List<Chromosome> chromosomesB = chromosomeGenerator
                .generateChromosomesBinary(populationAmount, rangeBegin, rangeEnd);

        List<Candidate> candidates = new ArrayList<>();

        for (int i = 0; i < chromosomesA.size(); i++) {
            candidates.add(new Candidate(Arrays.asList(chromosomesA.get(i), chromosomesB.get(i))));
        }

        return candidates;
    }

    private List<FunctionResult2> evaluation(List<Candidate> candidates, BinaryOperator<Double> function) {
        return candidates.stream()
                .map(candidate -> new FunctionResult2(candidate, function)).toList();
    }

    private List<FunctionResult2> selection(Selection selectionMethod, List<FunctionResult2> functionResults) {

        // TODO: 30/10/2022 add roulette method

        return switch (selectionMethod) {
            case TOURNAMENT ->
                    selection.tournamentMethod2(functionResults, /*TODO*/ 10, /*TODO*/ Tournament.SINGLE, /*TODO*/Mode.MINIMIZATION);
            case RANKING -> selection.rankingMethod2(functionResults, /*TODO*/ Rank.MINIMUM_VALUE);
            default -> selection.bestElementsMethod2(functionResults, /*TODO*/ 0.3f, /*TODO*/Mode.MINIMIZATION);

        };
    }

    private List<Candidate> crossover(Crossover2 crossMethod, List<Candidate> candidates, int populationAmount, int probability) {
        return switch (crossMethod) {
            case TWO_POINT -> Crossover2.TWO_POINT.compute(candidates, populationAmount, probability);
            case THREE_POINT -> Crossover2.THREE_POINT.compute(candidates, populationAmount, probability);
            case UNIFORM -> Crossover2.UNIFORM.compute(candidates, populationAmount, probability);
            default -> Crossover2.ONE_POINT.compute(candidates, populationAmount, probability);

        };
    }

    private List<Candidate> mutation(Mutation2 mutationMethod, List<Candidate> candidates, int probability) {
        return switch (mutationMethod) {
            case TWO_POINT -> Mutation2.TWO_POINT.compute(candidates, probability);
            case EDGE -> Mutation2.EDGE.compute(candidates, probability);
            default -> Mutation2.ONE_POINT.compute(candidates, probability);
        };
    }

    private List<Candidate> inversion(List<Candidate> candidates, int probability) {
        return Mutation2.INVERSE.compute(candidates, probability);
    }
}
