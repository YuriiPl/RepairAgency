package com.repairagency.repairagencyspring.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {
    private String password;

    private String name;

    private String email;

    private String login;

    private boolean acceptNewsLatter;

    private UserSex userSex;
}
