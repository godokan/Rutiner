package com.godokan.rutiner.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.godokan.rutiner.OnItemListener;
import com.godokan.rutiner.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView dayView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayView = itemView.findViewById(R.id.tvDay);
        }
    }

    ArrayList<LocalDate> days;
    OnItemListener listener;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener listener) {
        this.days = days;
        this.listener = listener;
    }

    public void setDays(ArrayList<LocalDate> days) {this.days = days;}

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.calendar_cell, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position){
        LocalDate date = days.get(position);
        try {
            String day = String.valueOf(date.getDayOfMonth());
            holder.dayView.setText(day);
        } catch (NullPointerException e) {
            holder.dayView.setText("");
        }

        if (position % 7 == 0)
            holder.dayView.setTextColor(Color.parseColor("#ffff4444"));
        else if ((position + 1) % 7 == 0) {
            holder.dayView.setTextColor(Color.parseColor("#ff33b5e5"));
        } else
            holder.dayView.setTextColor(Color.parseColor("#6a6a6a"));


        holder.dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int year = date.getYear();
//                int month = date.getMonthValue();
//                int day = date.getDayOfMonth();

                listener.onItemClick(date);

//                String fullDate = year+"-"+month+"-"+day;
//
//                Toast.makeText(holder.dayView.getContext(), fullDate, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}