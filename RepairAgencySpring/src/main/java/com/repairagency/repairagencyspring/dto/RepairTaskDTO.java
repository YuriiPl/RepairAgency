package com.repairagency.repairagencyspring.dto;

import com.repairagency.repairagencyspring.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RepairTaskDTO {

    public RepairTaskDTO(RepairTask repairTask){
        this.owner=repairTask.getOwner();
        this.repairer=repairTask.getRepairer();
        this.serviceName=repairTask.getServiceName();
        this.workStatus=repairTask.getWorkStatus();
        this.payStatus=repairTask.getPayStatus();
        this.dateCreate=repairTask.getDateCreate();
        this.dateFinish=repairTask.getDateFinish();
        this.price = repairTask.getPrice();
    }

    private UserDB owner;

    private UserDB repairer;

    private ServiceName serviceName;

    private WorkStatus workStatus;

    private PayStatus payStatus;

    private LocalDateTime dateCreate;

    private LocalDateTime dateFinish;

    private Long price;
}
