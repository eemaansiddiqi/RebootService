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

    public static void writeShutdownCountToFile(String handlerValue, Context context) throws IOException {

        //Store the shutdown count in a file
        File file = new File(Dir, "ShutdownCount.txt");
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
        File file = new File(Dir, "ShutdownCount.txt"); //Created a Text File for enabled count
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
        File file = new File(Dir, "ShutDownTime.txt");
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
        File file = new File(Dir, "ShutDownTime.txt");
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


    //Logging the Service activity
    public static void LogToFile(Boolean isStartingUp, String getPowerOnReason, String shutdownCount, String shutdownTime){
        String fileName = "ServiceActivityLog.txt";
        String symbols = " **************************************************";
        String timestamp = ("Timestamp: ")+Utils.formatDate(System.currentTimeMillis())+("   ");
        String startUpMessage = "    The device just powered up!  ";
        String shutdownMsg = "    The device is shutting down!  ";
        String previousShutDownCount = "    Last Shutdown Count:  ";
        String shutdownCntMsg = "    Device Started Count:  ";
        String powerOnReason = "    Power On Reason:  ";
        String shutdownTimeMsg = "    Shut Down Time:  ";

        File file = new File(Dir, fileName);
        if(!file.exists()) {
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
            bufferedWriter.write(timestamp);
            if(isStartingUp){
                bufferedWriter.write(startUpMessage);
                bufferedWriter.write(previousShutDownCount);
                bufferedWriter.write(shutdownCount);
                bufferedWriter.write(powerOnReason);
                bufferedWriter.write(getPowerOnReason);
                bufferedWriter.newLine();
                bufferedWriter.write(symbols);
                bufferedWriter.newLine();
            }
            else {
                bufferedWriter.write(shutdownMsg);
                bufferedWriter.write(shutdownTimeMsg);
                bufferedWriter.write(shutdownTime);
                bufferedWriter.write(shutdownCntMsg);
                bufferedWriter.write(shutdownCount);
                bufferedWriter.newLine();
                bufferedWriter.write(symbols);
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
