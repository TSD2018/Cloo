/* Created by Kartik Venkataraman, 14 Nov 2018 */

package com.example.cloo2019

import android.location.Location


/******* 8-JAN-2019:KARTIK_V1 TOP *****/
// Inserted in lieu of the code commented in the next section
class ToiletMaster(
    var toiletId: String,
//    var location: Location?,   // 9-Jan-2019:KARTIK_V1: Location does not have null assingment, so constructor fails
    var lat: Double,
    var lng: Double,
    var alt: Double,
    var toiletName: String,
    var address: String,
    var userRating: Double,
    var toiletAccess: Int,
    var genderType: Int,
    var toiletType: Int,
    var maintainedBy: String,
    var contact: String,
    var toiletOwnerBy: String,
    var toiletSponsor: String,
    var lastCleanedTimeStamp: String,
    var lastCleanedBy: String

) {
    constructor() : this(
        "", 0.0, 0.0, 0.0, "", "", 0.0, 0, 0,
        0, "", "", "", "","",""
    ) {
    }
}
/******* 8-JAN-2019:KARTIK_V1 END *****/

/******* 8-JAN-2019:KARTIK_V1 TOP *****/
// Commented in lieu of the code added above

/*
class ToiletMaster(
    var id: String, var latval: Double, var lngVal: Double, var altVal: Double, var address: String, var userRating: Int,
    var toiletAccess: Int, var genderType: Int, var toiletType: Int, var maintainedBy: String, var contact: String

) {
    constructor() : this(
        "", 0.0, 0.0, 0.0, "", 0, 0,
        0, 0, "", ""
    ) {
    }
}
        */

/******* 8-JAN-2019:KARTIK_V1 END *****/
