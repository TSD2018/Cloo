/* Created by Kartik Venkataraman, 14 Nov 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */

package com.example.cloo2019

import android.location.Location

object CurrentLocation {
    private val TAG = "CurrentLocation"

    private var mLastLocation: Location? = null
    private var location_provider = "default"
    private var radius = "1"

    init {
        mLastLocation?.latitude = 12.9718342     // default value
        mLastLocation?.longitude = 77.6562343     // default value
        mLastLocation?.altitude = 0.0  // default value
        mLastLocation?.accuracy = 0.0f  //
        mLastLocation?.provider = "default"
    }

    fun setLastLocation(location: Location, provider: String) {
        mLastLocation = location
        location_provider = provider
    }

    fun getLastLocation(): Location? {return mLastLocation}

    fun getLastLocationProvider(): String {return location_provider}

    fun getLastLatAsString() : String {
        return mLastLocation?.latitude.toString()
    }

    fun getLastLngAsString() : String {
        return mLastLocation?.longitude.toString()
    }

    fun getLookUpRadius() : String {
        return radius
    }

    fun getLocationDetailsAsString(): String {
        val loc = mLastLocation
        return if(loc != null) {"Lat: ${loc.latitude}\nLng: ${loc.longitude}\nAlt: ${loc.altitude}\n" +
                "Accuracy Captured: ${loc.accuracy}\nProvider: ${loc.provider}"}
        else {"Location not enabled or available.  Using defaults"}
    }

}