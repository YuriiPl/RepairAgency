package com.repairagency.repairagencyspring.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public enum Role {
    USER(Collections.singletonList(Authority.AUTHORITY_USER), "/account/user"),
    REPAIRER(Arrays.asList(Authority.AUTHORITY_USER, Authority.AUTHORITY_REPAIRER), "/account/repairer"),
    MANAGER(Arrays.asList(Authority.AUTHORITY_USER, Authority.AUTHORITY_MANAGER), "/account/manager");

    private final List<Authority> authorities;
    private final String homePage;

    Role(List<Authority> authorities, String homePage) {
        this.authorities = authorities;
        this.homePage = homePage;
    }

    public String getHomePage() {
        return homePage;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        return authorities
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toSet());
    }
}
