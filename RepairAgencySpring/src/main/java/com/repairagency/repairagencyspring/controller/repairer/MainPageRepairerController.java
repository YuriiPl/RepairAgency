package com.repairagency.repairagencyspring.controller.repairer;

import com.repairagency.repairagencyspring.controller.manager.TaskNotFoundException;
import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.RepairTask;
import com.repairagency.repairagencyspring.entity.WorkStatus;
import com.repairagency.repairagencyspring.repos.RepairTaskRepository;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;


@Log4j2
@Controller
@RequestMapping(value = "/account/repairer")
@PreAuthorize("hasAuthority('perm:repairer')")
public class MainPageRepairerController {

    final RepairTaskRepository repairTaskRepository;
    final UserRepository userRepository;
    final LocaleResolver localeResolver;
    final ResourceBundleMessageSource resourceBundleMessageSource;

    public MainPageRepairerController(RepairTaskRepository repairTaskRepository,
                                      UserRepository userRepository, LocaleResolver localeResolver,
                                      ResourceBundleMessageSource resourceBundleMessageSource) {
        this.repairTaskRepository = repairTaskRepository;
        this.userRepository = userRepository;
        this.localeResolver = localeResolver;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    @GetMapping("")
    public String masterPage(Authentication authentication, Model model, Pageable pageable)
    {
        Page<RepairTaskDTO> page = repairTaskRepository.findAllByRepairer_Login(authentication.getName(),pageable);
        model.addAttribute("page",page);
        model.addAttribute("url","repairer");
        return "account/repairer/repairermainpage";
    }

    @PostMapping("/setstatus/{id}/{stat}")
    @ResponseBody
    public HashMap<String, Object> setAcceptPay(@PathVariable(value = "id") Long taskId, @PathVariable(value = "stat") WorkStatus stat, HttpServletRequest request){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        result.put("hide","false");
        try {
            RepairTask repairTask = repairTaskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
            repairTask.setWorkStatus(stat);
            if(stat==WorkStatus.DONE){
                repairTask.setDateFinish(LocalDateTime.now());
                result.put("hide","true");
            }
            repairTaskRepository.save(repairTask);
            result.put("message",resourceBundleMessageSource.getMessage(stat.getMessageId(),null,localeResolver.resolveLocale(request)));
            result.put("nextstat","DONE");
        } catch (TaskNotFoundException ignore){
            result.put("status","error");
            result.put("type","wrong");
        }
        return result;
    }
}
