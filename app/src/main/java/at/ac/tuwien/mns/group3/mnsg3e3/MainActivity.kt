package at.ac.tuwien.mns.group3.mnsg3e3

import android.Manifest
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.text.Editable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import at.ac.tuwien.mns.group3.mnsg3e3.interfaces.ICommunication
import at.ac.tuwien.mns.group3.mnsg3e3.model.LocationReport
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.AppDatabase
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.AppDatabaseFactory
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.ReportRepository
import at.ac.tuwien.mns.group3.mnsg3e3.service.LocationReportIntentService
import at.ac.tuwien.mns.group3.mnsg3e3.service.PreferencesService
import at.ac.tuwien.mns.group3.mnsg3e3.util.BaseAdapter
import at.ac.tuwien.mns.group3.mnsg3e3.util.ExceptionHandler
import at.ac.tuwien.mns.group3.mnsg3e3.util.ReportConverter
import com.commonsware.cwac.saferoom.SQLCipherUtils
import com.commonsware.cwac.saferoom.SafeHelperFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ICommunication {

    private var reports: MutableList<Report> = mutableListOf<Report>();
    private var report:Report? = null;
    @Inject lateinit var repo:ReportRepository
    @Inject lateinit var prefService:PreferencesService
    @Inject lateinit var dbFactory: AppDatabaseFactory
    private var locationServiceInAction = false
    private var secureModeOn:Boolean = false


    override fun delete(report: Report?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        repo.delete(report)

    }

    override fun selected(): Report? {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return report
    }

    public fun isSecureModeOn(): Boolean {
        return secureModeOn
    }


    private var permissions: Boolean = false
    private var locationReportReceiver: LocationReportReceiver? = null

    /**
     * Asks for permissions if not granted yet.
     * Registers the receiver.
     * Init UI.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as GeolocationApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.permissions = askPermissions()

        val button = findViewById<FloatingActionButton>(R.id.button1)
        button.setOnClickListener { test() }

        // Old
        //this.repo = ReportRepository(application)
        val btn_sec = findViewById<FloatingActionButton>(R.id.btn_sec)
        btn_sec.setOnClickListener { changeSecurityMode() }

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        secureModeOn = prefService.isSecureMode(this)


        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))


        if (secureModeOn) {
            val alert = AlertDialog.Builder(this)

            alert.setTitle("access encrypted database")
            alert.setMessage("type in the password")
            alert.setCancelable(false)

            val input = EditText(this)
            alert.setView(input)

            alert.setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                login(input.text)
            })

            alert.show()
            btn_sec.isEnabled = false
            btn_sec.hide()
        } else {
            this.repo.connectDatabase()
        }


        updateListView()

        repo?.allReports?.observe(this, object: Observer<MutableList<Report>> {
            override fun onChanged(reps: MutableList<Report>?) {
                if (reps != null) {
                    reports = reps
                    updateListView()
                }
            }})


    }

    fun login(editable: Editable) {
        val factory = SafeHelperFactory.fromUser(editable)
        this.repo.connectDatabase(factory)
        repo?.allReports?.observe(this, object: Observer<MutableList<Report>> {
            override fun onChanged(reps: MutableList<Report>?) {
                if (reps != null) {
                    reports = reps
                    updateListView()
                }
            }})
    }

    override fun onStart() {
        super.onStart()
        this.registerLocationReceiver()
        Log.i("MainActivity", "Registered LocationReceiver")
    }

    /**
     * Unregisteres the locationReportReceiver
     */
    override fun onStop() {
        super.onStop()
        unregisterReceiver(locationReportReceiver)
        Log.i("MainActivity", "Unregistered LocationReceiver")
    }

    /**
     * Checks if permisions are already granted. if not it returns false and requests the permissions.
     */
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

    /**
     * Checks the result of the permision request and sets the permissions field if all permissions got granted.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 3223) {
            this.permissions = grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] ==
                    PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Register a new receiver for the LocationReportIntentService result.
     * Needs to be called in the beginning.
     */
    private fun registerLocationReceiver() {
        this.locationReportReceiver = LocationReportReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(LocationReportIntentService.LOCATIONREPORT_SERVICE)

        registerReceiver(locationReportReceiver, intentFilter)
    }

    private fun test() {
        if (!this.permissions) {
            this.askPermissions()
            return
        }
        if (locationServiceInAction) {
            Toast.makeText(this, "Wait for the result before starting a new Request", Toast.LENGTH_SHORT).show()
            return
        }
        startLocationService()
        Toast.makeText(this, "Sending a new Location Service Call. This could take a while ...", Toast.LENGTH_LONG).show()
        //updateListView()
    }

    private fun changeSecurityMode() {
        if (secureModeOn) {
            //currently no going back to normal mode
        } else {
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            //sharedPref.edit().putBoolean("secureModeOn", true)
            prefService.setSecureMode(this, true)

            val alert = AlertDialog.Builder(this)

            alert.setTitle("encrypt database")
            alert.setMessage("type in the password")

            val input = EditText(this)
            alert.setView(input)

            alert.setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                switchMode(input.text)
            })

            alert.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                //canceled
            })

            alert.show()
        }
    }

    private fun switchMode(inputEditable: Editable) {

        val db = dbFactory.getDatabase(this)
        db.close()
        SQLCipherUtils.encrypt(applicationContext,"report_database",inputEditable)

        val factory = SafeHelperFactory.fromUser(inputEditable)
        dbFactory.refreshInstance()
        this.repo.connectDatabase(factory)
        repo?.allReports?.observe(this, object: Observer<MutableList<Report>> {
            override fun onChanged(reps: MutableList<Report>?) {
                if (reps != null) {
                    reports = reps
                    updateListView()
                }
            }})


        secureModeOn = true
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        //sharedPref.edit().putBoolean("secureModeOn", true).commit()
        prefService.setSecureMode(this, true)
        btn_sec.hide()
        btn_sec.isEnabled = false

    }

    /**
     * This method invokes a new computation of a LocationReport.
     * It sends a new request to the LocationReportIntentService.
     */
    private fun startLocationService() {
        val intent = Intent()
        intent.setClass(this, LocationReportIntentService::class.java)
        startService(intent)
        locationServiceInAction = true
    }


    fun updateListView() {
        Log.i("mainActivity", "Updating MainActivity data")
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        var manager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(manager)
        var adapter = BaseAdapter(reports)
        recyclerView.setAdapter(adapter)
    }

    /**
     * The class which represensts the receiver for a computed LocationReport from the
     * IntentService.
     */
    private inner class LocationReportReceiver : BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            var bundle: Bundle = intent.extras
            var report : LocationReport = bundle.getSerializable(LocationReportIntentService.LOCATIONREPORT_INFO) as LocationReport

            var rep : Report = ReportConverter.toModelView(report)

            repo?.insert(rep)
            locationServiceInAction = false

        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            findViewById<FloatingActionButton>(R.id.button1).show()
            if (!secureModeOn) findViewById<FloatingActionButton>(R.id.btn_sec).show()
            supportFragmentManager.popBackStack()

        } else {
            Toast.makeText(this, "Closing the app.", Toast.LENGTH_LONG).show()
            finish()
        }
    }


}