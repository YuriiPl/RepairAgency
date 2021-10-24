package com.repairagency.repairagencyspring.controller;


import com.repairagency.repairagencyspring.DAO.RegFormService;
import com.repairagency.repairagencyspring.dto.UserDTO;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j2
@Controller
@RequestMapping(value = "/api")
public class RegFormController {

    private final RegFormService regFormService;

    @Autowired
    public RegFormController(RegFormService regFormService) {
        this.regFormService = regFormService;
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registrationFormController(@Valid UserDTO userDTO, BindingResult br, Model model){
        return regFormService.getRegForm(userDTO, br, model);
    }

    @GetMapping("/register")
    public String regForm(){
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
