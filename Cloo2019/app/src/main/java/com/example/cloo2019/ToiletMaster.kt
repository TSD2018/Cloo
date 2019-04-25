/* Created by Kartik Venkataraman, 14 Nov 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */

package com.example.cloo2019

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
    var lastCleanedBy: String,
    var sponsorImagePath: String

) {
    constructor() : this(
        "", 0.0, 0.0, 0.0, "", "", 0.0, 0, 0,
        0, 0, 0, 0, 0, "", "",
        "", "", "", "", ""
    )
}
