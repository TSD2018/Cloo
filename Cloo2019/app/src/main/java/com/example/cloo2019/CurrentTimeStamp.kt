/* Rev 0.2.  Code Cleanup - Kartik Venkataraman 29 Jan 2019 */
/* Rev 0.22  Mapped string literals to STRINGS.XML - Kartik Venkataraman 31 Jan 2019 */
/*           No changes to FireBase, will handle that as a part of the class restructuring */

package com.example.cloo2019

import android.os.Build
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CurrentTimeStamp {
    private val current: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now().toString()
    } else {

//        val str = this.getString(R.string.string_timestamp_not_available)  // gives an error, here.  I guess context is missing
        "Not available"   // Should be changed to a String Value
    }

    fun getString(): String {
        return current
    }

    fun getPresentableTimeString(timeStampString: String): String {
        // 2019-01-12T22:23:29.994 is the format of the string as an input
        var dateTimeString = "not known"

        if (timeStampString != "" && timeStampString != "Not available") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val dateTime: LocalDateTime = LocalDateTime.parse(timeStampString, DateTimeFormatter.ISO_DATE_TIME)
                dateTimeString = dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
            }
        }
        return dateTimeString
    }
}