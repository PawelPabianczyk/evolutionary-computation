package pl.pk.evolutionarycomputation.util.selection;

import pl.pk.evolutionarycomputation.enums.Mode;
import pl.pk.evolutionarycomputation.enums.Rank;
import pl.pk.evolutionarycomputation.enums.Tournament;
import pl.pk.evolutionarycomputation.model.FunctionResult;
import pl.pk.evolutionarycomputation.model.FunctionResult2;

import java.util.List;
import java.util.Map;

public interface ISelection {
    List<FunctionResult> bestElementsMethod(List<FunctionResult> functionResults,
                                            float percentageOfBestElements,
                                            Mode mode);
    List<FunctionResult2> bestElementsMethod2(List<FunctionResult2> functionResults,
                                            float percentageOfBestElements,
                                            Mode mode);

    Map<FunctionResult, Double> rouletteMethod(List<FunctionResult> functionResults,
                                               Mode mode);

    Map<FunctionResult2, Double> rouletteMethod2(List<FunctionResult2> functionResults,
                                               Mode mode);

    List<FunctionResult> tournamentMethod(List<FunctionResult> functionResults,
                                          int tournamentSize,
                                          Tournament tournament,
                                          Mode mode);

    List<FunctionResult2> tournamentMethod2(List<FunctionResult2> functionResults,
                                          int tournamentSize,
                                          Tournament tournament,
                                          Mode mode);

    List<FunctionResult> rankingMethod(List<FunctionResult> functionResults,
                                       Rank rank);

    List<FunctionResult2> rankingMethod2(List<FunctionResult2> functionResults,
                                       Rank rank);
}
