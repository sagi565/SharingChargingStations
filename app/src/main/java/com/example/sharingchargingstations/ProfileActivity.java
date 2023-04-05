package com.example.sharingchargingstations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.ChargingStationStatus;
import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.User;

public class ProfileActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etCity;
    private EditText etStreet;
    private EditText etHouseNumber;
    private TextView tvMyChargingStation;
    private ImageView btnDeleteChargingStation;
    private Button btnBack;
    private TextView tvItemAddress;
    private TextView tvItemHours;
    private TextView tvItemType;
    private TextView tvItemPricePerHour;
    private LinearLayout llChargingStation;

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

        tvItemAddress = findViewById(R.id.tvItemAddress);
        tvItemHours = findViewById(R.id.tvItemHours);
        tvItemType = findViewById(R.id.tvChargingType);
        tvItemPricePerHour = findViewById(R.id.tvPricePerHour);
        btnDeleteChargingStation = findViewById(R.id.iv_trash);
        llChargingStation = findViewById(R.id.llChargingStation);

        ChargingStation chargingStation = model.getCurrentUser().getMyChargingStation();
        if(chargingStation.getStatus() == ChargingStationStatus.active){
            tvItemType.setText(chargingStation.getType().toString());
            tvItemAddress.setText(chargingStation.getStationAddress().toString());
            tvItemHours.setText(chargingStation.getTime());
            tvItemPricePerHour.setText(chargingStation.getPricePerHour() + "₪");
            btnDeleteChargingStation.setColorFilter(Color.rgb(0,0,0));
            btnDeleteChargingStation.setEnabled(true);
        }
        else{
            tvItemType.setVisibility(View.GONE);
            tvItemAddress.setVisibility(View.GONE);
            tvItemHours.setText("Add Charging Station");
            btnDeleteChargingStation.setColorFilter(Color.rgb(50,50,50));
            tvItemPricePerHour.setVisibility(View.GONE);
            btnDeleteChargingStation.setEnabled(false);
        }

        etName.setText(model.getCurrentUser().getName());
        etCity.setText(model.getCurrentUser().getMyChargingStation().getStationAddress().getCity());
        etStreet.setText(model.getCurrentUser().getMyChargingStation().getStationAddress().getStreet());
        etHouseNumber.setText(model.getCurrentUser().getMyChargingStation().getStationAddress().getHouseNumber());
        btnBack = findViewById(R.id.btnBack);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                model.getCurrentUser().setName(s.toString());
            }
        });
        etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                model.getCurrentUser().getMyChargingStation().getStationAddress().setCity(s.toString());
            }
        });
        etStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                model.getCurrentUser().getMyChargingStation().getStationAddress().setStreet(s.toString());
            }
        });
        etHouseNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                model.getCurrentUser().getMyChargingStation().getStationAddress().setHouseNumber(s.toString());
            }
        });

        btnDeleteChargingStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvItemType.setVisibility(View.GONE);
                tvItemAddress.setVisibility(View.GONE);
                tvItemHours.setText("Add Charging Station");
                btnDeleteChargingStation.setColorFilter(Color.rgb(50,50,50));
                tvItemPricePerHour.setVisibility(View.GONE);
                model.getCurrentUser().getMyChargingStation().setStatus(ChargingStationStatus.canceled);
            }
        });

        llChargingStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().toString() == "" || etCity.getText().toString() == "" || etStreet.getText().toString() == "" || etHouseNumber.getText().toString() == ""){
                    Toast.makeText(ProfileActivity.this, "You need to fill in all the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(getApplicationContext(), AddChargingStationActivity.class);
                i.putExtra("City", etCity.getText().toString());
                i.putExtra("Street", etStreet.getText().toString());
                i.putExtra("HouseNumber", etHouseNumber.getText().toString());
                startActivity(i);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
}