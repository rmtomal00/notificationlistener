package com.notificationlistemer.tomal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.CellInfo;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_PHONE_STATE_PERMISSION = 111;
    private static final String USSD_CODE = "*121#";
    private TextView test;

    /*ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("connect");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("Disconnected");
        }
    };*/
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        test = findViewById(R.id.test);

        /*Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        startActivityForResult(intent, REQUEST_CAMERA_PERMISSION);*/

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_PHONE_NUMBERS,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CALL_PHONE}, REQUEST_PHONE_STATE_PERMISSION);
        } else {
            // Permission already granted, proceed to read SIM number
            readSimNumber();
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        *//*if (requestCode == REQUEST_CAMERA_PERMISSION && resultCode == RESULT_OK) {
            // The user granted the permission
            bindNotificationListenerService();
        }*//*

    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_STATE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to read SIM number
                //readSimNumber();
                //new USSDCodeReceiver(context).runUSSDCode();
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    private void readSimNumber() {
        SubscriptionManager subscriptionManager = getSystemService(SubscriptionManager.class);

        // Get the list of active subscription information
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            subscriptionManager.getPhoneNumber(3);
        }

        if (subscriptionInfoList != null) {
            for (SubscriptionInfo info : subscriptionInfoList) {
                // Extract and use information from each subscription
                int subscriptionId = info.getSubscriptionId();
                String displayName = info.getDisplayName().toString();
                String number = info.getNumber();

                // Do something with the subscription info
                // For example, you can display it in a TextView
                System.out.println(number + " " + subscriptionId + " " + displayName);
            }
        }
    }
}