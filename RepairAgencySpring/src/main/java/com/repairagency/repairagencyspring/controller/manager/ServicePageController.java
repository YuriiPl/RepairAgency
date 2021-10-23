package com.repairagency.repairagencyspring.controller.manager;

import com.repairagency.repairagencyspring.dto.ServiceDTO;
import com.repairagency.repairagencyspring.entity.ServiceName;
import com.repairagency.repairagencyspring.DAO.RepoRedirectService;
import com.repairagency.repairagencyspring.repos.ServiceNameRepository;
import lombok.extern.log4j.Log4j2;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Log4j2
@Controller
@PreAuthorize("hasAuthority('perm:manager')")
@RequestMapping(value = "/account/manager/service")
public class ServicePageController {

    ServiceNameRepository serviceNameRepository;

    public ServicePageController(ServiceNameRepository serviceNameRepository) {
        this.serviceNameRepository = serviceNameRepository;
    }


    @GetMapping("")
    public String servicePage( @PageableDefault(page = 0, size = 10)
                                   @SortDefault.SortDefaults({
                                           @SortDefault(sort = "name", direction = Sort.Direction.ASC),
                                           @SortDefault(sort = "id", direction = Sort.Direction.DESC)
                                   })
                                           Pageable pageable,Model model)
    {
        Page<ServiceName> page = serviceNameRepository.findAll(pageable);
        model.addAttribute("page",page);
        model.addAttribute("url","service");
        return "/account/manager/service";
    }

    @PostMapping("/add")
    public String managerPageAddService(@Valid ServiceDTO serviceDTO, BindingResult br, HttpServletRequest request, RedirectAttributes redirectAttributes)
    {
        return RepoRedirectService.save(serviceNameRepository,new ServiceName(serviceDTO),"../service",br, request,redirectAttributes);
    }

    @GetMapping("/rm/{id}")
    public String managerPageRmService(@PathVariable("id") Long id, HttpServletRequest request, RedirectAttributes redirectAttributes)
    {
        return RepoRedirectService.removeById(serviceNameRepository, id,"../../service",request,redirectAttributes);
    }


}
