package com.repairagency.repairagencyspring.dto;

import com.repairagency.repairagencyspring.model.Role;
import lombok.*;
import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,24}$", message = "wrongPassword")
    private String password;

    @NotBlank
    @Pattern(regexp = "^(?! )(?!.* $)(?!(?:.* ){2})[a-zA-Zа-щьюяіїєґА-ЩЮЯІЇЄҐ ']{2,}$", message = "wrongUsername")
    private String name;

    @Email(message = "wrongEmail")
    private String email;

    @Pattern(regexp = "^[\\w\\d]+$", message = "wrongLogin")
    @Size(min = 4, max = 24, message = "wrongLoginSize")
    private String login;

    private boolean acceptNewsLatter;

    @NotNull(message = "wrongGender")
    private UserSex userSex;

    private Role userRole;

    Long moneyCents;
}
