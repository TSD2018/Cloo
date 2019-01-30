/* Created by Kartik Venkataraman, 14 Nov 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */

package com.example.cloo2019

import android.location.Location

object CurrentLocation {
    private var mLastLocation: Location? = null

    fun setLastLocation(location: Location) {
        mLastLocation = location
    }

    fun getLastLocation(): Location? = mLastLocation
}