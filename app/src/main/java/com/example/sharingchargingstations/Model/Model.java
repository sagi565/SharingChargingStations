package com.example.sharingchargingstations.Model;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Model {
    private static final String TAG = "sharingchargingstations.Model";
    private Context context;

    public interface IModelUpdate {
        public void userUpdate();
        public void stationUpdate();
        public void rentalUpdate();

    }

    private  ArrayList<IModelUpdate> iModelUpdates = new ArrayList<>();

    public void registerModelUpdate(IModelUpdate iModelUpdate) {
        this.iModelUpdates.add(iModelUpdate);
    }

    public void unRegisterModelUpdate(IModelUpdate iModelUpdate){
        if (iModelUpdates.contains(iModelUpdate)) iModelUpdates.remove(iModelUpdate);
    }

    private static Model instance;

    private Model() {
        if (mAuth.getCurrentUser() != null && currentUser == null) currentUser = new User(mAuth.getCurrentUser());
        registerDBRef();
    }
    private void registerDBRef() {
        if (getAuthUser() != null) {
            usersRef = db.collection("Users");
            rentalsRef = db.collection("Rentals");
            stationsRef = db.collection("Stations");
            userRef = usersRef.document(getAuthUser().getUid());
            registerUserData();
            registerStationsData();
            registerRentalsData();

        } else {
            usersRef = null;
            rentalsRef = null;
            stationsRef = null;
            userRef = null;
            if(userListenerRegistration != null)
                userListenerRegistration.remove();
            if(stationsListenerRegistration != null)
                stationsListenerRegistration.remove();
            if(rentalsListenerRegistration != null)
                rentalsListenerRegistration.remove();
        }
    }
    public void setContext(Context context){
        this.context = context;
    }
    public static Model getInstance() {
        if (instance == null)
            instance = new Model();
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef;
    private CollectionReference stationsRef;
    private CollectionReference rentalsRef;
    private DocumentReference userRef;
    private User currentUser;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<ChargingStation> chargingStations = new ArrayList<>();
    private ArrayList<Rental> rentals = new ArrayList<>();
    private double totalRevenues;
    private double totalExpenses;

    public double getTotalRevenues() {
        double sum = 0;
        for (Rental rental : rentals) {
            //rental.get
        }
        return sum;
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
    public void loadData() {
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


        rentals.add(new Rental(chargingStations.get(0), currentUser, users.get(1), oneHourBack, Calendar.getInstance().getTime()));
        rentals.add(new Rental(chargingStations.get(1), currentUser, users.get(2), twoHourBack, Calendar.getInstance().getTime()));
        rentals.add(new Rental(chargingStations.get(2), currentUser, users.get(3), oneHourBack, Calendar.getInstance().getTime()));
        rentals.add(new Rental(chargingStations.get(3), users.get(3), currentUser, twoHourBack, Calendar.getInstance().getTime()));
        rentals.add(new Rental(chargingStations.get(4), users.get(2), currentUser, treeHourBack, Calendar.getInstance().getTime()));

        totalRevenues = 0;
        totalExpenses = 0;

        for (Rental r : rentals) {
            if (r.getHolderUser() == currentUser)
                totalRevenues += r.getPrice();
            else
                totalExpenses += r.getPrice();
        }

        //currentUser.getMyChargingStation().
    }
    public FirebaseUser getAuthUser() {
        return mAuth.getCurrentUser();
    }
    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //set user
                        currentUser = new User(getAuthUser());
                        raiseUserUpdate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: login " + e.getMessage() );
                        Toast.makeText(context, "sign in failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        raiseUserUpdate();
                    }
                });

    }
    public void createUser(String email, String password, String displayName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName)
                                .build();
                        mAuth.getCurrentUser().updateProfile(profileUpdates);
                        registerDBRef();
                        User user = new User(displayName, null);
                        user.setDocumentId(getAuthUser().getUid());
                        addUser(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(context, "Create user failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                        raiseUserUpdate();
                    }
                });
    }
    public void signOut() {
        mAuth.signOut();
        registerDBRef();
        raiseUserUpdate();
    }
    private void raiseUserUpdate() {
        for(IModelUpdate iModelUpdate : iModelUpdates){
            iModelUpdate.userUpdate();
        }
    }
    private void raiseRentalUpdate() {
        for(IModelUpdate iModelUpdate : iModelUpdates){
            iModelUpdate.rentalUpdate();
        }
    }
    private void addUser(User user) {
        usersRef.document(user.getDocumentId()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        raiseUserUpdate();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(context, "Add User Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void addRental(Rental rental) {
        rentalsRef.add(rental)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "onSuccess: added rental " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

            }
        });
    }
    private void updateUser(User user) {
        userRef.set(user);
    }
    private ListenerRegistration userListenerRegistration;
    private void registerUserData() {
        userListenerRegistration = usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.e(TAG, "onEvent: users changed " + error.getMessage() );
                    return;
                }
                User user = null;
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    user = documentChange.getDocument().toObject(User.class);
                    switch (documentChange.getType()) {
                        case ADDED:

                            user.setDocumentId(documentChange.getDocument().getId());
                            users.add(user);
                            break;
                        case MODIFIED:
                            String noteId = documentChange.getDocument().getId();
                            User user1 = users.stream()
                                    .filter(n -> n.getDocumentId().equals(noteId))
                                    .findAny()
                                    .orElse(null);
                            if (user1 != null) {
                                //update user fields
                            }
                        case REMOVED:
                            users.remove(user);
                            break;
                    }
                }
                raiseUserUpdate();
            }
        });
    }
    public void addChargingStation(ChargingStation chargingStation) {
        stationsRef.add(chargingStation)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "onSuccess: added charging station " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

            }
        });
    }
    private ListenerRegistration stationsListenerRegistration;
    private void registerStationsData() {

        stationsListenerRegistration = stationsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                ChargingStation station = null;
                for (DocumentChange documentChange : value.getDocumentChanges()) {

                    station = documentChange.getDocument().toObject(ChargingStation.class);
                    switch (documentChange.getType()) {
                        case ADDED:
                            station.setDocumentId(documentChange.getDocument().getId());
                            chargingStations.add(station);
                            if (station.getUser().getDocumentId().equals(getAuthUser().getUid())) getCurrentUser().setMyChargingStation(station);
                            break;
                        case MODIFIED:
                            String docId = documentChange.getDocument().getId();
                            ChargingStation localStation = chargingStations.stream()
                                    .filter(n -> n.getDocumentId().equals(docId))
                                    .findAny()
                                    .orElse(null);
                            if (localStation != null) {
                                localStation.setPricePerHour(station.getPricePerHour());
                                localStation.setStartHour(station.getStartHour());
                                localStation.setEndHour(station.getEndHour());
                                localStation.setStationAddress(station.getStationAddress());
                                localStation.setType(station.getType());
                                localStation.setChargingSpeed(station.getChargingSpeed());
                            }
                        case REMOVED:
                            chargingStations.remove(station);
                            break;
                    }
                }
                raiseStationUpdate();
            }
        });
    }
    public void updateChargingStation(ChargingStation chargingStation){
        stationsRef.document(chargingStation.getDocumentId()).set(chargingStation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: ChargingStation update " + chargingStation.getDocumentId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i(TAG, "onFailure:  ChargingStation update " + chargingStation.getDocumentId());
                    }
                });
    }


    private void raiseStationUpdate(){
        for(IModelUpdate update :iModelUpdates ){
            update.stationUpdate();
        }
    }
    public void updateRental(Rental rental){
        rentalsRef.document(rental.getDocumentId()).set(rental)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: rental update " + rental.getDocumentId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i(TAG, "onFailure:  rental update " + rental.getDocumentId());
                    }
                });
    }

    private ListenerRegistration rentalsListenerRegistration;
    private void registerRentalsData() {
        rentalsListenerRegistration = rentalsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                Rental rental = null;
                for (DocumentChange documentChange : value.getDocumentChanges()) {

                    rental = documentChange.getDocument().toObject(Rental.class);
                    switch (documentChange.getType()) {
                        case ADDED:
                            rental.setDocumentId(documentChange.getDocument().getId());
                            rentals.add(rental);
                            break;
                        case MODIFIED:
                            String docId = documentChange.getDocument().getId();
                            Rental localRental = rentals.stream()
                                    .filter(n -> n.getDocumentId().equals(docId))
                                    .findAny()
                                    .orElse(null);
                            if (localRental != null) {
                                localRental.setRenterUser(rental.getRenterUser());
                                localRental.setHolderUser(rental.getHolderUser());
                                localRental.setStartDate(rental.getStartDate());
                                localRental.setEndDate(rental.getEndDate());
                                localRental.setStatus(rental.getStatus());
                            }
                        case REMOVED:
                            rentals.remove(rental);
                            break;
                    }
                }
                raiseRentalUpdate();
            }
        });
    }

    private void raiseRentalsUpdate(){
        for(IModelUpdate update :iModelUpdates ){
            update.rentalUpdate();
        }
    }



}
