package com.example.sharingchargingstations.Model;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    public User getCurrentUser() {
        return currentUser;
    }

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private User currentUser;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<ChargingStation> chargingStations = new ArrayList<>();
    private ArrayList<Rental> rentals = new ArrayList<>();
    private double totalRevenues;
    private double totalExpenses;

    public double getTotalRevenues() {
        return totalRevenues;
    }

    public void setTotalRevenues(double totalRevenues) {
        this.totalRevenues = totalRevenues;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

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
        chargingStations.add(new ChargingStation(15, 3, 22, new Address("Givat Shmuel", "Hazeitim", "1"), TypeChargingStation.PP, 40, "Lovely charging station, very fast, no problems, on a quiet street."));
        chargingStations.add(new ChargingStation(17, 12, 18, new Address("Givat Shmuel", "Hazeitim", "2"), TypeChargingStation.CP, 60, "Lovely charging station, very fast, no problems, on a quiet street."));
        chargingStations.add(new ChargingStation(9, 15, 19, new Address("Givat Shmuel", "Hazeitim", "3"), TypeChargingStation.CCS2, 70, "Lovely charging station, very fast, no problems, on a quiet street."));
        chargingStations.add(new ChargingStation(12, 7, 13, new Address("Tel Aviv", "Ibn Gvirol", "26"), TypeChargingStation.CHAdeMO, 70, "Lovely charging station, very fast, no problems, on a quiet street."));
        chargingStations.add(new ChargingStation(8, 6, 8, new Address("Ramat Gan", "Jabotinsky", "65"), TypeChargingStation.CP, 30, "Lovely charging station, very fast, no problems, on a quiet street."));


        users.add(new User("Sagi", chargingStations.get(0)));
        users.add(new User("Tamir", chargingStations.get(1)));
        users.add(new User("Yoav", chargingStations.get(2)));
        users.add(new User("Yaniv", chargingStations.get(3)));
        users.add(new User("Noa", chargingStations.get(4)));

        currentUser = users.get(0);

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        date.setTime(cal.getTime().getTime());
        cal.setTime(date);
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();

        cal.add(Calendar.HOUR, -1);
        Date twoHourBack = cal.getTime();
        cal.add(Calendar.HOUR, -1);
        Date treeHourBack = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


        rentals.add(new Rental(currentUser, users.get(1), oneHourBack, Calendar.getInstance().getTime()));
        rentals.add(new Rental(currentUser, users.get(2), twoHourBack, Calendar.getInstance().getTime()));
        rentals.add(new Rental(currentUser, users.get(3), oneHourBack, Calendar.getInstance().getTime()));
        rentals.add(new Rental(users.get(3), currentUser, twoHourBack, Calendar.getInstance().getTime()));
        rentals.add(new Rental(users.get(2), currentUser, treeHourBack, Calendar.getInstance().getTime()));

        totalRevenues = 0;
        totalExpenses = 0;

        for(Rental r : rentals){
            if(r.getHolderUser() == currentUser)
                totalRevenues += r.getPrice();
            else
                totalExpenses += r.getPrice();
        }
    }

    public FirebaseUser getAuthUser(){
        return mAuth.getCurrentUser();
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
//                        raiseUserLogin();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, "sign in failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void createUser(String email, String password, String displayName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
//                        raiseUserLogin();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName)
                                .build();
                        mAuth.getCurrentUser().updateProfile(profileUpdates);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, "Create user failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
    }

}
