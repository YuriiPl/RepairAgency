package com.repairagency.repairagencyspring.controller.user;

import com.repairagency.repairagencyspring.DAO.Exceptions.CommentCreateException;
import com.repairagency.repairagencyspring.DAO.Exceptions.TaskNotFoundException;
import com.repairagency.repairagencyspring.DAO.Exceptions.UserNotFoundException;
import com.repairagency.repairagencyspring.DAO.RepoRedirectService;
import com.repairagency.repairagencyspring.DAO.user.MainPageUserService;
import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.*;
import com.repairagency.repairagencyspring.DAO.BalanceDAO;
import com.repairagency.repairagencyspring.repos.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;

@Log4j2
@Controller
@PreAuthorize("hasAnyAuthority('perm:user','perm:worker','perm:manager')")
@RequestMapping(value = "/account/user")
public class MainPageUserController {

    final LocaleResolver localeResolver;
    private final MainPageUserService mainPageUserService;

    public MainPageUserController(LocaleResolver localeResolver,
                                  MainPageUserService mainPageUserService) {
        this.localeResolver=localeResolver;
        this.mainPageUserService = mainPageUserService;
    }

    @GetMapping("")
    public String userPage(@RequestParam(value = "errorValue",required = false) String value,
                           Model model,
                           Authentication authentication
                           ,@PageableDefault(page = 0, size = 10)
                               @SortDefault.SortDefaults({
                                       @SortDefault(sort = "dateCreate", direction = Sort.Direction.DESC),
                                       @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                               })
                                       Pageable pageable
                            )
    {
        if(value != null){
            model.addAttribute("errorMoney",value);
        }
        model.addAttribute("page",mainPageUserService.getPageOwnerLogin(authentication.getName(),pageable));
        model.addAttribute("url","user");
        model.addAttribute("services",mainPageUserService.getServicesOrderByName());
        model.addAttribute("userMoney",((float)mainPageUserService.getCentsForExistingLogin(authentication.getName()))/100);

        return "account/user/usermainpage";
    }


    @PostMapping("/tasks/add")
    public String addRepairTask(@RequestParam(value = "serviceId") @NotNull Long serviceId,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes,
                                Authentication authentication){
        return mainPageUserService.createAndSaveRepairTask(serviceId,request,redirectAttributes,authentication.getName());
    }



    @PostMapping("/addmoney")
    public String addMoneyPage(@RequestParam(value = "money") @NotNull String money, Authentication authentication, HttpServletRequest request)
    {
        return mainPageUserService.addUserMoney(authentication.getName(),money,localeResolver.resolveLocale(request));
    }

    @PostMapping("/addcomment/{id}")
    public String addCommentPage(@RequestParam(value = "feedBack") String feedBack, @PathVariable(value = "id") Long id, Authentication authentication){
        feedBack=feedBack.substring(0, Math.min(feedBack.length(), 512));
        try {
            mainPageUserService.saveFeedBack(feedBack, id, authentication.getName());
        } catch (Exception ignore){}
        return "redirect:../../user";
    }

    @PostMapping("/getcomment/{id}")
    @ResponseBody
    public String getCommentPage( @PathVariable(value = "id") Long id){
        return mainPageUserService.getFeedbackMessage(id);
    }

    @PostMapping("/trytopay/{id}")
    @ResponseBody
    public HashMap<String, Object> tryToPay(@PathVariable(value = "id") Long taskId, Authentication authentication,HttpServletRequest request){
        return mainPageUserService.attemptToPayForTheService(taskId, authentication.getName(), localeResolver.resolveLocale(request));
    }

}
