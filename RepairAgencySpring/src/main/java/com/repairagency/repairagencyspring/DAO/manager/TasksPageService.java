package com.repairagency.repairagencyspring.DAO.manager;

import com.repairagency.repairagencyspring.DAO.Exceptions.TaskNotFoundException;
import com.repairagency.repairagencyspring.DAO.Exceptions.UserNotFoundException;
import com.repairagency.repairagencyspring.dto.FilterDataDTO;
import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.PayStatus;
import com.repairagency.repairagencyspring.entity.RepairTask;
import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.entity.WorkStatus;
import com.repairagency.repairagencyspring.repos.RepairTaskRepository;
import com.repairagency.repairagencyspring.repos.UserRepository;
import com.repairagency.repairagencyspring.security.Role;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;


@Validated
@Service
@Log4j2
public class TasksPageService {
    final UserRepository userRepository;
    final RepairTaskRepository repairTaskRepository;
    final ResourceBundleMessageSource resourceBundleMessageSource;
    final TasksPageServiceHelper tasksPageServiceHelper;

    public TasksPageService(UserRepository userRepository,
                            RepairTaskRepository repairTaskRepository,
                            ResourceBundleMessageSource resourceBundleMessageSource,
                            TasksPageServiceHelper tasksPageServiceHelper) {
        this.userRepository = userRepository;
        this.repairTaskRepository = repairTaskRepository;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
        this.tasksPageServiceHelper = tasksPageServiceHelper;
    }


    public List<UserDB> getAllRepairers(){
        return userRepository.findAllByUserRoleOrderByNameAsc(Role.REPAIRER);
    }


    public Page <RepairTaskDTO> getPageFilterDto(Pageable pageable,  FilterDataDTO filterData){
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
        return new PageImpl<>(listDto,pageable,page.getTotalElements());
    }

    public HashMap<String, Object> setPriceForTaskId(String money, Long taskId, Locale locale){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            final long cents = (long) NumberFormat.getNumberInstance(locale).parse(money).floatValue()*100;
            tasksPageServiceHelper.setPriceForTask(cents,taskId);
            result.put("money", resourceBundleMessageSource.getMessage("number.converter", new Float[]{(float)(cents)/100 },locale));
            result.put("message",resourceBundleMessageSource.getMessage(PayStatus.WAIT.getMessageId(),null,locale));
        } catch (TaskNotFoundException ignore){
            result.put("status","error");
            result.put("type","negative");
        } catch (Exception ignore) {
            result.put("status", "error");
            result.put("type", "wrong");
        }
        return result;
    }

    public HashMap<String, Object> cancelTask(Long taskId, Locale locale){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            tasksPageServiceHelper.cancelTask(taskId);
            result.put("message",resourceBundleMessageSource.getMessage(PayStatus.CANCELED.getMessageId(),null,locale));
        } catch (TaskNotFoundException ignore){
            result.put("status","error");
            result.put("type","wrong");
        }
        return result;
    }

    public HashMap<String, Object> acceptPay(Long taskId, Locale locale){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            tasksPageServiceHelper.acceptPay(taskId);
            result.put("message",resourceBundleMessageSource.getMessage(PayStatus.DONE.getMessageId(),null,locale));
        } catch (TaskNotFoundException ignore){
            result.put("status","error");
            result.put("type","wrong");
        }
        return result;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public HashMap<String, Object> setRepairer(Long taskId, Long userId){
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
