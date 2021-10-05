package com.repairagency.repairagencyspring.controller;


import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;


@Controller
public class PagesController {
    @GetMapping("/")
    public String mainPage(HttpServletRequest request, Model model)
    {
        return "index";
    }

//    @GetMapping("/error")
//    public String errorPage(HttpServletRequest request, Model model)
//    {
//        return "index";
//    }
}
