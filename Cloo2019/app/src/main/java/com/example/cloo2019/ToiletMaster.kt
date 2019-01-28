/* Created by Kartik Venkataraman, 14 Nov 2018 */

package com.example.cloo2019

import android.location.Location
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.RowIdLifetime


// Inserted in lieu of the code commented in the next section
class ToiletMaster(
    var toiletId: String,
//    var location: Location?,   // 9-Jan-2019:KARTIK_V1: Location does not have null assignment, so constructor fails
    var lat: Double,
    var lng: Double,
    var alt: Double,
    var toiletName: String,
    var address: String,
    var userRating: Double,
    var ratingSum: Int,
    var numberOfRatings: Int,
    var ratingSumLifeTime: Int,
    var numberOfRatingsLifeTime: Int,
    var toiletAccess: Int,  // 0: Public, 1: Restricted, 2: Private
    var genderType: Int,    // 1: Gents, 2: Ladies, 3: Unisex
    var toiletType: Int,    // 0: Urinals only, 1: squatting pan only, 2: Commode only 3: Urinals & Squatting 4: Urinals & Commode 5: All
    var maintainedBy: String,
    var contact: String,
    var toiletOwnerBy: String,
    var toiletSponsor: String,
    var lastCleanedTimeStamp: String,
    var lastCleanedBy: String

) {
    constructor() : this(
        "", 0.0, 0.0, 0.0, "", "", 0.0, 0, 0,
        0,0, 0,0, 0, "", "",
        "", "","",""
    ) {
    }
    /******* 19-JAN-2019:KARTIK_V1 TOP *****/
    // Dummy functions as of now, need to refactor the code to include these.

    fun readToiletMaster(toiletid: String, toiletRecord: ToiletMaster) : ToiletMaster {
        var fireDBRef: DatabaseReference
        fireDBRef = FirebaseDatabase.getInstance().getReference("ToiletMaster")
        return toiletRecord
    }

    fun writeToiletMaster(toiletRecord: ToiletMaster){

    }

    /******* 19-JAN-2019:KARTIK_V1 END *****/
}

