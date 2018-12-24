package at.ac.tuwien.mns.group3.mnsg3e3;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import at.ac.tuwien.mns.group3.mnsg3e3.service.LocationIntentService;

public class StarActivity extends AppCompatActivity {

    private boolean permissions;
    private LocationReceiver locationReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.permissions = askPermissions();
        this.registerLocationReceiver();

        Button button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    private boolean askPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
            return true;
        }
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 3223);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3223) {
            this.permissions = grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void registerLocationReceiver() {
        this.locationReceiver = new LocationReceiver();
        IntentFilter intentFilter =  new IntentFilter();
        intentFilter.addAction(LocationIntentService.MOZILLA_LOCATION_INFO);

        registerReceiver(locationReceiver, intentFilter);
    }

    private void test() {
        if (!this.permissions) {
            this.askPermissions();
            return;
        }
        startLocationService();
    }

    private void startLocationService() {
        Intent intent = new Intent();
        intent.setClass(this, LocationIntentService.class);
        startService(intent);
    }

    private class LocationReceiver extends BroadcastReceiver {
        @Override
         public void onReceive(Context ctx, Intent intent) {
            intent.getStringExtra(LocationIntentService.LOCATION_INFO);
        }
    }
}
