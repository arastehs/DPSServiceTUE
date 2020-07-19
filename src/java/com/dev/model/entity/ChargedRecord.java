package com.dev.model.entity;

import java.io.Serializable;

public class ChargedRecord implements Serializable {

    private String cardId;
    private int quota;
    private short seqNo;
    private String vehicleId;
    private int functionalRem;
    private int publicRem;
    private String quotaType;

    public int getFunctionalRem() {
        return functionalRem;
    }

    public void setFunctionalRem(int functionalRem) {
        this.functionalRem = functionalRem;
    }

    public int getPublicRem() {
        return publicRem;
    }

    public void setPublicRem(int publicRem) {
        this.publicRem = publicRem;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public short getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(short seqNo) {
        this.seqNo = seqNo;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }


}
