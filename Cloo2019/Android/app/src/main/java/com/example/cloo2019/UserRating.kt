/* Created by Kartik Venkataraman, 24 Nov 2018 */
/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */

package com.example.cloo2019


class UserRating(
    val ratingId: String, val toiletId: String, val userID: String, val userRating: Int,
    val userComments: String, var timeString: String
) {
    constructor() : this(
        "", "", "", 0, "", ""
    )
}

