package com.dev.model.dao;

import com.dev.model.entity.StatusResponse;

import java.sql.SQLException;

public interface LogInfo {
    public StatusResponse reportStageLogInfo(String name) throws SQLException;
    public void insertLogInfo(String name, String status) throws SQLException;
}
