/* Created by Kartik Venkataraman, 14 Nov 2018 */

package com.example.cloo2019

import android.location.Location

object CurrentLocation {
    private var mLastLocation: Location? = null

    fun setLastLocation(location:Location) {
        mLastLocation = location
    }

    fun getLastLocation(): Location? {
        return mLastLocation
    }
}