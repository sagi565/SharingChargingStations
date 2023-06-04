package com.example.sharingchargingstations;

import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.Model;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, Model.IModelUpdate {
    private ArrayList<ChargingStation> filterChargingStations = new ArrayList<>();
    private Model model = Model.getInstance();
    private EditText etSearch;
    private ImageView ivSearch;

    private ListView lstStations;
    private TextView tvTitle;
    private ArrayAdapter<ChargingStation> chargingStationArrayAdapter;
    private Dialog searchDialog;
    private FrameLayout frameLayout;
    private Dialog userDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Home charging station rentals");
        setContentView(R.layout.activity_main);
        LinearLayout LinearLayout = findViewById(R.id.linear_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) LinearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        model.setContext(getApplicationContext());
        userDialog = new Dialog(this);
        userDialog.setContentView(R.layout.dialog_user);
        userDialog.setCanceledOnTouchOutside(true);
        userDialog.setCancelable(true);
        showUserDialog();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SearchFragment()).commit();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);

        model.registerModelUpdate(this);

    }

    @Override
    public void onBackPressed() {
        if (!userDialog.isShowing()) super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                model.signOut();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showUserDialog() {
        if (model.getAuthUser() == null) {
            userDialog = new Dialog(this);

            userDialog.setContentView(R.layout.dialog_user);
            userDialog.setCanceledOnTouchOutside(true);
            userDialog.setCancelable(true);

            userDialog.show();
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

            userDialog.getWindow().setLayout(width, height);
            Switch aSwitch = userDialog.findViewById(R.id.swtchState);
            EditText etFullName = userDialog.findViewById(R.id.etDialogFullName);
            EditText etEmail = userDialog.findViewById(R.id.etDialogEmail);
            ImageView ivDialogImage = userDialog.findViewById(R.id.ivDialogImage);

            EditText etPassword = userDialog.findViewById(R.id.etDialogPassword);
            etFullName.setVisibility(View.GONE);
            Button btnSubmit = userDialog.findViewById(R.id.btnDialogDone);
            //ivDialogImage.setVisibility(View.VISIBLE);
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    etFullName.setVisibility(b ? View.VISIBLE : View.GONE);
                    ivDialogImage.setVisibility(View.VISIBLE);

                    btnSubmit.setText(b ? "Create" : "Sign in");
                }
            });
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (aSwitch.isChecked()) {
                        model.createUser(etEmail.getText().toString(), etPassword.getText().toString(), etFullName.getText().toString());
                    } else {
                        model.login(etEmail.getText().toString(), etPassword.getText().toString());
                    }

                    userDialog.dismiss();
                }
            });

//            ivDialogImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    openCamera();
//                }
//            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
                return true;
            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                return true;
            case R.id.navigation_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DashboardFragment()).commit();
                return true;
        }
        return false;
    }

    @Override
    public void userUpdate() {
        showUserDialog();
    }

    @Override
    public void stationUpdate() {
    }

    @Override
    public void rentalUpdate() {

    }
}