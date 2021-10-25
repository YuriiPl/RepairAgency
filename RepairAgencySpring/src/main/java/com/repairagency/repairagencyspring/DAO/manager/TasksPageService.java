package com.repairagency.repairagencyspring.DAO.manager;

import com.repairagency.repairagencyspring.dto.FilterDataDTO;
import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.PayStatus;
import com.repairagency.repairagencyspring.entity.RepairTask;
import com.repairagency.repairagencyspring.entity.UserDB;
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

    private HashMap<String, Object> getResult(ProcessFunction func, ProcessObject msgFunc, Long taskId){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            Object obj=func.doProcess();
            result.put("message",msgFunc.getMessage(obj,result));
        } catch (Exception ignore){
            result.put("status","error");
            result.put("type","wrong");
        }
        return result;
    }

    @FunctionalInterface
    private interface ProcessFunction{
        Object doProcess() throws Exception;
    }
    @FunctionalInterface
    private interface ProcessObject {
        String getMessage(Object obj,HashMap<String, Object> result);
    }

    private String getRepairerNameFromTask(RepairTask repairTask){
        return repairTask.getRepairer()==null?"":repairTask.getRepairer().getName();
    }

    private String getBundleMessage(String messageBundleId, Locale locale){
        return resourceBundleMessageSource.getMessage(messageBundleId,null,locale);
    }

    public HashMap<String, Object> acceptPay(Long taskId, Locale locale){
        return getResult(()-> tasksPageServiceHelper.acceptPay(taskId),(o, r)->getBundleMessage(PayStatus.DONE.getMessageId(),locale),taskId);
    }

    public HashMap<String, Object> cancelTask(Long taskId, Locale locale){
        return getResult(()->tasksPageServiceHelper.cancelTask(taskId),(o, r)->getBundleMessage(PayStatus.CANCELED.getMessageId(),locale),taskId);
    }

    public HashMap<String, Object> setRepairer(Long taskId, Long userId){
        return getResult(()->tasksPageServiceHelper.setRepairer(taskId,userId),(o, r)->getRepairerNameFromTask((RepairTask)o),taskId);
    }

    public HashMap<String, Object> setPriceForTaskId(String money, Long taskId, Locale locale){
        return getResult(()->{
                            long cents = (long) (NumberFormat.getNumberInstance(locale).parse(money).floatValue()*100);
                            return tasksPageServiceHelper.setPriceForTask(cents,taskId);
                        },
                (o, r)->{
                        r.put("money", resourceBundleMessageSource.getMessage("number.converter", new Float[]{(float)(((RepairTask)o).getPrice())/100 },locale));
                        return getBundleMessage(PayStatus.WAIT.getMessageId(),locale);
                    },
                taskId);
    }






}

