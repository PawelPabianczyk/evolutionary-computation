package pl.pk.evolutionarycomputation.enums;

import pl.pk.evolutionarycomputation.model.Chromosome;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Mutation {
    ONE_POINT {
        @Override
        protected byte[] mutation(byte[] bytes) {
            int position = ThreadLocalRandom.current().nextInt(bytes.length);
            bytes[position] = negateBit(bytes[position]);
            return bytes;
        }
    },
    TWO_POINT {
        @Override
        protected byte[] mutation(byte[] bytes) {
            int firstPosition = ThreadLocalRandom.current().nextInt(bytes.length);
            bytes[firstPosition] = negateBit(bytes[firstPosition]);
            int secondPosition = ThreadLocalRandom.current().nextInt(bytes.length);
            while (secondPosition == firstPosition)
                secondPosition = ThreadLocalRandom.current().nextInt(bytes.length);
            bytes[secondPosition] = negateBit(bytes[secondPosition]);
            return bytes;
        }
    },
    EDGE {
        @Override
        protected byte[] mutation(byte[] bytes) {
            if (ThreadLocalRandom.current().nextInt(2) == 0)
                bytes[0] = negateBit(bytes[0]);
            else
                bytes[bytes.length - 1] = negateBit(bytes[bytes.length - 1]);

            return bytes;
        }
    };

    private static byte negateBit(byte bit) {
        if (bit == 0)
            return 1;
        return 0;
    }

    public List<Chromosome> compute(List<Chromosome> chromosomes, int probability) {
        for (int i = 0; i < chromosomes.size(); i++) {

            if (ThreadLocalRandom.current().nextInt(1, 101) <= probability) {
                byte[] bytes = chromosomes.get(i).getBytesRepresentation();
                int minValue = chromosomes.get(i).getMinimumValue();
                int maxValue = chromosomes.get(i).getMaximumValue();
                chromosomes.set(i, new Chromosome(mutation(bytes), minValue, maxValue));
            }
        }

        return chromosomes;
    }

    protected abstract byte[] mutation(byte[] bytes);
}
