package pl.pk.evolutionarycomputation.util.selection.impl;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import pl.pk.evolutionarycomputation.enums.Mode;
import pl.pk.evolutionarycomputation.enums.Rank;
import pl.pk.evolutionarycomputation.enums.Tournament;
import pl.pk.evolutionarycomputation.model.FunctionResult;
import pl.pk.evolutionarycomputation.util.selection.ISelection;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SelectionImpl implements ISelection {


    @Override
    public List<FunctionResult> bestElementsMethod(List<FunctionResult> functionResults, int percentageOfBestElements, Mode mode) {
        int numberOfChromosomes = (int) Math.ceil(functionResults.size() * (percentageOfBestElements/100));

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
    public List<FunctionResult> rouletteMethod(List<FunctionResult> functionResults,int spinNumber, Mode mode) {
        double sum;

        if (mode.equals(Mode.MINIMIZATION)) {
            functionResults.forEach(result -> result.setValue(1.0 / result.getValue()));
        }

        sum = getSum(functionResults);

        Map<FunctionResult, Double> probabilityMap = getProbabilityMap(functionResults, sum);

        Map<Double, FunctionResult> distributionMap = getDistributionMap(probabilityMap);

        List<FunctionResult> selected = new ArrayList<>();

        for (int i = 0; i < spinNumber; i++) {
            double probability = ThreadLocalRandom.current().nextDouble(1.0);

            for (Double distribution :
                    distributionMap.keySet()) {
                if(probability < distribution){
                    selected.add(distributionMap.get(distribution));
                    distributionMap.remove(distribution);
                    break;
                }
            }
        }

        return selected;
    }


    @Override
    public List<FunctionResult> tournamentMethod(List<FunctionResult> functionResults, int tournamentSize, Tournament tournament, Mode mode) {
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
                .collect(Collectors.toList());
    }


    private FunctionResult getFirstFunctionResultByMode(List<FunctionResult> subList, Mode mode) {
        return switch (mode) {
            case MINIMIZATION -> subList.stream()
                    .min(FunctionResult::compareTo)
                    .get();

            case MAXIMIZATION -> subList.stream()
                    .max(FunctionResult::compareTo)
                    .get();
        };
    }


    private Map<Double, FunctionResult> getDistributionMap(Map<FunctionResult, Double> probabilityMap) {
        double distribution = 0.0;

        SortedMap<Double, FunctionResult> distributionMap
                = new TreeMap<>();

        for (FunctionResult functionResult : probabilityMap.keySet()) {
            distribution += probabilityMap.get(functionResult);
            distributionMap.put(distribution, functionResult);
        }

        return distributionMap;
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
