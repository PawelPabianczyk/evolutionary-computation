package pl.pk.evolutionarycomputation.enums;

import pl.pk.evolutionarycomputation.model.Chromosome;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Crossover {
    // TODO: 09/10/2022 add tests

    ONE_POINT{
        @Override
        protected void swapElements(byte[] a, byte[] b) {
            int point = ThreadLocalRandom.current().nextInt(1, a.length - 1);

            swap(a, b, 0, point);
        }
    },
    TWO_POINT{
        @Override
        protected void swapElements(byte[] a, byte[] b) {
            int firstPoint = ThreadLocalRandom.current().nextInt(1, a.length / 2);
            int secondPoint = ThreadLocalRandom.current().nextInt(firstPoint, a.length - 1);

            swap(a, b, firstPoint, secondPoint);
        }
    },
    THREE_POINT{
        @Override
        protected void swapElements(byte[] a, byte[] b) {
            int firstPoint = ThreadLocalRandom.current().nextInt(1, a.length / 3);
            int secondPoint = ThreadLocalRandom.current().nextInt(firstPoint, a.length * 2 / 3);
            int thirdPoint = ThreadLocalRandom.current().nextInt(firstPoint, a.length - 1);

            swap(a, b, 0, firstPoint);
            swap(a, b, secondPoint, thirdPoint);
        }
    },

    UNIFORM{
        @Override
        protected void swapElements(byte[] a, byte[] b) {
            for (int i = 0; i < a.length; i++) {
                if (ThreadLocalRandom.current().nextInt(2) == 0){
                    byte buffer = a[i];
                    a[i] = b[i];
                    b[i] = buffer;
                }
            }
        }
    };


    public List<Chromosome> crossover(List<Chromosome> chromosomes){
        Collections.shuffle(chromosomes);
        for (int i = 1; i < chromosomes.size(); i += 2) {

            byte[] a = chromosomes.get(i - 1).getBytesRepresentation();
            byte[] b = chromosomes.get(i).getBytesRepresentation();

            swapElements(a, b);

            chromosomes.set(i - 1, new Chromosome(a, chromosomes.get(i - 1).getMinimumValue(), chromosomes.get(i - 1).getMaximumValue()));
            chromosomes.set(i, new Chromosome(b, chromosomes.get(i).getMinimumValue(), chromosomes.get(i).getMaximumValue()));
        }

        return chromosomes;
    }

     protected abstract void swapElements(byte[] a, byte[] b);
    private static void swap(byte[] a, byte[] b, int pointA, int pointB) {
        byte[] buffer = new byte[a.length];
        for (int i = pointA; i < pointB; i++) {
            buffer[i] = a[i];
            a[i] = b[i];
            b[i] = buffer[i];
        }
    }

}
