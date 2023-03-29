package com.example.sharingchargingstations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.Rental;

import java.util.ArrayList;

public class RentalsActivity extends AppCompatActivity {
    private ArrayList<Rental> rentals = new ArrayList<>();
    private Model model = Model.getInstance();
    private ListView lstRentals;
    private ArrayAdapter<Rental> rentalsArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentals);
        rentals = model.getRentals();
        lstRentals = findViewById(R.id.lstStations);

        rentalsArrayAdapter = new ArrayAdapter<Rental>(this, R.layout.item_rental,rentals){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  getLayoutInflater().inflate(R.layout.item_rental,null);
                TextView tvItemAddress = view.findViewById(R.id.tvItemAddress);
                TextView tvItemHours = view.findViewById(R.id.tvItemHours);
                TextView tvItemMoney = view.findViewById(R.id.tvItemMoney);
                TextView tvItemRenterUser = view.findViewById(R.id.tvItemRenterUser);

                Rental rental = getItem(position);
                tvItemAddress.setText(rental.getHolderUser().getMyChargingStation().getStationAddress().toString());
                tvItemHours.setText(rental.getHolderUser().getMyChargingStation().getTime());
                tvItemMoney.setText((int) rental.getPrice());
                tvItemRenterUser.setText(rental);

                return view;
            }
        };
    }
}