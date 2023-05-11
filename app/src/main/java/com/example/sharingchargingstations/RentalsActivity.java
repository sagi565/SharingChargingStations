package com.example.sharingchargingstations;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.Rental;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RentalsActivity extends AppCompatActivity{
    private ArrayList<Rental> rentals = new ArrayList<>();
    private Model model = Model.getInstance();
    private ListView lstRentals;
    private ArrayAdapter<Rental> rentalsArrayAdapter;
    private TextView totalRevenues;
    private TextView totalExpeness;
    private TextView tvItemDate;
    private TextView tvItemHours;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentals);
        rentals = model.getRentals();
        lstRentals = findViewById(R.id.lvRentals);
        totalRevenues = findViewById(R.id.tvTotalRevenues);
        totalExpeness = findViewById(R.id.tvTotalExpenses);
        totalRevenues.setText("Revenues: " + String.valueOf(model.getTotalRevenues()) + "₪");
        totalExpeness.setText("Expenses: " + String.valueOf(model.getTotalExpenses()) + "₪");
        btnBack = findViewById(R.id.btnBack);


        Collections.sort(rentals, Comparator.comparingLong(Rental::getDateInLong));

        rentalsArrayAdapter = new ArrayAdapter<Rental>(this, R.layout.item_rental,rentals){
            @Override
            public View getView(int position, @Nullable View convertView, ViewGroup parent) {
                View view =  getLayoutInflater().inflate(R.layout.item_rental,null);
                tvItemDate = view.findViewById(R.id.tvItemDate);
                tvItemHours = view.findViewById(R.id.tvItemHours);
                TextView tvItemMoney = view.findViewById(R.id.tvItemMoney);
                TextView tvItemRenterUser = view.findViewById(R.id.tvItemRenterUser);
                ImageView imStatus = view.findViewById(R.id.ivStatus);
                Rental rental = getItem(position);

                switch (rental.getStatus()){
                    case panding:
                        imStatus.setImageResource(R.drawable.ic_panding);
                        break;
                    case inRent:
                        imStatus.setImageResource(R.drawable.ic_in_rent);
                        break;
                    case done:
                        imStatus.setImageResource(R.drawable.ic_done);
                        break;
                    case canceled:
                        imStatus.setImageResource(R.drawable.ic_canceled);
                        break;
                }
                if(rental.getHolderUser().getDocumentId().equals(model.getCurrentUser().getDocumentId())){
                    view.setBackgroundColor(Color.rgb(208, 240, 192));

                }
                else
                {
                    view.setBackgroundColor(Color.rgb(135,206,235));

                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                tvItemDate.setText(formatter.format(rental.getStartDate()));
                tvItemHours.setText(rental.getTime());
                tvItemMoney.setText(rental.getPrice() + "₪");
                tvItemRenterUser.setText(rental.getRenterUser().getName());

                return view;
            }
        };
        lstRentals.setAdapter(rentalsArrayAdapter);

        lstRentals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rental rental = rentals.get(position);
                int pos = model.getRentals().indexOf(rental);
                Intent i = new Intent(getApplicationContext(), RentalHistoryActivity.class);
                i.putExtra("pos", pos);
                i.putExtra("time", rental.getDate() + " | " + rental.getTime());
                startActivity(i);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        model.registerModelUpdate(new Model.IModelUpdate() {
            @Override
            public void userUpdate() {

            }

            @Override
            public void stationUpdate() {

            }
            @Override
            public void rentalUpdate() {
                //setList();
            }
        });


    }
    /*private void setList(){
        for(Rental rental : model.getRentals()){
            rentals.add(rental);
        }
        rentalsArrayAdapter.notifyDataSetChanged();
    }*/


}