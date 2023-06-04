package com.example.sharingchargingstations;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private TextView tvDescription;

    private Button btnRent;

    private Model model = Model.getInstance();
    private ChargingStation chargingStation;
    private User user;
    private int pos;


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
        tvDescription = findViewById(R.id.tvDescription);
        LinearLayout LinearLayout = findViewById(R.id.linear_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable)LinearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        btnRent = findViewById(R.id.btnRent);


        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos");
        chargingStation = Model.getInstance().getChargingStations().get(pos);
        user = chargingStation.getUser();

        tvRenterName.setText(user.getName());
        tvPricePerHour.setText(String.valueOf(chargingStation.getPricePerHour()).replace(".0", "") + "₪");
        tvAvailableHour.setText(chargingStation.getTime());
        tvStationAddress.setText(chargingStation.getStationAddress().toString());
        tvTypeOfChargingStation.setText(chargingStation.getType().toString());
        tvChargingSpeed.setText(String.valueOf(chargingStation.getChargingSpeed()) + " Kilowatt-hour");
        tvDescription.setText(chargingStation.getDescription());

        btnRent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ScheduleActivity.class);
                i.putExtra("pos", pos);
                startActivity(i);
                finish();
            }
        });

    }

}