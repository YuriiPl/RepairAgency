package com.repairagency.repairagencyspring.controller;


import com.repairagency.repairagencyspring.dto.UserDTO;
import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping(value = "/api")
public class RegFormController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegFormController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registrationFormController(@Valid UserDTO userDTO, BindingResult br, Model model){
        if(br.hasErrors()){
            Set<String> attributes = br.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toSet());
            model.addAttribute("errors",attributes);
            model.addAttribute("user", userDTO);
            return "api/reg_form";
        } else {
            try {
                userRepo.save(new UserDB(userDTO, passwordEncoder));
                log.info("{}", userDTO);
            } catch (DataIntegrityViolationException ex){
                log.warn("{}",ex.toString());
                model.addAttribute("errors",Collections.singleton("user_email_exist"));
                model.addAttribute("user", userDTO);
                return "api/reg_form";
            }
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/register")
    public String regForm(Model model){
        return "api/reg_form";
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<Map<String,Set<String>>> handleConstraintViolationException(DataIntegrityViolationException ex) {
//        log.warn("{} {}", ex.getClass(), ex.getLocalizedMessage());
//        return new ResponseEntity<>(Collections.singletonMap("message",Collections.singleton("user_email_exist")), HttpStatus.CONFLICT);
//    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Map<String,Set<String>>> handleRuntimeException(ConstraintViolationException ex) {
//        log.warn("{} {}", ex.getClass(), ex.getLocalizedMessage());
//        Set<String> collect = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
//        return new ResponseEntity<>(Collections.singletonMap("message",collect), HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(BindException.class)
//    public ResponseEntity<Map<String,Set<String>>> handleBindExceptionException(BindException ex) {
//        log.warn("{} {}", ex.getClass(), ex.getLocalizedMessage());
//        Set<String> collect = ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toSet());
//        return new ResponseEntity<>(Collections.singletonMap("message",collect), HttpStatus.BAD_REQUEST);
//    }

}
