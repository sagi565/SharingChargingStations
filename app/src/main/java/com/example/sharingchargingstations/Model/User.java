package com.example.sharingchargingstations.Model;

import com.google.firebase.firestore.Exclude;

public class User {
    private String documentId;
    private String name;
    private ChargingStation myChargingStation;

    public User(String name, ChargingStation myChargingStation) {
        this.name = name;
        this.myChargingStation = myChargingStation;
    }

    public User(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChargingStation getMyChargingStation() {
        return myChargingStation;
    }

    public void setMyChargingStation(ChargingStation myChargingStation) {
        this.myChargingStation = myChargingStation;
    }
    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

}
