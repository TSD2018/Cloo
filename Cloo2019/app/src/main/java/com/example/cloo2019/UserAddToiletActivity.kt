package com.example.cloo2019

import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user_add_toilet.*

class UserAddToiletActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var toiletAccess: Int = 0
    private var genderType: Int = 0
    private var toiletType: Int = 0
    private lateinit var toiletName: EditText
    private lateinit var locationAddress: EditText
    private lateinit var userId: TextView
    private lateinit var editComments: EditText
    private lateinit var setLocation: Location
    lateinit var ratingBar: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_add_toilet)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView2) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val tvLatLngDetails = findViewById<TextView>(R.id.textViewLocationDetails)
        tvLatLngDetails.text = CurrentLocation.getLocationDetailsAsString()

        toiletName = findViewById(R.id.editTextToiletName)
        locationAddress = findViewById(R.id.editTextAddress)
        ratingBar = findViewById(R.id.ratingBarUser)

        userId = findViewById(R.id.editTextComments)

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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.isMyLocationEnabled = true

        var dest = CurrentLocation.getLastLocation()
        if(dest!=null){
            val currentLatLng = LatLng(dest.latitude, dest.longitude)
            mMap.addMarker(MarkerOptions().position(currentLatLng).title(getString(R.string.strings_toilet_location)))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18.0f))
        }
    }



    private fun saveUserInputs(){

        val toiletNameString = toiletName.text.toString()
        setLocation = CurrentLocation.getLastLocation()!!
        val userComments = editTextComments.text.toString()
        val chosenRating = ratingBar.rating.toInt()

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
            setLocation?.altitude!!, toiletNameString,  looAddress, chosenRating.toDouble(),chosenRating,1,
            chosenRating,1,toiletAccess,genderType,0,"","","",
            "","","", "")
        FireDBRef.child(toiletId).setValue(toiletMasterRecord).addOnCompleteListener {
            Toast.makeText(this@UserAddToiletActivity, getString(R.string.status_toilet_master_created)     //"New toiler created..."
                , Toast.LENGTH_SHORT).show()
        }
//        Toast.makeText(this@MainActivityAddNewLoo,"Done!", Toast.LENGTH_SHORT).show()

        val database = FirebaseDatabase.getInstance()
        val ratingRef = database.getReference("ToiletRating/$toiletId")
        val toiletRatingId = ratingRef.push().key
        if(toiletRatingId == null) {
            Toast.makeText(this@UserAddToiletActivity, getString(R.string.error_msg_toilet_rating_id_null)   // "Toilet Record ID push key returned null"
                , Toast.LENGTH_SHORT).show()
            return
        }
        val toiletRatingRecord = UserRating(toiletRatingId,toiletId,"",chosenRating,userComments,"" )
        ratingRef.child(toiletRatingId).setValue(toiletRatingRecord).addOnCompleteListener {
            Toast.makeText(this@UserAddToiletActivity, getString(R.string.status_toilet_rating_saved)   // "and Rating Saved.  Thank you"
                , Toast.LENGTH_SHORT).show()
        }
        finish()



    }

}
