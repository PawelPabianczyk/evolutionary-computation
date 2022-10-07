package pl.pk.evolutionarycomputation.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Chromosome {
    private double value;
    private byte[] bytesRepresentation;

    public Chromosome(double value) {
        this.value = value;
        this.bytesRepresentation = new byte[]{};
    }
}
