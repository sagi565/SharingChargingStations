package com.example.sharingchargingstations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

public class AddChargingStationActivity extends AppCompatActivity {

    Spinner sChargingType=findViewById(R.id.s_chargingType);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_charging_station);
    }
}