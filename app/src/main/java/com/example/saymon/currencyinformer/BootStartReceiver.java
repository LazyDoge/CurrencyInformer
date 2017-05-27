package com.example.saymon.currencyinformer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by saymon on 15.01.17.
 */
public class BootStartReceiver extends BroadcastReceiver {

    public static SharedPreferences minimaxSaver;

    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.subService = new SubService();
        minimaxSaver = context.getSharedPreferences("minimax", Context.MODE_PRIVATE);
        MainActivity.subService.startChecker(context);
    }
}
