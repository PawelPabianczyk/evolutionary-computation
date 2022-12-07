package pl.pk.evolutionarycomputation.enums;

import org.springframework.beans.factory.annotation.Autowired;
import pl.pk.evolutionarycomputation.model.Candidate;
import pl.pk.evolutionarycomputation.model.Chromosome;
import pl.pk.evolutionarycomputation.util.mutation.IMutation;
import pl.pk.evolutionarycomputation.util.selection.ISelection;

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
    },
    GAUSS_MUTATION {
        @Override
        protected byte[] mutation(byte[] bytes) {

            return bytes;
        }
    },
    UNIFORM_MUTATION {
        @Override
        protected byte[] mutation(byte[] bytes) {

            return bytes;
        }
    },
    INVERSE {
        @Override
        protected byte[] mutation(byte[] bytes) {
            int firstPosition = ThreadLocalRandom.current().nextInt(bytes.length);
            bytes[firstPosition] = negateBit(bytes[firstPosition]);
            int secondPosition = ThreadLocalRandom.current().nextInt(bytes.length);
            while (secondPosition == firstPosition)
                secondPosition = ThreadLocalRandom.current().nextInt(bytes.length);

            int i = Math.min(firstPosition, secondPosition);
            int j = Math.max(firstPosition, secondPosition);

            while(i <j){
                byte buffer = bytes[i];
                bytes[i] = bytes[j];
                bytes[j] = buffer;
                i++;
                j--;
            }
            return bytes;
        }
    };

    private static byte negateBit(byte bit) {
        if (bit == 0)
            return 1;
        return 0;
    }

    public List<Candidate> compute(List<Candidate> candidates, int probability) {
        for (int i = 0; i < candidates.size(); i++) {

            List<Chromosome> canChromosomes = candidates.get(i).getChromosomes();

            for (int j = 0; j < canChromosomes.size(); j++) {
                if (ThreadLocalRandom.current().nextInt(1, 101) <= probability) {
                    byte[] bytes = canChromosomes.get(j).getBinaryRepresentation();
                    int minValue = canChromosomes.get(j).getMinimumValue();
                    int maxValue = canChromosomes.get(j).getMaximumValue();
                    canChromosomes.set(j, new Chromosome(mutation(bytes), minValue, maxValue));
                }
            }

            candidates.set(i, new Candidate(canChromosomes));

        }

        return candidates;
    }

    public List<Candidate> computeV2(List<Candidate> candidates, int probability) {
        for (int i = 0; i < candidates.size(); i++) {

            List<Chromosome> canChromosomes = candidates.get(i).getChromosomes();

            for (int j = 0; j < canChromosomes.size(); j++) {
                if (ThreadLocalRandom.current().nextInt(1, 101) <= probability) {
                    byte[] bytes = canChromosomes.get(j).getBinaryRepresentation();
                    int minValue = canChromosomes.get(j).getMinimumValue();
                    int maxValue = canChromosomes.get(j).getMaximumValue();
                    canChromosomes.set(j, new Chromosome(mutation(bytes), minValue, maxValue));
                }
            }

            candidates.set(i, new Candidate(canChromosomes));

        }

        return candidates;
    }


    protected abstract byte[] mutation(byte[] bytes);
}
