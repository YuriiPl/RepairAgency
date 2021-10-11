package com.repairagency.repairagencyspring.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(of={"amount"})
@Entity
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Long amount;

    @OneToOne (mappedBy="account", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private UserDB owner;
}
