package com.example.sharingchargingstations;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.Rental;
import com.example.sharingchargingstations.Model.RentalStatus;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DashboardFragment extends Fragment {
    private ArrayList<Rental> rentals = new ArrayList<>();
    private Model model = Model.getInstance();
    private ListView lstRentals;
    private ArrayAdapter<Rental> rentalsArrayAdapter;
    private ArrayAdapter<Rental> filterRentalsArrayAdapter;

    private TextView totalRevenues;
    private TextView totalExpeness;
    private TextView tvItemDate;
    private TextView tvItemHours;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Date currentDate = new Date();
        currentDate.setTime(currentDate.getTime() + 1000*60*60*3);

        for (Rental rental : model.getRentals()) {
            // Get the rentals of the current user
            if (rental.getRenterUser().getDocumentId().equals(model.getCurrentUser().getDocumentId())
                    || rental.getHolderUser().getDocumentId().equals(model.getCurrentUser().getDocumentId())) {
                //pending if start date bigger then now
                //inrent if end date bigger then now
                //done if end date is less then now
                if (rental.getStartDate().getTime() > currentDate.getTime())
                    rental.setStatus(RentalStatus.panding);
                if (rental.getEndDate().getTime() < currentDate.getTime())
                    rental.setStatus(RentalStatus.done);
                if (rental.getStartDate().getTime() < currentDate.getTime() && rental.getEndDate().getTime() > currentDate.getTime())
                    rental.setStatus(RentalStatus.inRent);
                rentals.add(rental);
            }
        }
        lstRentals = view.findViewById(R.id.lvRentals);
        totalRevenues = view.findViewById(R.id.tvTotalRevenues);
        totalExpeness = view.findViewById(R.id.tvTotalExpenses);
        totalRevenues.setText("Revenues: " + String.valueOf(model.getTotalRevenues()).replace(".0", "") + "₪");
        totalExpeness.setText("Expenses: " + String.valueOf(model.getTotalExpenses()).replace(".0", "") + "₪");
        Collections.sort(rentals, Comparator.comparingLong(Rental::getDateInLong));


        filterRentalsArrayAdapter = new ArrayAdapter<Rental>(getActivity(), R.layout.item_rental, rentals) {
            @Override
            public View getView(int position, @Nullable View convertView, ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.item_rental, null);
                tvItemDate = view.findViewById(R.id.tvItemDate);
                tvItemHours = view.findViewById(R.id.tvItemHours);
                TextView tvItemMoney = view.findViewById(R.id.tvItemMoney);
                TextView tvItemRenterUser = view.findViewById(R.id.tvItemRenterUser);
                ImageView imStatus = view.findViewById(R.id.ivStatus);
                Rental rental = getItem(position);

                switch (rental.getStatus()) {
                    case panding:
                        imStatus.setImageResource(R.drawable.ic_panding);
                        break;
                    case inRent:
                        imStatus.setImageResource(R.drawable.ic_in_rent);
                        break;
                    case done:
                        imStatus.setImageResource(R.drawable.ic_done);
                        break;
                }
                imStatus.setColorFilter(Color.rgb(0, 191, 255));
                if (rental.getHolderUser().getDocumentId().equals(model.getCurrentUser().getDocumentId())) {
                    view.setBackgroundColor(Color.rgb(208, 240, 192));
                } else {
                    view.setBackgroundColor(Color.rgb(255, 192, 203));
                }
                tvItemMoney.setTextColor(Color.rgb(0, 191, 255));
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                tvItemDate.setText(formatter.format(rental.getStartDate()));
                tvItemHours.setText(rental.getTime());
                tvItemMoney.setText(String.valueOf(rental.getPrice()).replace(".0", "") + "₪");
                if (rental.getRenterUser().getDocumentId().equals(model.getCurrentUser().getDocumentId()))
                    tvItemRenterUser.setText(rental.getHolderUser().getName());
                else
                    tvItemRenterUser.setText(rental.getRenterUser().getName());


                return view;
            }
        };

        lstRentals.setAdapter(filterRentalsArrayAdapter);

        lstRentals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rental rental = rentals.get(position);
                int pos = model.getRentals().indexOf(rental);
                Intent i = new Intent(getActivity(), RentalHistoryActivity.class);
                i.putExtra("pos", pos);
                i.putExtra("time", rental.getDate() + " | " + rental.getTime());
                startActivity(i);
            }
        });
        //setFilter();

        model.registerModelUpdate(new Model.IModelUpdate() {
            @Override
            public void userUpdate() {

            }

            @Override
            public void stationUpdate() {

            }

            @Override
            public void rentalUpdate() {

            }
        });

        return view;

    }
}
