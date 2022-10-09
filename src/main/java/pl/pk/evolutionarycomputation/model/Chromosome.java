package pl.pk.evolutionarycomputation.model;

import lombok.*;

import static pl.pk.evolutionarycomputation.binary.BinaryCalculator.decodeBinaryNumber;
import static pl.pk.evolutionarycomputation.binary.BinaryCalculator.getDecimal;

@Getter
@ToString
public class Chromosome {
    private final double value;
    private final byte[] bytesRepresentation;

    public Chromosome(double value) {
        this.value = value;
        this.bytesRepresentation = new byte[]{};
    }

    public Chromosome(byte[] bytesRepresentation, int minimumValue, int maximumValue) {
        this.bytesRepresentation = bytesRepresentation;
        value = decodeBinaryNumber(minimumValue, maximumValue, bytesRepresentation.length, getDecimal(bytesRepresentation));
    }
}
