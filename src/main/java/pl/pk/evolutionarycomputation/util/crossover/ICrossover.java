package pl.pk.evolutionarycomputation.util.crossover;

import pl.pk.evolutionarycomputation.enums.Mode;
import pl.pk.evolutionarycomputation.model.Candidate;

import java.util.List;
import java.util.function.BinaryOperator;

public interface ICrossover {

    List<Candidate> arithmeticCrossover(List<Candidate> candidates, int populationAmount, int probability);

    List<Candidate> linearCrossover(List<Candidate> candidates, int populationAmount, int probability, BinaryOperator<Double> function, Mode mode);

    List<Candidate> blendCrossoverAlpha(List<Candidate> candidates, int populationAmount, int probability, double alpha);

    List<Candidate> blendCrossoverAlphaAndBeta(List<Candidate> candidates, int populationAmount, int probability, double alpha, double beta);

    List<Candidate> averageCrossover(List<Candidate> candidates, int populationAmount, int probability);
}
