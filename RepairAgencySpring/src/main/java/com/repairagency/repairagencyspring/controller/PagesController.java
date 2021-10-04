package com.repairagency.repairagencyspring.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j
@Configuration
public class PagesController {

    @GetMapping("/")
    public String mainPage(Model model)
    {
        return "index";
    }

}
