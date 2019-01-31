/* Created by Kartik Venkataraman, 14 Nov 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */
/* Rev 0.22  Mapped string literals to STRINGS.XML - Kartik Venkataraman 31 Jan 2019 */
/*           No changes to FireBase, will handle that as a part of the class restructuring */

package com.example.cloo2019

import android.annotation.SuppressLint
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase


class MainActivityAddNewLoo : AppCompatActivity() {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    protected var mLastLocation: Location? = null
    private var LocationAddress: TextView? = null
    lateinit var ratingBar: RatingBar
    private lateinit var editComments: EditText


    @SuppressLint("MissingPermission", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main__add_new_loo)

        LocationAddress = findViewById(R.id.textView_Address)
        ratingBar = findViewById(R.id.ratingBar_addlocation)
        editComments = findViewById(R.id.editText_LooRatingComments)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mFusedLocationClient!!.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result

                    LocationAddress !!.text = getString(R.string.strings_latitude) +
                            " ${mLastLocation!!.latitude.toString()} "+
                            getString(R.string.strings_longitude) + " ${mLastLocation!!.longitude.toString()}"
//                    LocationAddress !!.text = "Latitude: ${mLastLocation!!.latitude.toString()} Logitude: ${mLastLocation!!.longitude.toString()}"
                } else {
                    LocationAddress!!.text = getString(R.string.no_location_detected)
                }
            }

//        if(this.ratingBar !=null){
            val buttonSubmit=findViewById<Button>(R.id.button_Submit)
            buttonSubmit?.setOnClickListener {
                val msg = ratingBar.rating.toString()
                Toast.makeText(this@MainActivityAddNewLoo, msg, Toast.LENGTH_SHORT).show()

                val msg_Comments = editComments.text

                val current = CurrentTimeStamp()
                Toast.makeText(this@MainActivityAddNewLoo, current.toString(), Toast.LENGTH_SHORT).show()

                Toast.makeText(this@MainActivityAddNewLoo, "$msg $msg_Comments", Toast.LENGTH_SHORT).show()

                saveUserInputs()
            }
  //      }
//        else
//            Toast.makeText(this@MainActivityAddNewLoo, "ratingBar == null", Toast.LENGTH_SHORT).show()
    }

    private fun saveUserInputs(){

        val userComments = editComments.text.toString()

        val chosenRating = ratingBar.rating.toInt()

        val looAddress = ""


        if(LocationAddress  == null){
            Toast.makeText(this@MainActivityAddNewLoo, getString(R.string.no_location_detected),
                Toast.LENGTH_SHORT).show()      // "Please set location"
            return
        }

        val FireDBRef = FirebaseDatabase.getInstance().getReference("ToiletMaster")
        val toiletId = FireDBRef.push().key
        if(toiletId == null) {
            Toast.makeText(this@MainActivityAddNewLoo,  getString(R.string.error_msg_toilet_record_id_null)     //"Toilet Master push key returned null"
                , Toast.LENGTH_SHORT).show()
            return
        }
        val toiletMasterRecord = ToiletMaster(toiletId, mLastLocation?.latitude!!, mLastLocation?.longitude!!,
            mLastLocation?.altitude!!, "",  looAddress, chosenRating.toDouble(),chosenRating,1,
            chosenRating,1,0,0,0,"","","",
            "","","")
        FireDBRef.child(toiletId).setValue(toiletMasterRecord).addOnCompleteListener {
            Toast.makeText(this@MainActivityAddNewLoo, getString(R.string.status_toilet_master_created)     //"New toiler created..."
                , Toast.LENGTH_SHORT).show()
        }
//        Toast.makeText(this@MainActivityAddNewLoo,"Done!", Toast.LENGTH_SHORT).show()

        val database = FirebaseDatabase.getInstance()
        val ratingRef = database.getReference("ToiletRating/$toiletId")
        val toiletRatingId = ratingRef.push().key
        if(toiletRatingId == null) {
            Toast.makeText(this@MainActivityAddNewLoo, getString(R.string.error_msg_toilet_rating_id_null)   // "Toilet Record ID push key returned null"
                , Toast.LENGTH_SHORT).show()
            return
        }
        val toiletRatingRecord = UserRating(toiletRatingId,toiletId,"",chosenRating,userComments,"" )
        ratingRef.child(toiletRatingId).setValue(toiletRatingRecord).addOnCompleteListener {
            Toast.makeText(this@MainActivityAddNewLoo, getString(R.string.status_toilet_rating_saved)   // "and Rating Saved.  Thank you"
             , Toast.LENGTH_SHORT).show()
        }
        finish()



    }
}
