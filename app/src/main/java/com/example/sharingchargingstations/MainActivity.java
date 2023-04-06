package com.example.sharingchargingstations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.ChargingStationStatus;
import com.example.sharingchargingstations.Model.Model;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ChargingStation> filterChargingStations = new ArrayList<>();
    private Model model = Model.getInstance();
    private EditText etSearch;
    private ListView lstStations;
    private ArrayAdapter<ChargingStation> chargingStationArrayAdapter;
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
                TextView tvItemType = view.findViewById(R.id.tvChargingType);
                TextView tvItemPrice = view.findViewById(R.id.tvItemPrice);

                ImageView ivChargingStation = view.findViewById(R.id.ivChargingStation);


                ChargingStation chargingStation = getItem(position);
                tvItemType.setText(chargingStation.getType().toString());
                tvItemAddress.setText(chargingStation.getStationAddress().toString());
                tvItemHours.setText(chargingStation.getTime());
                tvItemPrice.setText(chargingStation.getPricePerHour() + "₪");


                switch (tvItemType.getText().toString()){
                    case "PP":
                        ivChargingStation.setColorFilter(Color.rgb(147, 197, 114));
                        break;
                    case "CP":
                        ivChargingStation.setColorFilter(Color.rgb(135,206,235));
                        break;
                    case "CCS2":
                        ivChargingStation.setColorFilter(Color.rgb(240,128,128));
                        break;
                    case "CHAdeMO":
                        ivChargingStation.setColorFilter(Color.rgb(255, 234, 0));

                        tvItemType.setTextSize(14);
                        break;
                }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_user:
                Intent intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intentProfile);
                return true;
            case R.id.action_rentals:
                Intent intentRentals = new Intent(getApplicationContext(), RentalsActivity.class);
                startActivity(intentRentals);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    private void setFilter(String filter){
        filterChargingStations.clear();
        for(ChargingStation chargingStation : model.getChargingStations()){
            if (chargingStation.getStationAddress().toString().toLowerCase().contains(filter.toLowerCase())
                    && chargingStation.getStatus() == ChargingStationStatus.active
                    && chargingStation != model.getCurrentUser().getMyChargingStation()){
                filterChargingStations.add(chargingStation);
            }
        }
        chargingStationArrayAdapter.notifyDataSetChanged();
    }
}