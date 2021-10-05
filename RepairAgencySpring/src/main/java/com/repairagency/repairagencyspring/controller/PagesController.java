package com.repairagency.repairagencyspring.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Log4j
@Controller
public class PagesController {

    final
    LocaleResolver localeResolver;

    @Autowired
    public PagesController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @GetMapping("/")
    public String mainPage(HttpServletRequest request, Model model)
    {
        @AllArgsConstructor
        @Getter
        @Setter
        class Prod{
            public String name;
            public Integer price;
            public boolean inStock;
        }
        List<Prod> prodList = Arrays.asList(new Prod("Item1",10,true),new Prod("Item2",20,false),new Prod("Item3",30,true));
        DateTimeFormatter frDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(localeResolver.resolveLocale(request));
        String frFormattedDate = LocalDate.now().format(frDateFormatter);
        model.addAttribute("today",frFormattedDate);
        model.addAttribute("prods",prodList);
        return "index";
    }

}
