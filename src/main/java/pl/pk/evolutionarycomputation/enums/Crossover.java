package pl.pk.evolutionarycomputation.enums;

import pl.pk.evolutionarycomputation.model.Chromosome;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Crossover {
    // TODO: 09/10/2022 add tests
    // TODO: 09/10/2022 add uniform method


    private Crossover() {
    }

    public static List<Chromosome> onePointCrossover(List<Chromosome> chromosomes) {
        for (int i = 1; i < chromosomes.size(); i += 2) {

            byte[] a = chromosomes.get(i - 1).getBytesRepresentation();
            byte[] b = chromosomes.get(i).getBytesRepresentation();

            int point = ThreadLocalRandom.current().nextInt(1, a.length - 1);

            swapElements(a, b, 0, point);
            chromosomes.set(i - 1, new Chromosome(a, chromosomes.get(i - 1).getMinimumValue(), chromosomes.get(i - 1).getMaximumValue()));
            chromosomes.set(i, new Chromosome(b, chromosomes.get(i).getMinimumValue(), chromosomes.get(i).getMaximumValue()));
        }

        return chromosomes;
    }

    public static List<Chromosome> twoPointCrossover(List<Chromosome> chromosomes) {
        for (int i = 1; i < chromosomes.size(); i += 2) {

            byte[] a = chromosomes.get(i - 1).getBytesRepresentation();
            byte[] b = chromosomes.get(i).getBytesRepresentation();

            int firstPoint = ThreadLocalRandom.current().nextInt(1, a.length / 2);
            int secondPoint = ThreadLocalRandom.current().nextInt(firstPoint, a.length - 1);

            swapElements(a, b, firstPoint, secondPoint);

            chromosomes.set(i - 1, new Chromosome(a, chromosomes.get(i - 1).getMinimumValue(), chromosomes.get(i - 1).getMaximumValue()));
            chromosomes.set(i, new Chromosome(b, chromosomes.get(i).getMinimumValue(), chromosomes.get(i).getMaximumValue()));
        }

        return chromosomes;
    }

    public static List<Chromosome> threePointCrossover(List<Chromosome> chromosomes) {
        for (int i = 1; i < chromosomes.size(); i += 2) {

            byte[] a = chromosomes.get(i - 1).getBytesRepresentation();
            byte[] b = chromosomes.get(i).getBytesRepresentation();

            int firstPoint = ThreadLocalRandom.current().nextInt(1, a.length / 3);
            int secondPoint = ThreadLocalRandom.current().nextInt(firstPoint, a.length * 2 / 3);
            int thirdPoint = ThreadLocalRandom.current().nextInt(firstPoint, a.length - 1);

            swapElements(a, b, 0, firstPoint);
            swapElements(a, b, secondPoint, thirdPoint);

            chromosomes.set(i - 1, new Chromosome(a, chromosomes.get(i - 1).getMinimumValue(), chromosomes.get(i - 1).getMaximumValue()));
            chromosomes.set(i, new Chromosome(b, chromosomes.get(i).getMinimumValue(), chromosomes.get(i).getMaximumValue()));
        }

        return chromosomes;
    }

    private static void swapElements(byte[] a, byte[] b, int pointA, int pointB) {
        byte[] buffer = new byte[a.length];
        for (int i = pointA; i < pointB; i++) {
            buffer[i] = a[i];
            a[i] = b[i];
            b[i] = buffer[i];
        }
    }

}
