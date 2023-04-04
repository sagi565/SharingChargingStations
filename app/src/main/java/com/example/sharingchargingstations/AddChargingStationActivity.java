package com.example.sharingchargingstations;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Locale;

public class AddChargingStationActivity extends AppCompatActivity {

    private EditText etPricePerHour;
    private EditText etStartTime;
    private EditText etEndTime;
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

        typesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        sType.setAdapter(typesArrayAdapter);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(etStartTime.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (minute >= 30)
                            hourOfDay++;

                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, 0);
                        etStartTime.setText(selectedTime);
                    }
                }, 24, 0, true);
                timePickerDialog.setCanceledOnTouchOutside(false);
                timePickerDialog.setTitle("Select a round hour");
                timePickerDialog.show();
            }
        });
        etEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(etStartTime.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (minute >= 30)
                            hourOfDay++;

                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, 0);
                        etEndTime.setText(selectedTime);
                    }
                }, 24, 0, true);
                timePickerDialog.setCanceledOnTouchOutside(false);
                timePickerDialog.setTitle("Select a round hour");
                timePickerDialog.show();
            }
        });
    }
}