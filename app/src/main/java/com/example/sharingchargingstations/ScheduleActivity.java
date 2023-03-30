package com.example.sharingchargingstations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.Model;

import java.util.ArrayList;
import java.util.Date;

public class ScheduleActivity extends AppCompatActivity {
    private CalendarView simpleCalendarView;
    private ArrayList<String> hours = new ArrayList<>();
    private Model model = Model.getInstance();
    private ListView lvHours;
    private ArrayAdapter<String> hoursArrayAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        simpleCalendarView =  findViewById(R.id.cvDate);
        long selectedDate = simpleCalendarView.getDate();
        simpleCalendarView.setDate(new Date().getTime());
        simpleCalendarView.setFirstDayOfWeek(1);

        lvHours = findViewById(R.id.lvHours);


        hoursArrayAdapter = new ArrayAdapter<String>(this, R.layout.item_hour,hours){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  getLayoutInflater().inflate(R.layout.item_hour,null);
                TextView tvHour = view.findViewById(R.id.tvHour);

                ArrayList<String> availableHours = new ArrayList<>();

                tvHour.setText(hours.get(position));
                return view;
            }
        };






    }
}