package pl.pk.evolutionarycomputation.util.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.pk.evolutionarycomputation.model.Chromosome;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ChromosomeGenerator.class})
@EnableConfigurationProperties
class ChromosomeGeneratorTest {
    int numberOfElements;
    int firstXValue;
    int secondXValue;
    int expectedNumberOfElements;

    @Autowired
    ChromosomeGenerator chromosomeGenerator;

    @BeforeEach
    void setUp() {
        numberOfElements = 20;
        firstXValue = 5;
        secondXValue = 10;
        expectedNumberOfElements = 20;
    }

    @Test
    void generateGivenNumberOfChromosomesTest() {
        List<Chromosome> chromosomes = this.chromosomeGenerator
                .generateListOfChromosomes(numberOfElements, firstXValue, secondXValue);

        Assertions.assertEquals(expectedNumberOfElements, chromosomes.size());
    }

    @Test
    void checkValuesInChromosomesTest() {
        List<Chromosome> chromosomes = this.chromosomeGenerator
                .generateListOfChromosomes(numberOfElements, firstXValue, secondXValue)
                .stream()
                .filter(chromosome -> chromosome.getValue() <= secondXValue)
                .filter(chromosome -> chromosome.getValue() >= firstXValue - 5)
                .toList();

        Assertions.assertEquals(expectedNumberOfElements, chromosomes.size());
    }
}