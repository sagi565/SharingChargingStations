package com.example.sharingchargingstations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.Rental;
import com.example.sharingchargingstations.Model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;

public class ScheduleActivity extends AppCompatActivity {
    private static final String TAG = "ScheduleActivity";
    private CalendarView simpleCalendarView;
    private ArrayList<OrderHour> hours = new ArrayList<>();
    private Model model = Model.getInstance();
    private ListView lvHours;
    private ArrayAdapter hoursArrayAdapter;
    private ChargingStation chargingStation;
    private int pos;
    private Button btnRent;

    private int selectedHour = -1;
    private int selectedYear = -1;
    private int selectedMonth = -1;
    private int selectedDay = -1;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH) + 1;
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        pos = getIntent().getIntExtra("pos", -1);
        chargingStation = model.getChargingStations().get(pos);

        simpleCalendarView = findViewById(R.id.cvDate);
        btnRent = findViewById(R.id.btnRent);
        long selectedDate = simpleCalendarView.getDate();

        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rent();
            }
        });
        lvHours = findViewById(R.id.lvHours);
        chargingStation = Model.getInstance().getChargingStations().get(pos);
        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;

                setHoursList(year,month + 1, dayOfMonth);
                lvHours.setAdapter(hoursArrayAdapter);

            }
        });
        hoursArrayAdapter = new ArrayAdapter<OrderHour>(this, R.layout.item_hour, hours) {
            @Override
            public View getView(int position, @Nullable View convertView, ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.item_hour, null);
                OrderHour orderHour = hours.get(position);
                Log.w(TAG, "getView: " + position + " " + orderHour.hourStatus);
                TextView tvHour = view.findViewById(R.id.tvHour);
                tvHour.setText(orderHour.toString() );
                if (orderHour.hourStatus == HourStatus.occupied) tvHour.setBackgroundColor(Color.rgb(240,128,128));
                if (orderHour.hourStatus == HourStatus.selected) tvHour.setBackgroundColor(Color.rgb(135,206,235));

                return view;
            }
        };

        lvHours.setAdapter(hoursArrayAdapter);
        lvHours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) ((LinearLayout)view).getChildAt(0);
                tv.setBackgroundColor(Color.BLUE);
                selectedHour = tv.getText().charAt(0);
                Log.w(TAG, "onItemClick: position " + position );
                for (int i = 0; i < hours.size() ; i++) {
                    OrderHour orderHour = hours.get(i);
                    if (orderHour.hourStatus != HourStatus.occupied)
                    {
                        orderHour.hourStatus = (i == position) ? HourStatus.selected : HourStatus.free;
                    }
                }


                Toast.makeText(ScheduleActivity.this, "" + hours.get(position).hourStatus, Toast.LENGTH_SHORT).show();
                logHours();
                hoursArrayAdapter.notifyDataSetChanged();
            }
        });
        Date date = new Date();

        date.setTime(date.getTime());
        setHoursList(date.getYear() + 1900, date.getMonth() + 1, date.getDate());

    }

    private void logHours(){
        for (int i = 0; i < hours.size(); i++) {
            OrderHour orderHour  = hours.get(i);
            Log.w(TAG, "logHours: i = " + i + " " + orderHour.hourStatus + " status " +  orderHour.getStatus());
        }
    }

    private int getSelectedHour(){
        for(OrderHour hour : hours){
            if (hour.hourStatus == HourStatus.selected) return Integer.parseInt(hour.getContent().substring(0,2));
        }

        return -1;
    }
    private void setHoursList(final int year, final int month, final int day) {
        final int dateYear = year - 1900; //Java date year is since 1900 (2023 is 123)
        final int dateMonth = year - 1900; //Java date year is since 1900 (2023 is 123)
        Log.w(TAG, "setHoursList: ==================================="  );
        hours.clear();
        Rental rental;
        for (int i = (int) chargingStation.getMinHour(); i < (int) chargingStation.getMaxHour(); i++) {
            final int hour = i;
            rental = model.getRentals().stream()
                    .filter(new Predicate<Rental>() {
                        @Override
                        public boolean test(Rental r) {
                            return r.getStartDate().getYear() == year &&
                                    r.getStartDate().getMonth() == month &&
                                    r.getStartDate().getDate() == day &&
                                    r.getStartDate().getHours()== hour &&
                                    r.getHolderUser().getMyChargingStation() == chargingStation;
                        }
                    })
                    .findAny()
                    .orElse(null);
            String s = String.format("%02d", i) + ":00 - " + (i+1) + ":00";

            String tmp = rental == null ? "" : "Occupied";
            if(rental == null)
                hours.add(new OrderHour(s, HourStatus.free));
            else
                hours.add(new OrderHour(s, HourStatus.occupied));


            //            Toast.makeText(getApplicationContext(),"year: " + year + ", month: " + month + ", day: " + day,Toast.LENGTH_SHORT).show();

        }
        hoursArrayAdapter.notifyDataSetChanged();
    }

    public void rent(){
        if(selectedHour == -1)
            return;
        User holderUser = null;
        for(User u : model.getUsers()){
            if(u.getMyChargingStation() == chargingStation)
                holderUser = u;
        }
        selectedHour = 13;
        Calendar calendar = Calendar.getInstance();
        Date tmpDate = new Date(selectedYear, selectedMonth, selectedDay, selectedHour, 0);
        Log.w(TAG, "rent: " + tmpDate);
        calendar.set(selectedYear, selectedMonth,selectedDay , selectedHour, 0, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date endDate = calendar.getTime();

        Rental newRental = new Rental(holderUser, model.getCurrentUser(), startDate, endDate);
        model.getRentals().add(newRental);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
    private enum HourStatus{
        free,
        occupied,
        selected
    }
    private class OrderHour{
        private String content;
        private HourStatus hourStatus;
        private int status = 0;

        public OrderHour(String content, HourStatus hourStatus) {
            this.content = content;
            setHourStatus(hourStatus);
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
            Log.w(TAG, "setHourStatus: "+ hourStatus);
            this.hourStatus = hourStatus;
            switch (hourStatus){
                case free:
                    status = 0;
                    break;
                case occupied:
                    status = 1;
                    break;
                case selected:
                    status = 2;
                    break;
            }
        }
        public int getStatus(){return status;}
        @Override
        public String toString() {
            String s = hourStatus == HourStatus.selected ? " Selected" : "";
            return content + s;

        }
    }

}

