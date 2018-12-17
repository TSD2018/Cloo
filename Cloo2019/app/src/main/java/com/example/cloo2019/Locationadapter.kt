/* Created by Kartik Venkataraman, 14 Nov 2018 */

package com.example.cloo2019

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main__locate_loo.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.sqrt

class Locationadapter(val mCtx: Context, val layoutResId: Int, val locationList: List<cUserInput>)
    : ArrayAdapter<cUserInput>(mCtx, layoutResId, locationList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewLooName = view.findViewById<TextView>(R.id.textView_looName)
        //        val textViewLooSelect = view.findViewById<TextView>(R.id.textView_selectLoo)

        // KARTIK _28_NOV_2018: has to be modified to retrive, latlng, address.
        // need to get the current latlng to compute the distance!
        // how do I get the current latlng here? should I just pass them as argument for Locationadaptor class?

        val currentLocation = CurrentLocation.getLastLocation()
        val loo = locationList[position]

        // KARTIK _28_NOV_2018: Change the text to show the name and address of the loo. Would also need to have provision to show the distance

        val distanceLat = loo.latval - currentLocation!!.latitude
        val distanceLng = loo.lngVal - currentLocation!!.longitude

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

        textViewLooName.text = loo.address + "\nRating [" + ratings +"]      " + distanceStr


/*        textViewLooName.text = loo.address + "\n" + distanceLat.toString() + "," + distanceLng.toString() + " ltlg away" +
                "\nRating = " + loo.userRating
*/
/*        textViewLooName.text = "Lat: " + loo.latval.toString() + "Lng: " + loo.lngVal.toString() + "Alt: " +
                loo.altVal.toString() + "Rating: " + loo.userRating.toString() + "Comments: " + loo.userComments
*/
//        textViewLooSelect.setOnClickListener {
            textViewLooName.setOnClickListener {

            val i = Intent(mCtx, MapsActivity_Directions::class.java)

            i.putExtra("LAT", loo.latval)
            i.putExtra("LNG", loo.lngVal)
            i.putExtra("RATING", loo.userRating)
            i.putExtra("TOILET_ADDRESS", loo.address)
            i.putExtra("TOILET_ID", loo.id )
            startActivity(mCtx,i,null)

//            showUpdateDialog(loo)
        }


        return view
    }

    fun showUpdateDialog(loo: cUserInput) {
        val builder = AlertDialog.Builder(mCtx)

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.layout_select_loo, null)

        val textViewSelectedLoo = view.findViewById<TextView>(R.id.textview_Location)




        textViewSelectedLoo.text = "Lat: " + loo.latval.toString() + " Lng: " + loo.lngVal.toString() +
                " Alt: " + loo.altVal.toString() + " Rat: " + loo.userRating.toString() +
                " Comments: "+ loo.userComments




        builder.setView(view)



        builder.setPositiveButton("Ok") { dialog, which ->
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            return@setPositiveButton
        }

/*        builder.setNegativeButton("No") { dialog, which ->
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
*/
        val alert = builder.create()
        alert.show()


    }

}
