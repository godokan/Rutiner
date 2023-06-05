package com.godokan.rutiner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Calendar cal;
    RecyclerView calendarView;
    LocalDate selectedDate;
    CalendarAdapter calendarAdapter;
    Button btnPrev, btnNext;
    TextView tvMonth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


    }

    private String monthSelecter(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return date.format(formatter);
    }

    private void setCalendar() {

        calendarView = findViewById(R.id.recyclerView);
        btnPrev = calendarView.findViewById(R.id.btnPrev);
        btnNext = calendarView.findViewById(R.id.btnNext);
        tvMonth = findViewById(R.id.tvMonth);
        selectedDate = LocalDate.now();

    }

    private void setMonth() {

    }
}