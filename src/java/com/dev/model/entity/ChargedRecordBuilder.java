package com.dev.model.entity;

public class ChargedRecordBuilder {
    private ChargedRecord chargedRecord = new ChargedRecord();

    public ChargedRecordBuilder setIds(String vehicleId, String cardId) {
        chargedRecord.setVehicleId(vehicleId);
        chargedRecord.setCardId(cardId);
        return this;
    }

    public ChargedRecordBuilder setSeqNo(short seqNo) {
        chargedRecord.setSeqNo(seqNo);
        return this;
    }

    public ChargedRecordBuilder setQuota(int q, String quotaType) {
        chargedRecord.setQuota(q);
        chargedRecord.setQuotaType(quotaType);
        return this;
    }

    public ChargedRecordBuilder setRemains(int fRemain, int pRemain) {
        chargedRecord.setFunctionalRem(fRemain);
        chargedRecord.setPublicRem(pRemain);
        return this;
    }

    public ChargedRecord build() {
        return chargedRecord;
    }
}
