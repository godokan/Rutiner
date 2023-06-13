package com.godokan.rutiner.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.godokan.rutiner.OnItemListener;
import com.godokan.rutiner.R;
import com.godokan.rutiner.TableInfo;
import com.godokan.rutiner.helper.DateHelper;
import com.godokan.rutiner.helper.RutinDbHelper;

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

    private final Context context;

    public CalendarAdapter(Context context, ArrayList<LocalDate> days, OnItemListener listener) {
        this.context = context;
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
        RutinDbHelper helper = RutinDbHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        LocalDate date = days.get(position);
        DateHelper dateHelper = DateHelper.getInstance();

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

        try {
            Cursor cursor = db.rawQuery(("SELECT count(*) FROM "+ TableInfo.TABLE_NAME+" WHERE "+TableInfo.COLUMN_NAME_DATE+" = ?"), new String[]{dateHelper.parseDateString(date)});
            if (cursor.moveToNext()) {
                if(cursor.getInt(0)>0){
                    holder.dayView.setTypeface(holder.dayView.getTypeface(), Typeface.BOLD_ITALIC);
                } else {
                    holder.dayView.setTypeface(holder.dayView.getTypeface(), Typeface.BOLD);
                }
            }
            cursor.close();
        } catch (Exception e) {
            System.out.println("메인화면 조회 실패");
        }

        holder.dayView.setOnClickListener(v -> listener.onItemClick(date));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}
