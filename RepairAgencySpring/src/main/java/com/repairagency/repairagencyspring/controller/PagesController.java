package com.repairagency.repairagencyspring.controller;


import lombok.extern.log4j.Log4j;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/403")
    public String error403() {
        return "/errors/403";
    }

    @PreAuthorize("hasAuthority('perm:admin')")
    @GetMapping("/test")
    public String errorPage(HttpServletRequest request, Model model)
    {
        return "index";
    }
}
