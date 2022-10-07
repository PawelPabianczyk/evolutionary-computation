package pl.pk.evolutionarycomputation.util.selection.impl;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import pl.pk.evolutionarycomputation.enums.Mode;
import pl.pk.evolutionarycomputation.enums.Tournament;
import pl.pk.evolutionarycomputation.model.FunctionResult;
import pl.pk.evolutionarycomputation.util.selection.ISelection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SelectionImpl implements ISelection {

    @Override
    public List<FunctionResult> bestElementsMethod(List<FunctionResult> functionResults,
                                                   float percentageOfBestElements) {

        int numberOfChromosomes = (int) Math.ceil(functionResults.size() * percentageOfBestElements);

        return functionResults.stream()
                .sorted()
                .limit(numberOfChromosomes)
                .toList();
    }

    @Override
    public Map<FunctionResult, Double> rouletteMethod(List<FunctionResult> functionResults, Mode mode) {
        double sum;

        if (mode.equals(Mode.MINIMIZATION)) {
            functionResults.forEach(result -> result.setValue(1.0 / result.getValue())); //TODO
        }

        sum = getSum(functionResults);

        Map<FunctionResult, Double> probabilityMap = getProbabilityMap(functionResults, sum);

        return getDistributionMap(probabilityMap);
    }

    @Override
    public List<FunctionResult> tournamentMethod(List<FunctionResult> functionResults,
                                                 int tournamentSize,
                                                 Tournament tournament) {

        return switch (tournament) {
            case SINGLE -> singleTournament(functionResults, tournamentSize);
            case DOUBLE -> doubleTournament(functionResults, tournamentSize);
            case RANKING -> rankingTournament(functionResults);
        };
    }

    private List<FunctionResult> singleTournament(List<FunctionResult> functionResults, int tournamentSize) {
        return getListOfMaxValues(functionResults, tournamentSize);
    }

    private List<FunctionResult> doubleTournament(List<FunctionResult> functionResults, int tournamentSize) {
        for (int i = 0; i < 2; i++) {
            functionResults = getListOfMaxValues(functionResults, tournamentSize);
        }

        return functionResults;
    }

    private List<FunctionResult> rankingTournament(List<FunctionResult> functionResults) {
        return functionResults.stream()
                .sorted()
                .toList(); //TODO
    }

    private List<FunctionResult> getListOfMaxValues(List<FunctionResult> functionResults, int tournamentSize) {
        return Lists.partition(functionResults, tournamentSize).stream()
                .map(subList -> subList.stream().min(FunctionResult::compareTo).orElse(new FunctionResult())) //TODO
                .toList();
    }

    private Map<FunctionResult, Double> getDistributionMap(Map<FunctionResult, Double> probabilityMap) {
        Map<FunctionResult, Double> map = new HashMap<>();
        double distribution = 0.0;

        for (FunctionResult functionResult : probabilityMap.keySet()) {
            map.put(functionResult, distribution);
            distribution += functionResult.getValue(); //TODO
        }

        return map;
    }

    private double getSum(List<FunctionResult> functionResults) {
        return functionResults.stream()
                .map(FunctionResult::getValue)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private Map<FunctionResult, Double> getProbabilityMap(List<FunctionResult> functionResults, double sum) {
        return functionResults.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        result -> result.getValue() / sum //TODO
                ));
    }
}
