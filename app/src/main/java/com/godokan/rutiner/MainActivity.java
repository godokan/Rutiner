package com.godokan.rutiner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import java.time.LocalDate;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Calendar cal;
    RecyclerView calendarView;
    LocalDate selectedDate;
    CalendarAdapter calendarAdapter;
    Button btnPrev, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


    }

    private void setCalendar() {

        calendarView = findViewById(R.id.recyclerView);
        btnPrev = calendarView.findViewById(R.id.btnPrev);
        btnNext = calendarView.findViewById(R.id.btnNext);
        selectedDate = LocalDate.now();

    }
}