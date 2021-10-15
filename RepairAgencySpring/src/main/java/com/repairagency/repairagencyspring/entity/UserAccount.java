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

    public UserAccount(long amount){
        this.amount=amount;
    }

    @Column(nullable = false)
    private long amount;

    @OneToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH},fetch = FetchType.LAZY)
    @MapsId
    private UserDB owner;
}
