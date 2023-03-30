package com.example.sharingchargingstations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.Rental;
import com.example.sharingchargingstations.Model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;

public class ScheduleActivity extends AppCompatActivity {
    private CalendarView simpleCalendarView;
    private ArrayList<String> hours = new ArrayList<>();
    private Model model = Model.getInstance();
    private ListView lvHours;
    private ArrayAdapter<String> hoursArrayAdapter;
    private ChargingStation chargingStation;
    private int pos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        pos = getIntent().getIntExtra("pos", -1);
        chargingStation = model.getChargingStations().get(pos);
        simpleCalendarView = findViewById(R.id.cvDate);
        long selectedDate = simpleCalendarView.getDate();
        simpleCalendarView.setDate(new Date().getTime());
        simpleCalendarView.setFirstDayOfWeek(1);

        lvHours = findViewById(R.id.lvHours);
        chargingStation = Model.getInstance().getChargingStations().get(pos);
        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                setHoursList(year,month, dayOfMonth);
            }
        });
        hoursArrayAdapter = new ArrayAdapter<String>(this, R.layout.item_hour, hours) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.item_hour, null);
                TextView tvHour = view.findViewById(R.id.tvHour);
                tvHour.setText(hours.get(position));
                if (hours.get(position).length() > 8) tvHour.setBackgroundColor(Color.CYAN);
                return view;
            }
        };
        lvHours.setAdapter(hoursArrayAdapter);

    }

    private void setHoursList(final int year, final int month, final int day) {
        hours.clear();
        Rental rental;
        for (int i = (int) chargingStation.getMinHour(); i < (int) chargingStation.getMaxHour(); i++) {
            final int hour = i;
            rental = model.getRentals().stream()
                    .filter(new Predicate<Rental>() {
                        @Override
                        public boolean test(Rental r) {//todo: need to check station key
                            return r.getStartDate().getYear() == year &&
                                    r.getStartDate().getMonth() == month &&
                                    r.getStartDate().getDate() == day &&
                                    r.getStartDate().getHours() == hour &&
                                    r.getHolderUser().getMyChargingStation() == chargingStation;
                        }
                    })
                    .findAny()
                    .orElse(null);
            String tmp = rental == null ? "" : " Ocupaied";
            hours.add(i + " - " + (i+1));
        }
        hoursArrayAdapter.notifyDataSetChanged();
        Toast.makeText(this, "list count " + hours.size(), Toast.LENGTH_SHORT).show();
    }

}

