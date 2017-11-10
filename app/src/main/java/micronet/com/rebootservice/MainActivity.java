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
    private int ret = 0;
    private String time = "";
    EditText txtTime;
    TextView txtCurrentTime;
    Context context;
    RebootTrackerService rebootTrackerService;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("mctl");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        Intent service = new Intent(this, RebootTrackerService.class);
        startService(service);*/
    }

    public void onStart() {
        super.onStart();

        txtTime = (EditText) findViewById(R.id.editTxtTime);

        setActionBarTitle(String.format("Reboot Service v2.0"));

        setCurrentShutdownTime();

        findViewById(R.id.btnCurrentTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ret = setCurrentShutdownTime();
                } catch (Exception e){
                    Log.d(TAG, "Caught exception");
                }
            }
        });

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
                intent.setAction(ServiceManagerReceiver.ACTION_STOP_SERVICE);
                sendBroadcast(intent);
            }
        });

        findViewById(R.id.btnInit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rebootTrackerService == null){
                    RebootTrackerService rebootTrackerService = new RebootTrackerService();
                    rebootTrackerService.checkLogFolder();
                }
            }
        });

/*
        Intent service = new Intent(this, RebootTrackerService.class);
        startService(service);*/


    }

    public int setCurrentShutdownTime(){
        int ret = 0;
        String currentTime = "";
        if(rebootTrackerService == null){
            RebootTrackerService rebootTrackerService = new RebootTrackerService();
            rebootTrackerService.checkLogFolder();
        }
        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentShutdownTime);
        currentTime = ReadWriteFile.readShutdownTimeFromFile(context);
        if(currentTime == ""){
            currentTime = "0";
        }
        txtCurrentTime.setText("" + currentTime +" s");
        return ret;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
