package com.example.cloo2019

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class ParseToiletMaster {
    private val TAG = "ParseToiletMaster"

    val toilets = ArrayList<ToiletEntry>()

    fun parse(toiletData: String): Boolean {
        Log.d(TAG, "parse called with $toiletData")

        var status = true
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(toiletData.reader())
            var eventType = xpp.eventType
            var currentRecord = ToiletEntry()
            while (eventType != XmlPullParser.END_DOCUMENT){

            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }
        return status
    }

}