/* Created by Kartik Venkataraman, 14 Nov 2018 */

package com.example.cloo2019

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