package com.dev.common.security;

public class AdminAccessLevel implements AccessLevel {
    @Override
    public String getAccessLevel() {
        return "Admin";
    }
}
