package com.notificationlistemer.tomal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;

public class USSDCodeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals("android.intent.action.UMS_RESPONSE")) {
            String ussdResponse = intent.getStringExtra("message");
            // Process the USSD response here
            System.out.println(ussdResponse);
        }

    }
}
