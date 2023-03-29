package com.example.sharingchargingstations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.User;

public class StationDetailsActivity extends AppCompatActivity {

    private TextView tvRenterName;
    private TextView tvPricePerHour;
    private TextView tvAvailableHour;
    private TextView tvStationAddress;
    private TextView tvTypeOfChargingStation;
    private TextView tvChargingSpeed;
    private Button btnRent;
    private Button btnBack;


    private ChargingStation chargingStation;
    private User renter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);

        tvRenterName = findViewById(R.id.tvRenterName);
        tvPricePerHour = findViewById(R.id.tvPricePerHour);
        tvAvailableHour = findViewById(R.id.tvAvailableHour);
        tvStationAddress = findViewById(R.id.tvStationAddress);
        tvTypeOfChargingStation = findViewById(R.id.tvTypeStation);
        tvChargingSpeed = findViewById(R.id.tvChargingSpeed);
        btnRent = findViewById(R.id.btnRent);
        btnBack = findViewById(R.id.btnBack);

        Bundle extras = getIntent().getExtras();
        int pos = extras.getInt("pos");
        chargingStation = Model.getInstance().getChargingStations().get(pos);
        renter = Model.getInstance().getUsers().get(pos);

        tvRenterName.setText(renter.getName());
        tvPricePerHour.setText("Price Per Hour: " + String.valueOf(chargingStation.getPricePerHour()));
        tvAvailableHour.setText(chargingStation.getTime());
        tvStationAddress.setText(chargingStation.getStationAddress().toString());
        tvTypeOfChargingStation.setText(chargingStation.getType().toString());
        tvChargingSpeed.setText(String.valueOf(chargingStation.getChargingSpeed()) + " Kilowatt-hour");




    }
}