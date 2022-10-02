package pl.pk.evolutionarycomputation.binary;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.pk.evolutionarycomputation.binary.BinaryCalculator.*;

class BinaryCalculatorTest {

    @Test
    void getLengthOfChromosome_test() {
        assertEquals(25, getLengthOfChromosome(-10, 10));
        assertEquals(24, getLengthOfChromosome(-4.5, 4.5));
    }

    @Test
    void decodeBinaryNumber_test() {
        assertEquals(-9.01774, decodeBinaryNumber(-10, 10, 25, 1647962));
    }

    @Test
    void getDecimal_test() {
        assertEquals(1000, getDecimal("1111101000"));
        assertEquals(1000, getDecimal("1111101000"));
    }

    @Test
    void getDecimal_boolean_array_test() {
        assertEquals(1000, getDecimal(new byte[]{
                1,
                1,
                1,
                1,
                1,
                0,
                1,
                0,
                0,
                0
        }, 10));
    }
}