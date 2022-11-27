package pl.pk.evolutionarycomputation.util.mutation;

import pl.pk.evolutionarycomputation.model.Candidate;

import java.util.List;

public interface IMutation {

    List<Candidate> uniformMutation(List<Candidate> candidates, int probability);

    List<Candidate> GaussMutation(List<Candidate> candidates, int probability);
}
