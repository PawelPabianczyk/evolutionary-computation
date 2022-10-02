package pl.pk.evolutionarycomputation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.pk.evolutionarycomputation.model.GeneticAlgorithmConfiguration;

@Controller
public class BasicController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("userConf", new GeneticAlgorithmConfiguration());
        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String sendFormData(@ModelAttribute Model model) {
        return "index";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String redirectToIndex() {
        return "redirect:/index";
    }

    @RequestMapping(value = "/results", method = RequestMethod.GET)
    public String results() {
        return "/results";
    }
}
