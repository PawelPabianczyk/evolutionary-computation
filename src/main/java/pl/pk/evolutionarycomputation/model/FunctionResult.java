package pl.pk.evolutionarycomputation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Function;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FunctionResult implements Comparable<FunctionResult> {
    private Chromosome chromosome;
    private Double value;
    private Function<Double, Double> function;

    public FunctionResult(Chromosome chromosome, Function<Double, Double> function) {
        this.chromosome = chromosome;
        this.value = function.apply(chromosome.getValue());
        this.function = function;
    }

    @Override
    public int compareTo(FunctionResult o) {
        return this.value.compareTo(o.getValue());
    }
}
