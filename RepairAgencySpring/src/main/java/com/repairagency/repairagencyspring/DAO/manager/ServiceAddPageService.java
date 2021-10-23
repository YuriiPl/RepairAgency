package com.repairagency.repairagencyspring.DAO.manager;

import com.repairagency.repairagencyspring.DAO.RepoRedirectService;
import com.repairagency.repairagencyspring.entity.ServiceName;
import com.repairagency.repairagencyspring.repos.ServiceNameRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ServiceAddPageService {

    private final ServiceNameRepository serviceNameRepository;

    public ServiceAddPageService(ServiceNameRepository serviceNameRepository) {
        this.serviceNameRepository = serviceNameRepository;
    }

    public String save(ServiceName object, String redirectTo, BindingResult br, HttpServletRequest request, RedirectAttributes redirectAttributes){
        return RepoRedirectService.save(serviceNameRepository, object, redirectTo, request, redirectAttributes);
    }

    public String removeById(Long id, String redirectTo, HttpServletRequest request, RedirectAttributes redirectAttributes){
        return RepoRedirectService.removeById(serviceNameRepository,id,redirectTo,request,redirectAttributes);
    }

    public  Page<ServiceName> getAllServicesOnPage(Pageable pageable){
        return serviceNameRepository.findAll(pageable);
    }
}
