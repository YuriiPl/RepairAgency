package com.repairagency.repairagencyspring.DAO;

import com.repairagency.repairagencyspring.dto.UserDTO;
import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class RegFormService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public RegFormService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public String getRegForm(UserDTO userDTO, BindingResult br, Model model) {
        if(br.hasErrors()){
            Set<String> attributes = br.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toSet());
            model.addAttribute("errors",attributes);
            model.addAttribute("user", userDTO);
            return "api/reg_form";
        } else {
            try {
                userRepo.save(new UserDB(userDTO, passwordEncoder));
            } catch (DataIntegrityViolationException ex){
                model.addAttribute("errors", Collections.singleton("user_email_exist"));
                model.addAttribute("user", userDTO);
                return "api/reg_form";
            }
        }
        return "redirect:/auth/login";
    }
}
