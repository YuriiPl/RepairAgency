package com.repairagency.repairagencyspring.entity;

import com.repairagency.repairagencyspring.dto.UserDTO;
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
public class UserDB {

    public UserDB(UserDTO userDTO, PasswordEncoder passwordEncoder){
        this.password=passwordEncoder.encode(userDTO.getPassword());
        this.name= userDTO.getName();
        this.email= userDTO.getEmail();
        this.login= userDTO.getLogin();
        this.acceptNewsLatter= userDTO.isAcceptNewsLatter();
        this.userSex= userDTO.getUserSex();
        this.userRole= userDTO.getUserRole()==null?Role.USER: userDTO.getUserRole();
        this.moneyCents= userDTO.getMoneyCents()==null?0L: userDTO.getMoneyCents();
        this.locked=false;
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

    private boolean locked;

}
