package com.example.cloo2019

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_maps__directions.*
import kotlinx.android.synthetic.main.activity_user_add_toilet.*
import java.io.IOException
import com.google.android.gms.maps.SupportMapFragment
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener





class UserAddToiletActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private lateinit var locationCallback: LocationCallback
    // 2
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    private lateinit var markerOptions: MarkerOptions

    private var toiletAccess: Int = 0
    private var genderType: Int = 0
    private var toiletType: Int = 0
    private lateinit var toiletName: EditText
    private lateinit var locationAddress: EditText
    private lateinit var setLocation: Location
    private lateinit var tvLatLngDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_add_toilet)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setLocation = CurrentLocation.getLastLocation()!!

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView2) as SupportMapFragment
        mapFragment.getMapAsync(this)

        tvLatLngDetails = findViewById<TextView>(R.id.textViewLocationDetails)
        tvLatLngDetails.text = CurrentLocation.getLocationDetailsAsString()

        toiletName = findViewById(R.id.editTextToiletName)
        locationAddress = findViewById(R.id.editTextAddress)
//        ratingBar = findViewById(R.id.ratingBarUser)

//        userId = findViewById(R.id.editTextComments)

        val toiletAccessList =
//            arrayOf("Free Public Toilet", "2. Pay and Use Toilet", "3. For Customers Only", "4. Restricted Entry", "5. Private")
            arrayOf(getString(R.string.array_toilet_access_free_toilet),  // "Free Public Toilet",
                getString(R.string.array_toilet_access_pay_and_use),  // "2. Pay and Use Toilet",
                getString(R.string.array_toilet_access_for_customers_only), // "3. For Customers Only",
                getString(R.string.array_toilet_access_restricted_entry), // "4. Restricted Entry",
                getString(R.string.array_toilet_access_private_toilet)) // "5. Private")
        val spinnerToiletAccess = findViewById<Spinner>(R.id.spinner_toiletAccessUser)
        if (spinnerToiletAccess != null) {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, toiletAccessList)
            spinnerToiletAccess.adapter = arrayAdapter
            Log.i("TOILET ACCESS", "^^^^^^^^^^^^^^^ $toiletAccess ^^^^^^^^^^^^^^^^^^^^")
            spinnerToiletAccess.setSelection(toiletAccess - 1) // -1 is necessary for 0 index offset
            spinnerToiletAccess.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val toiletAccessString = toiletAccessList[position]
                    toiletAccess = when (toiletAccessString) {
                        getString(R.string.array_toilet_access_free_toilet) -> { 1 }
                        getString(R.string.array_toilet_access_pay_and_use) -> { 2 }
                        getString(R.string.array_toilet_access_for_customers_only) -> { 3 }
                        getString(R.string.array_toilet_access_restricted_entry) -> { 4 }
                        getString(R.string.array_toilet_access_private_toilet) -> { 5 }
                        else -> { 0 }
                    }
                }
            }
        }

        val spinnerGenderList = arrayOf(getString(R.string.array_toilet_gender_gents_only),     // "Gents Only",
            getString(R.string.array_toilet_gender_ladies_only),        // "Ladies Only",
            getString(R.string.array_toilet_gender_ladies_and_gents),   // "Ladies and Gents (Separate)",
            getString(R.string.array_toilet_gender_unisex))             // "Unisex")

        val spinnerGender = findViewById<Spinner>(R.id.spinner_GenderUser)
        if (spinnerToiletAccess != null) {
            val arrayAdapterGender = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerGenderList)

            spinnerGender.adapter = arrayAdapterGender

            spinnerGender.setSelection(genderType - 1)  // -1 is necessary for 0 index offset
            spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val genderString = spinnerGenderList[position]

                    genderType = when (genderString) {
                        getString(R.string.array_toilet_gender_gents_only) -> { 1 }
                        getString(R.string.array_toilet_gender_ladies_only) -> { 2 }
                        getString(R.string.array_toilet_gender_ladies_and_gents) -> { 3 }
                        getString(R.string.array_toilet_gender_unisex) -> { 4 }

                        else -> { 0 }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
        }

        when (toiletType) {
            1 -> { checkBoxCommodeUser.isChecked = true }
            2 -> { checkBoxIndianPanUser.isChecked = true }
            3 -> { checkBoxUrinalUser.isChecked = true }
        }

        // TODO - Add a map listner which will allow me to move the location


        val buttonSubmit = findViewById<Button>(R.id.buttonUserSubmit)
        buttonSubmit.setOnClickListener {
            saveUserInputs()
//            finish()
        }
    }

    override fun onMarkerClick(p0: Marker?) = false

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.getUiSettings().setZoomControlsEnabled(true)
        mMap.setOnMarkerClickListener(this)


        //******************************

        mMap.setOnMarkerDragListener(object:OnMarkerDragListener {
            override fun onMarkerDragStart(marker:Marker) {
 // TODO Auto-generated method stub
                // Here your code
                Toast.makeText(this@UserAddToiletActivity, "Dragging Start",
                Toast.LENGTH_SHORT).show()
            }

            override fun onMarkerDragEnd(marker:Marker) {
                // TODO Auto-generated method stub
                val position = marker.getPosition() //

                Log.d("TEMP", "New location = ${position.latitude}, ${position.longitude}")
                Log.d("TEMP", "New Address = ${getAddress(position)}")

                if(position != null) {
                    setLocation.latitude = position.latitude
                    setLocation.longitude = position.longitude
                    setLocation.altitude = 0.0
                    setLocation.accuracy = 0F
                    setLocation.provider = "manual"

                    tvLatLngDetails.text =
                        "Lat: ${setLocation.latitude}\nLng: ${setLocation.longitude}\nAlt: ${setLocation.altitude}\n" +
                                "Accuracy Captured: ${setLocation.accuracy}\nProvider: ${setLocation.provider}"
                    editTextAddress.setText(getAddress(position))
                }

            }

            override fun onMarkerDrag(marker:Marker) {
                // TODO Auto-generated method stub
                // Toast.makeText(MainActivity.this, "Dragging",
                // Toast.LENGTH_SHORT).show();
                println("Draagging")
            }
        })

        //***********************

        setUpMap()

        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
            }
        }



//        var dest = CurrentLocation.getLastLocation()
//        if(dest!=null){
//            placeMarkerOnMap(dest)
//            val currentLatLng = LatLng(dest.latitude, dest.longitude)
//            mMap.addMarker(MarkerOptions().position(currentLatLng).title(getString(R.string.strings_toilet_location)))
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18.0f))

//        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }

    }


    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        markerOptions = MarkerOptions().position(location)
        val titleStr = getAddress(location)  // add these two lines

        editTextAddress.setText(titleStr)
        markerOptions.title(titleStr)
        markerOptions.draggable(true)

//        markerOptions.title("Kartik")
        // 2
        mMap.addMarker(markerOptions)

    }

    private fun getAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // 2
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                Log.d("TEMP", "Value of address = $address, j = ${address.maxAddressLineIndex} ")
                Log.d("TEMP", "Just for break in execution")
                for (i in 0 until address.maxAddressLineIndex+1) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }





    private fun saveUserInputs(){

        val toiletNameString = toiletName.text.toString()
//        setLocation = CurrentLocation.getLastLocation()!!
//        val userComments = editTextComments.text.toString()
//        val chosenRating = ratingBar.rating.toInt()

//        val savedLatlng = markerOptions.position
//        Log.d("TEMP", "New position = ${savedLatlng.latitude}, ${savedLatlng.longitude}")
//        editTextAddress.setText(getAddress(savedLatlng))
//        Log.d("TEMP", "New Address ${editTextAddress.text.toString()}")
//
        val looAddress = editTextAddress.text.toString()


        if(setLocation == null){
            Toast.makeText(this@UserAddToiletActivity, getString(R.string.no_location_detected),
                Toast.LENGTH_SHORT).show()      // "Please set location"
            return
        }

        val FireDBRef = FirebaseDatabase.getInstance().getReference("ToiletMaster")
        val toiletId = FireDBRef.push().key
        if(toiletId == null) {
            Toast.makeText(this@UserAddToiletActivity,  getString(R.string.error_msg_toilet_record_id_null)     //"Toilet Master push key returned null"
                , Toast.LENGTH_SHORT).show()
            return
        }
        val toiletMasterRecord = ToiletMaster(toiletId, setLocation?.latitude!!, setLocation?.longitude!!,
            setLocation?.altitude!!, toiletNameString,  looAddress, 0.0,0,0,
            0,0,toiletAccess,genderType,0,"",
            "","", "","","", "",
            CurrentUser.getCurrentUser(), CurrentUser.getCurrentUserID())

        FireDBRef.child(toiletId).setValue(toiletMasterRecord).addOnCompleteListener {
            Toast.makeText(this@UserAddToiletActivity, getString(R.string.status_toilet_master_created)     //"New toiler created..."
                , Toast.LENGTH_SHORT).show()
        }
//        Toast.makeText(this@MainActivityAddNewLoo,"Done!", Toast.LENGTH_SHORT).show()

    /*  KARTIK_20_APR_2019 TOP
    *   Switch activity to User Rating
    *   Provide the Toilet ID as intent values to the User Rating Screen
    *   Comment the rest of the code
    *   Code Cleanup to remove unwanted controls, variables
    *
    *
    *
    * */

        val i = Intent(this, MainActivityRating::class.java)

        i.putExtra(getString(R.string.intent_toilet_id), toiletId)
        i.putExtra(getString(R.string.intent_toilet_address), looAddress)

        i.putExtra(getString(R.string.intent_rating_sum), 0)
        i.putExtra(getString(R.string.intent_number_of_ratings), 0)
        i.putExtra(getString(R.string.intent_rating_sum_lifetime), 0)
        i.putExtra(getString(R.string.intent_number_of_ratings_lifetime), 0)

        startActivity(i)




//    val database = FirebaseDatabase.getInstance()
//    val ratingRef = database.getReference("ToiletRating/$toiletId")
//    val toiletRatingId = ratingRef.push().key
//    if(toiletRatingId == null) {
//        Toast.makeText(this@UserAddToiletActivity, getString(R.string.error_msg_toilet_rating_id_null)   // "Toilet Record ID push key returned null"
//            , Toast.LENGTH_SHORT).show()
//        return
//    }
//    val toiletRatingRecord = UserRating(toiletRatingId,toiletId,"",chosenRating,userComments,"" )
//    ratingRef.child(toiletRatingId).setValue(toiletRatingRecord).addOnCompleteListener {
//        Toast.makeText(this@UserAddToiletActivity, getString(R.string.status_toilet_rating_saved)   // "and Rating Saved.  Thank you"
//            , Toast.LENGTH_SHORT).show()
//    }
/*  KARTIK_20_APR_2019 END */


        finish()



}

companion object {
    private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    private const val REQUEST_CHECK_SETTINGS = 2
}


}
