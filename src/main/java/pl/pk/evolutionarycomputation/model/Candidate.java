package pl.pk.evolutionarycomputation.model;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class Candidate {

    private final List<Chromosome> chromosomes;

    public Candidate(List<Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
    }
}
