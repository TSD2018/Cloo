/* Created by Kartik Venkataraman, 3 Dec 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */
/* Rev 0.22  Mapped string literals to STRINGS.XML - Kartik Venkataraman 31 Jan 2019 */
/*           No changes to FireBase, will handle that as a part of the class restructuring */
/*           Spinner text arrays are not updated.  Needs a little more study before implementing */
/* Rev 0.23  Mapped spinner controls to strings,xml */

package com.example.cloo2019

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
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

    // intent variable holders - all may not be required to be global- need to check
    private lateinit var toiletID: String
    private lateinit var toiletName: String
    private lateinit var mMap: GoogleMap
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var alt: Double = 0.0
    private lateinit var toiletAddress: String
    private var toiletAccess: Int = 0
    private var genderType: Int = 0
    private var toiletType: Int = 0
    private lateinit var toiletJanitor: String
    private lateinit var toiletContact: String

    private lateinit var toiletMaintainedBy: String
    private var userRating: Double = 0.0
    private lateinit var lastCleanedTimeStamp: String
    private lateinit var lastCleanedTimeStampPresentable: String
    private lateinit var toiletOwnedBy: String
    private lateinit var toiletSponsor: String
    private lateinit var sponsorPath: String   // KARTIK_31_MARCH_2019

    // form control member variable, requiring global scope
    private lateinit var looAddress: EditText
    private lateinit var editToiletName: EditText
    private lateinit var editMaintainedBy: EditText
    private lateinit var editContactNumber: EditText
    private lateinit var checkCommode: CheckBox
    private lateinit var checkIndianPan: CheckBox
    private lateinit var checkUrinal: CheckBox

    private var ratingSum: Int = 0
    private var numberOfRatings: Int = 0
    private var ratingSumLifeTime: Int = 0
    private var numberOfRatingsLifeTime: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_janitor)
        setSupportActionBar(toolbar)


        /************* 31 Jan 2019 - Kartik TOP ****************/
        // Replacing the intent string literals with the constants defined
        toiletID = intent.getStringExtra(getString(R.string.intent_toilet_id))    // ("TOILET_ID")
        toiletName = intent.getStringExtra(getString(R.string.intent_toilet_name))  // ("TOILET_NAME")
        lat = intent.getDoubleExtra(getString(R.string.intent_latitude), 0.0)  // ("LAT", 0.0)
        lng = intent.getDoubleExtra(getString(R.string.intent_longitude), 0.0) //("LNG", 0.0)
        alt = intent.getDoubleExtra(getString(R.string.intent_altitude),0.0)  //("ALT", 0.0)
        toiletAddress = intent.getStringExtra(getString(R.string.intent_toilet_address)) //"TOILET_ADDRESS")
        toiletAccess = intent.getIntExtra(getString(R.string.intent_toilet_access),0) //"TOILET_ACCESS", 0)
        genderType = intent.getIntExtra(getString(R.string.intent_toilet_gender), 0) //("TOILET_GENDER", 0)
        toiletType = intent.getIntExtra(getString(R.string.intent_toilet_type), 0) //("TOILET_TYPE", 0)
        toiletJanitor = intent.getStringExtra(getString(R.string.intent_janitor))  // ("TOILET_JANITOR")
        toiletContact = intent.getStringExtra(getString(R.string.intent_contact_number))  // ("TOILET_CONTACT")
        toiletMaintainedBy = intent.getStringExtra(getString(R.string.intent_toilet_maintained_by))  // ("TOILET_MAINTAINED BY")
        toiletOwnedBy = intent.getStringExtra(getString(R.string.intent_toilet_owner))  // ("TOILET_OWNED BY")
        toiletSponsor = intent.getStringExtra(getString(R.string.intent_sponsor))  // ("TOILET_SPONSOR")
        sponsorPath = intent.getStringExtra("SPONSOR_IMAGE_PATH")   // KARTIK_31_MARCH_2019

        userRating = intent.getDoubleExtra(getString(R.string.intent_rating), 0.0)  // ("RATING", 0.0)
        lastCleanedTimeStamp = intent.getStringExtra(getString(R.string.intent_last_cleaned))  // ("LAST_CLEANED")
        lastCleanedTimeStampPresentable = intent.getStringExtra(getString(R.string.intent_last_cleaned_presentable_timestamp))  // ("LAST_CLEANED_PRESENTABLE")

        ratingSum = intent.getIntExtra(getString(R.string.intent_rating_sum),0) //("RATING_SUM", 0)
        numberOfRatings = intent.getIntExtra(getString(R.string.intent_number_of_ratings),0) //("NUMBER_OF_RATINGS", 0)
        ratingSumLifeTime = intent.getIntExtra(getString(R.string.intent_rating_sum_lifetime),0) //("RATING_SUM_LIFETIME", 0)
        numberOfRatingsLifeTime = intent.getIntExtra(getString(R.string.intent_number_of_ratings_lifetime),0) //("NUMBER_OF_RATINGS_LIFETIME", 0)

        /************* 31 Jan 2019 - Kartik END ****************/


        editToiletName = findViewById(R.id.editText_ToiletName)
        editToiletName.setText(toiletName)
        editMaintainedBy = findViewById(R.id.editTextMaintainedBy)
        editMaintainedBy.setText(toiletMaintainedBy)
        editContactNumber = findViewById(R.id.editTextContact)
        editContactNumber.setText(toiletContact)
        checkCommode = findViewById(R.id.checkBoxCommode)
        checkIndianPan = findViewById(R.id.checkBoxIndianPan)
        checkUrinal = findViewById(R.id.checkBoxUrinal)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val looLatLng = findViewById<TextView>(R.id.textView_Latlng)
        looLatLng.text = getString(R.string.strings_latitude) + " $lat, " + getString(R.string.strings_longitude) +" $lng"

        looAddress = findViewById(R.id.editText_Loo_Address)
        looAddress.setText(toiletAddress)

        val toiletAccessList =
//            arrayOf("Free Public Toilet", "2. Pay and Use Toilet", "3. For Customers Only", "4. Restricted Entry", "5. Private")
            arrayOf(getString(R.string.array_toilet_access_free_toilet),  // "Free Public Toilet",
                getString(R.string.array_toilet_access_pay_and_use),  // "2. Pay and Use Toilet",
                getString(R.string.array_toilet_access_for_customers_only), // "3. For Customers Only",
                getString(R.string.array_toilet_access_restricted_entry), // "4. Restricted Entry",
                getString(R.string.array_toilet_access_private_toilet)) // "5. Private")
        val spinnerToiletAccess = findViewById<Spinner>(R.id.spinner_toiletAccess)
        if (spinnerToiletAccess != null) {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, toiletAccessList)

            spinnerToiletAccess.adapter = arrayAdapter

            Log.i("TOILET ACCESS", "^^^^^^^^^^^^^^^ $toiletAccess ^^^^^^^^^^^^^^^^^^^^")

            spinnerToiletAccess.setSelection(toiletAccess - 1) // -1 is necessary for 0 index offset

            spinnerToiletAccess.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val toiletAccessString = toiletAccessList[position]

                    toiletAccess = when (toiletAccessString) {
                        getString(R.string.array_toilet_access_free_toilet)         -> { 1 }
                        getString(R.string.array_toilet_access_pay_and_use)         -> { 2 }
                        getString(R.string.array_toilet_access_for_customers_only)  -> { 3 }
                        getString(R.string.array_toilet_access_restricted_entry)    -> { 4 }
                        getString(R.string.array_toilet_access_private_toilet)      -> { 5 }

//                        "Free Public Toilet" -> { 1 }
//                        "Pay and Use Toilet" -> { 2 }
//                        "For Customers Only" -> { 3 }
//                        "Restricted Entry"  -> { 4 }
//                        "Private" -> { 5 }
                        else -> { 0 }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
        }

//        val spinnerGenderList = arrayOf("Gents Only", "Ladies Only", "Ladies and Gents (Separate)", "Unisex")
        val spinnerGenderList = arrayOf(getString(R.string.array_toilet_gender_gents_only),     // "Gents Only",
            getString(R.string.array_toilet_gender_ladies_only),        // "Ladies Only",
            getString(R.string.array_toilet_gender_ladies_and_gents),   // "Ladies and Gents (Separate)",
            getString(R.string.array_toilet_gender_unisex))             // "Unisex")

        val spinnerGender = findViewById<Spinner>(R.id.spinner_Gender)
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

//                        "Gents Only" -> { 1 }
//                        "Ladies Only" -> { 2 }
//                        "Ladies and Gents (Separate)" -> { 3 }
//                        "Unisex" -> { 4 }
                        else -> { 0 }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
        }

        when (toiletType) {
            1 -> { checkCommode.isChecked = true }
            2 -> { checkIndianPan.isChecked = true }
            3 -> { checkUrinal.isChecked = true }
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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

        /*******  31 JAN 2019 KARTIK TOP ****/
        val dest = LatLng(intent.getDoubleExtra(getString(R.string.intent_latitude), 0.0),
            intent.getDoubleExtra(getString(R.string.intent_longitude), 0.0))
        val userRating = intent.getDoubleExtra(getString(R.string.intent_rating), 0.0)
        val altVal = intent.getDoubleExtra(getString(R.string.intent_altitude), 0.0)


//        val dest = LatLng(intent.getDoubleExtra("LAT", 0.0), intent.getDoubleExtra("LNG", 0.0))
//        val userRating = intent.getDoubleExtra("RATING", 0.0)
        val latVal = dest.latitude
        val lngVal = dest.longitude
//        val altVal = intent.getDoubleExtra("ALT", 0.0)
        mMap.addMarker(MarkerOptions().position(dest).title(getString(R.string.strings_toilet_location)))

//        mMap.addMarker(MarkerOptions().position(dest).title("Toilet Location"))
        /*******  31 JAN 2019 KARTIK END ****/

        mMap.moveCamera(CameraUpdateFactory.newLatLng(dest))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 12.0f))

        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.isMyLocationEnabled = true

        val saveButton = findViewById<Button>(R.id.buttonSave)
        saveButton.setOnClickListener {

            val toiletMaster = ToiletMaster()
            var toiletType = 0
            if (findViewById<CheckBox>(R.id.checkBoxCommode).isChecked) {
                toiletType = 1
            }
            if (findViewById<CheckBox>(R.id.checkBoxIndianPan).isChecked) {
                toiletType = 2
            }
            if (findViewById<CheckBox>(R.id.checkBoxUrinal).isChecked) {
                toiletType = 3
            }

// Items which can be changed from mobile.
// TODO we should add restrictions or onetime entry only for the below items
            toiletMaster.lat = latVal
            toiletMaster.lng = lngVal
            toiletMaster.alt = altVal
            toiletMaster.toiletName = editText_ToiletName.text.toString()
            toiletMaster.address = looAddress.text.toString()
            toiletMaster.toiletAccess = toiletAccess
            toiletMaster.contact = editContactNumber.text.toString()
            toiletMaster.genderType = genderType
            toiletMaster.toiletType = toiletType
            toiletMaster.maintainedBy = editMaintainedBy.text.toString()

// Items which CANNOT be changed from the mobile
            toiletMaster.toiletId = toiletID
            toiletMaster.userRating =
                    userRating   // retain the original read value, will be computed based on the ToiletRatings every time rating is added
            toiletMaster.toiletOwnerBy = toiletOwnedBy   // from the server
            toiletMaster.toiletSponsor = toiletSponsor   // from the server

            toiletMaster.lastCleanedBy = toiletJanitor   // from clean record
            toiletMaster.lastCleanedTimeStamp = lastCleanedTimeStamp    // from clean record
            toiletMaster.ratingSum = ratingSum   // retain the original value
            toiletMaster.numberOfRatings = numberOfRatings  // retain the original value
            toiletMaster.ratingSumLifeTime = ratingSumLifeTime   // retain the original value
            toiletMaster.numberOfRatingsLifeTime = numberOfRatingsLifeTime    // retain the original value
            toiletMaster.sponsorImagePath = sponsorPath  // from the server  :: KARTIK_31_MARCH_2019

            val fireDBRef = FirebaseDatabase.getInstance().getReference("ToiletMaster")
            fireDBRef.child(toiletID).setValue(toiletMaster).addOnCompleteListener {
                Toast.makeText(this@JanitorActivity, getString(R.string.status_toilet_master_updated), Toast.LENGTH_SHORT).show()
//                Toast.makeText(this@JanitorActivity, "Thank you! Toilet Master Updated", Toast.LENGTH_SHORT).show()
            }
//            Toast.makeText(this@JanitorActivity, "KV_DEBUG:: End of onMapReady in JanitorActivity!", Toast.LENGTH_SHORT)
//                .show()
            finish()
        }
    }
}
