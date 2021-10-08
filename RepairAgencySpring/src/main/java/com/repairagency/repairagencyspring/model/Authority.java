package com.repairagency.repairagencyspring.model;

public enum Authority {
    AUTHORITY_USER("perm:user"),
    AUTHORITY_REPAIRER("perm:repairer"),
    AUTHORITY_MANAGER("perm:manager");

    private final String authority;

    Authority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
