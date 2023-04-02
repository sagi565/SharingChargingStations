package com.example.sharingchargingstations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.Rental;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;

public class ScheduleActivity extends AppCompatActivity {
    private CalendarView simpleCalendarView;
    private ArrayList<OrderHour> hours = new ArrayList<>();
    private Model model = Model.getInstance();
    private ListView lvHours;
    private ArrayAdapter hoursArrayAdapter;
    private ChargingStation chargingStation;
    private int pos;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        pos = getIntent().getIntExtra("pos", -1);
        chargingStation = model.getChargingStations().get(pos);

        simpleCalendarView = findViewById(R.id.cvDate);
        long selectedDate = simpleCalendarView.getDate();


        lvHours = findViewById(R.id.lvHours);
        chargingStation = Model.getInstance().getChargingStations().get(pos);
        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                setHoursList(year,month + 1, dayOfMonth);
                lvHours.setAdapter(hoursArrayAdapter);

            }
        });
        hoursArrayAdapter = new ArrayAdapter<OrderHour>(this, R.layout.item_hour, hours) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.item_hour, null);
                TextView tvHour = view.findViewById(R.id.tvHour);
                tvHour.setText(hours.get(position).toString());
                if (hours.get(position).hourStatus == HourStatus.occupied) tvHour.setBackgroundColor(Color.rgb(240,128,128));
                if (hours.get(position).hourStatus == HourStatus.selected) tvHour.setBackgroundColor(Color.rgb(135,206,235));

                return view;
            }
        };

        lvHours.setAdapter(hoursArrayAdapter);
        lvHours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < hours.size() ; i++) {
                    OrderHour orderHour = hours.get(position);
                    if (orderHour.hourStatus != HourStatus.occupied)
                        orderHour.hourStatus = (i == position) ? HourStatus.selected : HourStatus.free;
                }

                hoursArrayAdapter.notifyDataSetChanged();
            }
        });
        Date date = new Date();
        date.setTime(date.getTime());
        setHoursList(date.getYear() + 1900, date.getMonth() + 1, date.getDate());



    }

    private void setHoursList(final int year, final int month, final int day) {
        final int dateYear = year - 1900; //Java date year is since 1900 (2023 is 123)
        final int dateMonth = year - 1900; //Java date year is since 1900 (2023 is 123)

        hours.clear();
        Rental rental;
        for (int i = (int) chargingStation.getMinHour(); i < (int) chargingStation.getMaxHour(); i++) {
            final int hour = i;
            rental = model.getRentals().stream()
                    .filter(new Predicate<Rental>() {
                        @Override
                        public boolean test(Rental r) {

                            return r.getStartDate().getYear() == dateYear &&
                                    r.getStartDate().getMonth() == month &&
                                    r.getStartDate().getDate() == day &&
                                    r.getStartDate().getHours() == hour &&
                                    r.getHolderUser().getMyChargingStation() == chargingStation;
                        }
                    })
                    .findAny()
                    .orElse(null);
            String tmp = rental == null ? "" : "                   Occupied";
            Toast.makeText(getApplicationContext(),"year: " + year + ", month: " + month + ", day: " + day,Toast.LENGTH_SHORT).show();
            String s = i + ":00 - " + (i+1) + ":00" + tmp;
            hours.add(new OrderHour(s, HourStatus.free));
        }
        hoursArrayAdapter.notifyDataSetChanged();
    }
    public void rentClick(View view){
        //Rental rental = new Rental(chargingStation.get)
    }
    private enum HourStatus{
        free,
        occupied,
        selected
    }
    private class OrderHour{
        private String content;
        private HourStatus hourStatus;

        public OrderHour(String content, HourStatus hourStatus) {
            this.content = content;
            this.hourStatus = hourStatus;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public HourStatus getHourStatus() {
            return hourStatus;
        }

        public void setHourStatus(HourStatus hourStatus) {
            this.hourStatus = hourStatus;
        }

        @Override
        public String toString() {
            String s = hourStatus == HourStatus.selected ? " Selected" : "";
            return content + s;

        }
    }

}

