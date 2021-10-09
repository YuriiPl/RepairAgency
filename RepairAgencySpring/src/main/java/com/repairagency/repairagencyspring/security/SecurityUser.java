package com.repairagency.repairagencyspring.security;

import com.repairagency.repairagencyspring.entity.UserDB;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
@Data
public class SecurityUser implements UserDetails {

    private final String userName;
    private final String password;
    private final Set<SimpleGrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserDetails fromUser(UserDB user){
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .authorities(user.getUserRole().getAuthorities())
                .accountLocked(user.isLocked()).build();
//        return new org.springframework.security.core.userdetails.User(
//                user.getLogin(),user.getPassword(),user.getUserRole().getAuthorities()
//        );
    }
}
