package com.repairagency.repairagencyspring.controller;


import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;


@Slf4j
@Controller
public class PagesController {

    final
    UserRepository userRepository;

    public PagesController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String mainPage(Authentication authentication)
    {
        if(authentication != null) {
            final Optional<UserDB> user = userRepository.findByLogin(authentication.getName());
            if(user.isPresent()){
                return "redirect:"+user.get().getUserRole().getHomePage();
            }
        }
        return "index";
    }

//    @PreAuthorize("hasAuthority('perm:admin')")
//    @GetMapping("/test")
//    public String errorPage(HttpServletRequest request, Model model)
//    {
//        return "index";
//    }

}
