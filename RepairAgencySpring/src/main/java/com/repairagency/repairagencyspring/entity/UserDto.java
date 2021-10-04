package com.repairagency.repairagencyspring.entity;

import com.repairagency.repairagencyspring.dto.User;
import com.repairagency.repairagencyspring.dto.UserSex;
import lombok.*;

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

    public UserDto(User user){
        this.password=user.getPassword();
        this.name=user.getName();
        this.email=user.getEmail();
        this.login=user.getLogin();
        this.acceptNewsLatter=user.isAcceptNewsLatter();
        this.userSex=user.getUserSex();
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    long id;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,24}$", message = "wrongPassword")
    @Column(name = "passwd")
    private String password;

    @Pattern(regexp = "^(?! )(?!.* $)(?!(?:.* ){2})[a-zA-Zа-щьюяіїєґА-ЩЮЯІЇЄҐ ']{2,}$", message = "wrongUsername")
    @Column(name = "username")
    private String name;

    @Email(message = "wrongEmail")
    @Column(name = "mail", unique = true)
    private String email;

    @Pattern(regexp = "^[\\w\\d]+$", message = "wrongLogin")
    @Size(min = 6, max = 24, message = "wrongLoginSize")
    @Column(name = "userlogin", unique = true)
    private String login;

    @Column(name = "newsaccept")
    private boolean acceptNewsLatter;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private UserSex userSex;

}
