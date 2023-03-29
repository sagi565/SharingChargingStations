package com.example.sharingchargingstations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.User;

public class ProfileActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etCity;
    private EditText etStreet;
    private EditText etHouseNumber;
    private TextView tvMyChargingStation;
    private Button btnDeleteChargingStation;
    private Button btnEditChargingStation;
    private Button btnAddChargingStation;

    private Model model = Model.getInstance();
    private User currentUser = model.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.etName);
        etCity = findViewById(R.id.etCity);
        etStreet = findViewById(R.id.etStreet);
        etHouseNumber = findViewById(R.id.etHouseNumber);

        tvMyChargingStation = findViewById(R.id.tvChargingStation);
        btnDeleteChargingStation = findViewById(R.id.btnDeleteChargingStation);
        btnEditChargingStation = findViewById(R.id.btnEditChargingStation);
        btnAddChargingStation = findViewById(R.id.btnAddChargingStation);

        etName.setText(model.getCurrentUser().getName());
        etCity.setText(model.getCurrentUser().getMyChargingStation().getStationAddress().getCity());
        etStreet.setText(model.getCurrentUser().getMyChargingStation().getStationAddress().getStreet());
        etHouseNumber.setText(model.getCurrentUser().getMyChargingStation().getStationAddress().getHouseNumber());

    }
}