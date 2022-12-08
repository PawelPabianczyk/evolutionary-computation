package pl.pk.evolutionarycomputation.util.mutation.impl;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;
import pl.pk.evolutionarycomputation.model.Candidate;
import pl.pk.evolutionarycomputation.model.Chromosome;
import pl.pk.evolutionarycomputation.util.Commons;
import pl.pk.evolutionarycomputation.util.mutation.IMutation;

import java.util.List;

@Component
public class MutationImpl implements IMutation {

  private final Random random;

  public MutationImpl() {
    this.random = new Random();
  }

  @Override
  public List<Candidate> uniformMutation(List<Candidate> candidates, int probability) {
    for (int i = 0; i < candidates.size(); i++) {

      List<Chromosome> canChromosomes = candidates.get(i).getChromosomes();

      for (int j = 0; j < canChromosomes.size(); j++) {
        if (ThreadLocalRandom.current().nextInt(1, 101) <= probability) {
          int minValue = canChromosomes.get(j).getMinimumValue();
          int maxValue = canChromosomes.get(j).getMaximumValue();
          canChromosomes.set(j,
              new Chromosome(ThreadLocalRandom.current().nextDouble(minValue, maxValue), minValue,
                  maxValue));
        }
      }

      candidates.set(i, new Candidate(canChromosomes));

    }

    return candidates;
  }

  @Override
  public List<Candidate> gaussMutation(List<Candidate> candidates, int probability) {
    for (int i = 0; i < candidates.size(); i++) {

      List<Chromosome> canChromosomes = candidates.get(i).getChromosomes();

      for (int j = 0; j < canChromosomes.size(); j++) {
        if (ThreadLocalRandom.current().nextInt(1, 101) <= probability) {
          int minValue = canChromosomes.get(j).getMinimumValue();
          int maxValue = canChromosomes.get(j).getMaximumValue();

          double chromosome;
          do {
            chromosome = canChromosomes.get(j).getValue() + this.random.nextGaussian();

          } while (Commons.isRangeIncorrect(chromosome, minValue, maxValue));

          canChromosomes.set(j,
              new Chromosome(chromosome, minValue,
                  maxValue));
        }
      }
      candidates.set(i, new Candidate(canChromosomes));
    }

    return candidates;
  }
}
