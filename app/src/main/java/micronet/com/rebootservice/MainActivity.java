package micronet.com.rebootservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent service = new Intent(this, RebootTrackerService.class);
        startService(service);
    }

    @Override
    protected void onStart() {
        super.onStart();
        finish();
    }
}
