package com.godokan.rutiner;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.godokan.rutiner.helper.DateHelper;
import com.godokan.rutiner.helper.NotificationHelper;
import com.godokan.rutiner.helper.RutinDbHelper;

import java.time.LocalDate;

public class AlarmService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        RutinDbHelper dbHelper = RutinDbHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DateHelper dateHelper = DateHelper.getInstance();
        LocalDate date = dateHelper.getNowDate();

        try {
            String sql = "select count(*) from "+TableInfo.TABLE_NAME+" where "+TableInfo.COLUMN_NAME_DATE+" = "+dateHelper.parseDateString(date);
            Cursor resultSet = db.rawQuery(sql, null);
            if (resultSet.moveToNext()){
                int count = resultSet.getInt(0);
                if (count>0){
                    NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                    notificationHelper.getChannel1Notification("습관달력", "오늘 "+count+"개의 활동이 있어요!");
                }
            }
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("서비스 장애");
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
