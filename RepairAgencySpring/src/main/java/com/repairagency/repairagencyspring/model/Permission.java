package com.repairagency.repairagencyspring.model;

public enum Permission {
    PERMISSION_READ("perm:read"),
    PERMISSION_WRITE("perm:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
