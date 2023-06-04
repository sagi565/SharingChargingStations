package com.example.sharingchargingstations.Model;

import com.google.firebase.auth.FirebaseUser;

public class User {
    private String documentId;
    private String name;
    private ChargingStation myChargingStation;
    private String profileImage;

    public User(String name, ChargingStation myChargingStation) {
        this.name = name;
        this.myChargingStation = myChargingStation;
    }

    public User(){}
    public User(FirebaseUser firebaseUser){
        documentId = firebaseUser.getUid();
        name = firebaseUser.getDisplayName();
    }

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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
