package com.repairagency.repairagencyspring.controller;

import com.repairagency.repairagencyspring.entity.Service;
import com.repairagency.repairagencyspring.repos.ServiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletRequest;


@Controller
@PreAuthorize("hasAuthority('perm:manager')")
@RequestMapping(value = "/account/")
public class ManagerPagesController {

    ServiceRepository serviceRepository;

    public ManagerPagesController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @GetMapping("manager")
    public String managerPage(HttpServletRequest request, Model model)
    {
        Iterable<Service> all = serviceRepository.findAll();
        model.addAttribute("services",all);
        return "account/manager/mainpage";
    }

    @PostMapping("managerAddService")
    public ModelAndView managerPageAddService(@RequestParam(name = "serviceName", required = true) String serviceName, RedirectAttributes redirectAttributes)
    {
        ModelAndView modelAndView = new ModelAndView();
        try {
            serviceRepository.save(new Service(0L, serviceName));
        } catch (Exception ex){
            redirectAttributes.addFlashAttribute("errorServiceExists",true);
        }
        RedirectView redirectView = new RedirectView("manager");
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        modelAndView.setView(redirectView);
        return modelAndView;
    }

//    @RequestMapping(value = "/secondPage", method = RequestMethod.POST)
//    public ModelAndView checkUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//
//        ModelAndView modelAndView = new ModelAndView();
//        if (!bindingResult.hasErrors()) {
//            RedirectView redirectView = new RedirectView("mainpage");
//            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
//            modelAndView.setView(redirectView);
//            redirectAttributes.addFlashAttribute(object));
//        } else {
//            modelAndView.setViewName("indexPage");
//        }
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/mainpage", method = RequestMethod.GET)
//    public String goMainPage(HttpServletRequest request) {
//
//        Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);
//        if (map != null) {
//            logger.info("It is redirect!");
//        } else {
//            logger.info("It is update!");
//        }
//        return "main";
//    }
}
