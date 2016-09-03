package com.moodybugs.sam.micromaxtwo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by sam on 8/24/16.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent startService = new Intent(context, Service.class);
        context.startService(startService);

    }
}
