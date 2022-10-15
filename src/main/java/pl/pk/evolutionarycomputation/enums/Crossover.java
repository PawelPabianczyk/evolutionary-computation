package pl.pk.evolutionarycomputation.enums;

import pl.pk.evolutionarycomputation.model.Chromosome;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Crossover {
    ONE_POINT {
        @Override
        protected void crossover(byte[] a, byte[] b) {
            int point = ThreadLocalRandom.current().nextInt(1, a.length - 1);

            swap(a, b, 0, point);
        }
    },
    TWO_POINT {
        @Override
        protected void crossover(byte[] a, byte[] b) {
            int firstPoint = ThreadLocalRandom.current().nextInt(1, a.length / 2);
            int secondPoint = ThreadLocalRandom.current().nextInt(firstPoint, a.length - 1);

            swap(a, b, firstPoint, secondPoint);
        }
    },
    THREE_POINT {
        @Override
        protected void crossover(byte[] a, byte[] b) {
            int firstPoint = ThreadLocalRandom.current().nextInt(1, a.length / 3);
            int secondPoint = ThreadLocalRandom.current().nextInt(firstPoint, a.length * 2 / 3);
            int thirdPoint = ThreadLocalRandom.current().nextInt(firstPoint, a.length - 1);

            swap(a, b, 0, firstPoint);
            swap(a, b, secondPoint, thirdPoint);
        }
    },

    UNIFORM {
        @Override
        protected void crossover(byte[] a, byte[] b) {
            for (int i = 0; i < a.length; i++) {
                if (ThreadLocalRandom.current().nextInt(2) == 0) {
                    byte buffer = a[i];
                    a[i] = b[i];
                    b[i] = buffer;
                }
            }
        }
    };


    private static void swap(byte[] a, byte[] b, int pointA, int pointB) {
        byte[] buffer = new byte[a.length];
        for (int i = pointA; i < pointB; i++) {
            buffer[i] = a[i];
            a[i] = b[i];
            b[i] = buffer[i];
        }
    }

    public List<Chromosome> compute(List<Chromosome> chromosomes, int probability) {
        Collections.shuffle(chromosomes);
        for (int i = 1; i < chromosomes.size(); i += 2) {

            if (ThreadLocalRandom.current().nextInt(1, 101) <= probability) {
                byte[] a = chromosomes.get(i - 1).getBytesRepresentation();
                byte[] b = chromosomes.get(i).getBytesRepresentation();

                crossover(a, b);

                chromosomes.set(i - 1, new Chromosome(a, chromosomes.get(i - 1).getMinimumValue(), chromosomes.get(i - 1).getMaximumValue()));
                chromosomes.set(i, new Chromosome(b, chromosomes.get(i).getMinimumValue(), chromosomes.get(i).getMaximumValue()));
            }
        }

        return chromosomes;
    }

    protected abstract void crossover(byte[] a, byte[] b);

}
