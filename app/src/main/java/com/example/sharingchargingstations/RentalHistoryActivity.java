package com.example.sharingchargingstations;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.Rental;

public class RentalHistoryActivity extends AppCompatActivity {
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvStationAddress;
    private TextView tvPricePerHour;
    private TextView tvTotalPrice;
    private TextView tvPeopleName;
    private ImageView ivStatus;


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
        tvTitle = findViewById(R.id.tvRentalHistory);
        LinearLayout LinearLayout = findViewById(R.id.linear_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable)LinearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

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
        }
        ivStatus.setColorFilter(Color.rgb(0,0,139));

        tvStationAddress.setText(rental.getChargingStation().getStationAddress().toString());
        tvPricePerHour.setText("Price Per Hour: " + String.valueOf(rental.getChargingStation().getPricePerHour()).replace(".0", "") + "₪");

        tvTotalPrice.setText("Total Price: " + String.valueOf(rental.getPrice()).replace(".0", "") + "₪");

        if(rental.getHolderUser().getDocumentId().equals(model.getCurrentUser().getDocumentId())){
            tvPeopleName.setText("Renter: " + rental.getRenterUser().getName());
        }
        else{
            tvPeopleName.setText("Owner: " + rental.getHolderUser().getName());
        }
    }
}