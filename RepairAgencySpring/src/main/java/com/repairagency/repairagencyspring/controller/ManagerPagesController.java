package com.repairagency.repairagencyspring.controller;

import com.repairagency.repairagencyspring.dto.ServiceDTO;
import com.repairagency.repairagencyspring.entity.Service;
import com.repairagency.repairagencyspring.model.RepoRedirectService;
import com.repairagency.repairagencyspring.repos.ServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Slf4j
@Controller
@PreAuthorize("hasAuthority('perm:manager')")
@RequestMapping(value = "/account/")
public class ManagerPagesController {

    ServiceRepository serviceRepository;

    public ManagerPagesController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }


    @GetMapping("manager")
    public String managerPage( @PageableDefault(page = 0, size = 10)
                                   @SortDefault.SortDefaults({
                                           @SortDefault(sort = "name", direction = Sort.Direction.ASC),
                                           @SortDefault(sort = "id", direction = Sort.Direction.DESC)
                                   })
                                           Pageable pageable,Model model)
    {
        Page<Service> page = serviceRepository.findAll(pageable);
        model.addAttribute("page",page);
        model.addAttribute("url","manager");
        return "account/manager/mainpage";
    }

    @PostMapping("managerAddService")
    public String managerPageAddService(@Valid ServiceDTO ServiceDTO, HttpServletRequest request, BindingResult br, RedirectAttributes redirectAttributes)
    {
        return RepoRedirectService.save(serviceRepository,new Service(ServiceDTO),"manager",br, request,redirectAttributes);
    }

    @GetMapping("service/{id}/rm")
    public String managerPageRmService(@PathVariable("id") Long id, HttpServletRequest request, ModelAndView modelAndView, RedirectAttributes redirectAttributes)
    {
        try {
            serviceRepository.deleteById(id);
        } catch (Exception ex){
            redirectAttributes.addFlashAttribute("removeDbError",ex.getMessage());
        }
        final String collect = request.getParameterMap().entrySet().stream()
                .filter(e-> e.getKey().equals("page")||e.getKey().equals("size"))
                .map(e -> e.getKey() + "=" + String.join("", e.getValue()))
                .collect(Collectors.joining("&"));
        return "redirect:/account/manager?"+collect;

//        return RepoRedirectService.removeById(serviceRepository,id,"../../manager",modelAndView,redirectAttributes);
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
