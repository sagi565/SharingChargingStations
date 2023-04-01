package com.example.sharingchargingstations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddChargingStationActivity extends AppCompatActivity {

    private EditText etPricePerHour;
    private EditText etStartTime;
    private EditText etEndTime;
    private Button btnStartTime;
    private Button btnEndTime;
    private Spinner sType;
    private EditText etChargingSpeed;
    private EditText etDescription;
    private Button btnSave;
    private Button btnBack;
    String[] types = new String[]{"PP", "CP", "CHAdeMO", "CCS2"};
    private ArrayAdapter<String> typesArrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_charging_station);

        sType = findViewById(R.id.sType);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        etPricePerHour = findViewById(R.id.etPricePerHour);
        etStartTime = findViewById(R.id.etStartTime);
        etEndTime = findViewById(R.id.etEndTime);
        etChargingSpeed = findViewById(R.id.etChargingSpeed);
        btnStartTime = findViewById(R.id.btnStartTime);
        btnEndTime = findViewById(R.id.btnEndTime);

        typesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        sType.setAdapter(typesArrayAdapter);



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
    }
}