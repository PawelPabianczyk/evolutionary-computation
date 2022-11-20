package pl.pk.evolutionarycomputation.service;


import org.springframework.stereotype.Service;
import pl.pk.evolutionarycomputation.dto.GeneticAlgorithmConfigurationDTO;
import pl.pk.evolutionarycomputation.dto.ResultsDTO;
import pl.pk.evolutionarycomputation.enums.*;
import pl.pk.evolutionarycomputation.model.Candidate;
import pl.pk.evolutionarycomputation.model.Chromosome;
import pl.pk.evolutionarycomputation.model.FunctionResult;
import pl.pk.evolutionarycomputation.util.generator.ChromosomeGenerator;
import pl.pk.evolutionarycomputation.util.selection.ISelection;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service
public class GeneticServiceImpl implements GeneticService {

    private final ChromosomeGenerator chromosomeGenerator;
    private final ISelection selection;
    private final DateTimeFormatter formatter;


    public GeneticServiceImpl(ChromosomeGenerator chromosomeGenerator, ISelection selection) {
        this.chromosomeGenerator = chromosomeGenerator;
        this.selection = selection;
        this.formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    }

    @Override
    public ResultsDTO perform(GeneticAlgorithmConfigurationDTO dto) {

        BinaryOperator<Double> bealeFunction = (x1, x2) ->
                Math.pow((1.5 - x1 + x1 * x2), 2) +
                        Math.pow((2.25 - x1 + x1 * Math.pow(x2, 2)), 2) +
                        Math.pow((2.625 - x1 + x1 * Math.pow(x2, 3)), 2);

        long calculationStartTimestamp = System.currentTimeMillis();

        List<Candidate> candidates = initializePopulation(dto.getPopulationAmount(), dto.getRangeBegin(), dto.getRangeEnd());


        List<FunctionResult> functionResults;

        List<Candidate> eliteCandidates;

        Map<Integer, Double> bestFunctionValues = new HashMap<>();
        Map<Integer, Double> avgFunctionValues = new HashMap<>();
        Map<Integer, Double> standardDeviations = new HashMap<>();


        for (int i = 0; i < dto.getEpochsAmount(); i++) {

            functionResults = evaluation(candidates, bealeFunction);

            avgFunctionValues.put(i + 1, functionResults.stream().mapToDouble(FunctionResult::getValue).average().getAsDouble());

            final double avgFunctionValue = avgFunctionValues.values().stream().mapToDouble(Double::doubleValue).average().getAsDouble();

            double sum = functionResults.stream().mapToDouble(FunctionResult::getValue).map(x -> Math.pow((x - avgFunctionValue), 2)).sum();
            standardDeviations.put(i + 1, Math.sqrt(sum / functionResults.size()));

            eliteCandidates = eliteStrategy(functionResults, dto.getEliteStrategyAmount(), Mode.MINIMIZATION);

            bestFunctionValues.put(i + 1, new FunctionResult(eliteCandidates.get(0), bealeFunction).getValue());

            functionResults = selection(dto.getSelectionMethod(), functionResults);

            candidates = crossover(dto.getCrossMethod(), functionResults.stream().map(FunctionResult::getCandidate).collect(Collectors.toList()), dto.getPopulationAmount(), dto.getCrossProbability());

            candidates = mutation(dto.getMutationMethod(), candidates, dto.getMutationProbability());

            candidates = inversion(candidates, dto.getInversionProbability());

            candidates.addAll(eliteCandidates);
        }

        long calculationEndTimestamp = System.currentTimeMillis();

        double calculationTime = (calculationEndTimestamp - calculationStartTimestamp) / 1000.0;

        saveToFile(bestFunctionValues, "best_function_values");
        saveToFile(avgFunctionValues, "avg_function_values");
        saveToFile(standardDeviations, "standard_deviations");

        return new ResultsDTO(calculationTime, bestFunctionValues, avgFunctionValues, standardDeviations); //TODO
    }

    private void saveToFile(Map<Integer, Double> map, String filename) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(formatter);

        File file = new File(String.format("results/%s_%s.csv", formattedDateTime, filename));

        try{
            file.createNewFile();
            PrintWriter pw = new PrintWriter(file);
            pw.println("generation,value");
            map.entrySet().stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertToCSV(Map.Entry<Integer, Double> integerDoubleEntry) {
        return integerDoubleEntry.getKey() + "," + integerDoubleEntry.getValue();
    }


    private List<Candidate> eliteStrategy(List<FunctionResult> functionResults, int eliteStrategyAmount, Mode mode) {
        // TODO: 30/10/2022 it should be always even number
        return switch (mode) {
            case MINIMIZATION -> functionResults.stream()
                    .sorted()
                    .map(FunctionResult::getCandidate)
                    .limit(eliteStrategyAmount)
                    .collect(Collectors.toList());

            case MAXIMIZATION -> functionResults.stream()
                    .sorted(Comparator.reverseOrder())
                    .map(FunctionResult::getCandidate)
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

    private List<FunctionResult> evaluation(List<Candidate> candidates, BinaryOperator<Double> function) {
        return candidates.stream()
                .map(candidate -> new FunctionResult(candidate, function)).toList();
    }

    private List<FunctionResult> selection(Selection selectionMethod, List<FunctionResult> functionResults) {

        // TODO: 30/10/2022 add roulette method

        return switch (selectionMethod) {
            case TOURNAMENT ->
                    selection.tournamentMethod(functionResults, /*TODO*/ 10, /*TODO*/ Tournament.SINGLE, /*TODO*/Mode.MINIMIZATION);
            case RANKING -> selection.rankingMethod(functionResults, /*TODO*/ Rank.MINIMUM_VALUE);
            default -> selection.bestElementsMethod(functionResults, /*TODO*/ 0.3f, /*TODO*/Mode.MINIMIZATION);

        };
    }

    private List<Candidate> crossover(Crossover crossMethod, List<Candidate> candidates, int populationAmount, int probability) {
        return switch (crossMethod) {
            case TWO_POINT -> Crossover.TWO_POINT.compute(candidates, populationAmount, probability);
            case THREE_POINT -> Crossover.THREE_POINT.compute(candidates, populationAmount, probability);
            case UNIFORM -> Crossover.UNIFORM.compute(candidates, populationAmount, probability);
            default -> Crossover.ONE_POINT.compute(candidates, populationAmount, probability);

        };
    }

    private List<Candidate> mutation(Mutation mutationMethod, List<Candidate> candidates, int probability) {
        return switch (mutationMethod) {
            case TWO_POINT -> Mutation.TWO_POINT.compute(candidates, probability);
            case EDGE -> Mutation.EDGE.compute(candidates, probability);
            default -> Mutation.ONE_POINT.compute(candidates, probability);
        };
    }

    private List<Candidate> inversion(List<Candidate> candidates, int probability) {
        return Mutation.INVERSE.compute(candidates, probability);
    }
}
