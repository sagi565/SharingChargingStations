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
        chargingStations.add(new ChargingStation(12, 7, 3, new Address("Tel Aviv", "Ibn Gvirol", "26"), TypeChargingStation.MostBatteryEVs, 70));
        chargingStations.add(new ChargingStation(8, 6, 8, new Address("Ramat Gan", "Jabotinsky", "65"), TypeChargingStation.PlugInHybrid, 30));


        users.add(new User("Sagi", chargingStations.get(0), 15, 30));
        users.add(new User("Tamir", chargingStations.get(1), 25, 20));
        users.add(new User("Yoav", chargingStations.get(2), 45, 20));
        users.add(new User("Yaniv", chargingStations.get(3), 35, 40));
        users.add(new User("Noa", chargingStations.get(4), 35, 10));

        currentUser = users.get(0);
        rentals.add()
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
