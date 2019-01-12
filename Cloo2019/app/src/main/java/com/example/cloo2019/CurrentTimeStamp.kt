package com.example.cloo2019

import android.os.Build
import java.time.LocalDateTime

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
}