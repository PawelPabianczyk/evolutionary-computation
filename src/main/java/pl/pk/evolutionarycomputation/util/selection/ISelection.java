package pl.pk.evolutionarycomputation.util.selection;

import pl.pk.evolutionarycomputation.enums.Mode;
import pl.pk.evolutionarycomputation.enums.Tournament;
import pl.pk.evolutionarycomputation.model.FunctionResult;

import java.util.List;
import java.util.Map;

public interface ISelection {
    List<FunctionResult> bestElementsMethod(List<FunctionResult> functionResults, float percentageOfBestElements);

    Map<FunctionResult, Double> rouletteMethod(List<FunctionResult> functionResults, Mode mode);

    List<FunctionResult> tournamentMethod(List<FunctionResult> functionResults, int tournamentSize, Tournament tournament);
}
