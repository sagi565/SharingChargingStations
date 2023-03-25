package com.example.sharingchargingstations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.Model;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ChargingStation> filterChargingStations = new ArrayList<>();
    private Model model = Model.getInstance();
    private EditText etSearch;
    private ListView lstStations;
    private ArrayAdapter<ChargingStation> chargingStationArrayAdapter;
    private ImageView ivProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstStations = findViewById(R.id.lstStations);

        chargingStationArrayAdapter = new ArrayAdapter<ChargingStation>(this, R.layout.item_charging_station,filterChargingStations){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  getLayoutInflater().inflate(R.layout.item_charging_station,null);
                TextView tvItemAddress = view.findViewById(R.id.tvItemAddress);
                TextView tvItemHours = view.findViewById(R.id.tvItemHours);

                ChargingStation chargingStation = getItem(position);
                tvItemAddress.setText(chargingStation.getStationAddress().toString());
                tvItemHours.setText(chargingStation.getTime());

                return view;
            }
        };
        setFilter("");
        lstStations.setAdapter(chargingStationArrayAdapter);
        etSearch = findViewById(R.id.etSearchStation);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        lstStations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChargingStation chargingStation = filterChargingStations.get(position);
                int pos = model.getChargingStations().indexOf(chargingStation);
                Intent i = new Intent(getApplicationContext(), StationDetailsActivity.class);
                i.putExtra("pos", pos);
                startActivity(i);
            }
        });
    }
    private void setFilter(String filter){
        filterChargingStations.clear();
        for(ChargingStation chargingStation : model.getChargingStations()){
            System.out.println("sagi" + chargingStation);
            if (chargingStation.getStationAddress().toString().toLowerCase().contains(filter.toLowerCase())){
                filterChargingStations.add(chargingStation);

            }
        }
        chargingStationArrayAdapter.notifyDataSetChanged();
    }

}