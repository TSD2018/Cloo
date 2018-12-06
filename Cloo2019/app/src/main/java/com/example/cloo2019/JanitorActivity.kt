/* Created by Kartik Venkataraman, 3 Dec 2018 */

package com.example.cloo2019

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_janitor.*
import kotlinx.android.synthetic.main.content_janitor.*

class JanitorActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var toiletID: String
    private lateinit var toiletAddress: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_janitor)
        setSupportActionBar(toolbar)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val radioGroupAccessType = findViewById<RadioGroup>(R.id.radiogroup_access)
        radioGroupAccessType?.setOnCheckedChangeListener { group, checkedId ->
            var text = "You selected: "
            if (R.id.radioButton_Public == checkedId) "Public"
            if (R.id.radioButton_Restricted == checkedId) "Restricted"
            if (R.id.radioButtonPrivate == checkedId) "Private"
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        val dest = LatLng(intent.getDoubleExtra("LAT", 0.0), intent.getDoubleExtra("LNG", 0.0))

        val latVal = dest.latitude
        val lngVal = dest.longitude
        val altVal = 0.0

        toiletID = intent.getStringExtra("TOILET_ID")
        toiletAddress = intent.getStringExtra("TOILET_ADDRESS")

        val looLatLng = findViewById<TextView>(R.id.textView_Latlng)
        looLatLng.setText("Lat: ${dest.latitude.toString()}, Lng: ${dest.longitude.toString()}")

        val looAddress = findViewById<EditText>(R.id.editText_Loo_Address)
        looAddress.setText(toiletAddress)

        var genderType = 0
        val maintainedBy= findViewById<EditText>(R.id.editTextJanitor)
        val contactJanitor= findViewById<EditText>(R.id.editTextContact)

        if(findViewById<CheckBox>(R.id.checkBoxGents).isChecked) {genderType = 1}
        if(findViewById<CheckBox>(R.id.checkBoxLadies).isChecked) {genderType = 2}
        if(findViewById<CheckBox>(R.id.checkBoxUniSex).isChecked) {genderType = 3}

        mMap.addMarker(MarkerOptions().position(dest).title("Toilet Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dest))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 12.0f))

        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.isMyLocationEnabled = true

        val saveButton = findViewById<Button>(R.id.buttonSave)
        saveButton.setOnClickListener(){
            // Update the record with all contents into the Toilet Master for that ToiletID

            // var latval: Double, var lngVal: Double, var altVal: Double, var address: String, var userRating: Int,
            //    var toiletAccess: Int, var genderType: Int, var toiletType: Int, var maintainedBy: String, var contact: String

            var toiletMaster = ToiletMaster()


            toiletMaster.id = toiletID
            toiletMaster.latval = latVal
            toiletMaster.lngVal = lngVal
            toiletMaster.altVal = altVal
            toiletMaster.address = looAddress.text.toString()
            toiletMaster.userRating = 0     // should be 0, will be computed based on the ToiletRatings
            toiletMaster.toiletAccess = genderType   // should be changed to a constant to indicate PUBLIC
            toiletMaster.maintainedBy = maintainedBy.text.toString()
            toiletMaster.contact = contactJanitor.text.toString()


            val FireDBRef = FirebaseDatabase.getInstance().getReference("toilet_master")

            FireDBRef.child(toiletID).setValue(toiletMaster).addOnCompleteListener {
                Toast.makeText(this@JanitorActivity,"Thank you! Toilet Master Updated", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this@JanitorActivity,"KV_DEBUG:: End of onMapReady in JanitorActivity!", Toast.LENGTH_SHORT).show()


        }
    }




}
