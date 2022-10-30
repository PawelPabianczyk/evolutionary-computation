package pl.pk.evolutionarycomputation.util.selection;

import pl.pk.evolutionarycomputation.enums.Mode;
import pl.pk.evolutionarycomputation.enums.Rank;
import pl.pk.evolutionarycomputation.enums.Tournament;
import pl.pk.evolutionarycomputation.model.FunctionResult;

import java.util.List;
import java.util.Map;

public interface ISelection {

    List<FunctionResult> bestElementsMethod(List<FunctionResult> functionResults,
                                            float percentageOfBestElements,
                                            Mode mode);

    Map<FunctionResult, Double> rouletteMethod(List<FunctionResult> functionResults,
                                               Mode mode);

    List<FunctionResult> tournamentMethod(List<FunctionResult> functionResults,
                                          int tournamentSize,
                                          Tournament tournament,
                                          Mode mode);

    List<FunctionResult> rankingMethod(List<FunctionResult> functionResults,
                                       Rank rank);
}
