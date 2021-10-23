package com.repairagency.repairagencyspring.entity;

import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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
        this.price = repairTaskDTO.getLongPrice();
        this.feedBack=new FeedBack();
        this.feedBack.setRepairTask(this);
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

    @OneToOne(mappedBy = "repairTask", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private FeedBack feedBack;

    public void setFeedBackMessage(String feedBack) {
        this.feedBack.setMessage(feedBack);
    }
}
