package com.godokan.rutiner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView dayView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayView = itemView.findViewById(R.id.tvDay);
        }
    }

    ArrayList<String> days;

    public CalendarAdapter(ArrayList<String> days) {
        this.days = days;
    }

    public void setDays(ArrayList<String> days) {this.days = days;}

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.calendar_cell, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.dayView.setText(days.get(position));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}
