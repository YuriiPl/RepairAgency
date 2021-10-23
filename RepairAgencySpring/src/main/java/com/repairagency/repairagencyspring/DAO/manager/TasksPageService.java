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
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
public class TasksPageService {
    final UserRepository userRepository;
    final RepairTaskRepository repairTaskRepository;
    final ResourceBundleMessageSource resourceBundleMessageSource;


    public TasksPageService(UserRepository userRepository, RepairTaskRepository repairTaskRepository, ResourceBundleMessageSource resourceBundleMessageSource) {
        this.userRepository = userRepository;
        this.repairTaskRepository = repairTaskRepository;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
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
        return new PageImpl<>(listDto,pageable,listDto.size());
    }


    public HashMap<String, Object> setPriceForTaskId(String money, Long taskId, Locale locale){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            final Number moneyValue = NumberFormat.getNumberInstance(locale).parse(money);
            if(moneyValue.floatValue()>0) {
                RepairTask repairTask = repairTaskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
                repairTask.setPrice((long) (moneyValue.floatValue()*100));
                repairTask.setPayStatus(PayStatus.WAIT);
                repairTaskRepository.save(repairTask);
                result.put("money", resourceBundleMessageSource.getMessage("number.converter", new Float[]{repairTask.getPrice().floatValue() / 100 },locale));
                result.put("message",resourceBundleMessageSource.getMessage(PayStatus.WAIT.getMessageId(),null,locale));
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

    public HashMap<String, Object> cancelTask(Long taskId, Locale locale){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            RepairTask repairTask = repairTaskRepository.findByIdAndWorkStatusNot(taskId, WorkStatus.DONE).orElseThrow(TaskNotFoundException::new);
            if(repairTask.getPayStatus() != PayStatus.DONE){
                repairTask.setPrice(0L);
            }
            repairTask.setPayStatus(PayStatus.CANCELED);
            repairTaskRepository.save(repairTask);
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
            RepairTask repairTask = repairTaskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
            repairTask.setPayStatus(PayStatus.DONE);
            repairTaskRepository.save(repairTask);
            result.put("message",resourceBundleMessageSource.getMessage(PayStatus.DONE.getMessageId(),null,locale));
        } catch (TaskNotFoundException ignore){
            result.put("status","error");
            result.put("type","wrong");
        }
        return result;
    }

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
