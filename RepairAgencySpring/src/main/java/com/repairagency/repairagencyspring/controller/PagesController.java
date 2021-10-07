package com.repairagency.repairagencyspring.controller;


import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


@Controller
public class PagesController {
    @GetMapping("/")
    public String mainPage(HttpServletRequest request, Model model)
    {
        return "index";
    }

    @PreAuthorize("hasAuthority('perm:admin')")
    @GetMapping("/test")
    public String errorPage(HttpServletRequest request, Model model)
    {
        return "index";
    }

}
