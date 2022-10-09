package pl.pk.evolutionarycomputation.util.generator;

import org.springframework.stereotype.Component;
import pl.pk.evolutionarycomputation.model.Chromosome;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

import static pl.pk.evolutionarycomputation.binary.BinaryCalculator.getLengthOfChromosome;

@Component
public class ChromosomeGenerator {

    public List<Chromosome> generateListOfChromosomes(int numberOfElements, int minimumValue, int maximumValue) {
        return DoubleStream.generate(() -> ThreadLocalRandom
                    .current()
                    .nextDouble() * (maximumValue - minimumValue) + minimumValue)
                .distinct()
                .limit(numberOfElements)
                .mapToObj(Chromosome::new)
                .toList();
    }

    public List<Chromosome> generateChromosomesBinary(int numberOfElements, int minimumValue, int maximumValue) {
        List<Chromosome> chromosomes= new LinkedList<>();
        int lengthOfChromosome = getLengthOfChromosome(minimumValue, maximumValue);

        for (int i = 0; i < numberOfElements; i++) {
            byte[] bytesRepresentation = new byte[lengthOfChromosome];
            for (int j = 0; j < lengthOfChromosome; j++) {
                bytesRepresentation[j] = (byte) ThreadLocalRandom.current().nextInt(2);
            }
            chromosomes.add(new Chromosome(bytesRepresentation, minimumValue, maximumValue));
        }
        return chromosomes;
    }
}
