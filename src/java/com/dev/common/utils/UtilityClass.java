package com.dev.common.utils;

public class UtilityClass {
    public String concat(String... strs) {
        StringBuilder sb = new StringBuilder(strs[0]);
        for (int i = 1; i < strs.length; i++) {
            sb.append(strs[i]);
        }
        return sb.toString();
    }
}
