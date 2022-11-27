package pl.pk.evolutionarycomputation.model;

import lombok.*;

import static pl.pk.evolutionarycomputation.binary.BinaryCalculator.decodeBinaryNumber;
import static pl.pk.evolutionarycomputation.binary.BinaryCalculator.getDecimal;

@Getter
@ToString
public class Chromosome {
    private final double value;
    private final byte[] binaryRepresentation;

    private final int minimumValue;

    private final int maximumValue;

    public Chromosome(byte[] binaryRepresentation, int minimumValue, int maximumValue) {
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.binaryRepresentation = binaryRepresentation;
        value = decodeBinaryNumber(minimumValue, maximumValue, binaryRepresentation.length, getDecimal(binaryRepresentation));
    }

    public Chromosome(double value, int minimumValue, int maximumValue) {
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.binaryRepresentation = null;
        this.value = value;
    }


}
