package com.example.sharingchargingstations.Model;

public class ChargingStation {

    private double pricePerHour;
    private float minHour;
    private float maxHour;
    private Address stationAddress;
    private TypeChargingStation type;
    private double ChargingSpeed;

    public ChargingStation(double pricePerHour, float minHour, float maxHour, Address stationAddress, TypeChargingStation type, double chargingSpeed) {
        this.pricePerHour = pricePerHour;
        setMinHour(minHour);
        setMaxHour(maxHour);
        this.maxHour = maxHour;
        this.stationAddress = stationAddress;
        this.type = type;
        ChargingSpeed = chargingSpeed;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public float getMinHour() {
        return minHour;
    }

    public void setMinHour(float minHour) { // אפשר רק חצאי שעות
        this.minHour = minHour;
    }

    public float getMaxHour() {
        return maxHour;
    }

    public void setMaxHour(float maxHour) {
        this.maxHour = maxHour;
    }

    public Address getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(Address stationAddress) {
        this.stationAddress = stationAddress;
    }

    public TypeChargingStation getType() {
        return type;
    }

    public void setType(TypeChargingStation type) {
        this.type = type;
    }

    public double getChargingSpeed() {
        return ChargingSpeed;
    }

    public void setChargingSpeed(double chargingSpeed) {
        ChargingSpeed = chargingSpeed;
    }

    /*public String floatToTime(float time){
        return time;
    }*/

    @Override
    public String toString() {
        return "ChargingStation{" +
                "pricePerHour=" + pricePerHour +
                ", minHour=" + minHour +
                ", maxHour=" + maxHour +
                ", stationAddress=" + stationAddress +
                ", type=" + type +
                ", ChargingSpeed=" + ChargingSpeed +
                '}';
    }
}
