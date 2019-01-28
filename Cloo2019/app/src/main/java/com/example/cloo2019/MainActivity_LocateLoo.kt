/* Created by Kartik Venkataraman, 14 Nov 2018 */

package com.example.cloo2019

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_main__locate_loo.*

class MainActivity_LocateLoo : AppCompatActivity() {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationAddress: TextView? = null
    var mLastLocation: Location? = null

    lateinit var fireDBRef: DatabaseReference
    lateinit var looList: MutableList<ToiletMaster>  // 8-JAN-2019:KARTIK
//    lateinit var looList: MutableList<cUserInput>  // 8-JAN-2019:KARTIK
    lateinit var looListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main__locate_loo)
        setSupportActionBar(toolbar)

        looListView = findViewById(R.id.listview_loo_locations)
        looList = mutableListOf()
        fireDBRef = FirebaseDatabase.getInstance().getReference("ToiletMaster")
//        fireDBRef = FirebaseDatabase.getInstance().getReference("toilet_master")
//        fireDBRef = FirebaseDatabase.getInstance().getReference("ratings")


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationAddress = findViewById<View>(R.id.textView_current_location) as TextView
        mLocationAddress!!.text = ""
        getLastLocation()

        val buttonGetLatLong=findViewById<Button>(R.id.button_RefreshLocation)
        buttonGetLatLong?.setOnClickListener {
            mLocationAddress!!.text = ""
            getLastLocation()
        }


        fireDBRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    looList.clear()
                    val temp = p0.children
                    val qait=0
                    for (looCnt in p0.children){
                        val loo = looCnt?.getValue(ToiletMaster::class.java)  // 8-JAN-2019:KARTIK
//                        val loo = looCnt.getValue(cUserInput::class.java)  // 8-JAN-2019:KARTIK
                        looList.add(loo!!)
                    }

                    val adapter = Locationadapter(this@MainActivity_LocateLoo, R.layout.loos, looList

                    )
                    looListView.adapter = adapter
                }
            }
        })

        fab.setOnClickListener{
                val i = Intent(this, MainActivity_AddNewLoo::class.java)
                startActivity(i)
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient!!.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result

                    mLocationAddress!!.text = "Latitude: " + mLastLocation!!.latitude.toString() +
                            "\nLogitude: " + mLastLocation!!.longitude.toString()
                    CurrentLocation.setLastLocation(mLastLocation!!)
                } else {
                    Log.i(MainActivity_LocateLoo.TAG, "getLastLocation:exception", task.exception)
                    showMessage(getString(R.string.no_location_detected))
                }
            }
    }

    private fun showMessage(text: String) {
        Toast.makeText(this@MainActivity_LocateLoo, text, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main_activity__locate_loo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    public override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.ACCESS_FINE_LOCATION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(MainActivity_LocateLoo.TAG, "Displaying permission rationale to provide additional context.")

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                View.OnClickListener {
                    // Request permission
                    startLocationPermissionRequest()
                })

        } else {
            Log.i(MainActivity_LocateLoo.TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest()
        }
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this@MainActivity_LocateLoo,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MainActivity_LocateLoo.REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun showSnackbar(mainTextStringId: Int, actionStringId: Int,
                             listener: View.OnClickListener) {

        Toast.makeText(this@MainActivity_LocateLoo, getString(mainTextStringId), Toast.LENGTH_LONG).show()
    }

    companion object {

        private val TAG = "LocationProvider"

        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.i(MainActivity_LocateLoo.TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(MainActivity_LocateLoo.TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation()
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                    View.OnClickListener {
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package",
                            BuildConfig.APPLICATION_ID, null)
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    })
            }
        }
    }
}
