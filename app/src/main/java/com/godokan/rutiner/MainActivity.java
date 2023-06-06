package com.godokan.rutiner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView calendarView;
    LocalDate selectedDate;
    Button btnPrev, btnNext;
    TextView tvMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        setCalendar();
    }

    private void setCalendar() {
        calendarView = findViewById(R.id.recyclerView);
        btnPrev = calendarView.findViewById(R.id.btnPrev);
        btnNext = calendarView.findViewById(R.id.btnNext);
        tvMonth = findViewById(R.id.tvMonth);
        selectedDate = LocalDate.now();
        setMonth();
    }

    private void setMonth() {
        tvMonth.setText(String.format(monthSelector(selectedDate)+"月"));
        ArrayList<String> days = daysInMonthArray(selectedDate);
        CalendarAdapter adapter = new CalendarAdapter(days);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 7);
        calendarView.setLayoutManager(manager);
        calendarView.setAdapter(adapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> days = new ArrayList<>();
        Month month = Month.from(date);
        int endOfMonth = month.length(date.isLeapYear());
        LocalDate startOfMonth = date.withDayOfMonth(1);
        int firstDayOfWeek = startOfMonth.getDayOfWeek().getValue(); // 선택한 달의 첫 번째 주의 첫 번째 날에 대한 요일

        for(int i = 1; i <= 42; i++) { // 한 달 4주, 이전 달 마지막 주, 다음 달 첫 주 총 6주 (42일). 달력 정렬용.
            if(i <= firstDayOfWeek) {
                days.add("");
            } else if (i > endOfMonth + firstDayOfWeek) {
                LocalDate next = LocalDate.now().plusMonths(1);
                int nextFirst = next.withDayOfMonth(i-endOfMonth).getDayOfWeek().getValue();
                int last = date.withDayOfMonth(endOfMonth).getDayOfWeek().getValue();
                if(last - nextFirst>1){
                    days.add("");
                } else {
                    break;
                }
            } else {
                days.add(String.valueOf(i - firstDayOfWeek));
            }
        }
        return days;
    }

    private String monthSelector(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M");
        return date.format(formatter);
    }
}