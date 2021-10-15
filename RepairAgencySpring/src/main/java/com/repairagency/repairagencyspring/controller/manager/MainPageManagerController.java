package com.repairagency.repairagencyspring.controller.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@PreAuthorize("hasAuthority('perm:manager')")
@RequestMapping(value = "/account/manager")
public class MainPageManagerController {

    @GetMapping("")
    public String managerPage()
    {

        return "redirect:/account/manager/tasks/new";
    }

}
