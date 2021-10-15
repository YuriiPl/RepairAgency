//package com.repairagency.repairagencyspring.controller;
//
//import com.repairagency.repairagencyspring.entity.UserDB;
//import com.repairagency.repairagencyspring.repos.UserRepository;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/auth")
//public class AuthController {
//
//    final UserRepository userRepository;
//
//    public AuthController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
////    @GetMapping("/login")
////    public String getLoginPage(Authentication authentication) {
////        if (authentication != null) {
////            final Optional<UserDB> user = userRepository.findByLogin(authentication.getName());
////            if (user.isPresent()) {
////                return "redirect:" + user.get().getUserRole().getHomePage();
////            }
////        }
////        return "auth/login";
////    }
//
//    @GetMapping("/success")
//    public String getSuccessPage(){
//        return "success";
//    }
//}
