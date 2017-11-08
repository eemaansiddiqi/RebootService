package micronet.com.rebootservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

/**
 * Created by eemaan.siddiqi on 1/17/2017.
 */

public class RebootTrackerService extends Service {

    Context context;
    String poweOnReason = "";
    private Handler shutDownHandler;
    private int shutdownCount;
    private String shutdownCountVal;
    private int shutDownTime;
    private String shutdownTimeValue;
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

        if (shutDownHandler == null) {
            shutDownHandler = new Handler(Looper.myLooper());
            shutDownHandler.post(ShutdownThread);
        }

        //Creating a Directory if it isn't available
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File Root = Environment.getExternalStorageDirectory();
            ReadWriteFile.Dir = new File(Root.getAbsolutePath() + "/MicronetService");
            if (!ReadWriteFile.Dir.exists()) {
                ReadWriteFile.Dir.mkdir();
            }
        }
        try {
            initializeShutdownCnt(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializeShutDownTime(context);

        if(deviceManager == null){
            deviceManager = new DeviceManager();
        }
        poweOnReason = deviceManager.getPowerOnReason();
        Log.d(TAG, "Reason: " + poweOnReason);
        ReadWriteFile.LogToFile(true, poweOnReason, ReadWriteFile.readShutdownCount(context), ReadWriteFile.readShutdownTimeFromFile(context));
    }

    private void increaseShutDownCount() throws IOException {
        shutdownCount++;
        shutdownCountVal = Integer.toString(shutdownCount);
        ReadWriteFile.writeShutdownCountToFile(shutdownCountVal, context);
        ReadWriteFile.LogToFile(false, "", shutdownCountVal, shutdownTimeValue);
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
                    sleep(shutDownTime*1000);
                    increaseShutDownCount();
                    Log.d(TAG, "Shutting down, shutdownCount = value "+ shutdownCount);
                    java.lang.Process proc = Runtime.getRuntime().exec(new String[]{"setprop", "sys.powerctl", "shutdown"});
                }
            }
            catch (Exception e) {
                Log.d(TAG, "run: bh");
            }
            shutDownHandler.postDelayed(this, SIXTY_SECONDS);
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"STOP");
        Process.killProcess(Process.myPid());
        Toast.makeText(this,"Service Stopped", Toast.LENGTH_LONG).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

}