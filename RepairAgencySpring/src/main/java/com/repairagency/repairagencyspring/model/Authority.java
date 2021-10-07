package com.repairagency.repairagencyspring.model;

public enum Authority {
    AUTHORITY_USER("perm:user"),
    AUTHORITY_WORKER("perm:worker"),
    AUTHORITY_ADMIN("perm:admin");

    private final String authority;

    Authority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
