package com.example.cloo2019

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class ParseAPI {
    private val TAG = "ParseAPI"
    val toilets = ArrayList<ToiletMaster>()

    fun handleJson(jsonString: String?) : Boolean {
        var status = true

        try {
            val jsonArray = JSONArray(jsonString)
            for (rec in 0..(jsonArray.length() - 1))
            {
                val jsonToiletRec = jsonArray.getJSONObject(rec)
                var toiletRecord = ToiletMaster()
                toiletRecord.address= jsonToiletRec.getString("address")
                toiletRecord.alt= jsonToiletRec.getDouble("alt")
                toiletRecord.contact = jsonToiletRec.getString("contact")
                toiletRecord.genderType = jsonToiletRec.getInt("genderType")
                toiletRecord.lastCleanedBy = jsonToiletRec.getString("lastCleanedBy")
                toiletRecord.lastCleanedTimeStamp = jsonToiletRec.getString("lastCleanedTimeStamp")
                toiletRecord.lat = jsonToiletRec.getDouble("lat")
                toiletRecord.lng = jsonToiletRec.getDouble("lng")
                toiletRecord.maintainedBy = jsonToiletRec.getString("maintainedBy")
                toiletRecord.numberOfRatings = jsonToiletRec.getInt("numberOfRatings")
                toiletRecord.numberOfRatingsLifeTime = jsonToiletRec.getInt("numberOfRatingsLifeTime")
                toiletRecord.ratingSum = jsonToiletRec.getInt("ratingSum")
                toiletRecord.ratingSumLifeTime = jsonToiletRec.getInt("ratingSumLifeTime")
//                toiletRecord.sponsor_image = jsonToiletRec.getString("sponsor_image")
                toiletRecord.toiletAccess = jsonToiletRec.getInt("toiletAccess")
                toiletRecord.toiletId = jsonToiletRec.getString("toiletId")
                toiletRecord.toiletName = jsonToiletRec.getString("toiletName")
                toiletRecord.toiletOwnerBy = jsonToiletRec.getString("toiletOwnerBy")
                toiletRecord.toiletSponsor = jsonToiletRec.getString("toiletSponsor")
                toiletRecord.toiletType = jsonToiletRec.getInt("toiletType")
                toiletRecord.userRating = jsonToiletRec.getDouble("userRating")
                Log.d(TAG, toiletRecord.toString())
                toilets.add(toiletRecord)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }
        return status
    }

}