package pl.pk.evolutionarycomputation.util.generator;

import org.springframework.stereotype.Component;
import pl.pk.evolutionarycomputation.model.Chromosome;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

@Component
public class ChromosomeGenerator {

    public List<Chromosome> generateListOfChromosomes(int numberOfElements, int minimumValue, int maximumValue) {
        return DoubleStream.generate(() -> {
                    return ThreadLocalRandom
                                .current()
                                .nextDouble() * (maximumValue - minimumValue) + minimumValue;
                })
                .distinct()
                .limit(numberOfElements)
                .mapToObj(Chromosome::new)
                .toList();
    }
}
