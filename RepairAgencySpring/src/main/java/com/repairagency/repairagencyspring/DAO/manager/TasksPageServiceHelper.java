package com.repairagency.repairagencyspring.DAO.manager;

import com.repairagency.repairagencyspring.DAO.Exceptions.TaskNotFoundException;
import com.repairagency.repairagencyspring.entity.PayStatus;
import com.repairagency.repairagencyspring.entity.RepairTask;
import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.entity.WorkStatus;
import com.repairagency.repairagencyspring.repos.RepairTaskRepository;
import com.repairagency.repairagencyspring.repos.UserRepository;
import com.repairagency.repairagencyspring.security.Role;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

@Validated
@Service
@Log4j2
public class TasksPageServiceHelper {

    private final RepairTaskRepository repairTaskRepository;
    private final UserRepository userRepository;

    public TasksPageServiceHelper(RepairTaskRepository repairTaskRepository, UserRepository userRepository) {
        this.repairTaskRepository = repairTaskRepository;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public RepairTask setPriceForTask(@Min(1) long cents, Long taskId) throws TaskNotFoundException {
        RepairTask repairTask = repairTaskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        repairTask.setPrice(cents);
        repairTask.setPayStatus(PayStatus.WAIT);
        return repairTask;
    }

    @Transactional(rollbackFor = Exception.class)
    public RepairTask cancelTask(Long taskId) throws TaskNotFoundException {
        RepairTask repairTask = repairTaskRepository.findByIdAndWorkStatusNot(taskId, WorkStatus.DONE).orElseThrow(TaskNotFoundException::new);
        if(repairTask.getPayStatus() != PayStatus.DONE){
            repairTask.setPrice(0L);
        }
        repairTask.setPayStatus(PayStatus.CANCELED);
        return repairTask;
    }

    @Transactional(rollbackFor = Exception.class)
    public RepairTask acceptPay(Long taskId) throws TaskNotFoundException {
        RepairTask repairTask = repairTaskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        repairTask.setPayStatus(PayStatus.DONE);
        return repairTask;
    }

    @Transactional(rollbackFor = Exception.class)
    public RepairTask setRepairer(Long taskId, Long userId) throws TaskNotFoundException {
        RepairTask repairTask = repairTaskRepository.findByIdAndWorkStatus(taskId,WorkStatus.FREE).orElseThrow(TaskNotFoundException::new);
        UserDB repairer = userRepository.findByIdAndUserRole(userId, Role.REPAIRER).orElse(null);
        repairTask.setRepairer(repairer);
        repairTask.setWorkStatus(WorkStatus.FREE);
        return repairTask;
    }
}
