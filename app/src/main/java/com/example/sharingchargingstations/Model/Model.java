package com.example.sharingchargingstations.Model;

import java.util.ArrayList;

public class Model {
    private static Model instance;
    private Model(){
        loadData();
    }
    public static Model getInstance(){
        if(instance == null)
            instance = new Model();
        return instance;
    }

    private User currentUser;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<ChargingStation> chargingStations = new ArrayList<>();
    private ArrayList<Rental> rentals = new ArrayList<>();

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<ChargingStation> getChargingStations() {
        return chargingStations;
    }

    public ArrayList<Rental> getRentals() {
        return rentals;
    }

    public void loadData(){
        chargingStations.add(new ChargingStation(15, 13, 15, new Address("Givat Shmuel", "Hazeitim", "1"), TypeChargingStation.BatteryEVs, 40));
        chargingStations.add(new ChargingStation(17, 12, 18, new Address("Givat Shmuel", "Hazeitim", "2"), TypeChargingStation.MostBatteryEVs, 60));
        chargingStations.add(new ChargingStation(9, 15, 19, new Address("Givat Shmuel", "Hazeitim", "3"), TypeChargingStation.BatteryEVs, 70));
        users.add(new User("Sagi", chargingStations.get(0)));
        users.add(new User("Tamir", chargingStations.get(1)));
        users.add(new User("Yoav", chargingStations.get(2)));
    }
}
