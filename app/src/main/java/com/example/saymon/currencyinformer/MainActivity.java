package com.example.saymon.currencyinformer;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    static double currentValue = 50;
   static double maxValue;
   static double minValue;
String tVText = "update later";
    EditText editText;
    EditText editText2;

    static boolean needNotification = false;
    public static SubService subService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editText = (EditText) findViewById(R.id.minEditTXT);
        editText2 = (EditText) findViewById(R.id.maxEditTXT);
        if (BootStartReceiver.minimaxSaver == null){
            BootStartReceiver.minimaxSaver = getSharedPreferences("minimax", Context.MODE_PRIVATE);
        }

            if (BootStartReceiver.minimaxSaver.contains("max")) {
                editText.setText(String.valueOf(BootStartReceiver.minimaxSaver.getFloat("min", 0)));
                editText2.setText(String.valueOf(BootStartReceiver.minimaxSaver.getFloat("max", 0)));
            }

        final TextView textView = (TextView) findViewById(R.id.currEditTXT);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreashing", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                final TextView textView1 = textView;
//                String tVText;
                if (BootStartReceiver.minimaxSaver == null){
                    BootStartReceiver.minimaxSaver = getSharedPreferences("minimax", Context.MODE_PRIVATE);
                }

                if (BootStartReceiver.minimaxSaver.contains("curr")){
                    tVText = String.valueOf(BootStartReceiver.minimaxSaver.getFloat("curr", 0));

                }
//                else tVText = "update later";
                textView1.setText(tVText);
            }
        });
        if (subService == null) {
            subService = new SubService();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onBtnClick(View view) {
        subService.startChecker(this.getApplicationContext());
//        System.out.println(subService.updater.current);
//        if (BootStartReceiver.minimaxSaver != null) {
//            SharedPreferences.Editor editor = BootStartReceiver.minimaxSaver.edit();
//            editor.putFloat("min", Float.parseFloat(String.valueOf(editText.getText())));
//            editor.putFloat("max", Float.parseFloat(String.valueOf(editText2.getText())));
//            editor.apply();
//        }

//        startService(new Intent(this, SubService.class));
        

//        Context context = getApplicationContext();
//        Intent notifIntent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        Resources resources = context.getResources();
//        Notification.Builder builder = new Notification.Builder(context);
//        builder.setWhen(System.currentTimeMillis())
//                .setAutoCancel(true)
//                .setSmallIcon(android.R.drawable.btn_plus)
//                .setContentText("1")
//                .setContentTitle("2");
//
//        Notification notification = builder.build();
//
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//     if (currentValue>maxValue){
//
//     }
//
//            }
//        });


            }

    public void updateClick(View view) {
        if (BootStartReceiver.minimaxSaver != null) {
            SharedPreferences.Editor editor = BootStartReceiver.minimaxSaver.edit();
            editor.putFloat("min", Float.parseFloat(String.valueOf(editText.getText())));
            editor.putFloat("max", Float.parseFloat(String.valueOf(editText2.getText())));
            editor.apply();
        }


    }
}
