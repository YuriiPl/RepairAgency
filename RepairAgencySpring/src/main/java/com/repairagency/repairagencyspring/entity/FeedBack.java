package com.repairagency.repairagencyspring.entity;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name="FeedBack")
public class FeedBack {
    @Id
    private Long id;

    @OneToOne(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
    @MapsId
    RepairTask repairTask;

    @Column(length = 512)
    private String message;
}
