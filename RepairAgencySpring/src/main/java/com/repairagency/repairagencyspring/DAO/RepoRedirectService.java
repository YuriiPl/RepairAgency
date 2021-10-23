package com.repairagency.repairagencyspring.DAO;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
public class RepoRedirectService {

    public static String parametersFromHttpRequest(HttpServletRequest request){
        return request.getParameterMap().entrySet().stream()
                .filter(e-> e.getKey().equals("page")||e.getKey().equals("size"))
                .map(e -> e.getKey() + "=" + String.join("", e.getValue()))
                .collect(Collectors.joining("&"));
    }

    public static <T,ID> String save(CrudRepository<T, ID> repo, T object, String redirectTo, BindingResult br, HttpServletRequest request, RedirectAttributes redirectAttributes){
        if(!br.hasErrors()){
            try {
                repo.save(object);
            } catch (Exception ex){
                redirectAttributes.addFlashAttribute("saveDbError",ex.getMessage());
            }
        } else {
            Set<String> attributes = br.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toSet());
            redirectAttributes.addFlashAttribute("validateError",attributes);
        }
        return "redirect:"+redirectTo+"?"+parametersFromHttpRequest(request);
    }

    public static <T,ID> String save(CrudRepository<T, ID> repo, T object, String redirectTo, HttpServletRequest request, RedirectAttributes redirectAttributes){
        try {
            repo.save(object);
        } catch (Exception ex){
            redirectAttributes.addFlashAttribute("saveDbError",ex.getMessage());
        }
        return "redirect:"+redirectTo+"?"+parametersFromHttpRequest(request);
    }

    public static <T,ID> String removeById(CrudRepository<T, ID> repo, ID id, String redirectTo, HttpServletRequest request, RedirectAttributes redirectAttributes){
        try {
            repo.deleteById(id);
        } catch (Exception ex){
            redirectAttributes.addFlashAttribute("removeDbError",ex.getMessage());
        }
        return "redirect:"+redirectTo+"?"+parametersFromHttpRequest(request);
    }
}