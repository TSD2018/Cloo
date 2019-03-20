/* Created by Kartik Venkataraman, 14 Nov 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */

package com.example.cloo2019

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

object CurrentLocation {
    private val TAG = "CurrentLocation"

    private var mLastLocation: Location? = null
    private var location_provider = "default"
    private var radius = "1"

    init {
        mLastLocation?.latitude = 12.9718342     // default value
        mLastLocation?.longitude = 77.6562343     // default value
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
}