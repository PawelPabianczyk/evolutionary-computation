package pl.pk.evolutionarycomputation.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pk.evolutionarycomputation.dto.GeneticAlgorithmConfigurationDTO;
import pl.pk.evolutionarycomputation.dto.ResultsDTO;
import pl.pk.evolutionarycomputation.enums.*;
import pl.pk.evolutionarycomputation.model.Candidate;
import pl.pk.evolutionarycomputation.model.Chromosome;
import pl.pk.evolutionarycomputation.model.FunctionResult;
import pl.pk.evolutionarycomputation.util.crossover.ICrossover;
import pl.pk.evolutionarycomputation.util.generator.ChromosomeGenerator;
import pl.pk.evolutionarycomputation.util.mutation.IMutation;
import pl.pk.evolutionarycomputation.util.selection.ISelection;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service
public class GeneticServiceImpl implements GeneticService {

    @Autowired
    IMutation iMutation;

    @Autowired
    ICrossover iCrossover;

    @Autowired
    ISelection iSelection;

    private final ChromosomeGenerator chromosomeGenerator;
    private final ISelection selection;
    private final DateTimeFormatter formatter;

    private final String AVERAGE_CROSSOVER = "AVERAGE_CROSSOVER";
    private final String BLEND_CROSSOVER_ALPHA_BETA = "BLEND_CROSSOVER_ALPHA_BETA";
    private final String BLEND_CROSSOVER_ALPHA = "BLEND_CROSSOVER_ALPHA";
    private final String LINEAR_CROSSOVER = "LINEAR_CROSSOVER";
    private final String ARITHMETIC_CROSSOVER = "ARITHMETIC_CROSSOVER";


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

            eliteCandidates = eliteStrategy(functionResults, dto.getEliteStrategyAmount(), getMode(dto.isMaximization()));

            bestFunctionValues.put(i + 1, new FunctionResult(eliteCandidates.get(0), bealeFunction).getValue());

            functionResults = selection(dto.getSelectionMethod(), functionResults, getMode(dto.isMaximization()), dto.getTournament(), dto.getPercentageOfBestElements(), dto.getTournamentSize(), dto.getSpinNumber());

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

        return new ResultsDTO(calculationTime, bestFunctionValues, avgFunctionValues, standardDeviations);
    }
    @Override
    public ResultsDTO performV2(GeneticAlgorithmConfigurationDTO dto) {

        BinaryOperator<Double> bealeFunction = (x1, x2) ->
                Math.pow((1.5 - x1 + x1 * x2), 2) +
                        Math.pow((2.25 - x1 + x1 * Math.pow(x2, 2)), 2) +
                        Math.pow((2.625 - x1 + x1 * Math.pow(x2, 3)), 2);

        long calculationStartTimestamp = System.currentTimeMillis();

        List<Candidate> candidates = initializePopulationV2(dto.getPopulationAmount(), dto.getRangeBegin(), dto.getRangeEnd());


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

            eliteCandidates = eliteStrategy(functionResults, dto.getEliteStrategyAmount(), getMode(dto.isMaximization()));

            bestFunctionValues.put(i + 1, new FunctionResult(eliteCandidates.get(0), bealeFunction).getValue());

            functionResults = selection(dto.getSelectionMethod(), functionResults, getMode(dto.isMaximization()), dto.getTournament(), dto.getPercentageOfBestElements(), dto.getTournamentSize(), dto.getSpinNumber());

            candidates = crossoverV2(dto.getCrossMethodV2(), functionResults.stream().map(FunctionResult::getCandidate).collect(Collectors.toList()), getMode(dto.isMaximization()),bealeFunction,  dto.getPopulationAmount(), dto.getCrossProbability(), dto.getAlpha(), dto.getBeta());

            candidates = mutationV2(dto.getMutationMethodV2(), candidates, dto.getMutationProbability());

//          candidates = inversion(candidates, dto.getInversionProbability());

            candidates.addAll(eliteCandidates);
        }

        long calculationEndTimestamp = System.currentTimeMillis();

        double calculationTime = (calculationEndTimestamp - calculationStartTimestamp) / 1000.0;

        saveToFile(bestFunctionValues, "best_function_values");
        saveToFile(avgFunctionValues, "avg_function_values");
        saveToFile(standardDeviations, "standard_deviations");

        return new ResultsDTO(calculationTime, bestFunctionValues, avgFunctionValues, standardDeviations);
    }

    private static Mode getMode(boolean isMaximization) {
        return isMaximization ? Mode.MAXIMIZATION : Mode.MINIMIZATION;
    }

    private void saveToFile(Map<Integer, Double> map, String filename) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(formatter);

        File file = new File(String.format("results/%s_%s.csv", formattedDateTime, filename));

        try {
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
        eliteStrategyAmount = eliteStrategyAmount % 2 == 0 ? eliteStrategyAmount : eliteStrategyAmount + 1;
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

    private List<Candidate> initializePopulationV2(int populationAmount, int rangeBegin, int rangeEnd) {
        List<Chromosome> chromosomesA = chromosomeGenerator
                .generateChromosomes(populationAmount, rangeBegin, rangeEnd);
        List<Chromosome> chromosomesB = chromosomeGenerator
                .generateChromosomes(populationAmount, rangeBegin, rangeEnd);

        List<Candidate> candidates = new ArrayList<>();

        for (int i = 0; i < chromosomesA.size(); i++) {
            candidates.add(new Candidate(Arrays.asList(chromosomesA.get(i), chromosomesB.get(i))));
        }

        return candidates;
    }

    private List<FunctionResult> evaluation(List<Candidate> candidates, BinaryOperator<Double> function) {
        return candidates.stream()
                .map(candidate -> new FunctionResult(candidate, function)).collect(Collectors.toList());
    }

    private List<FunctionResult> selection(Selection selectionMethod, List<FunctionResult> functionResults, Mode mode,
                                           Tournament tournament, int percentageOfBestElements, int tournamentSize, int spinNumber) {

        return switch (selectionMethod) {
            case ROULETTE -> selection.rouletteMethod(functionResults, spinNumber, mode);
            case TOURNAMENT -> selection.tournamentMethod(functionResults, tournamentSize, tournament, mode);
            case RANKING ->
                    selection.rankingMethod(functionResults, mode == Mode.MINIMIZATION ? Rank.MINIMUM_VALUE : Rank.MAXIMUM_VALUE);
            default -> selection.bestElementsMethod(functionResults, percentageOfBestElements, mode);

        };
    }

    private List<Candidate> crossover(Crossover crossMethod, List<Candidate> candidates, int populationAmount, int probability) {
        return switch (crossMethod) {
            case TWO_POINT -> Crossover.TWO_POINT.compute(candidates, populationAmount, probability);
            case THREE_POINT -> Crossover.THREE_POINT.compute(candidates, populationAmount, probability);
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

    private List<Candidate> mutationV2(String mutationMethod, List<Candidate> candidates, int probability) {
        return switch (mutationMethod) {
            case "UNIFORM_MUTATION" -> iMutation.uniformMutation(candidates, probability);
            default -> iMutation.gaussMutation(candidates, probability);
        };
    }

    private List<Candidate> crossoverV2(String crossMethod, List<Candidate> candidates, Mode mode, BinaryOperator<Double> function, int populationAmount, int probability, double alpha, double beta) {
        List<Candidate> candidatesList = new ArrayList<>();
        return switch (crossMethod) {
            case "ARITHMETIC_CROSSOVER" -> iCrossover.arithmeticCrossover(candidates, populationAmount, probability);
            case "LINEAR_CROSSOVER" -> iCrossover.linearCrossover(candidates, populationAmount, probability, function, mode);
            case "BLEND_CROSSOVER_ALPHA" -> iCrossover.blendCrossoverAlpha(candidates, populationAmount, probability, alpha);
            case "AVERAGE_CROSSOVER" -> iCrossover.averageCrossover(candidates, populationAmount, probability);
            case "BLEND_CROSSOVER_ALPHA_BETA" -> iCrossover.blendCrossoverAlphaAndBeta(candidates, populationAmount, probability, alpha, beta);
            default -> candidatesList;
        };
    }

    private List<Candidate> inversion(List<Candidate> candidates, int probability) {
        return Mutation.INVERSE.compute(candidates, probability);
    }
}
