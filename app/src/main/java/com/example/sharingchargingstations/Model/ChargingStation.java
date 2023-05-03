package com.example.sharingchargingstations.Model;

import com.google.firebase.firestore.Exclude;

public class ChargingStation {

    private double pricePerHour;
    private float startHour;
    private float endHour;
    private Address stationAddress;
    private TypeChargingStation type;
    private double ChargingSpeed;
    private String documentId;
    private User user;

    public ChargingStationStatus getStatus() {
        return status;
    }

    public void setStatus(ChargingStationStatus status) {
        this.status = status;
    }

    private ChargingStationStatus status;

    public float getStartHour() {
        return startHour;
    }

    public float getEndHour() {
        return endHour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Exclude
    public String getDocumentId() {return documentId;}

    public void setDocumentId(String documentId) {this.documentId = documentId;}

    private String description;
    public ChargingStation (){}
    public ChargingStation(double pricePerHour, float startHour, float endHour, Address stationAddress, TypeChargingStation type, double chargingSpeed, String description) {
        this.user = Model.getInstance().getCurrentUser();
        this.pricePerHour = pricePerHour;
        setStartHour(startHour);
        setEndHour(endHour);
        this.endHour = endHour;
        this.stationAddress = stationAddress;
        this.type = type;
        ChargingSpeed = chargingSpeed;
        this.description = description;
        status = ChargingStationStatus.active;
    }

    public User getUser(){return user;}
    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public float getMinHour() {
        return startHour;
    }

    public void setStartHour(float minHour) { // אפשר רק חצאי שעות
        this.startHour = minHour;
    }

    public void setEndHour(float endHour) {
        this.endHour = endHour;
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
    @Exclude
    public String getTime(){
        return (String)((int)startHour + ":" + (int)((int)startHour - startHour) + "0" +  " - " + (int)endHour + ":" + (int)((int)endHour - endHour) + "0");
    }
    @Exclude
    public String getTime(float time){
        if(time < 10)
            return (String)("0" + (int)time + ":" + (int)((int)time - time) + "0");
        return (String)((int)time + ":" + (int)((int)time - time) + "0");
    }

    @Override
    public String toString() {
        return "ChargingStation{" +
                "pricePerHour=" + pricePerHour +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                ", stationAddress=" + stationAddress +
                ", type=" + type +
                ", ChargingSpeed=" + ChargingSpeed +
                ", description='" + description + '\'' +
                '}';
    }
    @Exclude
    public String getProperties(){
        return pricePerHour + " " + startHour + " " + endHour + " " + stationAddress.toString() + " " + type + " " + ChargingSpeed;
    }
}
