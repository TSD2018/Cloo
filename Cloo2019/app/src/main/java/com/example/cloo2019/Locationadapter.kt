/* Created by Kartik Venkataraman, 14 Nov 2018 */

package com.example.cloo2019
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main__locate_loo.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.sqrt

/******* 8-JAN-2019:KARTIK_V1 TOP *****/
//Changed the class from cUserInput to ToiletMaster
//class Locationadapter(val mCtx: Context, val layoutResId: Int, val locationList: List<cUserInput>)
//: ArrayAdapter<cUserInput>(mCtx, layoutResId, locationList) {
/******* 8-JAN-2019:KARTIK_V1 END *****/
class Locationadapter(val mCtx: Context, val layoutResId: Int, val locationList: List<ToiletMaster>)
    : ArrayAdapter<ToiletMaster>(mCtx, layoutResId, locationList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)
        val textViewLooName = view.findViewById<TextView>(R.id.textView_looName)

        val currentLocation = CurrentLocation.getLastLocation()

        /***** 13 Jan 2019:Kartik TOP *********/
        // Added the check for currentLocation == null, incase the location services are turned off
        // and/or getLastLocation returns null.
        // TBD: There is a function to force get the location, which google maps use.  Need to find
        // that and use the same.  would need to decide if it should be done here or on the Create
        // of the landing page (which might be better :))
        if (currentLocation == null){
            Toast.makeText(mCtx, "Location unknown, please restart with location set.",Toast.LENGTH_SHORT).show()
            return view
        }
        // Hopefully the above check should prevent the application from crashing on startup when
        // location is not available
        /***** 13 Jan 2019:Kartik END *********/
        val loo = locationList[position]
        val distanceLat = loo.lat - currentLocation!!.latitude
        val distanceLng = loo.lng - currentLocation!!.longitude

        var distance = sqrt(((distanceLat).pow(2)) + ((distanceLng).pow(2))) * 111 /*km*/ * 1000
        var distanceStr: String

        if (distance > 1000.0) {
            var df = DecimalFormat("##.#")
            distance = distance / 1000.0
            df.roundingMode = RoundingMode.CEILING
            distanceStr = df.format(distance) + " km(s) away"
        }
        else {
            var df = DecimalFormat("###")
            df.roundingMode = RoundingMode.CEILING
            distanceStr = df.format(distance) + " mtr(s) away"
        }

        var ratings: String = "     "
        val numStars = loo.userRating.toInt()

        if(numStars == 0){
            ratings = " N A "
        }
        else{
            ratings = "*".repeat(numStars) + " ".padEnd(5-numStars)
        }

        var lastCleanedTimeStamp = CurrentTimeStamp().getPresentableTimeString(loo.lastCleanedTimeStamp)
        textViewLooName.text = loo.address + "\nRating [" + ratings +"]      " + distanceStr + "\nCleaned on: $lastCleanedTimeStamp"

        textViewLooName.setOnClickListener {
            val i = Intent(mCtx, MapsActivity_Directions::class.java)
            i.putExtra("LAT", loo.lat)
            i.putExtra("LNG", loo.lng)
            i.putExtra("ALT", loo.alt)
            i.putExtra("RATING", loo.userRating)
            i.putExtra("TOILET_ADDRESS", loo.address)
            i.putExtra("TOILET_ID", loo.toiletId )
            i.putExtra("LAST_CLEANED", lastCleanedTimeStamp)
            startActivity(mCtx,i,null)
        }
        return view
    }
}
