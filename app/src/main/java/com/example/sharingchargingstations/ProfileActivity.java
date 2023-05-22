package com.example.sharingchargingstations;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharingchargingstations.Model.ChargingStation;
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



        model.registerModelUpdate(this);

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
    @Override
    public void rentalUpdate() {

    }
}