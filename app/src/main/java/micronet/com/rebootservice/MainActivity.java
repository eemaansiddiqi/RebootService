package micronet.com.rebootservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String time = "";
    Boolean isServiceRunning = false;
    EditText txtTime;
    TextView txtCurrentTime;
    TextView txtIsServiceRunning;
    Context context;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("mctl");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCurrentShutdownTime();

        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(ServiceManagerReceiver.ACTION_START_SERVICE);
                sendBroadcast(intent);
            }
        });

        findViewById(R.id.btnStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(ServiceManagerReceiver.ACTION_PAUSE_SERVICE);
                sendBroadcast(intent);
            }

        });


    }

    public void onStart() {
        super.onStart();
        txtTime = (EditText) findViewById(R.id.editTxtTime);

        findViewById(R.id.btnSetTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    time = txtTime.getText().toString();
                    ReadWriteFile.writeShutdownTimeToFile(time, context);
                } catch (Exception e){
                    Log.d(TAG, "Caught exception");
                }
            }
        });

        findViewById(R.id.btnCurrentTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setCurrentShutdownTime();
                } catch (Exception e){
                    Log.d(TAG, "Caught exception");
                }
            }
        });
    }

    public void setCurrentShutdownTime(){
        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentShutdownTime);
        String currentTime = ReadWriteFile.readShutdownTimeFromFile(context);
        if(currentTime == ""){
            currentTime = "0";
        }
        txtCurrentTime.setText("" + currentTime +" s");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
