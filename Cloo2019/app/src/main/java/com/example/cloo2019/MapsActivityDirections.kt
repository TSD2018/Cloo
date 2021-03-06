/* Created by Kartik Venkataraman, 14 Nov 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */
/* Rev 0.22  Mapped string literals to STRINGS.XML - Kartik Venkataraman 31 Jan 2019 */
/*           No changes to FireBase, will handle that as a part of the class restructuring */

package com.example.cloo2019

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivityDirections : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var toiletID: String
    private lateinit var toiletAddress: String
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var alt: Double = 0.0
    private var userRating: Double = 0.0
    private lateinit var lastCleanedTimeStamp: String
    private lateinit var lastCleanedTimeStampPresentable: String
    private var ratingSum: Int = 0
    private var numberOfRatings: Int = 0
    private var ratingSumLifeTime: Int = 0
    private var numberOfRatingsLifeTime: Int = 0
    private lateinit var toiletContact: String
    private lateinit var toiletJanitor: String
    private lateinit var toiletMaintainedBy: String
    private lateinit var toiletName: String
    private lateinit var toiletOwnedBy: String
    private lateinit var toiletSponsor: String
    private lateinit var sponsorPath: String
    private lateinit var contributor: String
    private lateinit var contributorId: String

    private var genderType: Int = 0
    private var toiletAccess: Int = 0
    private var toiletType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps__directions)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        /***** 31 Jan 2019:Kartik TOP *********/
        lat = intent.getDoubleExtra(getString(R.string.intent_latitude), 0.0)
        lng = intent.getDoubleExtra(getString(R.string.intent_longitude), 0.0)
        alt = intent.getDoubleExtra(getString(R.string.intent_altitude), 0.0)

        genderType = intent.getIntExtra(getString(R.string.intent_toilet_gender), 0)
        toiletAccess = intent.getIntExtra(getString(R.string.intent_toilet_access), 0)
        toiletType = intent.getIntExtra(getString(R.string.intent_toilet_type), 0)

        toiletContact = intent.getStringExtra(getString(R.string.intent_contact_number))   // ("TOILET_CONTACT")
        toiletJanitor = intent.getStringExtra(getString(R.string.intent_janitor))
        toiletMaintainedBy = intent.getStringExtra(getString(R.string.intent_toilet_maintained_by))
        toiletName = intent.getStringExtra(getString(R.string.intent_toilet_name))
        toiletOwnedBy = intent.getStringExtra(getString(R.string.intent_toilet_owner))
        toiletSponsor = intent.getStringExtra(getString(R.string.intent_sponsor))
        sponsorPath = intent.getStringExtra(getString(R.string.intent_sponsor_image_path))   // KARTIK_31-March-2019

        contributor = intent.getStringExtra(getString(R.string.intent_user_name))   // KARTIK_2_June_2019

        contributorId = intent.getStringExtra(getString(R.string.intent_user_id))   // KARTIK_2_June_2019
        if(contributorId == "") {
            contributor = CurrentUser.getCurrentUser()
            contributorId = CurrentUser.getCurrentUserID()
        }
        // KARTIK_2_June_2019 --- End


        toiletID = intent.getStringExtra(getString(R.string.intent_toilet_id))
        toiletAddress = intent.getStringExtra(getString(R.string.intent_toilet_address))
        userRating = intent.getDoubleExtra(getString(R.string.intent_rating), 0.0)

        ratingSum = intent.getIntExtra(getString(R.string.intent_rating_sum), 0)
        numberOfRatings = intent.getIntExtra(getString(R.string.intent_number_of_ratings), 0)
        ratingSumLifeTime = intent.getIntExtra(getString(R.string.intent_rating_sum_lifetime), 0)
        numberOfRatingsLifeTime = intent.getIntExtra(getString(R.string.intent_number_of_ratings_lifetime), 0)

        lastCleanedTimeStamp = intent.getStringExtra(getString(R.string.intent_last_cleaned))
        lastCleanedTimeStampPresentable = intent.getStringExtra(getString(R.string.intent_last_cleaned_presentable_timestamp))
        val toiletFeatures = intent.getStringExtra(getString(R.string.intent_toilet_features))



        val toiletSponsorTextView = findViewById<TextView>(R.id.textViewSponsor)
        toiletSponsorTextView.text = toiletSponsor

        val toiletFeaturesView = findViewById<TextView>(R.id.textView_feature)
        toiletFeaturesView.text = toiletFeatures

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        val dest = LatLng(lat, lng)

        val looAddress = findViewById<TextView>(R.id.textView_loo_address)
        val currentRating = findViewById<RatingBar>(R.id.ratingBar)
        looAddress.text = toiletAddress
        currentRating.setIsIndicator(true)
        if (numberOfRatings > 0)
            currentRating.rating = (ratingSum / numberOfRatings).toFloat()
        else
            currentRating.rating = 0.0F

        val lastCleanedTimeStampTextView = findViewById<TextView>(R.id.textViewCleanTimeStamp)
        lastCleanedTimeStampTextView!!.text = lastCleanedTimeStampPresentable  // will show timestamp on load

        mMap.addMarker(MarkerOptions().position(dest).title(getString(R.string.strings_toilet_location)))
//        mMap.addMarker(MarkerOptions().position(dest).title("Destination"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dest))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 12.0f))

        mMap.uiSettings.isZoomControlsEnabled = true
//        mMap.setOnMarkerClickListener(this)

        mMap.isMyLocationEnabled = true

// 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }

        val buttonJanitor = findViewById<Button>(R.id.button_Janitor)
        buttonJanitor?.setOnClickListener {
            val i = Intent(this, JanitorActivity::class.java)

            /***** 31 Jan 2019:Kartik TOP *********/


            i.putExtra(getString(R.string.intent_latitude), lat)
            i.putExtra(getString(R.string.intent_longitude), lng)
            i.putExtra(getString(R.string.intent_toilet_address), toiletAddress)
            i.putExtra(getString(R.string.intent_toilet_id), toiletID)

            i.putExtra(getString(R.string.intent_altitude), alt)
            i.putExtra(getString(R.string.intent_rating), userRating)
            i.putExtra(getString(R.string.intent_last_cleaned), lastCleanedTimeStamp)
            i.putExtra(getString(R.string.intent_last_cleaned_presentable_timestamp), lastCleanedTimeStampPresentable)
            i.putExtra(getString(R.string.intent_toilet_gender), genderType)
            i.putExtra(getString(R.string.intent_toilet_access), toiletAccess)
            i.putExtra(getString(R.string.intent_contact_number), toiletContact)
            i.putExtra(getString(R.string.intent_janitor), toiletJanitor)
            i.putExtra(getString(R.string.intent_toilet_maintained_by), toiletMaintainedBy)
            i.putExtra(getString(R.string.intent_toilet_name), toiletName)
            i.putExtra(getString(R.string.intent_toilet_owner), toiletOwnedBy)
            i.putExtra(getString(R.string.intent_sponsor), toiletSponsor)
            i.putExtra("SPONSOR_IMAGE_PATH",sponsorPath)        // KARTIK_31-March-2019
            i.putExtra(getString(R.string.intent_user_name), contributor)     // KARTIK_2-June-2019
            i.putExtra(getString(R.string.intent_user_name), contributorId)   // KARTIK_2-June-2019


            i.putExtra(getString(R.string.intent_toilet_type), toiletType)
            i.putExtra(getString(R.string.intent_rating_sum), ratingSum)
            i.putExtra(getString(R.string.intent_number_of_ratings), numberOfRatings)
            i.putExtra(getString(R.string.intent_rating_sum_lifetime), ratingSumLifeTime)
            i.putExtra(getString(R.string.intent_number_of_ratings_lifetime), numberOfRatingsLifeTime)

            startActivity(i)
        }

        val buttonClean = findViewById<Button>(R.id.buttonClean)
        buttonClean?.setOnClickListener {
            val i = Intent(this, CleanActivity::class.java)

            /***** 31 Jan 2019:Kartik TOP *********/
            i.putExtra(getString(R.string.intent_toilet_id), toiletID)
            i.putExtra(getString(R.string.intent_toilet_name), toiletName)
            i.putExtra(getString(R.string.intent_janitor), toiletJanitor)

            /***** 31 Jan 2019:Kartik END *********/

            // User ratings are reset to 0 after toilet is cleaned
            // No need to share the current values with this intent
            startActivity(i)
        }

        val buttonNavigate = findViewById<Button>(R.id.button_Navigate)
        buttonNavigate?.setOnClickListener {
            val navigationIntentUri =
                Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + dest.latitude.toString() + "," + dest.longitude.toString())
            val mapIntent = Intent(Intent.ACTION_VIEW, navigationIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivityForResult(mapIntent, 10)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // if this is true, this means your first intent finished. So you can start your second intent
        if (requestCode == 10) {

            val i = Intent(this, MainActivityRating::class.java)
            // KARTIK to pass the toilet ID
            /***** 31 Jan 2019:Kartik TOP *********/

            i.putExtra(getString(R.string.intent_toilet_id), toiletID)
            i.putExtra(getString(R.string.intent_toilet_address), toiletAddress)

            i.putExtra(getString(R.string.intent_rating_sum), ratingSum)
            i.putExtra(getString(R.string.intent_number_of_ratings), numberOfRatings)
            i.putExtra(getString(R.string.intent_rating_sum_lifetime), ratingSumLifeTime)
            i.putExtra(getString(R.string.intent_number_of_ratings_lifetime), numberOfRatingsLifeTime)

            /***** 31 Jan 2019:Kartik END *********/
            startActivity(i)
        }
    }
}
