package com.repairagency.repairagencyspring.controller;


import com.repairagency.repairagencyspring.dto.User;
import com.repairagency.repairagencyspring.entity.UserDto;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void registrationFormController(@Valid User user){
        userRepo.save(new UserDto(user, passwordEncoder));
        log.info("{}", user);
    }

    @GetMapping("/register")
    public String regForm(Model model){
        return "api/reg_form";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,Set<String>>> handleConstraintViolationException(DataIntegrityViolationException ex) {
        log.warn("{} {}", ex.getClass(), ex.getLocalizedMessage());
        return new ResponseEntity<>(Collections.singletonMap("message",Collections.singleton("user_email_exist")), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String,Set<String>>> handleRuntimeException(ConstraintViolationException ex) {
        log.warn("{} {}", ex.getClass(), ex.getLocalizedMessage());
        Set<String> collect = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        return new ResponseEntity<>(Collections.singletonMap("message",collect), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String,Set<String>>> handleBindExceptionException(BindException ex) {
        log.warn("{} {}", ex.getClass(), ex.getLocalizedMessage());
        Set<String> collect = ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toSet());
        return new ResponseEntity<>(Collections.singletonMap("message",collect), HttpStatus.BAD_REQUEST);
    }

}
