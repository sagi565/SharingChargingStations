package com.example.sharingchargingstations;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.ChargingStationStatus;
import com.example.sharingchargingstations.Model.Model;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ChargingStation> filterChargingStations = new ArrayList<>();
    private Model model = Model.getInstance();
    private EditText etSearch;
    private ImageView ivSearch;

    private ListView lstStations;
    private TextView tvTitle;
    private ArrayAdapter<ChargingStation> chargingStationArrayAdapter;
    private Dialog searchDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showUserDialog();
        lstStations = findViewById(R.id.lstStations);
        tvTitle = findViewById(R.id.tvTitle);
        ivSearch = findViewById(R.id.ivSearch);

        tvTitle.setText("Hello " + model.getCurrentUser().getName());

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });

        model.setContext(getApplicationContext());
        model.registerModelUpdate(new Model.IModelUpdate() {
            @Override
            public void userUpdate() {
                //when user in doing signout  we should show user dialog for sign in
                showUserDialog();
            }

            @Override
            public void stationUpdate() {
                setFilter(etSearch.getText().toString().trim());
                chargingStationArrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void rentalUpdate() {

            }
        });
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
                Intent intentProfile = new Intent(this, ProfileActivity.class);

                startActivityForResult(intentProfile, 10);
                return true;
            case R.id.action_rentals:
                Intent intentRentals = new Intent(this, RentalsActivity.class);
                startActivity(intentRentals);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showSearchDialog()
    {
        searchDialog = new Dialog(this);
        searchDialog.setContentView(R.layout.dialog_search);
        searchDialog.setTitle("Search");
        searchDialog.show();


        EditText etMinPrice = searchDialog.findViewById(R.id.etMinPrice);
        EditText etMaxPrice = searchDialog.findViewById(R.id.etMaxPrice);

        EditText etTime = searchDialog.findViewById(R.id.etTime);
        Spinner sType = searchDialog.findViewById(R.id.sType);
        EditText etMinSpeed = searchDialog.findViewById(R.id.etMinChargingSpeed);
        EditText etMaxSpeed = searchDialog.findViewById(R.id.etMaxChargingSpeed);
        EditText etContainsDescription = searchDialog.findViewById(R.id.etContainsDescription);
        Button btnSearch = searchDialog.findViewById(R.id.btnSearch);

        String[] types = new String[]{"Select Type","PP", "CP", "CHAdeMO", "CCS2"};
        ArrayAdapter<String> typesArrayAdapter;
        sType.setPrompt("");


        typesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        sType.setAdapter(typesArrayAdapter);

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(etTime.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (minute >= 30)
                            hourOfDay++;

                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, 0);
                        etTime.setText(selectedTime);
                    }
                }, 24, 0, true);
                timePickerDialog.setCanceledOnTouchOutside(false);
                timePickerDialog.setTitle("Select a round hour");
                timePickerDialog.show();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!etMinPrice.getText().toString().equals("") && !etMaxPrice.getText().toString().equals("") && Integer.parseInt(etMinPrice.getText().toString()) > Integer.parseInt(etMaxPrice.getText().toString())){
                    Toast.makeText(MainActivity.this, "Min Price is more than max price", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!etMinSpeed.getText().toString().equals("") && !etMaxSpeed.getText().toString().equals("") && Integer.parseInt(etMinSpeed.getText().toString()) > Integer.parseInt(etMaxSpeed.getText().toString())){
                    Toast.makeText(MainActivity.this, "Min speed is more than max speed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(etMinPrice.getText().toString().equals(""))
                    etMinPrice.setText("-1");
                if(etMaxPrice.getText().toString().equals(""))
                    etMaxPrice.setText("1000");

                if(etMinSpeed.getText().toString().equals(""))
                    etMinSpeed.setText("-1");
                if(etMaxSpeed.getText().toString().equals(""))
                    etMaxSpeed.setText("1000");

                if(etTime.getText().toString().equals(""))
                    etTime.setText("-10");

                searchDialog.dismiss();
                setAdvancedFilter(Double.valueOf(etMinPrice.getText().toString()), Double.valueOf(etMaxPrice.getText().toString()), Integer.parseInt(etTime.getText().subSequence(0,2).toString())
                ,sType.getSelectedItem().toString(), Double.valueOf(etMinSpeed.getText().toString()), Double.valueOf(etMaxSpeed.getText().toString())
                ,etContainsDescription.getText().toString());

            }
        });


    }
    private void setAdvancedFilter(double minPrice, double maxPrice, int time, String type, double minSpeed, double maxSpeed, String ContainsDescription){
        filterChargingStations.clear();
        for(ChargingStation chargingStation : model.getChargingStations()){
            if (chargingStation.getPricePerHour() >= minPrice
                && chargingStation.getPricePerHour() <= maxPrice
                && (time == -1 || (time >= chargingStation.getStartHour() && time <= chargingStation.getEndHour()))
                && (type == "Select Type" || type.equals(chargingStation.getType().toString()))
                && minSpeed <= chargingStation.getChargingSpeed()
                && maxSpeed >= chargingStation.getChargingSpeed()
                && chargingStation.getDescription().contains(ContainsDescription)
                && chargingStation.getStatus() == ChargingStationStatus.active
                && chargingStation != model.getCurrentUser().getMyChargingStation()){
                filterChargingStations.add(chargingStation);
            }
        }
        chargingStationArrayAdapter.notifyDataSetChanged();

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

    private void showUserDialog(){
        if (model.getAuthUser() == null){
            Dialog userDialog = new Dialog(this);
            userDialog.setContentView(R.layout.dialog_user);

            userDialog.show();
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

            userDialog.getWindow().setLayout(width, height);
            Switch aSwitch = userDialog.findViewById(R.id.swtchState);
            EditText etFullName = userDialog.findViewById(R.id.etDialogFullName);
            EditText etEmail = userDialog.findViewById(R.id.etDialogEmail);
            etEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String txt = editable.toString();
                    if (Patterns.EMAIL_ADDRESS.matcher(txt).matches())
                    {
                        Toast.makeText(MainActivity.this, "Email is not valid", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            EditText etPassword = userDialog.findViewById(R.id.etDialogPassword);
            etFullName.setVisibility(View.GONE);
            Button btnSubmit = userDialog.findViewById(R.id.btnDialogDone);
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    etFullName.setVisibility(b ? View.VISIBLE : View.GONE);
                    btnSubmit.setText(b? "Create" : "Sign in");
                }
            });
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (aSwitch.isChecked()){
                        model.createUser(etEmail.getText().toString(), etPassword.getText().toString(), etFullName.getText().toString());
                    }
                    else
                    {
                        model.login(etEmail.getText().toString(), etPassword.getText().toString());
                    }
                    userDialog.dismiss();
                }
            });
//            ivDialogImage = userDialog.findViewById(R.id.ivDialogImage);
//            ivDialogImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    openCamera();
//                }
//            });
        }
    }
}