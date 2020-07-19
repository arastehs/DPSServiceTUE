package com.dev.common.security;

public class ReportAccessLevel implements AccessLevel {
    @Override
    public String getAccessLevel() {
        return "Report";
    }
}
