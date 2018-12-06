/* Created by Kartik Venkataraman, 14 Nov 2018 */

package com.example.cloo2019

class cUserInput (
    val id: String, val latval: Double, val lngVal: Double, val altVal: Double, val address: String, val userRating: Int,
    val userComments: String
) {
    constructor() : this("", 0.0, 0.0, 0.0, "", 0, "") {

    }

}