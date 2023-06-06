package com.example.sharingchargingstations.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Model {

    //region ModelUpdate
    public interface IModelUpdate {
        public void userUpdate();

        public void stationUpdate();

        public void rentalUpdate();

    }

    private ArrayList<IModelUpdate> iModelUpdates = new ArrayList<>();

    public void registerModelUpdate(IModelUpdate iModelUpdate) {
        this.iModelUpdates.add(iModelUpdate);
    }

    public void unRegisterModelUpdate(IModelUpdate iModelUpdate) {
        if (iModelUpdates.contains(iModelUpdate)) iModelUpdates.remove(iModelUpdate);
    }
    //endregion

    //region Properties, Constructor, Getters and Setters

    private Model() {
        registerDBRef();
    }

    private void setCurrentUser(){
        if (currentUser == null) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getDocumentId().equals(mAuth.getUid())) {
                    currentUser = users.get(i);
                }
            }
        }
    }

    private static Model instance;
    private User currentUser;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<ChargingStation> chargingStations = new ArrayList<>();
    private ArrayList<Rental> rentals = new ArrayList<>();
    private static final String TAG = "sharingchargingstations.Model";
    private Context context;

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<ChargingStation> getChargingStations() {
        return chargingStations;
    }

    public ArrayList<Rental> getRentals() {
        return rentals;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static Model getInstance() {
        if (instance == null)
            instance = new Model();
        return instance;
    }


    //endregion

    //region FireBase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("UserImages");
    private CollectionReference usersRef;
    private CollectionReference stationsRef;
    private CollectionReference rentalsRef;
    private DocumentReference userRef;

    public FirebaseUser getAuthUser() {
        return mAuth.getCurrentUser();
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
            if (userListenerRegistration != null)
                userListenerRegistration.remove();
            if (stationsListenerRegistration != null)
                stationsListenerRegistration.remove();
            if (rentalsListenerRegistration != null)
                rentalsListenerRegistration.remove();
        }
    }
    //endregion

    //region Revenues and Expenses
    public double getTotalRevenues() {
        double totalRevenues = 0;
        for (Rental rental : rentals) {
            if (rental.getHolderUser().getDocumentId().equals(currentUser.getDocumentId()) && rental.getStatus() != RentalStatus.cancelled)
                totalRevenues += rental.getPrice();
        }
        return totalRevenues;
    }

    public double getTotalExpenses() {
        double totalExpenses = 0;
        for (Rental rental : rentals) {
            if (rental.getRenterUser().getDocumentId().equals(currentUser.getDocumentId()) && rental.getStatus() != RentalStatus.cancelled)
                totalExpenses += rental.getPrice();
        }
        return totalExpenses;
    }
    //endregion

    //region Current User
    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //set user
                        currentUser = new User(getAuthUser());
                        for(ChargingStation c : chargingStations){
                            if(c.getUser().getDocumentId().equals(currentUser.getDocumentId())){
                                currentUser.setMyChargingStation(c);
                            }
                        }
                        setCurrentUser();
                        raiseUserUpdate();
                        raiseStationUpdate();
                        raiseRentalUpdate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: login " + e.getMessage());
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
                        //currentUser = user;
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
    //endregion

    //region Users
    private ListenerRegistration userListenerRegistration;

    private void updateUser(User user) {
        userRef.set(user);
    }

    private void raiseUserUpdate() {
        for (IModelUpdate iModelUpdate : iModelUpdates) {
            iModelUpdate.userUpdate();
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

    private void registerUserData() {
        userListenerRegistration = usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "onEvent: users changed " + error.getMessage());
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
                                user1.setProfileImage(user.getProfileImage());
                                user1.setMyChargingStation(user.getMyChargingStation());
                                user1.setName(user.getName());
                                user1.setDocumentId(user1.getDocumentId());
                            }
                        case REMOVED:
                            users.remove(user);
                            break;
                    }
                }
                setCurrentUser();
                raiseUserUpdate();
            }
        });
    }

    //endregion

    //region ChargingStations

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
                            if (station.getUser().getDocumentId().equals(getAuthUser().getUid()))
                                getCurrentUser().setMyChargingStation(station);
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

    public void updateChargingStation(ChargingStation chargingStation) {
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

    private void raiseStationUpdate() {
        for (IModelUpdate iModelUpdate : iModelUpdates) {
            iModelUpdate.stationUpdate();
        }
    }

    //endregion

    //region Rentals
    private ListenerRegistration rentalsListenerRegistration;
    public void updateRental(Rental rental) {
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

    private void raiseRentalUpdate() {
        for (IModelUpdate iModelUpdate : iModelUpdates) {
            iModelUpdate.rentalUpdate();
        }
    }
    //endregion

    //region Image
    public void uploadUserImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference imageRef = storageReference.child(currentUser.getDocumentId()+".jpg");
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        currentUser.setProfileImage(uri.toString());
                        updateUser(currentUser);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: upload image");
            }
        });

    }
//endregion


}
