package com.repairagency.repairagencyspring.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public enum Role {
    USER(Collections.singletonList(Permission.PERMISSION_READ)),
    ADMIN(Arrays.asList(Permission.PERMISSION_READ,Permission.PERMISSION_WRITE));

    private final List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

//    public Set<Permission> getPermissions() {
//        return new HashSet<>(permissions);
//    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        return permissions
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
