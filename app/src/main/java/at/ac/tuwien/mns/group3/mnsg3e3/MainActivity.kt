package at.ac.tuwien.mns.group3.mnsg3e3

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Button
import android.widget.Toast
import at.ac.tuwien.mns.group3.mnsg3e3.model.LocationReport
import at.ac.tuwien.mns.group3.mnsg3e3.service.LocationReportIntentService

class MainActivity : AppCompatActivity() {

    private var permissions: Boolean = false
    private var locationReceiver: LocationReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.permissions = askPermissions()
        this.registerLocationReceiver()

        val button = findViewById<Button>(R.id.button1)
        button.setOnClickListener { test() }
    }

    private fun askPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CHANGE_WIFI_STATE
            ) != PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_WIFI_STATE
            ) != PackageManager.PERMISSION_DENIED
        ) {
            return true
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
            ),
            3223
        )
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 3223) {
            this.permissions = grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] ==
                    PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun registerLocationReceiver() {
        this.locationReceiver = LocationReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(LocationReportIntentService.LOCATIONREPORT_SERVICE)

        registerReceiver(locationReceiver, intentFilter)
    }

    private fun test() {
        if (!this.permissions) {
            this.askPermissions()
            return
        }
        startLocationService()
    }

    private fun startLocationService() {
        val intent = Intent()
        intent.setClass(this, LocationReportIntentService::class.java)
        startService(intent)
    }

    private inner class LocationReceiver : BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            var bundle: Bundle = intent.extras
            var report : LocationReport = bundle.getSerializable(LocationReportIntentService.LOCATIONREPORT_INFO) as LocationReport

            //var report: LocationReport = intent.getSerializableExtra(LocationIntentService.LOCATIONREPORT_INFO) as LocationReport
            Toast.makeText(ctx, report.difference.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}