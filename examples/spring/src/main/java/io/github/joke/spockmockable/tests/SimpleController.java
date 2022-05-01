package io.github.joke.spockmockable.tests;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SimpleController {

    private final SimpleService simpleService;

    public SimpleController(SimpleService simpleService) {
        this.simpleService = simpleService;
    }

    @ResponseBody
    @GetMapping("/name")
    public String getRoot() {
        return simpleService.getName();
    }

}
