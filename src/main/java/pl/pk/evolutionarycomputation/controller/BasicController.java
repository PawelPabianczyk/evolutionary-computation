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
        int rangeBegin = 0, rangeEnd = 0, populationAmount = 0, epochsAmount = 0, chromosomeAmount = 0, eliteStrategyAmount= 0, crossProbability=0, mutationProbability=0, inversionProbability=0;
        boolean maximization = false;
        String selectionMethod = null, crossMethod = null;
        Mutation mutationMethod = Mutation.ONE_POINT;
        if(parts.length == 13) {
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
            selectionMethod = tempParts[1];

            tempParts = parts[10].split("=");
            crossMethod = tempParts[1];

            tempParts = parts[11].split("=");
            String tempStr  = tempParts[1];

            if(tempStr.equals("true")) {
                maximization = true;
            } else {
                maximization = false;
            }
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
        }
        switch (crossMethod) {
            case "ONE_POINT" -> crossover = Crossover.ONE_POINT;
            case "TWO_POINT" -> crossover = Crossover.TWO_POINT;
            case "THREE_POINT" -> crossover = Crossover.THREE_POINT;
            case "UNIFORM" -> crossover = Crossover.UNIFORM;
        }

        Tournament tournament = Tournament.SINGLE;
        int percentageOfBestElements = 30;
        int tournamentSize = 10;
        int spinNumber = 30;

        GeneticAlgorithmConfigurationDTO geneticAlgorithmConfigurationDTO = new GeneticAlgorithmConfigurationDTO(rangeBegin, rangeEnd, populationAmount, epochsAmount, chromosomeAmount, eliteStrategyAmount, crossProbability, mutationProbability, inversionProbability,selection,crossover,mutationMethod,tournament, maximization, percentageOfBestElements, tournamentSize, spinNumber);
        ResultsDTO resultsDTO = geneticService.perform(geneticAlgorithmConfigurationDTO);
        return results(model, resultsDTO);
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
