package micronet.com.rebootservice;

import android.content.Context;
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
    public static BufferedWriter bufferedWriter = null;
    public static FileWriter fileWriter = null;

    //Write function for Restart Count
    public static void writeRestartCountToFile(String handlerValue, Context context){
        File file = new File(Dir, "RestartCount.txt"); //Created a Text File for storing the enabled count
        if(!file.exists()) {
            //If RestartCount.txt is not found, reset the count to 0
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

    //Read Function for Restart Count
    public static String readRestartCountFromFile(Context context) {

        String ret = "";
        File file = new File(Dir, "RestartCount.txt"); //Created a Text File for enabled count
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
    //Write function for Shutdown Time
    public static void writeShutDownTimeToFile(String handlerValue, Context context){
        File file = new File(Dir, "ShutDownTime.txt"); //Created a Text File for storing the enabled count
        if(!file.exists()) {
            //If ResetCount.txt is not found, reset the count to 0
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

    //Read Function for Shut Down Time
    public static String readShutDownTimeFromFile(Context context) {

        String ret = "";
        File file = new File(Dir, "ShutDownTime.txt"); //Created a Text File for enabled count
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
    public static void serviceRestartLog(String restartCountValue, Context context) {
        String timestamp=("Timestamp:   ")+TimeStamp.formatDate(System.currentTimeMillis())+("   "); //Getting current time stamp
        String restartCount="     Restart Count:  ";
        File file = new File(Dir, "ServiceRestartLog.txt");//Created a Text File to maintain the service activity log
        if(!file.exists()) {
            Log.d(TAG, "File Doesn't exist");
        }
        try {
            fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(timestamp);
            bufferedWriter.write(restartCount);
            bufferedWriter.write(restartCountValue);
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
    public static void serviceActivityLog(Boolean pauseStatus, Context context){
        String String_pauseStatus=String.valueOf(pauseStatus);
        String timestamp=("Timestamp:   ")+TimeStamp.formatDate(System.currentTimeMillis())+("   "); //Getting current time stamp
        String paused="     Paused Status:  ";
        File file = new File(Dir, "RestartPausedLog.txt");//Created a Text File to maintain the service activity log
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
}
