package com.repairagency.repairagencyspring.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name="Tasks")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long ownerId;

    private Long repairerId;

    private Long serviceId;

    private WorkStatus workStatus;

    private PayStatus payStatus;

    private LocalDateTime dateCreate;

    private LocalDateTime dateFinish;
}
