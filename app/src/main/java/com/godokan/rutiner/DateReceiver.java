package com.godokan.rutiner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DateReceiver extends BroadcastReceiver {
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
            Intent serviceIntent = new Intent(context, AlarmService.class);
            this.context.startService(serviceIntent);
        }
    }
}
