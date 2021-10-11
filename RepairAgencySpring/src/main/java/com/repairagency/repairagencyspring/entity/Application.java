package com.repairagency.repairagencyspring.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserDB owner;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserDB repairer;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
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

    @Column(nullable = false)
    private Long price;
}
