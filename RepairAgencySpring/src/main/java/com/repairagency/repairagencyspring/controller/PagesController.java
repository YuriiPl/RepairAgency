package com.repairagency.repairagencyspring.controller;


import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;


@Log4j2
@Controller
public class PagesController {

    final UserRepository userRepository;

    public PagesController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String getCorrectPage(Authentication authentication, String basicPage){
        if(authentication != null) {
            final Optional<UserDB> user = userRepository.findByLogin(authentication.getName());
            if(user.isPresent()){
                return "redirect:"+user.get().getUserRole().getHomePage();
            }
        }
        return basicPage;
    }

    @GetMapping("/")
    public String mainPage(Authentication authentication)
    {
        return getCorrectPage(authentication,"index");
    }

    @GetMapping("/auth/login")
    public String getLoginPage(Authentication authentication) {
        return getCorrectPage(authentication,"auth/login");
    }

}
