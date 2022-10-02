package pl.pk.evolutionarycomputation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
