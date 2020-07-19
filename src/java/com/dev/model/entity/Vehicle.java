/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dev.model.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement
public class Vehicle implements Serializable {
    //@XmlElement
    private String cardId;
    private String cardStatus;
    private String clazz;
    private int functionalRemain;
    private short lastSeq;
    private int publicRemain;
    private String ruleId;
    private String fuelCode;
    private String vehicleId;
    private int bulkSize;
    private boolean isProcessed;

    public Vehicle() {
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public int getFunctionalRemain() {
        return functionalRemain;
    }

    public void setFunctionalRemain(int functionalRemain) {
        this.functionalRemain = functionalRemain;
    }

    public short getLastSeq() {
        return lastSeq;
    }

    public void setLastSeq(short lastSeq) {
        this.lastSeq = lastSeq;
    }

    public int getPublicRemain() {
        return publicRemain;
    }

    public void setPublicRemain(int publicRemain) {
        this.publicRemain = publicRemain;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getFuelCode() {
        return fuelCode;
    }

    public void setFuelCode(String fuelCode) {
        this.fuelCode = fuelCode;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getBulkSize() {
        return bulkSize;
    }

    public void setBulkSize(int bulkSize) {
        this.bulkSize = bulkSize;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public void addSeq(int i) {
        lastSeq++;
    }
}

