package pl.pk.evolutionarycomputation.util.selection;

import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.pk.evolutionarycomputation.enums.Mode;
import pl.pk.evolutionarycomputation.enums.Rank;
import pl.pk.evolutionarycomputation.enums.Tournament;
import pl.pk.evolutionarycomputation.model.FunctionResult;
import pl.pk.evolutionarycomputation.util.selection.impl.SelectionImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SelectionImpl.class})
@EnableConfigurationProperties
class ISelectionTest {
    List<FunctionResult> listOfExamples;

    @Autowired
    SelectionImpl selection;

    @BeforeEach
    void setUp() {
        Function<Double, Double> quadraticFunction = (x) -> (2 * Math.pow(x, 2)) + 5;

        listOfExamples = new LinkedList<>();
//        listOfExamples.add(new FunctionResult(new Chromosome(-3), quadraticFunction));
//        listOfExamples.add(new FunctionResult(new Chromosome(-2.5), quadraticFunction));
//        listOfExamples.add(new FunctionResult(new Chromosome(2), quadraticFunction));
//        listOfExamples.add(new FunctionResult(new Chromosome(4), quadraticFunction));
//        listOfExamples.add(new FunctionResult(new Chromosome(1.75), quadraticFunction));
//        listOfExamples.add(new FunctionResult(new Chromosome(3.25), quadraticFunction));
//        listOfExamples.add(new FunctionResult(new Chromosome(8.5), quadraticFunction));
//        listOfExamples.add(new FunctionResult(new Chromosome(5.5), quadraticFunction));
//        listOfExamples.add(new FunctionResult(new Chromosome(9.75), quadraticFunction));
    }

    @Disabled("Disabled until decimal converter is created")
    @Test
    void bestElementsMethodTest() {
        float percentage = 0.3f;
        float expectedValue = Math.round(listOfExamples.size() * percentage);

        List<FunctionResult> resultList = this.selection.bestElementsMethod(
                this.listOfExamples,
                percentage,
                Mode.MINIMIZATION
        );

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

    @Disabled("Disabled until decimal converter is created")
    @Test
    void rouletteMethodTest() {
        Map<FunctionResult, Double> functionResultDoubleMap = this.selection.rouletteMethod(listOfExamples, Mode.MAXIMIZATION);

        Assertions.assertEquals(0.042761, functionResultDoubleMap.get(listOfExamples.get(0)), 0.000001);
        Assertions.assertEquals(0.075296305, functionResultDoubleMap.get(listOfExamples.get(1)), 0.000001);
        Assertions.assertEquals(0.099465489, functionResultDoubleMap.get(listOfExamples.get(2)), 0.000001);
        Assertions.assertEquals(0.168254706, functionResultDoubleMap.get(listOfExamples.get(3)), 0.000001);
        Assertions.assertEquals(0.18893795, functionResultDoubleMap.get(listOfExamples.get(4)), 0.000001);
        Assertions.assertEquals(0.237508715, functionResultDoubleMap.get(listOfExamples.get(5)), 0.000001);
        Assertions.assertEquals(0.515454334, functionResultDoubleMap.get(listOfExamples.get(6)), 0.000001);
        Assertions.assertEquals(0.63722984, functionResultDoubleMap.get(listOfExamples.get(7)), 0.000001);
        Assertions.assertEquals(1, functionResultDoubleMap.get(listOfExamples.get(8)), 0.000001);
    }

    @Disabled("Disabled until decimal converter is created")
    @Test
    void SINGLE_tournamentMethodTest() {
        int tournamentSize = 3;

        List<FunctionResult> resultList = this.selection
                .tournamentMethod(this.listOfExamples, tournamentSize, Tournament.SINGLE, Mode.MINIMIZATION);

        Assertions.assertEquals(3, resultList.size());
    }


    @Disabled("Disabled until decimal converter is created")
    @Test
    void DOUBLE_tournamentMethodTest() {
        int tournamentSize = 3;

        List<FunctionResult> copyOfListOfExamples = new LinkedList<>(listOfExamples);

        List<FunctionResult> resultList = this.selection
                .tournamentMethod(this.listOfExamples, tournamentSize, Tournament.DOUBLE, Mode.MINIMIZATION);

        Assertions.assertEquals(1, resultList.size());
        Assertions.assertEquals(resultList.get(0), copyOfListOfExamples.get(4));
    }

    @Disabled("Disabled until decimal converter is created")
    @Test
    void MINIMUM_VALUE_rankingMethodTest() {
        int expectedNumberOfElements = 45;

        List<FunctionResult> resultList = this.selection.rankingMethod(
                this.listOfExamples,
                Rank.MINIMUM_VALUE
        );

        Assertions.assertEquals(expectedNumberOfElements, resultList.size());
    }

    @Disabled("Disabled until decimal converter is created")
    @Test
    void MAXIMUM_VALUE_rankingMethodTest() {
        int expectedNumberOfElements = 45;

        List<FunctionResult> resultList = this.selection.rankingMethod(
                this.listOfExamples,
                Rank.MAXIMUM_VALUE
        );

        Assertions.assertEquals(expectedNumberOfElements, resultList.size());
    }
}