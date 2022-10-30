package pl.pk.evolutionarycomputation.model;

import lombok.*;

import java.util.function.BinaryOperator;

@Getter
@ToString
public class FunctionResult implements Comparable<FunctionResult> {
    private final Candidate candidate;
    private Double value;
    private final BinaryOperator<Double> function;

    public FunctionResult(Candidate candidate, BinaryOperator<Double> function) {
        this.candidate = candidate;
        this.value = function.apply(candidate.getChromosomes().get(0).getValue(), candidate.getChromosomes().get(1).getValue());
        this.function = function;
    }

    @Override
    public int compareTo(FunctionResult o) {
        return this.value.compareTo(o.getValue());
    }

    // TODO: 30/10/2022 add equals, hashcode


    public void setValue(Double value) {
        this.value = value;
    }
}
