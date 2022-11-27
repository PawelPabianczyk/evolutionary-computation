package pl.pk.evolutionarycomputation.util.crossover.impl;

import org.springframework.stereotype.Component;
import pl.pk.evolutionarycomputation.enums.Mode;
import pl.pk.evolutionarycomputation.model.Candidate;
import pl.pk.evolutionarycomputation.model.Chromosome;
import pl.pk.evolutionarycomputation.util.crossover.ICrossover;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

@Component
public class CrossoverImpl implements ICrossover {
    @Override
    public List<Candidate> arithmeticCrossover(List<Candidate> candidates, int populationAmount, int probability) {
        List<Candidate> newCandidates = new ArrayList<>();
        while (newCandidates.size() < populationAmount) {
            Collections.shuffle(candidates);
            int minimumValue = candidates.get(0).getChromosomes().get(0).getMinimumValue();
            int maximumValue = candidates.get(0).getChromosomes().get(0).getMaximumValue();
            for (int i = 1; i < candidates.size(); i += 2) {
                if (ThreadLocalRandom.current().nextInt(1, 101) <= probability) {
                    List<Chromosome> can1Chromosomes = candidates.get(i - 1).getChromosomes();
                    double x1 = can1Chromosomes.get(0).getValue();
                    double y1 = can1Chromosomes.get(1).getValue();
                    List<Chromosome> can2Chromosomes = candidates.get(i).getChromosomes();
                    double x2 = can2Chromosomes.get(0).getValue();
                    double y2 = can2Chromosomes.get(1).getValue();

                    double k = ThreadLocalRandom.current().nextDouble(1);

                    double newX1 = k * x1 + (1 - k) * x2;
                    double newY1 = k * y1 + (1 - k) * y2;
                    if (isRangeInCorrect(newX1, newY1, minimumValue, maximumValue)){
                        break;
                    }

                    double newX2 = (1 - k) * x1 + k * x2;
                    double newY2 = (1 - k) * y1 + k * y2;
                    if (isRangeInCorrect(newX2, newY2, minimumValue, maximumValue)){
                        break;
                    }

                    newCandidates.add(createNewCandidate(newX1, newY1, minimumValue, maximumValue));
                    newCandidates.add(createNewCandidate(newX2, newY2, minimumValue, maximumValue));
                }
            }
        }
        return newCandidates.stream().limit(populationAmount).collect(Collectors.toList());
    }

    private Candidate createNewCandidate(double x, double y, int minimumValue, int maximumValue) {
        return new Candidate(asList(new Chromosome(x, minimumValue, maximumValue), new Chromosome(y, minimumValue, maximumValue)));
    }

    @Override
    public List<Candidate> linearCrossover(List<Candidate> candidates, int populationAmount, int probability, BinaryOperator<Double> function, Mode mode) {
        List<Candidate> newCandidates = new ArrayList<>();
        while (newCandidates.size() < populationAmount) {
            Collections.shuffle(candidates);
            int minimumValue = candidates.get(0).getChromosomes().get(0).getMinimumValue();
            int maximumValue = candidates.get(0).getChromosomes().get(0).getMaximumValue();
            for (int i = 1; i < candidates.size(); i += 2) {
                if (ThreadLocalRandom.current().nextInt(1, 101) <= probability) {
                    List<Chromosome> can1Chromosomes = candidates.get(i - 1).getChromosomes();
                    double x1 = can1Chromosomes.get(0).getValue();
                    double y1 = can1Chromosomes.get(1).getValue();
                    List<Chromosome> can2Chromosomes = candidates.get(i).getChromosomes();
                    double x2 = can2Chromosomes.get(0).getValue();
                    double y2 = can2Chromosomes.get(1).getValue();


                    double newX1 = x1 / 2 + x2 / 2;
                    double newY1 = y1 / 2 + y2 / 2;

                    if (isRangeInCorrect(newX1, newY1, minimumValue, maximumValue)){
                        break;
                    }

                    Candidate z = createNewCandidate(newX1, newY1, minimumValue, maximumValue);

                    double newX2 = 3 * x1 / 2 - x2 / 2;
                    double newY2 = 3 * y1 / 2 + y2 / 2;

                    if (isRangeInCorrect(newX2, newY2, minimumValue, maximumValue)){
                        break;

                    }

                    Candidate v = createNewCandidate(newX2, newY2, minimumValue, maximumValue);

                    double newX3 = 3 * x2 / 2 - x1 / 2;
                    double newY3 = 3 * y2 / 2 + y1 / 2;

                    if (isRangeInCorrect(newX3, newY3, minimumValue, maximumValue)){
                        break;
                    }

                    Candidate w = createNewCandidate(newX3, newY3, minimumValue, maximumValue);

                    newCandidates.addAll(getBestTwo(asList(z,v,w),function,mode));
                }
            }
        }
        return newCandidates.stream().limit(populationAmount).collect(Collectors.toList());
    }

    private boolean isRangeInCorrect(double x, double y, int minimumValue, int maximumValue) {
        return x<minimumValue || x>maximumValue || y<minimumValue || y>maximumValue;
    }

    private List<Candidate> getBestTwo(List<Candidate> candidates, BinaryOperator<Double> function, Mode mode) {
        return switch (mode) {
            case MINIMIZATION -> getSortedCandidateStream(candidates, function)
                    .limit(2)
                    .collect(Collectors.toList());
            case MAXIMIZATION -> getSortedCandidateStream(candidates, function)
                    .skip(1)
                    .collect(Collectors.toList());
        };
    }

    private static Stream<Candidate> getSortedCandidateStream(List<Candidate> candidates, BinaryOperator<Double> function) {
        return candidates.stream().sorted(Comparator.comparingDouble(c ->
                function.apply(c.getChromosomes().get(0).getValue(), c.getChromosomes().get(1).getValue())));
    }

    @Override
    public List<Candidate> blendCrossoverAlpha(List<Candidate> candidates, int populationAmount, int probability) {
        return null;
    }

    @Override
    public List<Candidate> blendCrossoverAlphaAndBeta(List<Candidate> candidates, int populationAmount, int probability) {
        return null;
    }

    @Override
    public List<Candidate> averageCrossover(List<Candidate> candidates, int populationAmount, int probability) {
        return null;
    }
}
