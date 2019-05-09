package micronet.com.rebootservice;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by eemaan.siddiqi on 1/17/2017.
 */

public class ReadWriteFile {
    public static File Dir;
    public static File appLogDir;
    public static BufferedWriter bufferedWriter = null;
    public static FileWriter fileWriter = null;
    public static DeviceManager deviceManager;

    public static void writeShutdownCountToFile(String handlerValue, Context context) throws IOException {

        //Store the shutdown count in a file
        File file = new File(appLogDir, "ShutdownCount.txt");
        if(!file.exists()) {
            //If ShutdownCount.txt is not found, reset the count to 0
            handlerValue = "0";
            file.createNewFile();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(handlerValue.getBytes());
            fileOutputStream.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public static String readShutdownCount(Context context) {

        String ret = "";
        File file = new File(appLogDir, "ShutdownCount.txt"); //Created a Text File for enabled count
        if(!file.exists()) { return ret;}
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }
            fileReader.close();
            ret = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (Exception e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
        return ret;
    }


    public static void writeShutdownTimeToFile(String handlerValue, Context context){
        File file = new File(appLogDir, "ShutDownTime.txt");
        if(!file.exists()) {
            //If ShutdownTime.txt is not found, reset the count to 0
            handlerValue = "0";
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(handlerValue.getBytes());
            fileOutputStream.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public static String readShutdownTimeFromFile(Context context) {

        String ret = "";
        File file = new File(appLogDir, "ShutDownTime.txt");
        if(!file.exists()) { return ret;}
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }
            fileReader.close();
            ret = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (Exception e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
        return ret;
    }


    public static void serviceActivityLog(Boolean pauseStatus, Context context){
        String String_pauseStatus= String.valueOf(pauseStatus);
        String timestamp=("Timestamp:   ") +Utils.formatDate(System.currentTimeMillis())+("   "); //Getting current time stamp
        String paused="     Paused Status:  ";
        File file = new File(appLogDir, "RestartPausedLog.txt");//Created a Text File to maintain the service activity log
        if(!file.exists()) {
            Log.d(TAG, "File Doesn't exist");
        }
        try {
            fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(timestamp);
            bufferedWriter.write(paused);
            bufferedWriter.write(String_pauseStatus);
            bufferedWriter.newLine();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            e.printStackTrace();
        }
        finally {
            try {
                if (bufferedWriter!=null)
                    bufferedWriter.close();
                if (fileWriter!=null)
                    fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Logging the Service activitydate
    public static void LogCsvToFile(Boolean isStartingUp, String getPowerOnReason, String shutdownCount, String shutdownTime){
        Boolean fileExists = true;
        String fileName = "RebootServiceActivityLog.csv";
        String header = "Time Stamp, Serial no, OS Version, MCU Version, FPGA Version,Service Event, Elapsed Time, Shutdown Time Interval (in sec), Previous Shutdown Trigger Count, Power On Reason, Current Shutdown Trigger Count, RTC time, current device time ";
        String startUpMessage = "    Device Started  ";
        String shutdownMsg = "    Device Shutting down  ";
        String columnSep = ",";

        if(deviceManager==null){
            deviceManager = new DeviceManager();
        }

        String osVersion=Build.DISPLAY;
        String mcuVersion=deviceManager.getMcuVer();
        String fpgaVersion = Integer.toHexString(deviceManager.getFpgaVer());
        String rtcTime=deviceManager.getRTCTime();
        String currentTime=Utils.formatDateForRTC(System.currentTimeMillis(),true);
        Log.d(TAG, "Device: " + Build.SERIAL+", OS: "+ osVersion + " , MCU: "+ mcuVersion + " , FPGA: " + fpgaVersion + " ,RTC: " + rtcTime + " , AndroidTime: " + currentTime);
        Log.d(TAG, " Time: " + Utils.formatDateForRTC(System.currentTimeMillis(),true));
        File file = new File(appLogDir, fileName);
        if(!file.exists()) {
            fileExists = false;
            Log.d(TAG, "ServiceActivityLog.txt: File Doesn't exist");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            bufferedWriter = new BufferedWriter(fileWriter);
            if(!fileExists){
                bufferedWriter.write(header);
                bufferedWriter.newLine();
            }
            bufferedWriter.write(Utils.formatDate(System.currentTimeMillis()) + columnSep);
            bufferedWriter.write(Build.SERIAL + columnSep);
            bufferedWriter.write(osVersion + columnSep);
            bufferedWriter.write(mcuVersion + columnSep);
            bufferedWriter.write(fpgaVersion + columnSep);
            if(isStartingUp) {
                bufferedWriter.write(startUpMessage + columnSep);
                bufferedWriter.write(Utils.formatUptime(SystemClock.uptimeMillis())+ columnSep);
                bufferedWriter.write(shutdownTime + columnSep);
                bufferedWriter.write(shutdownCount + columnSep);
                bufferedWriter.write(getPowerOnReason + columnSep);
                bufferedWriter.write("Not Applicable" + columnSep);
                bufferedWriter.write(rtcTime + columnSep);
                bufferedWriter.write(currentTime + columnSep);
                bufferedWriter.newLine();
            }
            else {
                bufferedWriter.write(shutdownMsg + columnSep);
                bufferedWriter.write(SystemClock.uptimeMillis() + columnSep);
                bufferedWriter.write(shutdownTime + columnSep);
                bufferedWriter.write("Not Applicable" + columnSep);
                bufferedWriter.write(getPowerOnReason + columnSep);
                bufferedWriter.write(shutdownCount + columnSep);
                bufferedWriter.write(rtcTime + columnSep);
                bufferedWriter.write(currentTime+ columnSep);
                bufferedWriter.newLine();
            }
        }

        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            e.printStackTrace();
        }

        finally {
            try {
                if (bufferedWriter!=null)
                    bufferedWriter.close();
                if (fileWriter!=null)
                    fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
