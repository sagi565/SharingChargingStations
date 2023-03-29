package com.example.sharingchargingstations.Model;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private String name;
    private ChargingStation myChargingStation;
    //todo: add profile image ??
//    private Rental currentRental;
//    private List<Rental> HistoryRental;
    private double totalRevenues;
    private double totalExpeness;

    public User(String name, ChargingStation myChargingStation, double totalRevenues, double totalExpeness) {
        this.name = name;
        this.myChargingStation = myChargingStation;
        this.totalRevenues = totalRevenues;
        this.totalExpeness = totalExpeness;
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

//    public Rental getCurrentRental() {
//        return currentRental;
//    }
//
//    public void setCurrentRental(Rental currentRental) {
//        this.currentRental = currentRental;
//    }
//
//    public List<Rental> getHistoryRental() {
//        return HistoryRental;
//    }
//
//    public void setHistoryRental(List<Rental> historyRental) {
//        HistoryRental = historyRental;
//    }

    public double getTotalRevenues() {
        return totalRevenues;
    }

    public void setTotalRevenues(double totalRevenues) {
        this.totalRevenues = totalRevenues;
    }

    public double getTotalExpeness() {
        return totalExpeness;
    }

    public void setTotalExpeness(double totalExpeness) {
        this.totalExpeness = totalExpeness;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", myChargingStation=" + myChargingStation +
                //", currentRental=" + currentRental +
                //", HistoryRental=" + HistoryRental +
                ", totalRevenues=" + totalRevenues +
                ", totalExpeness=" + totalExpeness +
                '}';
    }
}
