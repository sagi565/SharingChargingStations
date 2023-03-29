package com.example.sharingchargingstations.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private String name;
    private ChargingStation myChargingStation;
    private Rental currentRental;
    private List<Rental> HistoryRental;

    public User(String name, ChargingStation myChargingStation) {
        this.name = name;
        this.myChargingStation = myChargingStation;
        HistoryRental = new ArrayList<>();
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

    public Rental getCurrentRental() {
        return currentRental;
    }

    public void setCurrentRental(Rental currentRental) {
        this.currentRental = currentRental;
    }

    public List<Rental> getHistoryRental() {
        return HistoryRental;
    }

    public void setHistoryRental(List<Rental> historyRental) {
        HistoryRental = historyRental;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(myChargingStation, user.myChargingStation) && Objects.equals(currentRental, user.currentRental) && Objects.equals(HistoryRental, user.HistoryRental);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, myChargingStation, currentRental, HistoryRental);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", myChargingStation=" + myChargingStation +
                ", currentRental=" + currentRental +
                ", HistoryRental=" + HistoryRental +
                '}';
    }
}
