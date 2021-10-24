package com.repairagency.repairagencyspring.DAO.manager;

import com.repairagency.repairagencyspring.DAO.Exceptions.TaskNotFoundException;
import com.repairagency.repairagencyspring.entity.PayStatus;
import com.repairagency.repairagencyspring.entity.RepairTask;
import com.repairagency.repairagencyspring.entity.WorkStatus;
import com.repairagency.repairagencyspring.repos.RepairTaskRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

@Validated
@Service
@Log4j2
public class TasksPageServiceHelper {

    final RepairTaskRepository repairTaskRepository;

    public TasksPageServiceHelper(RepairTaskRepository repairTaskRepository) {
        this.repairTaskRepository = repairTaskRepository;
    }

    @Transactional
    public void setPriceForTask(@Min(1) long cents, Long taskId) throws TaskNotFoundException {
        RepairTask repairTask = repairTaskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        repairTask.setPrice(cents);
        repairTask.setPayStatus(PayStatus.WAIT);
    }

    @Transactional
    public void cancelTask(Long taskId) throws TaskNotFoundException {
        RepairTask repairTask = repairTaskRepository.findByIdAndWorkStatusNot(taskId, WorkStatus.DONE).orElseThrow(TaskNotFoundException::new);
        if(repairTask.getPayStatus() != PayStatus.DONE){
            repairTask.setPrice(0L);
        }
        repairTask.setPayStatus(PayStatus.CANCELED);
    }

    @Transactional
    public void acceptPay(Long taskId) throws TaskNotFoundException {
        RepairTask repairTask = repairTaskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        repairTask.setPayStatus(PayStatus.DONE);
        //repairTaskRepository.save(repairTask);
    }
}
