package com.repairagency.repairagencyspring.entity;

import com.repairagency.repairagencyspring.dto.User;
import com.repairagency.repairagencyspring.dto.UserSex;
import com.repairagency.repairagencyspring.model.Role;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "user") // This tells Hibernate to make a table out of this class
public class UserDto {

    public UserDto(User user, PasswordEncoder passwordEncoder){
        this.password=passwordEncoder.encode(user.getPassword());
        this.name=user.getName();
        this.email=user.getEmail();
        this.login=user.getLogin();
        this.acceptNewsLatter=user.isAcceptNewsLatter();
        this.userSex=user.getUserSex();
        this.userRole=user.getUserRole()==null?Role.USER:user.getUserRole();
        this.moneyCents=user.getMoneyCents()==null?0L:user.getMoneyCents();
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    long id;

    @Column(name = "passwd")
    private String password;

    @Column(name = "username")
    private String name;

    @Column(name = "mail", unique = true)
    private String email;

    @Column(name = "userlogin", unique = true)
    private String login;

    @Column(name = "newsaccept")
    private boolean acceptNewsLatter;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private UserSex userSex;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private Role userRole;

    @Column(name = "cents")
    @PositiveOrZero(message = "wrongMoney")
    Long moneyCents;

}
