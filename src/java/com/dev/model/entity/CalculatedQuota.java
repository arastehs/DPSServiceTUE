/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dev.model.entity;

import java.io.Serializable;

/**
 *
 * @author arasteh.s
 */
public class CalculatedQuota implements Serializable {

    private int calculatedQuota;
    private String calculatedType;

    public int getCalculatedQuota() {
        return calculatedQuota;
    }

    public void setCalculatedQuota(int calQuota) {
        this.calculatedQuota = calQuota;
    }

    public String getCalculatedType() {
        return calculatedType;
    }

    public void setCalculatedType(String CalType) {
        this.calculatedType = CalType;
    }


}
