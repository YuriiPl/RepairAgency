package com.repairagency.repairagencyspring.controller;


import com.repairagency.repairagencyspring.DAO.RootAndLoginPagesService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Log4j2
@Controller
public class RootAndLoginPagesController {

    private final RootAndLoginPagesService rootAndLoginPagesService;

    public RootAndLoginPagesController(RootAndLoginPagesService rootAndLoginPagesService) {
        this.rootAndLoginPagesService = rootAndLoginPagesService;
    }


    @GetMapping("/")
    public String mainPage(Authentication authentication)
    {
        return rootAndLoginPagesService.getCorrectPage(authentication,"index");
    }

    @GetMapping("/auth/login")
    public String getLoginPage(Authentication authentication) {
        return rootAndLoginPagesService.getCorrectPage(authentication,"auth/login");
    }

}
