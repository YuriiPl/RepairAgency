package com.repairagency.repairagencyspring.controller.manager;

import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.PayStatus;
import com.repairagency.repairagencyspring.entity.RepairTask;
import com.repairagency.repairagencyspring.repos.FeedBackRepository;
import com.repairagency.repairagencyspring.repos.RepairTaskRepository;
import com.repairagency.repairagencyspring.repos.ServiceNameRepository;
import com.repairagency.repairagencyspring.repos.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
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
public class NewTasksPageController {

    final LocaleResolver localeResolver;
    final UserAccountRepository userAccountRepository;
    final RepairTaskRepository repairTaskRepository;
    final ServiceNameRepository serviceNameRepository;
    final FeedBackRepository feedBackRepository;


    public NewTasksPageController(LocaleResolver localeResolver,
                                  UserAccountRepository userAccountRepository,
                                  RepairTaskRepository repairTaskRepository,
                                  ServiceNameRepository serviceNameRepository,
                                  FeedBackRepository feedBackRepository) {
        this.userAccountRepository=userAccountRepository;
        this.localeResolver=localeResolver;
        this.repairTaskRepository=repairTaskRepository;
        this.serviceNameRepository=serviceNameRepository;
        this.feedBackRepository = feedBackRepository;
    }

    @GetMapping("/new")
    public String newTasksPage(
            Model model,
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "serviceName.name", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            })
                    Pageable pageable
    )
    {
        Page<RepairTaskDTO> page = repairTaskRepository.findAllByPayStatus(PayStatus.FREE,pageable);
        model.addAttribute("page",page);
        model.addAttribute("url","new");
        return "account/manager/managernewtasks";
    }

    @PostMapping("/setprice/{id}")
    @ResponseBody
    public HashMap<String, Object> addCommentPage(@RequestParam(value = "money") String money, @PathVariable(value = "id") Long taskId, HttpServletRequest request){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            final Number moneyValue = NumberFormat.getNumberInstance(localeResolver.resolveLocale(request)).parse(money);
            if(moneyValue.floatValue()>0) {
                RepairTask repairTask = repairTaskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
                repairTask.setPrice((long) (moneyValue.floatValue()*100));
                repairTask.setPayStatus(PayStatus.WAIT);
                log.warn(String.valueOf(moneyValue.floatValue()));
                repairTaskRepository.save(repairTask);
                result.put("money",repairTask.getPrice().floatValue()/100+"$");
            } else{
                result.put("status","error");
                result.put("type","negative");
            }
        } catch (ParseException ignore){
            result.put("status","error");
            result.put("type","wrong");
        }
        return result;
    }

}



