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
import android.widget.Toast;

import com.example.sharingchargingstations.Model.Address;
import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.ChargingStationStatus;
import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.TypeChargingStation;

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
    private String[] types = new String[]{"PP", "CP", "CHAdeMO", "CCS2"};
    private ArrayAdapter<String> typesArrayAdapter;
    private Model model = Model.getInstance();

    private String city;
    private String street;
    private String houseNumber;

    private ChargingStation myChargingStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_charging_station);

        city = getIntent().getStringExtra("City");
        street = getIntent().getStringExtra("Street");
        houseNumber = getIntent().getStringExtra("HouseNumber");



        sType = findViewById(R.id.sType);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        etPricePerHour = findViewById(R.id.etPricePerHour);
        etStartTime = findViewById(R.id.etStartTime);
        etEndTime = findViewById(R.id.etEndTime);
        etChargingSpeed = findViewById(R.id.etChargingSpeed);
        etDescription = findViewById(R.id.etDescription);


        typesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        sType.setAdapter(typesArrayAdapter);
        myChargingStation = model.getCurrentUser().getMyChargingStation();

        if(model.getCurrentUser().getMyChargingStation().getStatus() == ChargingStationStatus.active){
            etPricePerHour.setText(myChargingStation.getPricePerHour() + "₪");
            etStartTime.setText(myChargingStation.getTime(myChargingStation.getStartHour()));
            etEndTime.setText(myChargingStation.getTime(myChargingStation.getEndHour()));
            sType.setSelection(typesArrayAdapter.getPosition(myChargingStation.getType().toString()));
            etChargingSpeed.setText(String.valueOf(myChargingStation.getPricePerHour()));
            etDescription.setText(myChargingStation.getDescription());
        }

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(etPricePerHour.getText().toString().length() == 0 ||
                    etStartTime.getText().toString().length() == 0 ||
                    etEndTime.getText().toString().length() == 0 ||
                    etChargingSpeed.getText().toString().length() == 0){
                Toast.makeText(AddChargingStationActivity.this, "You need to fill in all the details", Toast.LENGTH_SHORT).show();
                return;
            }
            if(Float.parseFloat(etStartTime.getText().toString().substring(0, 2)) > Float.parseFloat(etEndTime.getText().toString().substring(0, 2))){
                Toast.makeText(AddChargingStationActivity.this, "Start time is after end time", Toast.LENGTH_SHORT).show();
                return;
            }
            ChargingStation c = new ChargingStation(Double.parseDouble(etPricePerHour.getText().toString().replace('₪', ' ')),
                    Float.parseFloat(etStartTime.getText().toString().substring(0, 2)),
                    Float.parseFloat(etEndTime.getText().toString().substring(0, 2)),
                    new Address(city, street, houseNumber),
                    TypeChargingStation.valueOf(sType.getSelectedItem().toString()),
                    Double.valueOf(etChargingSpeed.getText().toString()),
                    etDescription.getText().toString());

            for(int i = 0; i < model.getChargingStations().size(); i++){
                if(model.getChargingStations().get(i) ==  model.getCurrentUser().getMyChargingStation()){
                    model.getChargingStations().remove(i);
                    model.getChargingStations().add(c);
                }
            }
            model.getCurrentUser().setMyChargingStation(c);
            model.getCurrentUser().getMyChargingStation().setStatus(ChargingStationStatus.active);
            finish();
            }
        });
    }
}