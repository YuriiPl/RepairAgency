package com.repairagency.repairagencyspring.controller.repairer;

import com.repairagency.repairagencyspring.DAO.repairer.MainPageRepairerService;
import com.repairagency.repairagencyspring.entity.WorkStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;


@Log4j2
@Controller
@RequestMapping(value = "/account/repairer")
@PreAuthorize("hasAuthority('perm:repairer')")
public class MainPageRepairerController {

    final LocaleResolver localeResolver;
    private final MainPageRepairerService mainPageRepairerService;

    public MainPageRepairerController(LocaleResolver localeResolver,
                                      MainPageRepairerService mainPageRepairerService) {
        this.localeResolver = localeResolver;
        this.mainPageRepairerService = mainPageRepairerService;
    }

    @GetMapping("")
    public String masterPage(Authentication authentication, Model model, Pageable pageable)
    {
        model.addAttribute("page",mainPageRepairerService.getAllTaskForRepairer(authentication.getName(),pageable));
        model.addAttribute("url","repairer");
        return "account/repairer/repairermainpage";
    }

    @PostMapping("/setstatus/{id}/{stat}")
    @ResponseBody
    public HashMap<String, Object> setAcceptPay(@PathVariable(value = "id") @NotNull Long taskId, @PathVariable(value = "stat") @NotNull WorkStatus stat, HttpServletRequest request){
        return mainPageRepairerService.setAcceptPay(taskId,stat,localeResolver.resolveLocale(request));
    }
}
