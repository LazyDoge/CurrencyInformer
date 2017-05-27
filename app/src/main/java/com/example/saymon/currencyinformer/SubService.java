package com.example.saymon.currencyinformer;

import android.annotation.TargetApi;
import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * Created by saymon on 29.12.16.
 */
public class SubService extends BroadcastReceiver {
    static double current = 0;
    double max;
    double min;
    Updater updater = new Updater(50, 100, 0);
    Thread thread = new Thread(updater);
    NotificationManager nm;
    boolean isOn = true;
    int failCounter;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * Used to name the worker thread, important only for debugging.
     */


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock1 = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "lock1");
        wakeLock1.acquire();

        try {
            sendNotif();
        } catch (InterruptedException e) {
        }

        if (BootStartReceiver.minimaxSaver == null) {
            BootStartReceiver.minimaxSaver = context.getSharedPreferences("minimax", Context.MODE_PRIVATE);
        }

        if (BootStartReceiver.minimaxSaver.contains("min") && BootStartReceiver.minimaxSaver.contains("max")) {
            max = (double) BootStartReceiver.minimaxSaver.getFloat("max", 0);
            min = (double) BootStartReceiver.minimaxSaver.getFloat("min", 0);
        } else {
            max = 65;
            min = 55;
        }
        if (BootStartReceiver.minimaxSaver != null) {
            SharedPreferences.Editor editor = BootStartReceiver.minimaxSaver.edit();
            editor.putFloat("curr", (float) current);
            editor.apply();
        }


//        String conTitle = String.valueOf(max++);

        String conText = "Something went wrong";
        String conTitle = "Oops";

        boolean isNotifNeeded = false;
        if (current == -1.0) {
            if (BootStartReceiver.minimaxSaver != null) {
                if (BootStartReceiver.minimaxSaver.contains("fail")){
                    failCounter = BootStartReceiver.minimaxSaver.getInt("fail", 0);
                    System.out.println("init failcounetr " + failCounter);
                }

                SharedPreferences.Editor editor = BootStartReceiver.minimaxSaver.edit();
                editor.putInt("fail", ++failCounter);
                System.out.println("putting FC");
                editor.apply();
            }

        } else {
            if (BootStartReceiver.minimaxSaver != null) {

                SharedPreferences.Editor editor = BootStartReceiver.minimaxSaver.edit();
                editor.putInt("fail", 0);
                editor.apply();
                failCounter = 0;
            }
        }
        if (failCounter > 2) {
            isNotifNeeded = true;
            conText = "Can't get update for ages";
            conTitle = "Внимание ошибка обновления";
        }
        if (failCounter == 0) {
            if (current > max || current < min) {
                isNotifNeeded = true;
                conText = String.valueOf(current);
                conTitle = "!!! $ runout !!!";
            }
        }
        System.out.println(failCounter);


//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println(max);
//        System.out.println(updater.current);
//        System.out.println(current);
//        System.out.println();
//        System.out.println();
//        System.out.println();
        if (isNotifNeeded) {

            Intent notifIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notifIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Resources resources = context.getResources();
            Notification.Builder builder = new Notification.Builder(context);
            builder.setWhen(System.currentTimeMillis())
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.dollarmark)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.dollar_stats_icon))
                    .setContentText(conText)
                    .setContentTitle(conTitle);

            Notification notification = builder.build();
            notification.defaults = Notification.DEFAULT_ALL;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(101, notification);

        }
        isNotifNeeded = false;
        wakeLock1.release();

    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        while (isOn){
//           // sendNotif();
//           // updater.current;
//            Context context = getApplicationContext();
//            Intent notifIntent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//            Resources resources = context.getResources();
//            Notification.Builder builder = new Notification.Builder(context);
//            builder.setWhen(System.currentTimeMillis())
//                    .setAutoCancel(true)
//                    .setSmallIcon(android.R.drawable.btn_plus)
//                    .setContentText("1")
//                    .setContentTitle("2");
//
//            Notification notification = builder.build();
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(101, notification);
//            try {
//                TimeUnit.SECONDS.sleep(7);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }

//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//
//    protected void onHandleIntent(Intent intent) {
//        while (isOn){автозагрузка приложения android
//             sendNotif();
//            String conTitle = String.valueOf(updater.max);
//            String conText = String.valueOf(updater.current);
//            Context context = getApplicationContext();
//            Intent notifIntent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//            Resources resources = context.getResources();
//            Notification.Builder builder = new Notification.Builder(context);
//            builder.setWhen(System.currentTimeMillis())
//                    .setAutoCancel(true)
//                    .setSmallIcon(R.drawable.dollarmark)
//                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.dollar_stats_icon))
//                    .setContentText(conText)
//                    .setContentTitle(conTitle);
//
//            Notification notification = builder.build();
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(101, notification);
//            try {
//                TimeUnit.MINUTES.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }


    public void startChecker(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SubService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3*60*60*1000, pendingIntent);
    }

    void sendNotif() throws InterruptedException {
        String valueOnNotification;
//        Thread thread = new Thread(updater);
        thread.start();
        thread.join();
//        TimeUnit.SECONDS.sleep(1);


    }
}
