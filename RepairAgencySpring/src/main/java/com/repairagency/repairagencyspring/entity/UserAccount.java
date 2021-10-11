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
    private long amount;

    public void addMoney(long cents){
        amount+=cents;
    }

    @OneToOne (mappedBy = "account", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private UserDB owner;
}
