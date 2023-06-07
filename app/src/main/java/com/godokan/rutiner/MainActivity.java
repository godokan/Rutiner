package com.godokan.rutiner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView calendarView;
    CalendarAdapter adapter;
    LocalDate selectedDate;
    ArrayList<LocalDate> days;
    Button btnPrev, btnNext;
    TextView tvMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        selectedDate = LocalDate.now();
        calendarView = findViewById(R.id.recyclerView);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        tvMonth = findViewById(R.id.tvMonth);
        setMonth();

        System.out.println("Set");

        btnNext.setOnClickListener(v->{
            selectedDate = selectedDate.plusMonths(1);
            updateMonth();
        });

        btnPrev.setOnClickListener(v->{
            selectedDate = selectedDate.minusMonths(1);
            updateMonth();
        });
    }

    private void setMonth() {
        tvMonth.setText(String.format(monthSelector(selectedDate)+"月"));
        days = daysInMonthArray(selectedDate);
        adapter = new CalendarAdapter(days);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 7);
        calendarView.setLayoutManager(manager);
        calendarView.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateMonth() {
        tvMonth.setText(String.format(monthSelector(selectedDate)+"月"));
        days = daysInMonthArray(selectedDate);
        adapter.setDays(days);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> days = new ArrayList<>();
        Month month = Month.from(date);
        int endOfMonth = month.length(date.isLeapYear());
        LocalDate startOfMonth = date.withDayOfMonth(1);
        int firstDayOfWeek = startOfMonth.getDayOfWeek().getValue(); // 선택한 달의 첫 번째 주의 첫 번째 날에 대한 요일

        int totalDays = endOfMonth + firstDayOfWeek;
        int rowCount = (totalDays <= 35) ? 5 : 6;

        boolean isCurrentMonth = false;

        for (int i = 1; i <= rowCount * 7; i++) {
            if (i <= firstDayOfWeek) {
                if(firstDayOfWeek==7)
                    days.clear();
                else days.add(null);
            } else if (i > totalDays) {
                if (isCurrentMonth) {
                    days.add(null);
                } else {
                    days.add(LocalDate.of(date.getYear(), date.getMonth(), i - firstDayOfWeek));
                }
            } else {
                days.add(LocalDate.of(date.getYear(), date.getMonth(), i - firstDayOfWeek));
                isCurrentMonth = true;
            }
        }
        return days;
    }

    private String monthSelector(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M");
        return date.format(formatter);
    }
}