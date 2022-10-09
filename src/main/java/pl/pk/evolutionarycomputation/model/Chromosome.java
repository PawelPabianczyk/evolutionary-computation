package pl.pk.evolutionarycomputation.model;

import lombok.*;

import static pl.pk.evolutionarycomputation.binary.BinaryCalculator.decodeBinaryNumber;
import static pl.pk.evolutionarycomputation.binary.BinaryCalculator.getDecimal;

@Getter
@ToString
public class Chromosome {
    private final double value;
    private final byte[] bytesRepresentation;

    private final int minimumValue;

    private final int maximumValue;

    public Chromosome(double value) {
        this.value = value;
        this.bytesRepresentation = new byte[]{};
        this.minimumValue = 0;  // TODO: 09/10/2022 only for testing
        this.maximumValue = 100;    // TODO: 09/10/2022 only for testing
    }

    public Chromosome(byte[] bytesRepresentation, int minimumValue, int maximumValue) {
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.bytesRepresentation = bytesRepresentation;
        value = decodeBinaryNumber(minimumValue, maximumValue, bytesRepresentation.length, getDecimal(bytesRepresentation));
    }
}
