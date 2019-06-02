package com.example.cloo2019

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.math.RoundingMode
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

class ToiletAdapter(context: Context, private val resource: Int, private val toilets: List<ToiletMaster>) :
    ArrayAdapter<ToiletMaster>(context, resource) {
/*class ToiletAdapter(context: Context, private val resource: Int, private val toilets: List<ToiletEntry>) :
    ArrayAdapter<ToiletEntry>(context, resource) {
*/
    private val TAG = "ToiletAdapter"
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return toilets.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(resource, parent, false)

        val tvName: TextView = view.findViewById(R.id.textViewToiletName)
        val tvRating: TextView = view.findViewById(R.id.textViewRating)
        val tvDistance: TextView = view.findViewById(R.id.textViewDistance)
        val tvCleaned: TextView = view.findViewById(R.id.textviewCleaned)
        val tvSponsor: TextView = view.findViewById(R.id.textviewSponsor)
        val tvGenderIcon: ImageView = view.findViewById(R.id.imageViewGender)
        val ivSponsor: ImageView = view.findViewById(R.id.imageViewSponsor)

        val currentToilet = toilets[position]

//        tvId.text = currentToilet.toiletId
        tvName.text = currentToilet.toiletName
        tvRating.text = formatRatings(currentToilet.numberOfRatings, currentToilet.ratingSum)
        tvDistance.text = getDistance(currentToilet.lat, currentToilet.lng)
        tvCleaned.text = formatCleanedTimeStamp(currentToilet.lastCleanedTimeStamp)
        tvSponsor.text = formatSponsor(currentToilet.toiletSponsor)

        val urlStr = URL(context.getString(R.string.url_server) + currentToilet.sponsorImagePath)
        Log.d(TAG, "Sponsor URL path = ${urlStr}")

        Picasso.get()
//            .load("https://s3.scoopwhoop.com/anj/logos/709764582.jpg")
//            .load(context.getString(R.string.url_server)+"uploads/test.jpg")
            .load(context.getString(R.string.url_server)+currentToilet.sponsorImagePath)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.sponsor)
            .into(ivSponsor)


        when (currentToilet.genderType){
            1 -> {context.getString(R.string.array_toilet_gender_gents_only)
                tvGenderIcon.setImageResource(R.drawable.male)
            }   //"Gents Only"}
            2 -> {context.getString(R.string.array_toilet_gender_ladies_only)
                tvGenderIcon.setImageResource(R.drawable.female)
            }   //"Ladies Only"}
            3 -> {context.getString(R.string.array_toilet_gender_ladies_and_gents)
                tvGenderIcon.setImageResource(R.drawable.both)
            }      //"Ladies and Gents (Separate)"}
            4 -> {context.getString(R.string.array_toilet_gender_unisex)}   //"Unisex"}
            else -> {context.getString(R.string.error_toilet_gender_unknown)}   //"unknown"}
        }

        val lineItem = view.findViewById<ConstraintLayout>(R.id.constraintItem)
        lineItem.setOnClickListener(){

                val toiletAccessType = when (currentToilet.toiletAccess) {
                    1 -> {context.getString(R.string.array_toilet_access_free_toilet)} //{"Free Public Toilet"}
                    2 -> {context.getString(R.string.array_toilet_access_pay_and_use)} //{"Pay and Use Toilet"}
                    3 -> {context.getString(R.string.array_toilet_access_for_customers_only)} //{"For Customers Only"}
                    4 -> {context.getString(R.string.array_toilet_access_restricted_entry)} //{"Restricted Entry"}
                    5 -> {context.getString(R.string.array_toilet_access_private_toilet)} //{"Private"}
                    else -> {context.getString(R.string.error_toilet_access_unknown)} //{"Toilet Access Unknown"}
                }

//            var toiletGender = mCtx.getString(R.string.strings_toilet_for)  // Toilet for:
                val toiletGender = when (currentToilet.genderType) {
                    1 -> {context.getString(R.string.array_toilet_gender_gents_only)
                    }   //"Gents Only"}
                    2 -> {context.getString(R.string.array_toilet_gender_ladies_only)
                    }   //"Ladies Only"}
                    3 -> {context.getString(R.string.array_toilet_gender_ladies_and_gents)
                    }      //"Ladies and Gents (Separate)"}
                    4 -> {context.getString(R.string.array_toilet_gender_unisex)}   //"Unisex"}
                    else -> {context.getString(R.string.error_toilet_gender_unknown)}   //"unknown"}
                }

//            var toiletType = mCtx.getString(R.string.strings_toilet_style)  // "Toilet Style: "
                val toiletType = when (currentToilet.toiletType) {
                    1 -> {context.getString(R.string.check_label_western)}   //"Western"}
                    2 -> {context.getString(R.string.check_types_indian)}   //"Indian"}
                    3 -> {context.getString(R.string.check_types_urinal)}   //"Urinals only"}
                    else -> {context.getString(R.string.error_toilet_style_unknown)}   //"unknown"}
                }

                val toiletFeatures = toiletAccessType + "\n" + toiletGender + "\n" + toiletType

                val i = Intent(context, MapsActivityDirections::class.java)
                /************* 31 Jan 2019 - Kartik TOP ****************/
                i.putExtra(context.getString(R.string.intent_latitude), currentToilet.lat)
                i.putExtra(context.getString(R.string.intent_longitude), currentToilet.lng)
                i.putExtra(context.getString(R.string.intent_altitude), currentToilet.alt)
                i.putExtra(context.getString(R.string.intent_rating), currentToilet.userRating)
                i.putExtra(context.getString(R.string.intent_toilet_address), currentToilet.address)
                i.putExtra(context.getString(R.string.intent_toilet_id), currentToilet.toiletId)
                i.putExtra(context.getString(R.string.intent_last_cleaned_presentable_timestamp),
                    CurrentTimeStamp().getPresentableTimeString(currentToilet.lastCleanedTimeStamp))
                i.putExtra(context.getString(R.string.intent_last_cleaned), currentToilet.lastCleanedTimeStamp)
                i.putExtra(context.getString(R.string.intent_toilet_features), toiletFeatures)
                i.putExtra(context.getString(R.string.intent_toilet_gender), currentToilet.genderType)
                i.putExtra(context.getString(R.string.intent_toilet_access), currentToilet.toiletAccess)
                i.putExtra(context.getString(R.string.intent_contact_number), currentToilet.contact)
                i.putExtra(context.getString(R.string.intent_janitor), currentToilet.lastCleanedBy)
                i.putExtra(context.getString(R.string.intent_toilet_maintained_by), currentToilet.maintainedBy)
                i.putExtra(context.getString(R.string.intent_toilet_name), currentToilet.toiletName)
                i.putExtra(context.getString(R.string.intent_toilet_owner), currentToilet.toiletOwnerBy)
                i.putExtra(context.getString(R.string.intent_sponsor), currentToilet.toiletSponsor)
                i.putExtra(context.getString(R.string.intent_sponsor_image_path), currentToilet.sponsorImagePath)        // KARTIK_31-March-2019
                i.putExtra(context.getString(R.string.intent_user_name), currentToilet.contributor)     // KARTIK_2-June-2019
                i.putExtra(context.getString(R.string.intent_user_id), currentToilet.contributorId)   // KARTIK_2-June-2019

                i.putExtra(context.getString(R.string.intent_toilet_type), currentToilet.toiletType)
                i.putExtra(context.getString(R.string.intent_rating_sum), currentToilet.ratingSum)
                i.putExtra(context.getString(R.string.intent_number_of_ratings), currentToilet.numberOfRatings)
                i.putExtra(context.getString(R.string.intent_rating_sum_lifetime), currentToilet.ratingSumLifeTime)
                i.putExtra(context.getString(R.string.intent_number_of_ratings_lifetime), currentToilet.numberOfRatingsLifeTime)

                startActivity(context, i, null)
        }
        return view
    }

    private fun getDistance(lat: Double, lng: Double) : String {

        var distanceStr: String = ""

        val currentLocation = CurrentLocation.getLastLocation()
        if(CurrentLocation.getLastLocationProvider() != "default")
        {
            val distanceLat = lat - currentLocation!!.latitude
            val distanceLng = lng - currentLocation!!.longitude

            var distance = sqrt(((distanceLat).pow(2)) + ((distanceLng).pow(2))) * 111 /*km*/ * 1000

            if (distance > 1000.0) {
                val df = DecimalFormat("##.#")
                distance /= 1000.0
                df.roundingMode = RoundingMode.CEILING
                distanceStr = df.format(distance) + context.getString(R.string.strings_km_away) //" km(s) away"
            } else {
                val df = DecimalFormat("###")
                df.roundingMode = RoundingMode.CEILING
                distanceStr = df.format(distance) + context.getString(R.string.strings_mtrs_away) //" mtr(s) away"
            }
        }
        return distanceStr
    }

    private fun formatRatings(noRatings : Int, sumRatings: Int):String {
        var ratingStr = "* - "
        if (noRatings > 0)
            ratingStr += round((sumRatings.toDouble() / noRatings.toDouble())).toInt()

        else
            ratingStr += "NA"
        return ratingStr
    }

    private fun formatSponsor (sponsor : String) : String {
        var toiletSponserString: String = ""
        if(sponsor != "") {
            toiletSponserString = "Sponsor: " + sponsor
        }
        return toiletSponserString
    }

    private fun formatCleanedTimeStamp( cleanedTS: String) : String {
        val tsString = "Cleaned on:\n" + CurrentTimeStamp().getPresentableTimeString(cleanedTS)
        return tsString
    }

}