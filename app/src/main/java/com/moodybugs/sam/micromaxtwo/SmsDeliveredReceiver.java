package com.moodybugs.sam.micromaxtwo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by sam on 8/26/16.
 */
public class SmsDeliveredReceiver extends BroadcastReceiver {

    private final String DEBUG_TAG = getClass().getSimpleName().toString();
    private static final String ACTION_SMS_DELIVERED = "SMS_DELIVERED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(ACTION_SMS_DELIVERED)) {

            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "SMS Delivered",Toast.LENGTH_SHORT).show();
                    context.stopService(new Intent(context, Service.class));
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "SMS not delivered",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
