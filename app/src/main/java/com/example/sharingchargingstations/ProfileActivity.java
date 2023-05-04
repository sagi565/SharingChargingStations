package com.example.sharingchargingstations;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.ChargingStationStatus;
import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.User;

public class ProfileActivity extends AppCompatActivity implements Model.IModelUpdate {
    private EditText etName;
    private EditText etCity;
    private EditText etStreet;
    private EditText etHouseNumber;
    private TextView tvMyChargingStation;
    private ImageView btnDeleteChargingStation;
    private Button btnBack;
    private Button btnSignOut;

    private TextView tvItemAddress;
    private TextView tvItemHours;
    private TextView tvItemType;
    private TextView tvItemPricePerHour;
    private LinearLayout llChargingStation;

    private Model model = Model.getInstance();
    private User currentUser = model.getCurrentUser();
    private ChargingStation chargingStation;


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

        chargingStation = model.getCurrentUser().getMyChargingStation();
        if(chargingStation != null && chargingStation.getStatus() == ChargingStationStatus.active){
            updateUiFields();
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

        btnBack = findViewById(R.id.btnBack);
        btnSignOut = findViewById(R.id.btnSignOut);




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
                startActivityForResult(i,1);
            }
        });




        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.signOut();
                finish();
            }
        });

        model.registerModelUpdate(this);

    }

    private void updateUiFields(){
        tvItemType.setText(chargingStation.getType().toString());
        tvItemAddress.setText(chargingStation.getStationAddress().toString());
        tvItemHours.setText(chargingStation.getTime());
        tvItemPricePerHour.setText(chargingStation.getPricePerHour() + "₪");
        btnDeleteChargingStation.setColorFilter(Color.rgb(0,0,0));
        btnDeleteChargingStation.setEnabled(true);
        etCity.setText(model.getCurrentUser().getMyChargingStation().getStationAddress().getCity());
        etStreet.setText(model.getCurrentUser().getMyChargingStation().getStationAddress().getStreet());
        etHouseNumber.setText(model.getCurrentUser().getMyChargingStation().getStationAddress().getHouseNumber());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK)
            updateUiFields();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.unRegisterModelUpdate(this);
    }

    @Override
    public void userUpdate() {

    }

    @Override
    public void stationUpdate() {

    }
}