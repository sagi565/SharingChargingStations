package com.example.sharingchargingstations;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.Rental;

public class RentalHistoryActivity extends AppCompatActivity {
    private TextView tvTime;
    private TextView tvStationAddress;
    private TextView tvPricePerHour;
    private TextView tvTotalPrice;
    private TextView tvPeopleName;
    private ImageView ivStatus;
    private Button btnBack;


    private Model model = Model.getInstance();
    private Rental rental;
    private int pos;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_history);

        tvTime = findViewById(R.id.tvTime);
        tvStationAddress = findViewById(R.id.tvStationAddress);
        tvPricePerHour = findViewById(R.id.tvPricePerHour);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvPeopleName = findViewById(R.id.tvRenterName);
        ivStatus = findViewById(R.id.ivStatus);
        btnBack = findViewById(R.id.btnBack);

        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos");
        time = extras.getString("time");

        rental = model.getRentals().get(pos);
        tvTime.setText(time);
        switch (rental.getStatus()){
            case panding:
                ivStatus.setImageResource(R.drawable.ic_panding);
                break;
            case inRent:
                ivStatus.setImageResource(R.drawable.ic_in_rent);
                break;
            case done:
                ivStatus.setImageResource(R.drawable.ic_done);
                break;
            case canceled:
                ivStatus.setImageResource(R.drawable.ic_canceled);
                break;
        }
        tvStationAddress.setText(rental.getChargingStation().getStationAddress().toString());
        tvPricePerHour.setText("Price Per Hour: " + String.valueOf(rental.getChargingStation().getPricePerHour()) + "₪");
        tvTotalPrice.setText("Total Price: " + String.valueOf(rental.getPrice()) + "₪");

        if(rental.getHolderUser() == model.getCurrentUser()){
            tvPeopleName.setText("Owner: You  -  Renter: " + rental.getRenterUser().getName());
            tvTotalPrice.setTextColor(Color.rgb(50,205,50)); // green
        }
        else{
            tvPeopleName.setText("Owner: " + model.getCurrentUser().getName() + "  -  Renter: You");
            tvTotalPrice.setTextColor(Color.rgb(30,144,255)); // blue
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RentalsActivity.class));
            }
        });



    }
}