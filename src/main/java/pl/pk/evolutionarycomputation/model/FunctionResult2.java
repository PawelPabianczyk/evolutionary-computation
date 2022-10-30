package pl.pk.evolutionarycomputation.model;

import lombok.*;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FunctionResult2 implements Comparable<FunctionResult2> {
    private Candidate candidate;
    private Double value;
    private BinaryOperator<Double> function;

    public FunctionResult2(Candidate candidate, BinaryOperator<Double> function) {
        this.candidate = candidate;
        this.value = function.apply(candidate.getChromosomes().get(0).getValue(), candidate.getChromosomes().get(1).getValue());
        this.function = function;
    }

    @Override
    public int compareTo(FunctionResult2 o) {
        return this.value.compareTo(o.getValue());
    }


}
