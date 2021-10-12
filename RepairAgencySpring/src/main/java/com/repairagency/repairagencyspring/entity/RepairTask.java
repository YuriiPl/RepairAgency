package com.repairagency.repairagencyspring.entity;

import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@Entity
public class RepairTask {

    public RepairTask(RepairTaskDTO repairTaskDTO){
        this.owner=repairTaskDTO.getOwner();
        this.repairer=repairTaskDTO.getRepairer();
        this.serviceName=repairTaskDTO.getServiceName();
        this.workStatus=repairTaskDTO.getWorkStatus();
        this.payStatus=repairTaskDTO.getPayStatus();
        this.dateCreate=repairTaskDTO.getDateCreate();
        this.dateFinish=repairTaskDTO.getDateFinish();
        this.price = repairTaskDTO.getPrice();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserDB owner;

    @ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(nullable = true)
    private UserDB repairer;

    @ManyToOne(optional=false, fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private ServiceName serviceName;

    @Enumerated(EnumType.STRING)
    private WorkStatus workStatus;

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    @Column(nullable = false)
    private LocalDateTime dateCreate;

    @Column
    private LocalDateTime dateFinish;

    @Column
    private Long price;
}
