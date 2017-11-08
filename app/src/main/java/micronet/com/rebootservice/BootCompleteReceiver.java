package micronet.com.rebootservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by eemaan.siddiqi on 1/17/2017.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            //BOOT_COMPLETE Intent Receive
            Intent serviceIntent = new Intent(context, RebootTrackerService.class);
            context.startService(serviceIntent);
        }
}

