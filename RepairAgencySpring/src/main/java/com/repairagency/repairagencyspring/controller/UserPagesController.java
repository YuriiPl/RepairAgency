package com.repairagency.repairagencyspring.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "/account")
public class UserPagesController {

    @PreAuthorize("hasAnyAuthority('perm:user')")
    @GetMapping("/testpage")
    public String testPage(){
        return "account/testpage";
    }

    @GetMapping("/whoami")
    public String whoAmIPage(){
        return "account/whoami";
    }

    @PreAuthorize("hasAnyAuthority('perm:user','perm:worker','perm:manager')")
    @GetMapping("/user")
    public String userPage(HttpServletRequest request, Model model)
    {
        return "account/user/mainpage";
    }

    @PreAuthorize("hasAuthority('perm:repairer')")
    @GetMapping("/repairer")
    public String masterPage(HttpServletRequest request, Model model)
    {
        return "account/repairer/mainpage";
    }

    @PreAuthorize("hasAuthority('perm:manager')")
    @GetMapping("/manager")
    public String managerPage(HttpServletRequest request, Model model)
    {
        return "account/manager/mainpage";
    }

}