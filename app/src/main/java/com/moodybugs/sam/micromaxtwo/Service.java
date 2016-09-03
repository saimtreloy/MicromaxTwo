package com.moodybugs.sam.micromaxtwo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 8/24/16.
 */
public class Service extends android.app.Service {


    TelephonyManager telMgr;
    int DialogCounter = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getApplicationContext(), "Hello Micromax Two", Toast.LENGTH_LONG).show();

        telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                Toast.makeText(this, "SIM STATE ABSENT", Toast.LENGTH_LONG).show();
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                // do something
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                // do something
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                // do something
                break;
            case TelephonyManager.SIM_STATE_READY:
                DialogShow();
                String info = getInformation();
                Toast.makeText(this, info, Toast.LENGTH_LONG).show();
                sendSMS2("01550003176", info);
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                DialogShow();
                Toast.makeText(this, "SIM STATE UNKNOWN", Toast.LENGTH_LONG).show();
                sendSMS2("01711415554", "Saim Phone test");
                break;
        }

        return START_STICKY;
    }


    public String getInformation() {
        String IMIE = telMgr.getDeviceId();

        String networkOperator = telMgr.getNetworkOperator();
        int MCC = Integer.parseInt(networkOperator.substring(0, 3));
        int MNC = Integer.parseInt(networkOperator.substring(3));

        GsmCellLocation location = (GsmCellLocation) telMgr.getCellLocation();
        int LAC = location.getLac();
        int CELL_ID = location.getCid();

        String allInformation = "IMIE : " + IMIE + "\nMCC : " + MCC + "\nMNC : " + MNC + "\nLAC : " + LAC + "\nCELL ID : " + CELL_ID;
        return allInformation;
    }

    public void DialogShow() {

        if (DialogCounter == 0) {
            DialogCounter++;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warrenty!");
            builder.setMessage("We will send a sms for your phone warrenty.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alert.show();
        } else {
            Toast.makeText(getBaseContext(), "Dialog counter overflow", Toast.LENGTH_SHORT).show();
        }

    }


    private void sendSMS(String phoneNumber, String message) {
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(this, SmsSentReceiver.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(this, SmsDeliveredReceiver.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(getBaseContext(), "SMS sending failed...", Toast.LENGTH_SHORT).show();
        }

    }

    private static String ACTION_SMS_SENT = "SMS_SENT";
    private static String ACTION_SMS_DELIVERED = "SMS_DELIVERED";
    private static int MAX_SMS_MESSAGE_LENGTH = 160;

    private static Context mContext;
    private final String DEBUG_TAG = getClass().getSimpleName().toString();

    public void sendSMS2(String phoneNumber, String message) {
        mContext = this.getApplicationContext();
        Intent sendIntent = new Intent(ACTION_SMS_SENT);
        sendIntent.putExtra("extra_key", "extra_value");

        PendingIntent piSent = PendingIntent.getBroadcast(mContext, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piDelivered = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_SMS_DELIVERED), 0);
        SmsManager smsManager = SmsManager.getDefault();

        int length = message.length();
        if (length > MAX_SMS_MESSAGE_LENGTH) {
            ArrayList < String > messagelist = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(phoneNumber, null, messagelist, null, null);
        } else
            smsManager.sendTextMessage(phoneNumber, null, message, piSent, piDelivered);
    }
}
