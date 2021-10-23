package com.repairagency.repairagencyspring.DAO.repairer;

import com.repairagency.repairagencyspring.DAO.Exceptions.TaskNotFoundException;
import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.RepairTask;
import com.repairagency.repairagencyspring.entity.WorkStatus;
import com.repairagency.repairagencyspring.repos.RepairTaskRepository;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;

@Service
public class MainPageRepairerService {

    private final RepairTaskRepository repairTaskRepository;
    private final ResourceBundleMessageSource resourceBundleMessageSource;

    public MainPageRepairerService(RepairTaskRepository repairTaskRepository,
                                   ResourceBundleMessageSource resourceBundleMessageSource) {
        this.repairTaskRepository = repairTaskRepository;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    public Page<RepairTaskDTO> getAllTaskForRepairer(String login, Pageable pageable){
        return repairTaskRepository.findAllByRepairer_Login(login,pageable);
    }

    public HashMap<String, Object> setAcceptPay(Long taskId, WorkStatus stat, Locale locale){
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
            result.put("message",resourceBundleMessageSource.getMessage(stat.getMessageId(),null,locale));
            result.put("nextstat","DONE");
        } catch (TaskNotFoundException ignore){
            result.put("status","error");
            result.put("type","wrong");
        }
        return result;
    }
}
