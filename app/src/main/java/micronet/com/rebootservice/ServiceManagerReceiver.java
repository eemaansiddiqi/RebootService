package micronet.com.rebootservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

/**
 * Created by eemaan.siddiqi on 1/18/2017.
 */

public class ServiceManagerReceiver extends BroadcastReceiver {

    public static final String ACTION_START_SERVICE = "micronet.com.rebootservice.START_SERVICE";
    public static final String ACTION_STOP_SERVICE = "micronet.com.rebootservice.STOP_SERVICE";
    public volatile static boolean pauseStatus;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ACTION_STOP_SERVICE)) {
            Log.d(TAG, "ACTION_STOP_SERVICE received");
            //  pause service
            pauseStatus = true;
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ReadWriteFile.serviceActivityLog(pauseStatus,context);
            Intent service = new Intent(context,RebootTrackerService.class);
            boolean res = context.stopService(service);
            Log.d(TAG, "Service Stopped by User - status="+res);

        }
        else if(intent.getAction().equals(ACTION_START_SERVICE) ) {
            Log.d(TAG, "ACTION_START_SERVICE received");
            // start service
            pauseStatus = false;
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent service = new Intent(context,RebootTrackerService.class);
            context.startService(service);
            Log.d(TAG, "Service Resumed by User");     }
    }
}

