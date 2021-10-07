package com.repairagency.repairagencyspring.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public enum Role {
    USER(Collections.singletonList(Authority.AUTHORITY_USER)),
    WORKER(Arrays.asList(Authority.AUTHORITY_USER, Authority.AUTHORITY_WORKER)),
    MANAGER(Arrays.asList(Authority.AUTHORITY_USER, Authority.AUTHORITY_ADMIN));

    private final List<Authority> authorities;

    Role(List<Authority> authorities) {
        this.authorities = authorities;
    }

//    public Set<Permission> getPermissions() {
//        return new HashSet<>(permissions);
//    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        return authorities
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toSet());
    }
}
