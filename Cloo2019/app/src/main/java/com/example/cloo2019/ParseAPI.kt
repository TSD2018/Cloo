package com.example.cloo2019

import android.util.Log
import org.json.JSONArray
import java.lang.Exception
import java.net.URL

class ParseAPI {
    private val TAG = "ParseAPI"
    val toilets = ArrayList<ToiletMaster>()



    fun handleJson(jsonString: String?) : Boolean {
        var status = true
        val jsonArray : JSONArray

        try {
            jsonArray = JSONArray(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
            return status
        }
        for (rec in 0..(jsonArray.length() - 1))
        {
            Log.d(TAG, "Number of records to show = ${jsonArray.length()-1}")
            val jsonToiletRec = jsonArray.getJSONObject(rec)
            val toiletRecord = ToiletMaster()
            toiletRecord.address = jsonToiletRec.getString("address")
            toiletRecord.alt = jsonToiletRec.getDouble("alt")
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
            try {
                toiletRecord.sponsorImagePath =
                    jsonToiletRec.getString("sponsor_image")    // This tag or element might not be there, throws an exception
                Log.d(TAG, "Sponsor Image Path = ${toiletRecord.sponsorImagePath}")
                // sponsor_image = url + getString("sponsor_image")
            } catch (e: Exception) {
                Log.d(TAG, "Exception while reading json record, Json element ${e.toString()} missing")
            }
            toiletRecord.toiletAccess = jsonToiletRec.getInt("toiletAccess")
            toiletRecord.toiletId = jsonToiletRec.getString("toiletId")
            toiletRecord.toiletName = jsonToiletRec.getString("toiletName")
            toiletRecord.toiletOwnerBy = jsonToiletRec.getString("toiletOwnerBy")
            toiletRecord.toiletSponsor = jsonToiletRec.getString("toiletSponsor")
            toiletRecord.toiletType = jsonToiletRec.getInt("toiletType")
            toiletRecord.userRating = jsonToiletRec.getDouble("userRating")
            Log.d(TAG, toiletRecord.toString())
            Log.d(TAG, "record counter $rec")
            toilets.add(toiletRecord)
        }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            status = false
//        }
        return status
    }

}