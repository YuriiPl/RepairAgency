package com.repairagency.repairagencyspring.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


@Controller("/account")
public class UserPagesController {
    @PreAuthorize("hasAnyAuthority('perm:user','perm:worker','perm:admin')")
    @GetMapping("/user")
    public String userPage(HttpServletRequest request, Model model)
    {
        return "index";
    }

    @PreAuthorize("hasAuthority('perm:worker')")
    @GetMapping("/master")
    public String masterPage(HttpServletRequest request, Model model)
    {
        return "index";
    }

    @PreAuthorize("hasAuthority('perm:admin')")
    @GetMapping("/manager")
    public String managerPage(HttpServletRequest request, Model model)
    {
        return "index";
    }

}