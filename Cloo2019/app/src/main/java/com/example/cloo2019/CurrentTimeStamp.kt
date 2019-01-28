package com.example.cloo2019

import android.os.Build
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CurrentTimeStamp() {
    val current: String

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            current = LocalDateTime.now().toString()
        } else {
            current = "Not available"
        }
    }

    fun getString() : String {
        return current
    }

    fun getPresentableTimeString(timeStampString: String) : String {
        // 2019-01-12T22:23:29.994 is the format of the string as an input
        var dateTimeString:String = "not known"

        if(timeStampString != ""){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                var dateTime: LocalDateTime
                dateTime = LocalDateTime.parse(timeStampString, DateTimeFormatter.ISO_DATE_TIME)
                dateTimeString = dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
    //            dateTime = LocalDateTime.parse(timeStampString, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
    //            dateTimeString = dateTime.toString()
            }
        }
        return dateTimeString
    }
}