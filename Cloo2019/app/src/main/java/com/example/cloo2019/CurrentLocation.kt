/* Created by Kartik Venkataraman, 14 Nov 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */

package com.example.cloo2019

import android.location.Location

object CurrentLocation {
    private var mLastLocation: Location? = null
    private var location_provider = "default"

    init{
        mLastLocation?.latitude = 12.9718342     // default value
        mLastLocation?.longitude = 77.6562343     // default value
    }

    fun setLastLocation(location: Location, provider: String) {
        mLastLocation = location
        location_provider = provider
    }

    fun getLastLocation(): Location? {return mLastLocation}

    fun getLastLocationProvider(): String {return location_provider}
}