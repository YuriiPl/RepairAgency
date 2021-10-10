package com.repairagency.repairagencyspring.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class RepoRedirectService {
//    public static <T,ID> ModelAndView save(CrudRepository<T, ID> repo, Object object, String redirectTo, BindingResult br, HttpServletRequest request, RedirectAttributes redirectAttributes){
//        ModelAndView modelAndView = new ModelAndView();
//        if(!br.hasErrors()){
//            try {
//                repo.save((T)object);
//            } catch (Exception ex){
//                redirectAttributes.addFlashAttribute("saveDbError",ex.getMessage());
//            }
//        } else {
//            Set<String> attributes = br.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toSet());
//            log.warn(String.valueOf(attributes));
//            redirectAttributes.addFlashAttribute("validateError",attributes);
//        }
//        final String parameters = request.getParameterMap().entrySet().stream()
//                .map(e -> e.getKey() + "=" + String.join("", e.getValue()))
//                .collect(Collectors.joining("&"));
//        RedirectView redirectView = new RedirectView(redirectTo+"?"+parameters);
//        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
//        modelAndView.setView(redirectView);
//        return modelAndView;
//    }

    public static <T,ID> String save(CrudRepository<T, ID> repo, Object object, String redirectTo, BindingResult br, HttpServletRequest request, RedirectAttributes redirectAttributes){
        if(!br.hasErrors()){
            try {
                repo.save((T)object);
            } catch (Exception ex){
                redirectAttributes.addFlashAttribute("saveDbError",ex.getMessage());
            }
        } else {
            Set<String> attributes = br.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toSet());
            log.warn(String.valueOf(attributes));
            redirectAttributes.addFlashAttribute("validateError",attributes);
        }
        final String parameters = request.getParameterMap().entrySet().stream()
                .filter(e-> e.getKey().equals("page")||e.getKey().equals("size"))
                .map(e -> e.getKey() + "=" + String.join("", e.getValue()))
                .collect(Collectors.joining("&"));
        return "redirect:"+redirectTo+"?"+parameters;
    }

    public static <T,ID> ModelAndView removeById(CrudRepository<T, ID> repo, ID id, String redirectTo, ModelAndView modelAndView, RedirectAttributes redirectAttributes){
        RedirectView redirectView = new RedirectView(redirectTo);
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        modelAndView.setView(redirectView);
        try {
            repo.deleteById(id);
        } catch (Exception ex){
            redirectAttributes.addFlashAttribute("removeDbError",ex.getMessage());
        }
        return modelAndView;
    }
}
