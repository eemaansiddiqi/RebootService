package micronet.com.rebootservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Created by eemaan.siddiqi on 1/17/2017.
 */

public class RebootTrackerService extends Service {

    Context context;
    String poweOnReason = "";
    private Handler shutdownServiceHandler;
    private Handler shutdownDeviceHandler;
    private int shutdownCount;
    private String shutdownCountVal;
    private int shutDownTime;
    private String shutdownTimeValue;
    private long currentTime   = 0;
    private long xShutdownTime = 0;
    private int minShutDownTime = 30;
    private int SIXTY_SECONDS = 60000;

    DeviceManager deviceManager;
    ReadWriteFile readWriteFile;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        context = this;

        if (shutdownServiceHandler == null) {
            shutdownServiceHandler = new Handler(Looper.myLooper());
            shutdownServiceHandler.post(ShutdownThread);
        }

        checkLogFolder();

        if(deviceManager == null){
            deviceManager = new DeviceManager();
        }
        poweOnReason = deviceManager.getPowerOnReason();
        Log.d(TAG, "Reason: " + poweOnReason);
        ReadWriteFile.LogCsvToFile(true, poweOnReason, ReadWriteFile.readShutdownCount(context), ReadWriteFile.readShutdownTimeFromFile(context));
    }

    public void checkLogFolder(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File Root = Environment.getExternalStorageDirectory();
            ReadWriteFile.Dir = new File(Root.getAbsolutePath() + "/MicronetService");
            if (!ReadWriteFile.Dir.exists()) {
                ReadWriteFile.Dir.mkdir();
            }
        }
        initializeShutDownTime(context);
        try {
            initializeShutdownCnt(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void increaseShutDownCount() throws IOException {
        shutdownCount++;
        shutdownCountVal = Integer.toString(shutdownCount);
        ReadWriteFile.writeShutdownCountToFile(shutdownCountVal, context);
        ReadWriteFile.LogCsvToFile(false, "", shutdownCountVal, shutdownTimeValue);
        Log.d(TAG, "Increased Shutdown Count :" + shutdownCountVal);
    }

    public void initializeShutdownCnt(Context context) throws IOException {
        if (ReadWriteFile.readShutdownCount(context) == "") {
            //Initializing handler Count to 0 (When the service restarts)
            shutdownCount = 0;
            shutdownCountVal = Integer.toString(shutdownCount);
            ReadWriteFile.writeShutdownCountToFile(shutdownCountVal, context);
        } else {
            shutdownCountVal = ReadWriteFile.readShutdownCount(context);
            shutdownCount = Integer.parseInt(shutdownCountVal);
        }
    }


    public void initializeShutDownTime(Context context){
        if (ReadWriteFile.readShutdownTimeFromFile(context) == "") {
            //Initializing handler Count to 0 (When the service restarts)
            shutDownTime = 0;
            shutdownTimeValue = Integer.toString(shutDownTime);
            ReadWriteFile.writeShutdownTimeToFile(shutdownTimeValue, context);
        } else {
            shutdownTimeValue = ReadWriteFile.readShutdownTimeFromFile(context);
            shutDownTime = Integer.parseInt(shutdownTimeValue);
            //Do not allow restart times that wouldn't allow enough time to disable it
            if (shutDownTime < minShutDownTime) {
                shutDownTime = 0;
            }
        }
    }

    final Runnable ShutdownThread = new Runnable() {
        @Override
        public void run() {

            shutdownTimeValue = ReadWriteFile.readShutdownTimeFromFile(context);
            shutDownTime = Integer.parseInt(shutdownTimeValue);
            //The interval should be greater than 30s.
            if (shutDownTime < minShutDownTime){
                shutDownTime = 0;
            }
            Log.d(TAG, "Shut Down Time: " + shutDownTime);
            try {
                if (shutDownTime != 0){
                    Log.d(TAG,"Sleep for " +shutDownTime +" seconds");
                    xShutdownTime = SystemClock.uptimeMillis() +  TimeUnit.SECONDS.toMillis(shutDownTime);
                    if (shutdownDeviceHandler == null) {
                        shutdownDeviceHandler = new Handler(Looper.myLooper());
                    }
                    shutdownDeviceHandler.postAtTime(shutdown, xShutdownTime);
                  /*  sleep(shutDownTime*1000);
                    increaseShutDownCount();
                    Log.d(TAG, "Shutting down, shutdownCount = value "+ shutdownCount);
                    java.lang.Process proc = Runtime.getRuntime().exec(new String[]{"setprop", "sys.powerctl", "shutdown"});*/
                }
            }
            catch (Exception e) {
                Log.d(TAG, "run: bh");
                e.printStackTrace();
            }
            shutdownServiceHandler.postDelayed(this, SIXTY_SECONDS);
        }
    };

    private Runnable shutdown = new Runnable() {
        @Override
        public void run() {
            try {
                increaseShutDownCount();
                Log.d(TAG, "Shutting down, shutdownCount = value "+ shutdownCount);
                java.lang.Process proc = Runtime.getRuntime().exec(new String[]{"setprop", "sys.powerctl", "shutdown"});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"STOP");
        shutdownServiceHandler.removeCallbacks(ShutdownThread);
        shutdownDeviceHandler.removeCallbacks(shutdown);
        //Process.killProcess(Process.myPid());
        Toast.makeText(this,"Service Stopped", Toast.LENGTH_LONG).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}