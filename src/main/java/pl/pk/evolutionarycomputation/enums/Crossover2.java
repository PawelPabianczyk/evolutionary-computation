package pl.pk.evolutionarycomputation.enums;

import pl.pk.evolutionarycomputation.model.Candidate;
import pl.pk.evolutionarycomputation.model.Chromosome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Crossover2 {
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

    public List<Candidate> compute(List<Candidate> candidates, int probability) {
        List<Candidate> newCandidates = new ArrayList<>();
        while (newCandidates.size() < candidates.size()) {
            Collections.shuffle(candidates);
            for (int i = 1; i < candidates.size(); i += 2) {
                if (ThreadLocalRandom.current().nextInt(1, 101) <= probability) {

                    List<Chromosome> can1Chromosomes = candidates.get(i - 1).getChromosomes();
                    List<Chromosome> newCan1Chromosomes = new ArrayList<>();
                    List<Chromosome> can2Chromosomes = candidates.get(i).getChromosomes();
                    List<Chromosome> newCan2Chromosomes = new ArrayList<>();


                    for (int j = 0; j < can1Chromosomes.size(); j++) {
                        byte[] can1Chromosome = can1Chromosomes.get(j).getBinaryRepresentation();
                        byte[] can2Chromosome = can2Chromosomes.get(j).getBinaryRepresentation();

                        crossover(can1Chromosome, can2Chromosome);

                        newCan1Chromosomes.add(new Chromosome(can1Chromosome, can1Chromosomes.get(j).getMinimumValue(), can1Chromosomes.get(j).getMaximumValue()));
                        newCan2Chromosomes.add(new Chromosome(can2Chromosome, can2Chromosomes.get(j).getMinimumValue(), can2Chromosomes.get(j).getMaximumValue()));
                    }

                    newCandidates.add(new Candidate(newCan1Chromosomes));
                    newCandidates.add(new Candidate(newCan2Chromosomes));
                }
            }
        }

        return newCandidates.stream().limit(candidates.size()).toList();
    }

    protected abstract void crossover(byte[] a, byte[] b);

}
