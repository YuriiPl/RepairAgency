package com.repairagency.repairagencyspring.controller.manager;

import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.PayStatus;
import com.repairagency.repairagencyspring.entity.RepairTask;
import com.repairagency.repairagencyspring.repos.FeedBackRepository;
import com.repairagency.repairagencyspring.repos.RepairTaskRepository;
import com.repairagency.repairagencyspring.repos.ServiceNameRepository;
import com.repairagency.repairagencyspring.repos.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;

@Slf4j
@Controller
@PreAuthorize("hasAuthority('perm:manager')")
@RequestMapping(value = "/account/manager/tasks")
public class TasksPageController {

    final LocaleResolver localeResolver;
    final UserAccountRepository userAccountRepository;
    final RepairTaskRepository repairTaskRepository;
    final ServiceNameRepository serviceNameRepository;
    final FeedBackRepository feedBackRepository;
    final ResourceBundleMessageSource resourceBundleMessageSource;


    public TasksPageController(LocaleResolver localeResolver,
                               UserAccountRepository userAccountRepository,
                               RepairTaskRepository repairTaskRepository,
                               ServiceNameRepository serviceNameRepository,
                               FeedBackRepository feedBackRepository, ResourceBundleMessageSource resourceBundleMessageSource) {
        this.userAccountRepository=userAccountRepository;
        this.localeResolver=localeResolver;
        this.repairTaskRepository=repairTaskRepository;
        this.serviceNameRepository=serviceNameRepository;
        this.feedBackRepository = feedBackRepository;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    @GetMapping("/new")
    public String newTasksPage(
            Model model,
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
//                    @SortDefault(sort = "serviceName.name", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "dateCreate", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            })
                    Pageable pageable
    )
    {
        Page<RepairTaskDTO> page = repairTaskRepository.findAllByIdIsNotNull(pageable);
        model.addAttribute("page",page);
        model.addAttribute("url","new");
        return "account/manager/managernewtasks";
    }

    @PostMapping("/setprice/{id}")
    @ResponseBody
    public HashMap<String, Object> setPrice(@RequestParam(value = "money") String money, @PathVariable(value = "id") Long taskId, HttpServletRequest request){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            final Number moneyValue = NumberFormat.getNumberInstance(localeResolver.resolveLocale(request)).parse(money);
            if(moneyValue.floatValue()>0) {
                RepairTask repairTask = repairTaskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
                repairTask.setPrice((long) (moneyValue.floatValue()*100));
                repairTask.setPayStatus(PayStatus.WAIT);
                repairTaskRepository.save(repairTask);
                result.put("money", resourceBundleMessageSource.getMessage("number.converter", new Float[]{repairTask.getPrice().floatValue() / 100 },localeResolver.resolveLocale(request)));
                result.put("message",resourceBundleMessageSource.getMessage(PayStatus.WAIT.getMessageId(),null,localeResolver.resolveLocale(request)));
            } else{
                result.put("status","error");
                result.put("type","negative");
            }
        } catch (ParseException | TaskNotFoundException ignore){
            result.put("status","error");
            result.put("type","wrong");
        }
        return result;
    }

    @PostMapping("/cancel/{id}")
    @ResponseBody
    public HashMap<String, Object> setCanceled(@PathVariable(value = "id") Long taskId, HttpServletRequest request){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            RepairTask repairTask = repairTaskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
            repairTask.setPayStatus(PayStatus.CANCELED);
//            repairTask.setPrice(0L);
            repairTaskRepository.save(repairTask);
            result.put("message",resourceBundleMessageSource.getMessage(PayStatus.CANCELED.getMessageId(),null,localeResolver.resolveLocale(request)));
        } catch (TaskNotFoundException ignore){
            result.put("status","error");
            result.put("type","wrong");
        }
        return result;
    }

    @PostMapping("/acceptpay/{id}")
    @ResponseBody
    public HashMap<String, Object> setAcceptPay(@PathVariable(value = "id") Long taskId, HttpServletRequest request){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            RepairTask repairTask = repairTaskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
            repairTask.setPayStatus(PayStatus.DONE);
            repairTaskRepository.save(repairTask);
            result.put("message",resourceBundleMessageSource.getMessage(PayStatus.DONE.getMessageId(),null,localeResolver.resolveLocale(request)));
        } catch (TaskNotFoundException ignore){
            result.put("status","error");
            result.put("type","wrong");
        }
        return result;
    }

}



