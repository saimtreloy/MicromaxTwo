package com.moodybugs.sam.micromaxtwo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sam on 8/26/16.
 */
public class SmsSentReceiver extends BroadcastReceiver {

    private final String DEBUG_TAG = getClass().getSimpleName().toString();
    private static final String ACTION_SMS_SENT = "SMS_SENT";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(ACTION_SMS_SENT)) {

            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "SMS Sent", Toast.LENGTH_SHORT).show();
                    context.stopService(new Intent(context, Service.class));

                    PackageManager pm  = context.getPackageManager();
                    ComponentName componentName = new ComponentName(context, BootReceiver.class);
                    pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);

                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(context, "No service", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }



}
