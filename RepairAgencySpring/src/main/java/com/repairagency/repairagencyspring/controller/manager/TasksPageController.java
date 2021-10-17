package com.repairagency.repairagencyspring.controller.manager;

import com.repairagency.repairagencyspring.dto.FilterDataDTO;
import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.PayStatus;
import com.repairagency.repairagencyspring.entity.RepairTask;
import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.entity.WorkStatus;
import com.repairagency.repairagencyspring.repos.*;
import com.repairagency.repairagencyspring.security.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.*;

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
    final UserRepository userRepository;


    public TasksPageController(LocaleResolver localeResolver,
                               UserAccountRepository userAccountRepository,
                               RepairTaskRepository repairTaskRepository,
                               ServiceNameRepository serviceNameRepository,
                               FeedBackRepository feedBackRepository, ResourceBundleMessageSource resourceBundleMessageSource, UserRepository userRepository) {
        this.userAccountRepository=userAccountRepository;
        this.localeResolver=localeResolver;
        this.repairTaskRepository=repairTaskRepository;
        this.serviceNameRepository=serviceNameRepository;
        this.feedBackRepository = feedBackRepository;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
        this.userRepository = userRepository;
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
                Pageable pageable,
                FilterDataDTO filterData
            )
    {
//        log.warn(filter.toString());
//        RepairTask findParam=new RepairTask();
//        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("owner.name", "serviceName");;
//        if(filter.getUserName() != null && filter.getUserName().length()>0) {
//            matcher=matcher.withMatcher("repairer.name", contains().ignoreCase() );
//            findParam.setRepairer(new UserDB());
//            findParam.getRepairer().setName(filter.getUserName());
//        }
//        if(filter.getPayStatus() != null){
//            matcher.withMatcher("payStatus", exact() );
//            findParam.setPayStatus(filter.getPayStatus());
//        }
//        if(filter.getTaskStatus() != null){
//            matcher.withMatcher("workStatus", exact() );
//            findParam.setWorkStatus(filter.getTaskStatus());
//        }

        RepairTask filterTask = new RepairTask();
        filterTask.setWorkStatus(filterData.getWorkStatus());
        filterTask.setPayStatus(filterData.getPayStatus());

        ExampleMatcher matcher = ExampleMatcher.matching();
        if(filterData.getRepairerName() != null && filterData.getRepairerName().length()>0) {
            matcher=matcher.withMatcher("repairer.name", contains().ignoreCase());
            UserDB repairer= new UserDB();
            repairer.setName(filterData.getRepairerName());
            filterTask.setRepairer(repairer);
        } else {
            matcher=matcher.withIgnorePaths("repairer");
        }
        Example<RepairTask> example = Example.of(filterTask, matcher);
        Page<RepairTask> page = repairTaskRepository.findAll(example, pageable);
        List<RepairTaskDTO> listDto = page.stream().map(RepairTaskDTO::new).collect(Collectors.toList());
        Page <RepairTaskDTO> pageDto = new PageImpl<RepairTaskDTO>(listDto,pageable,listDto.size());

        List<UserDB> repairers = userRepository.findAllByUserRoleOrderByNameAsc(Role.REPAIRER);
        model.addAttribute("page",pageDto);
        model.addAttribute("repairers",repairers);
        model.addAttribute("url","new");
        model.addAttribute("filter",filterData);
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
            RepairTask repairTask = repairTaskRepository.findByIdAndWorkStatusNot(taskId,WorkStatus.DONE).orElseThrow(TaskNotFoundException::new);
            if(repairTask.getPayStatus() != PayStatus.DONE){
                repairTask.setPrice(0L);
            }
            repairTask.setPayStatus(PayStatus.CANCELED);
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

    @PostMapping("/setrepairer/{id}/{idrep}")
    @ResponseBody
    public HashMap<String, Object> setRepairer(@PathVariable(value = "id") Long taskId, @PathVariable(value = "idrep") Long userId, HttpServletRequest request){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            RepairTask repairTask = repairTaskRepository.findByIdAndWorkStatus(taskId,WorkStatus.FREE).orElseThrow(TaskNotFoundException::new);
            if(userId==-1){
                repairTask.setRepairer(null);
                result.put("message","");
            } else {
                UserDB repairer = userRepository.findByIdAndUserRole(userId, Role.REPAIRER).orElseThrow(UserNotFoundException::new);
                repairTask.setRepairer(repairer);
                result.put("message",repairer.getName());
            }
            repairTask.setWorkStatus(WorkStatus.FREE);
            repairTaskRepository.save(repairTask);
        } catch (TaskNotFoundException | UserNotFoundException ignore){
            result.put("status","error");
            result.put("type","wrong");
        }
        return result;
    }

}



