package com.example.sharingchargingstations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.Rental;
import com.example.sharingchargingstations.Model.User;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private EditText edName;
    private TextView tvMyChargingStation;
    private Button btnDeleteChargingStation;
    private Button btnEditChargingStation;
    private Button btnAddChargingStation;
    private TextView tvMyCurrentRental;
    private Button btnDeleteMyCurrentRental;
    private Button tvEditCurrentRental;
    private TextView totalRevenues;
    private TextView totalExpeness;
    private ArrayList<Rental> rentalsHistory = new ArrayList<>();
    private ListView lvRentalsHistory;
    private ArrayAdapter<Rental> rentalsHistoryArrayAdapter;

    private Model model = Model.getInstance();
    private User currentUser = model.getCurrentUser();
    private String Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edName = findViewById(R.id.etName);
        tvMyChargingStation = findViewById(R.id.tvChargingStation);
        btnDeleteChargingStation = findViewById(R.id.btnDeleteChargingStation);
        btnEditChargingStation = findViewById(R.id.btnEditChargingStation);
        btnAddChargingStation = findViewById(R.id.btnAddChargingStation);
        tvMyCurrentRental = findViewById(R.id.tvCurrentRental);
        btnDeleteMyCurrentRental = findViewById(R.id.btnDeleteCurrentRental);
        tvEditCurrentRental = findViewById(R.id.btnEditCurrentRental);
        totalRevenues = findViewById(R.id.tvTotalRevenues);
        totalExpeness = findViewById(R.id.tvTotalExpenses);
        lvRentalsHistory = findViewById(R.id.lvRentalsHistory);

        edName.setText(model.getCurrentUser().getName());
        totalRevenues.setText("Total Revenues: " + String.valueOf(currentUser.getTotalRevenues()));
        totalExpeness.setText("Total Expenses: " + String.valueOf(currentUser.getTotalExpeness()));

        if(currentUser.getMyChargingStation() == null){
            tvMyChargingStation.setText("Empty");
        }
        else{
            tvMyChargingStation.setText(currentUser.getMyChargingStation().toString());
        }
        btnDeleteChargingStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.setMyChargingStation(null);
                tvMyChargingStation.setText("Empty");

            }
        });
        btnEditChargingStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddChargingStationActivity.class);
                startActivity(intent);
            }
        });
        edName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){
                    edName.setText("");
                    model.getCurrentUser().setName(s.toString());
                }
            }

        });
    }
}