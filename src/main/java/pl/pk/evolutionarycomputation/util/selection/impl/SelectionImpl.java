package pl.pk.evolutionarycomputation.util.selection.impl;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import pl.pk.evolutionarycomputation.enums.Mode;
import pl.pk.evolutionarycomputation.enums.Rank;
import pl.pk.evolutionarycomputation.enums.Tournament;
import pl.pk.evolutionarycomputation.model.FunctionResult;
import pl.pk.evolutionarycomputation.model.FunctionResult2;
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
    public List<FunctionResult2> bestElementsMethod2(List<FunctionResult2> functionResults, float percentageOfBestElements, Mode mode) {
        int numberOfChromosomes = (int) Math.ceil(functionResults.size() * percentageOfBestElements);

        return switch (mode) {
            case MINIMIZATION -> sortAsc2(functionResults).stream()
                    .limit(numberOfChromosomes)
                    .collect(Collectors.toList());
            case MAXIMIZATION -> sortDesc2(functionResults).stream()
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
    public Map<FunctionResult2, Double> rouletteMethod2(List<FunctionResult2> functionResults, Mode mode) {
        double sum;

        if (mode.equals(Mode.MINIMIZATION)) {
            functionResults.forEach(result -> result.setValue(1.0 / result.getValue()));
        }

        sum = getSum2(functionResults);

        Map<FunctionResult2, Double> probabilityMap = getProbabilityMap2(functionResults, sum);

        return getDistributionMap2(probabilityMap);
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
    public List<FunctionResult2> tournamentMethod2(List<FunctionResult2> functionResults, int tournamentSize, Tournament tournament, Mode mode) {
        return switch (tournament) {
            case SINGLE -> singleTournament2(functionResults, tournamentSize, mode);
            case DOUBLE -> doubleTournament2(functionResults, tournamentSize, mode);
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

    @Override
    public List<FunctionResult2> rankingMethod2(List<FunctionResult2> functionResults, Rank rank) {
        return null;
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

    private List<FunctionResult2> sortAsc2(List<FunctionResult2> functionResults) {
        return functionResults.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    private List<FunctionResult> sortDesc(List<FunctionResult> functionResults) {
        return functionResults.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private List<FunctionResult2> sortDesc2(List<FunctionResult2> functionResults) {
        return functionResults.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private List<FunctionResult> singleTournament(List<FunctionResult> functionResults,
                                                  int tournamentSize, Mode mode) {
        return getListOfMaxValuesFromShuffledSubLists(functionResults, tournamentSize, mode);
    }

    private List<FunctionResult2> singleTournament2(List<FunctionResult2> functionResults,
                                                  int tournamentSize, Mode mode) {
        return getListOfMaxValuesFromShuffledSubLists2(functionResults, tournamentSize, mode);
    }

    private List<FunctionResult> doubleTournament(List<FunctionResult> functionResults,
                                                  int tournamentSize, Mode mode) {

        functionResults = getListOfMaxValuesFromShuffledSubLists(functionResults, tournamentSize, mode);
        functionResults = getListOfMaxValuesFromShuffledSubLists(functionResults, tournamentSize, mode);

        return functionResults;
    }

    private List<FunctionResult2> doubleTournament2(List<FunctionResult2> functionResults,
                                                  int tournamentSize, Mode mode) {

        functionResults = getListOfMaxValuesFromShuffledSubLists2(functionResults, tournamentSize, mode);
        functionResults = getListOfMaxValuesFromShuffledSubLists2(functionResults, tournamentSize, mode);

        return functionResults;
    }

    private List<FunctionResult> getListOfMaxValuesFromShuffledSubLists(List<FunctionResult> functionResults,
                                                                        int tournamentSize, Mode mode) {
        Collections.shuffle(functionResults);

        return Lists.partition(functionResults, tournamentSize).stream()
                .map(subList -> getFirstFunctionResultByMode(subList, mode))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private List<FunctionResult2> getListOfMaxValuesFromShuffledSubLists2(List<FunctionResult2> functionResults,
                                                                        int tournamentSize, Mode mode) {
        Collections.shuffle(functionResults);

        return Lists.partition(functionResults, tournamentSize).stream()
                .map(subList -> getFirstFunctionResultByMode2(subList, mode))
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

    private FunctionResult2 getFirstFunctionResultByMode2(List<FunctionResult2> subList, Mode mode) {
        return switch (mode) {
            case MINIMIZATION -> subList.stream()
                    .min(FunctionResult2::compareTo)
                    .orElse(new FunctionResult2());

            case MAXIMIZATION -> subList.stream()
                    .max(FunctionResult2::compareTo)
                    .orElse(new FunctionResult2());
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

    private Map<FunctionResult2, Double> getDistributionMap2(Map<FunctionResult2, Double> probabilityMap) {
        double distribution = 0.0;
        Map<FunctionResult2, Double> map = new LinkedHashMap<>();

        for (FunctionResult2 functionResult : probabilityMap.keySet()) {
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

    private double getSum2(List<FunctionResult2> functionResults) {
        return functionResults.stream()
                .map(FunctionResult2::getValue)
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

    private Map<FunctionResult2, Double> getProbabilityMap2(List<FunctionResult2> functionResults, double sum) {
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
