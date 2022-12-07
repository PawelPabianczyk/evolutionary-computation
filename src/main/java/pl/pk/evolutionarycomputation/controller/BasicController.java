package pl.pk.evolutionarycomputation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.pk.evolutionarycomputation.dto.GeneticAlgorithmConfigurationDTO;
import pl.pk.evolutionarycomputation.dto.ResultsDTO;
import pl.pk.evolutionarycomputation.enums.Crossover;
import pl.pk.evolutionarycomputation.enums.Mutation;
import pl.pk.evolutionarycomputation.enums.Selection;
import pl.pk.evolutionarycomputation.enums.Tournament;
import pl.pk.evolutionarycomputation.service.GeneticService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
public class BasicController {

    @Autowired
    GeneticService geneticService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("algConf", new GeneticAlgorithmConfigurationDTO());
        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String sendFormData(@RequestBody String body, Model model) {
        String[] parts = body.split("&");
        int rangeBegin = 0, rangeEnd = 0, populationAmount = 0, epochsAmount = 0, chromosomeAmount = 0, eliteStrategyAmount= 0, crossProbability=0, mutationProbability=0, inversionProbability=0, percentageOfBestElements = 0, tournamentSize = 0, spinNumber = 0;
        boolean maximization = false;
        String selectionMethod = null, crossMethod = null, mutationMethodStr = null;
        Mutation mutationMethod = Mutation.ONE_POINT;
        double alpha = 10;
        double beta = 10;
        if(parts.length == 17) {
            maximization = true;
        }

        if(parts.length >= 16) {
            String[] tempParts = parts[0].split("=");
            rangeBegin = Integer.parseInt(tempParts[1]);

            tempParts = parts[1].split("=");
            rangeEnd = Integer.parseInt(tempParts[1]);

            tempParts = parts[2].split("=");
            populationAmount = Integer.parseInt(tempParts[1]);

            tempParts = parts[3].split("=");
            epochsAmount = Integer.parseInt(tempParts[1]);

            tempParts = parts[4].split("=");
            chromosomeAmount = Integer.parseInt(tempParts[1]);

            tempParts = parts[5].split("=");
            eliteStrategyAmount = Integer.parseInt(tempParts[1]);

            tempParts = parts[6].split("=");
            crossProbability = Integer.parseInt(tempParts[1]);

            tempParts = parts[7].split("=");
            mutationProbability = Integer.parseInt(tempParts[1]);

            tempParts = parts[8].split("=");
            inversionProbability = Integer.parseInt(tempParts[1]);

            tempParts = parts[9].split("=");
            percentageOfBestElements = Integer.parseInt(tempParts[1]);

            tempParts = parts[10].split("=");
            tournamentSize = Integer.parseInt(tempParts[1]);

            tempParts = parts[11].split("=");
            spinNumber = Integer.parseInt(tempParts[1]);

            tempParts = parts[12].split("=");
            alpha = Double.parseDouble(tempParts[1]);

            tempParts = parts[13].split("=");
            beta = Double.parseDouble(tempParts[1]);

            tempParts = parts[14].split("=");
            selectionMethod = tempParts[1];

            tempParts = parts[15].split("=");
            crossMethod = tempParts[1];

            tempParts = parts[16].split("=");
            mutationMethodStr  = tempParts[1];
        }
        else {
            System.out.println("Error: all data required!");
        }

        Selection selection = null;
        Crossover crossover = null;
        Mutation mutation = null;

        switch (selectionMethod) {
            case "best" -> selection = Selection.BEST_ELEMENTS;
            case "roulette" -> selection = Selection.ROULETTE;
            case "tournament" -> selection = Selection.TOURNAMENT;
            case "ranking" -> selection = Selection.RANKING;
        }
        switch (crossMethod) {
            case "ONE_POINT" -> crossover = Crossover.ONE_POINT;
            case "TWO_POINT" -> crossover = Crossover.TWO_POINT;
            case "THREE_POINT" -> crossover = Crossover.THREE_POINT;
            case "UNIFORM" -> crossover = Crossover.UNIFORM;
        }

        switch (mutationMethodStr) {
            case "ONE_POINT" -> mutation = Mutation.ONE_POINT;
            case "TWO_POINT" -> mutation = Mutation.TWO_POINT;
        }


        Tournament tournament = Tournament.SINGLE;

        GeneticAlgorithmConfigurationDTO geneticAlgorithmConfigurationDTO = new GeneticAlgorithmConfigurationDTO(rangeBegin, rangeEnd, populationAmount, epochsAmount, chromosomeAmount, eliteStrategyAmount, crossProbability, mutationProbability, inversionProbability,selection,crossover,mutation,tournament, maximization, percentageOfBestElements, tournamentSize, spinNumber, alpha, beta, crossMethod, mutationMethodStr);
        List<String> methodsV1 = Arrays.asList("ONE_POINT", "TWO_POINT", "UNIFORM", "THREE_POINT");
        List<String> methodsV2 = Arrays.asList("BLEND_CROSSOVER_ALPHA_BETA", "ARITHMETIC_CROSSOVER", "LINEAR_CROSSOVER", "AVERAGE_CROSSOVER", "BLEND_CROSSOVER_ALPHA", "UNIFORM_MUTATION", "GAUSS_MUTATION");
        if(methodsV1.contains(crossMethod) && methodsV1.contains(mutationMethodStr)){
            ResultsDTO resultsDTO = geneticService.perform(geneticAlgorithmConfigurationDTO);
            return results(model, resultsDTO);
        }else if(methodsV2.contains(crossMethod) && methodsV2.contains(mutationMethodStr)){
            ResultsDTO resultsDTO = geneticService.performV2(geneticAlgorithmConfigurationDTO);
            return results(model, resultsDTO);
        } else{
            geneticAlgorithmConfigurationDTO.setCrossMethodV2("LINEAR_CROSSOVER");
            geneticAlgorithmConfigurationDTO.setMutationMethodV2("UNIFORM_MUTATION");
            ResultsDTO resultsDTO = geneticService.performV2(geneticAlgorithmConfigurationDTO);
            return results(model, resultsDTO);
        }
    }



    @RequestMapping(value = "", method = RequestMethod.GET)
    public String redirectToIndex() {
        return "redirect:/index";
    }

    @RequestMapping(value = "/results", method = RequestMethod.GET)
    public String results(Model model, ResultsDTO configuration) {

        Map<Integer, Double> best = configuration.bestFunctionValues();
        Map<Integer, Double> avg = configuration.avgFunctionValues();
        Map<Integer, Double> standard = configuration.standardDeviations();

        int[] bestGen = new int[best.size()];
        double[] bestVal = new double[best.size()];

        for(int i = 1; i < best.size(); i++) {
            bestGen[i] = i;
            bestVal[i] = best.get(i);
        }

        int[] avgGen = new int[avg.size()];
        double[] avgVal = new double[avg.size()];

        for(int i = 1; i < avg.size(); i++) {
            avgGen[i] = i;
            avgVal[i] = avg.get(i);
        }

        int[] stdGen = new int[standard.size()];
        double[] stdVal = new double[standard.size()];

        for(int i = 1; i < standard.size(); i++) {
            stdGen[i] = i;
            stdVal[i] = standard.get(i);
        }

        model.addAttribute("generationBest", bestGen);
        model.addAttribute("valueBest", bestVal);
        model.addAttribute("linearChartBest", true);

        model.addAttribute("generationAvg", avgGen);
        model.addAttribute("valueAvg", avgVal);
        model.addAttribute("linearChartAvg", true);

        model.addAttribute("generationStd", stdGen);
        model.addAttribute("valueStd", stdVal);
        model.addAttribute("linearChartStd", true);
        return "/results";
    }
}
