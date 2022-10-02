package pl.pk.evolutionarycomputation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BasicController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
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
