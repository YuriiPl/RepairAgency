package com.repairagency.repairagencyspring.dto;

import com.repairagency.repairagencyspring.entity.RepairTask;
import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.entity.UserSex;
import com.repairagency.repairagencyspring.security.Role;
import lombok.*;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder    //remove?
@ToString
public class UserDTO {

    public UserDTO(UserDB userDB){
        this.password="";
        this.name=userDB.getName();
        this.email=userDB.getEmail();
        this.login=userDB.getLogin();
        this.acceptNewsLatter=userDB.getAcceptNewsLatter();
        this.userSex=userDB.getUserSex();
        this.userRole=userDB.getUserRole();
        this.moneyCents=userDB.getAccount().getAmount();
//        this.locked
        this.applications=userDB.getRepairTasks();
    }

    List<RepairTask> applications;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zа-щьюяіїєґ])(?=.*[A-ZА-ЩЮЯІЇЄҐ])(?=.*\\d)[a-zA-Zа-щьюяіїєґА-ЩЮЯІЇЄҐ\\d]{6,24}$", message = "wrongPassword")
    private String password;

    @NotBlank
    @Pattern(regexp = "^(?! )(?!.* $)(?!(?:.* ){2})[a-zA-Zа-щьюяіїєґА-ЩЮЯІЇЄҐ ']{2,}$", message = "wrongUsername")
    private String name;

    @Email(message = "wrongEmail")
    private String email;

    @Pattern(regexp = "^[a-zA-Zа-щьюяіїєґА-ЩЮЯІЇЄҐ\\d]+$", message = "wrongLogin")
    @Size(min = 4, max = 24, message = "wrongLoginSize")
    private String login;

    private boolean acceptNewsLatter;

    @NotNull(message = "wrongGender")
    private UserSex userSex;

    private Role userRole;

    Long moneyCents;
}
