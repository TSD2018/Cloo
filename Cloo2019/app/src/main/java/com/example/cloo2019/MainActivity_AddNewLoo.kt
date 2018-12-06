/* Created by Kartik Venkataraman, 14 Nov 2018 */

package com.example.cloo2019

import android.annotation.SuppressLint
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import java.time.LocalDateTime
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase


class MainActivity_AddNewLoo : AppCompatActivity() {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    protected var mLastLocation: Location? = null
    private var LocationAddress: TextView? = null
    lateinit var ratingBar: RatingBar
    private lateinit var editComments: EditText


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main__add_new_loo)

        LocationAddress = findViewById<TextView>(R.id.textView_Address)
        ratingBar = findViewById<RatingBar>(R.id.ratingBar_addlocation)
        editComments = findViewById<EditText>(R.id.editText_LooRatingComments)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mFusedLocationClient!!.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result

                    LocationAddress !!.text = "Latitude: " + mLastLocation!!.latitude.toString() +
                            "Logitude: " + mLastLocation!!.longitude.toString()
                } else {
//                    Log.i(MainActivity_LocateLoo.TAG, "getLastLocation:exception", task.exception)
                    LocationAddress!!.text = getString(R.string.no_location_detected)
                }
            }

        if(ratingBar!=null){
            val buttonSubmit=findViewById<Button>(R.id.button_Submit)
            buttonSubmit?.setOnClickListener {
                val msg = ratingBar.rating.toString()
                Toast.makeText(this@MainActivity_AddNewLoo, msg, Toast.LENGTH_SHORT).show()

                val msg_Comments = editComments.text

                val current = LocalDateTime.now()
                Toast.makeText(this@MainActivity_AddNewLoo, "${current.toString()}", Toast.LENGTH_SHORT).show()

                Toast.makeText(this@MainActivity_AddNewLoo, "$msg $msg_Comments", Toast.LENGTH_SHORT).show()

                saveUserInputs()
            }
        }
        else
            Toast.makeText(this@MainActivity_AddNewLoo, "ratingBar == null", Toast.LENGTH_SHORT).show()
    }

    private fun saveUserInputs(){

        val userComments: String

        val ChosenRating: Int
        val current = LocalDateTime.now()

        val toilet_id: String
        val looAddress: String

        userComments = editComments.text.toString()
        ChosenRating = ratingBar.rating.toInt()
        looAddress = ""


        if(LocationAddress  == null){
            Toast.makeText(this@MainActivity_AddNewLoo, "Please set location", Toast.LENGTH_SHORT).show()
            return
        }

        val FireDBRef = FirebaseDatabase.getInstance().getReference("toilet_master")

        val cUserInputId = FireDBRef.push().key

        if(cUserInputId == null) {
            Toast.makeText(this@MainActivity_AddNewLoo, "push key returned null", Toast.LENGTH_SHORT).show()
            return
        }

        /* TOP:::2018-NOV-03:::Kartik::: to be taken care of */
        // Should we change the lat/lng to double instead?  Ideal would be to store the location itself
        // location contains the time as well.  If we get location, we should not be capturing the time
        // time for now will be stored as a string.  It is a bug, I am unable to fix

        /* END:::2018-NOV-03:::Kartik::: to be taken care of */

        val asb = cUserInput(cUserInputId, mLastLocation!!.latitude.toDouble(), mLastLocation!!.longitude.toDouble(),
            mLastLocation!!.altitude.toDouble(),  looAddress, ChosenRating, userComments
        )
/*        val asb = cUserInput(cUserInputId, !!.latitude.toDouble(), mLastLocation!!.longitude.toDouble(),
            mLastLocation!!.altitude.toDouble(),  ChosenRating, userComments, "${current.toString()}"
        )
*/
        FireDBRef.child(cUserInputId).setValue(asb).addOnCompleteListener {
            Toast.makeText(this@MainActivity_AddNewLoo,"Thank you! Rating Saved", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this@MainActivity_AddNewLoo,"Done!", Toast.LENGTH_SHORT).show()


        /* TOP:::2018-NOV-03:::Kartik:::  DEBUG CODE should be be removed!  */

        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
        // Write a message to the database  DEBUG CODE should be be removed!
        /* END:::2018-NOV-03:::Kartik::: DEBUG CODE should be be removed! */

        finish()
    }
}
