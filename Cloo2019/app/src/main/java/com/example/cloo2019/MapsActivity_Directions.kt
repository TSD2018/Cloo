/* Created by Kartik Venkataraman, 14 Nov 2018 */

package com.example.cloo2019

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import org.json.JSONObject

class MapsActivity_Directions : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var toiletID: String
    private lateinit var toiletAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps__directions)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

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

        val dest = LatLng(intent.getDoubleExtra("LAT", 0.0), intent.getDoubleExtra("LNG", 0.0))
        val rtng = intent.getIntExtra("RATING",0)

        toiletID = intent.getStringExtra("TOILET_ID")
        toiletAddress = intent.getStringExtra("TOILET_ADDRESS")

        val looAddress = findViewById<TextView>(R.id.textView_loo_address)
        val currentRating = findViewById<RatingBar>(R.id.ratingBar)
        looAddress.text = toiletAddress
        currentRating.setIsIndicator(true)
        currentRating.rating = rtng.toFloat()


//        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(dest).title("Destination"))
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
            i.putExtra("LAT", intent.getDoubleExtra("LAT", 0.0))
            i.putExtra("LNG", intent.getDoubleExtra("LNG", 0.0))
            i.putExtra("TOILET_ADDRESS", toiletAddress)
            i.putExtra("TOILET_ID", toiletID )
            startActivity(i)
        }

        val buttonClean = findViewById<Button>(R.id.buttonClean)
        buttonClean?.setOnClickListener {
            val i = Intent(this, CleanActivity::class.java)
            i.putExtra("TOILET_ID", toiletID )
            startActivity(i)
        }



        val buttonNavigate = findViewById<Button>(R.id.button_Navigate)
        buttonNavigate?.setOnClickListener {


            /*            val i = Intent(android.content.Intent.ACTION_VIEW)
            i.setData(Uri.parse("geo:0,0?q="+loo.latval.toString()+","+loo.lngVal.toString()))
            val ichooser = Intent.createChooser(i, "Launch Maps")
            startActivity(mCtx,ichooser,null)


            https://www.google.com/maps/dir/?api=1&destination=+ dest.latitude.toString() + "," + dest.longitude.toString()


*/
            val navigationIntentUri =
                Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + dest.latitude.toString() + "," + dest.longitude.toString())
//            val navigationIntentUri = Uri.parse("google.navigation:q=" + dest.latitude.toString() + "," + dest.longitude.toString())
//            val navigationIntentUri = Uri.parse("geo:0,0?q="+dest.latitude.toString() +","+dest.longitude.toString())
            val mapIntent = Intent(Intent.ACTION_VIEW, navigationIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivityForResult(mapIntent, 10)

//            val i = Intent(this, MainActivity_AddNewLoo::class.java)
//            startActivity(i)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // if this is true, this means your first intent finished. So you can start your second intent
        if (requestCode == 10) {

            val i = Intent(this, MainActivity_Rating::class.java)
            // KARTIK to pass the toilet ID
            i.putExtra("TOILET_ID", toiletID )
            i.putExtra("TOILET_ADDRESS", toiletAddress)

//            val i = Intent(this, MainActivity_AddNewLoo::class.java)
            startActivity(i)
        }
    }


    //On button press Directions, call the Navigation

    /*
    val navigationIntentUri = Uri.parse("google.navigation:q=" + newLatLng.latval.toString() + "," + newLatLng.lngVal.toString())
    val mapIntent = Intent(Intent.ACTION_VIEW,navigationIntentUri )
    mapIntent.setPackage("com.google.android.apps.maps")
    startActivity(mCtx,mapIntent,null)
*/


/*
                dest.latitude.toString()
                dest.longitude.toString()

                val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                        currentLatLng.latitude.toString() + "," +
                        currentLatLng.longitude.toString() + "&destination=" +
                        dest.latitude.toString() + "," +
                        dest.longitude.toString() + "&key=" + getString(R.string.google_maps_key)

                val path: MutableList<List<LatLng>> = ArrayList()

                val requestQueue = Volley.newRequestQueue(this)

                val dRequest = object: StringRequest(Request.Method.GET, urlDirections, Response.Listener<String> { response ->
//                        Toast("All is well")

                    val jsonResponse = JSONObject(response)
                    val routes = jsonResponse.getJSONArray("routes")
                    val legs = routes.getJSONObject(0).getJSONArray("legs")
                    val steps = legs.getJSONObject(0).getJSONArray("steps")

                    for (i in 0 until steps.length()) {
                        val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
//                        path.add(Po)
                        path.add(PolyUtil.decode(points))
                    }

                    for (i in 0 until path.size) {
                        mMap.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
                    }


                }, Response.ErrorListener{
                    _ ->



//                        Toast("Error in Listening")

                    }){}

                requestQueue.add(dRequest)

            }
        }
    }
*/
}
