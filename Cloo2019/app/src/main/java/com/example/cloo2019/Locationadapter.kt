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

        val loo = locationList[position]

        // KARTIK _28_NOV_2018: Change the text to show the name and address of the loo. Would also need to have provision to show the distance

        val distanceLat = loo.latval - 0.0 //currentLocation.latitude
        val distanceLng = loo.lngVal - 0.0 //currentLocation.longitude

        textViewLooName.text = loo.address + "\n" + distanceLat.toString() + "," + distanceLng.toString() + " ltlg away" +
                "\nRating = " + loo.userRating

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
