package com.dev.model.entity;

public class VehicleBuilder {

    private Vehicle vehicle = new Vehicle();

    public VehicleBuilder addCard(String cardId, String vehId) {
        vehicle.setCardId(cardId);
        vehicle.setVehicleId(vehId);
        return this;
    }


    public VehicleBuilder addRules(String rule, String clazz, String fuelCode) {
        vehicle.setRuleId(rule);
        vehicle.setClazz(clazz);
        vehicle.setFuelCode(fuelCode);
        return this;
    }

    public VehicleBuilder withVehicleStatus(String cardStatus) {
        vehicle.setCardStatus(cardStatus);
        return this;
    }

    public VehicleBuilder withRemain(int functionalRemain, short lastSeq, int publicRemain) {
        vehicle.setFunctionalRemain(functionalRemain);
        vehicle.setPublicRemain(publicRemain);
        vehicle.setLastSeq(lastSeq);
        return this;
    }

    public VehicleBuilder withBulk(int bulkSize) {
        vehicle.setBulkSize(bulkSize);
        return this;
    }

    public VehicleBuilder addProcessesStatus(boolean isProcessed) {
        vehicle.setProcessed(isProcessed);
        return this;
    }

    public Vehicle build() {

        return vehicle;
    }

}
