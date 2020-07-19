package com.dev.service.workers;

import java.util.concurrent.Callable;

public abstract class Job implements Callable {

    protected String id = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
