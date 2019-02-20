/* Created by Kartik Venkataraman, 14 Nov 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */
/* Rev 0.22  Mapped string literals to STRINGS.XML - Kartik Venkataraman 31 Jan 2019 */
/*           No changes to FireBase, will handle that as a part of the class restructuring */
/*           No changes yet done to items related to spinner control options.  Need to be explored */
/* Rev 0.23  Mapped spinner controls to strings,xml */

package com.example.cloo2019

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.cloo2019.CurrentLocation.getLastLocation
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

class Locationadapter(
    private val mCtx: Context,
    private val layoutResId: Int,
    private val locationList: List<ToiletMaster>
) :
    ArrayAdapter<ToiletMaster>(mCtx, layoutResId, locationList) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater = LayoutInflater.from(mCtx)
        val view: View
        view = layoutInflater.inflate(layoutResId, null)
        val textViewLooName = view.findViewById<TextView>(R.id.textView_looName)

        val currentLocation = getLastLocation()

        val locationProvider: String = CurrentLocation.getLastLocationProvider()

        /***** 13 Jan 2019:Kartik TOP *********/
        // Added the check for currentLocation == null, incase the location services are turned off
        // and/or getLastLocation returns null.
        // TBD: There is a function to force get the location, which google maps use.  Need to find
        // that and use the same.  would need to decide if it should be done here or on the Create
        // of the landing page (which might be better :))

        /***** 1 Feb 2019:Kartik TOP *********/
/* To be removed with the default */
/*
        if (currentLocation == null) {

            Toast.makeText(mCtx, "Location unknown, please restart with location set.", Toast.LENGTH_SHORT).show()
            return view
        }
  */
        /***** 1 Feb 2019:Kartik END *********/
        /***** 13 Jan 2019:Kartik END *********/

        val loo = locationList[position]
        var distanceStr: String = ""

        if(locationProvider != "default") {
            val distanceLat = loo.lat - currentLocation!!.latitude
            val distanceLng = loo.lng - currentLocation!!.longitude

            var distance = sqrt(((distanceLat).pow(2)) + ((distanceLng).pow(2))) * 111 /*km*/ * 1000

            if (distance > 1000.0) {
                val df = DecimalFormat("##.#")
                distance /= 1000.0
                df.roundingMode = RoundingMode.CEILING
                distanceStr = df.format(distance) + mCtx.getString(R.string.strings_km_away) //" km(s) away"
            } else {
                val df = DecimalFormat("###")
                df.roundingMode = RoundingMode.CEILING
                distanceStr = df.format(distance) + mCtx.getString(R.string.strings_mtrs_away) //" mtr(s) away"
            }
        }

        val numStars = if (loo.numberOfRatings > 0)
            round((loo.ratingSum.toDouble() / loo.numberOfRatings.toDouble())).toInt()
        else
            0

        val ratings = if (numStars == 0) {
            mCtx.getString(R.string.strings_na_spaces)  //" N A "
        } else {
            "*".repeat(numStars) + " ".padEnd(5 - numStars)
        }

        val lastCleanedTimeStampPresentable = CurrentTimeStamp().getPresentableTimeString(loo.lastCleanedTimeStamp)

        if(loo.toiletSponsor != "") {
            val toiletSponserString: String = "\nSponsor: " + loo.toiletSponsor
            textViewLooName.text = "${loo.toiletName}\n" + mCtx.getString(R.string.strings_rating) +
                    "[$ratings]      $distanceStr\n" + mCtx.getString(R.string.strings_cleaned_on) +
                    " $lastCleanedTimeStampPresentable" + toiletSponserString
        }
        else {
            textViewLooName.text = "${loo.toiletName}\n" + mCtx.getString(R.string.strings_rating) +
                    "[$ratings]      $distanceStr\n" + mCtx.getString(R.string.strings_cleaned_on) +
                    " $lastCleanedTimeStampPresentable"
        }


/*
        textViewLooName.text = """${loo.toiletName}
Rating [$ratings]      $distanceStr
Cleaned on: $lastCleanedTimeStampPresentable"""
*/
        textViewLooName.setOnClickListener {
            val toiletAccessType = when (loo.toiletAccess) {
                1 -> {mCtx.getString(R.string.array_toilet_access_free_toilet)} //{"Free Public Toilet"}
                2 -> {mCtx.getString(R.string.array_toilet_access_pay_and_use)} //{"Pay and Use Toilet"}
                3 -> {mCtx.getString(R.string.array_toilet_access_for_customers_only)} //{"For Customers Only"}
                4 -> {mCtx.getString(R.string.array_toilet_access_restricted_entry)} //{"Restricted Entry"}
                5 -> {mCtx.getString(R.string.array_toilet_access_private_toilet)} //{"Private"}
                else -> {mCtx.getString(R.string.error_toilet_access_unknown)} //{"Toilet Access Unknown"}
            }

//            var toiletGender = mCtx.getString(R.string.strings_toilet_for)  // Toilet for:
            val toiletGender = when (loo.genderType) {
                1 -> {mCtx.getString(R.string.array_toilet_gender_gents_only)}   //"Gents Only"}
                2 -> {mCtx.getString(R.string.array_toilet_gender_ladies_only)}   //"Ladies Only"}
                3 -> {mCtx.getString(R.string.array_toilet_gender_ladies_and_gents)}   //"Ladies and Gents (Separate)"}
                4 -> {mCtx.getString(R.string.array_toilet_gender_unisex)}   //"Unisex"}
                else -> {mCtx.getString(R.string.error_toilet_gender_unknown)}   //"unknown"}
            }

//            var toiletType = mCtx.getString(R.string.strings_toilet_style)  // "Toilet Style: "
            val toiletType = when (loo.toiletType) {
                1 -> {mCtx.getString(R.string.check_label_western)}   //"Western"}
                2 -> {mCtx.getString(R.string.check_types_indian)}   //"Indian"}
                3 -> {mCtx.getString(R.string.check_types_urinal)}   //"Urinals only"}
                else -> {mCtx.getString(R.string.error_toilet_style_unknown)}   //"unknown"}
            }

            val toiletFeatures = toiletAccessType + "\n" + toiletGender + "\n" + toiletType

            val i = Intent(mCtx, MapsActivityDirections::class.java)
            /************* 31 Jan 2019 - Kartik TOP ****************/
            i.putExtra(mCtx.getString(R.string.intent_latitude), loo.lat)
            i.putExtra(mCtx.getString(R.string.intent_longitude), loo.lng)
            i.putExtra(mCtx.getString(R.string.intent_altitude), loo.alt)
            i.putExtra(mCtx.getString(R.string.intent_rating), loo.userRating)
            i.putExtra(mCtx.getString(R.string.intent_toilet_address), loo.address)
            i.putExtra(mCtx.getString(R.string.intent_toilet_id), loo.toiletId)
            i.putExtra(mCtx.getString(R.string.intent_last_cleaned_presentable_timestamp), lastCleanedTimeStampPresentable)
            i.putExtra(mCtx.getString(R.string.intent_last_cleaned), loo.lastCleanedTimeStamp)
            i.putExtra(mCtx.getString(R.string.intent_toilet_features), toiletFeatures)
            i.putExtra(mCtx.getString(R.string.intent_toilet_gender), loo.genderType)
            i.putExtra(mCtx.getString(R.string.intent_toilet_access), loo.toiletAccess)
            i.putExtra(mCtx.getString(R.string.intent_contact_number), loo.contact)
            i.putExtra(mCtx.getString(R.string.intent_janitor), loo.lastCleanedBy)
            i.putExtra(mCtx.getString(R.string.intent_toilet_maintained_by), loo.maintainedBy)
            i.putExtra(mCtx.getString(R.string.intent_toilet_name), loo.toiletName)
            i.putExtra(mCtx.getString(R.string.intent_toilet_owner), loo.toiletOwnerBy)
            i.putExtra(mCtx.getString(R.string.intent_sponsor), loo.toiletSponsor)
            i.putExtra(mCtx.getString(R.string.intent_toilet_type), loo.toiletType)
            i.putExtra(mCtx.getString(R.string.intent_rating_sum), loo.ratingSum)
            i.putExtra(mCtx.getString(R.string.intent_number_of_ratings), loo.numberOfRatings)
            i.putExtra(mCtx.getString(R.string.intent_rating_sum_lifetime), loo.ratingSumLifeTime)
            i.putExtra(mCtx.getString(R.string.intent_number_of_ratings_lifetime), loo.numberOfRatingsLifeTime)
            /************* 31 Jan 2019 - Kartik END ****************/

//
//            i.putExtra("LAT", loo.lat)
//            i.putExtra("LNG", loo.lng)
//            i.putExtra("ALT", loo.alt)
//            i.putExtra("RATING", loo.userRating)
//            i.putExtra("TOILET_ADDRESS", loo.address)
//            i.putExtra("TOILET_ID", loo.toiletId)
//            i.putExtra("LAST_CLEANED_PRESENTABLE", lastCleanedTimeStampPresentable)
//            i.putExtra("LAST_CLEANED", loo.lastCleanedTimeStamp)
//            i.putExtra("TOILET_FEATURES", toiletFeatures)
//            i.putExtra("TOILET_GENDER", loo.genderType)
//            i.putExtra("TOILET_ACCESS", loo.toiletAccess)
//            i.putExtra("TOILET_CONTACT", loo.contact)
//            i.putExtra("TOILET_JANITOR", loo.lastCleanedBy)
//            i.putExtra("TOILET_MAINTAINEDBY", loo.maintainedBy)
//            i.putExtra("TOILET_NAME", loo.toiletName)
//            i.putExtra("TOILET_OWNEDBY", loo.toiletOwnerBy)
//            i.putExtra("TOILET_SPONSOR", loo.toiletSponsor)
//            i.putExtra("TOILET_TYPE", loo.toiletType)
//            i.putExtra("RATING_SUM", loo.ratingSum)
//            i.putExtra("NUMBER_OF_RATINGS", loo.numberOfRatings)
//            i.putExtra("RATING_SUM_LIFETIME", loo.ratingSumLifeTime)
//            i.putExtra("NUMBER_OF_RATINGS_LIFETIME", loo.numberOfRatingsLifeTime)
            startActivity(mCtx, i, null)
        }
        return view
    }
}
