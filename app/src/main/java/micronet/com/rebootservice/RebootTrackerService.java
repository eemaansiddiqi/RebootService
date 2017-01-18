package micronet.com.rebootservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

/**
 * Created by eemaan.siddiqi on 1/17/2017.
 */

public class RebootTrackerService extends Service {
    Context context;
    private Handler restartHandler;
    private int restartCount;
    private String restartCountValue;
    private int shutDownTime;
    private String shutDownTimeValue;
    private int minShutDownTime = 30; //Minimum Time to restart the device
    private int SIXTY_SECONDS = 60000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        context = this;
        if (restartHandler == null) {
            restartHandler = new Handler(Looper.myLooper());
            restartHandler.post(Reboot_Counter);
        }

        //Creating a Directory if it isn't available
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File Root = Environment.getExternalStorageDirectory(); //Creating File Storage
            ReadWriteFile.Dir = new File(Root.getAbsolutePath() + "/MicronetService");
            if (!ReadWriteFile.Dir.exists()) {
                ReadWriteFile.Dir.mkdir();
            }
        }
        if (ReadWriteFile.readRestartCountFromFile(context) == "") {
            //Initializing handler Count to 0 (When the service restarts)
            restartCount = 0;
            restartCountValue = Integer.toString(restartCount);
            ReadWriteFile.writeRestartCountToFile(restartCountValue, context);
        } else {
            restartCountValue = ReadWriteFile.readRestartCountFromFile(context);
            restartCount = Integer.parseInt(restartCountValue);
        }
        if (ReadWriteFile.readShutDownTimeFromFile(context) == "") {
            //Initializing handler Count to 0 (When the service restarts)
            shutDownTime = 0;
            shutDownTimeValue = Integer.toString(restartCount);
            ReadWriteFile.writeShutDownTimeToFile(shutDownTimeValue, context);
        } else {
            shutDownTimeValue = ReadWriteFile.readShutDownTimeFromFile(context);
            shutDownTime = Integer.parseInt(shutDownTimeValue);
            if (shutDownTime < minShutDownTime) //Do not allow restart times that wouldn't allow enough time to disable it
            {
                shutDownTime = 0;
            }
        }
    }

    private void increaseRestartCount() {
        restartCount++;
        restartCountValue = Integer.toString(restartCount);
        ReadWriteFile.writeRestartCountToFile(restartCountValue, context);
        Log.d(TAG, "increased Restart Count :" + restartCountValue);
        ReadWriteFile.serviceRestartLog(restartCountValue, context);
    }

    final Runnable Reboot_Counter = new Runnable() {
        @Override
        public void run() {
            shutDownTimeValue = ReadWriteFile.readShutDownTimeFromFile(context);
            shutDownTime = Integer.parseInt(shutDownTimeValue);
            if (shutDownTime < minShutDownTime) //Do not allow restart times that wouldn't allow enough time to disable it
            {
                shutDownTime = 0;
            }
            Log.d(TAG, "Shut Down Time" + shutDownTime);
            try {
                if (shutDownTime!=0){
                    Log.d(TAG,"Sleep for shut down time" +shutDownTime);
                    sleep(shutDownTime*1000);
                    increaseRestartCount();
                    Log.d(TAG, "shutting down after incrementing, incremented value "+restartCount);
                    Process proc = Runtime.getRuntime().exec(new String[]{"setprop", "sys.powerctl", "shutdown"});
                }
            }
            catch (Exception e) {
                Log.d(TAG, "run: bh");
            }
            restartHandler.postDelayed(this, SIXTY_SECONDS);
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"STOP");
        restartHandler.removeCallbacks(Reboot_Counter);
        Toast.makeText(this,"Service Stopped",Toast.LENGTH_LONG).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

}