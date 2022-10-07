package pl.pk.evolutionarycomputation.util.selection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.pk.evolutionarycomputation.enums.Tournament;
import pl.pk.evolutionarycomputation.model.Chromosome;
import pl.pk.evolutionarycomputation.model.FunctionResult;
import pl.pk.evolutionarycomputation.util.selection.impl.SelectionImpl;

import java.util.List;
import java.util.function.Function;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SelectionImpl.class})
@EnableConfigurationProperties
class ISelectionTest {
    List<FunctionResult> listOfExamples;

    @Autowired
    SelectionImpl selection;    //TODO

    @BeforeEach
    void setUp() {
        Function<Double, Double> quadraticFunction = (x) -> (2 * Math.pow(x, 2)) + 5;

        listOfExamples = List.of(
                new FunctionResult(new Chromosome(-3), quadraticFunction),
                new FunctionResult(new Chromosome(-2.5), quadraticFunction),
                new FunctionResult(new Chromosome(2), quadraticFunction),
                new FunctionResult(new Chromosome(4), quadraticFunction),
                new FunctionResult(new Chromosome(1.75), quadraticFunction),
                new FunctionResult(new Chromosome(3.25), quadraticFunction),
                new FunctionResult(new Chromosome(8.5), quadraticFunction),
                new FunctionResult(new Chromosome(5.5), quadraticFunction),
                new FunctionResult(new Chromosome(9.75), quadraticFunction)
        );
    }

    @Test
    void bestElementsMethod() {
        float percentage = 0.3f;
        float expectedValue = Math.round(listOfExamples.size() * percentage); //TODO

        List<FunctionResult> resultList = this.selection.bestElementsMethod(this.listOfExamples, percentage);

        int resultSize = resultList.size();

        Assertions.assertEquals(
                expectedValue,
                resultSize,
                0.1
        );

        Assertions.assertEquals(listOfExamples.get(4), resultList.get(0));
        Assertions.assertEquals(listOfExamples.get(2), resultList.get(1));
        Assertions.assertEquals(listOfExamples.get(1), resultList.get(2));
    }

    @Test
    void rouletteMethod() {
    }

    @Test
    void tournamentMethod() {
        int tournamentSize = 3;

        List<FunctionResult> resultList = this.selection.
                tournamentMethod(this.listOfExamples, tournamentSize, Tournament.SINGLE);


        Assertions.assertTrue(resultList.contains(listOfExamples.get(4)));
//        Assertions.assertTrue(resultList.contains(listOfExamples.get(1)));    //TODO
//        Assertions.assertTrue(resultList.contains(listOfExamples.get(5)));    //TODO
    }
}