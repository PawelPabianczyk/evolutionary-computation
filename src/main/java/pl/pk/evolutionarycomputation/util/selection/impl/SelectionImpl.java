package pl.pk.evolutionarycomputation.util.selection.impl;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import pl.pk.evolutionarycomputation.enums.Mode;
import pl.pk.evolutionarycomputation.enums.Rank;
import pl.pk.evolutionarycomputation.enums.Tournament;
import pl.pk.evolutionarycomputation.model.FunctionResult;
import pl.pk.evolutionarycomputation.util.selection.ISelection;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SelectionImpl implements ISelection {

    @Override
    public List<FunctionResult> bestElementsMethod(List<FunctionResult> functionResults,
                                                   float percentageOfBestElements,
                                                   Mode mode) {
        int numberOfChromosomes = (int) Math.ceil(functionResults.size() * percentageOfBestElements);

        return switch (mode) {
            case MINIMIZATION -> sortAsc(functionResults).stream()
                    .limit(numberOfChromosomes)
                    .collect(Collectors.toList());
            case MAXIMIZATION -> sortDesc(functionResults).stream()
                    .limit(numberOfChromosomes)
                    .collect(Collectors.toList());
        };
    }

    @Override
    public Map<FunctionResult, Double> rouletteMethod(List<FunctionResult> functionResults, Mode mode) {
        double sum;

        if (mode.equals(Mode.MINIMIZATION)) {
            functionResults.forEach(result -> result.setValue(1.0 / result.getValue()));
        }

        sum = getSum(functionResults);

        Map<FunctionResult, Double> probabilityMap = getProbabilityMap(functionResults, sum);

        return getDistributionMap(probabilityMap);
    }

    @Override
    public List<FunctionResult> tournamentMethod(List<FunctionResult> functionResults,
                                                 int tournamentSize,
                                                 Tournament tournament, Mode mode) {
        return switch (tournament) {
            case SINGLE -> singleTournament(functionResults, tournamentSize, mode);
            case DOUBLE -> doubleTournament(functionResults, tournamentSize, mode);
        };
    }

    @Override
    public List<FunctionResult> rankingMethod(List<FunctionResult> functionResults, Rank rank) {
        List<FunctionResult> resultList = new LinkedList<>();

        List<FunctionResult> sortedList = switch (rank) {
            case MINIMUM_VALUE -> sortAsc(functionResults);
            case MAXIMUM_VALUE -> sortDesc(functionResults);
        };

        for (FunctionResult result : sortedList) {
            addElementByRank(sortedList, resultList, result);
        }

        return resultList;
    }

    private void addElementByRank(List<FunctionResult> sortedList,
                                  List<FunctionResult> resultList,
                                  FunctionResult result) {
        IntStream.range(0, sortedList.indexOf(result) + 1)
                .mapToObj(i -> result)
                .forEachOrdered(resultList::add);
    }

    private List<FunctionResult> sortAsc(List<FunctionResult> functionResults) {
        return functionResults.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    private List<FunctionResult> sortDesc(List<FunctionResult> functionResults) {
        return functionResults.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private List<FunctionResult> singleTournament(List<FunctionResult> functionResults,
                                                  int tournamentSize, Mode mode) {
        return getListOfMaxValuesFromShuffledSubLists(functionResults, tournamentSize, mode);
    }

    private List<FunctionResult> doubleTournament(List<FunctionResult> functionResults,
                                                  int tournamentSize, Mode mode) {

        functionResults = getListOfMaxValuesFromShuffledSubLists(functionResults, tournamentSize, mode);
        functionResults = getListOfMaxValuesFromShuffledSubLists(functionResults, tournamentSize, mode);

        return functionResults;
    }

    private List<FunctionResult> getListOfMaxValuesFromShuffledSubLists(List<FunctionResult> functionResults,
                                                                        int tournamentSize, Mode mode) {
        Collections.shuffle(functionResults);

        return Lists.partition(functionResults, tournamentSize).stream()
                .map(subList -> getFirstFunctionResultByMode(subList, mode))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private FunctionResult getFirstFunctionResultByMode(List<FunctionResult> subList, Mode mode) {
        return switch (mode) {
            case MINIMIZATION -> subList.stream()
                    .min(FunctionResult::compareTo)
                    .orElse(new FunctionResult());

            case MAXIMIZATION -> subList.stream()
                    .max(FunctionResult::compareTo)
                    .orElse(new FunctionResult());
        };
    }

    private Map<FunctionResult, Double> getDistributionMap(Map<FunctionResult, Double> probabilityMap) {
        double distribution = 0.0;
        Map<FunctionResult, Double> map = new LinkedHashMap<>();

        for (FunctionResult functionResult : probabilityMap.keySet()) {
            distribution += probabilityMap.get(functionResult);
            map.put(functionResult, distribution);
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
                                functionResult -> functionResult,
                                functionResult -> (functionResult.getValue() / sum),
                                (a, b) -> b,
                                LinkedHashMap::new
                        )
                );
    }
}
