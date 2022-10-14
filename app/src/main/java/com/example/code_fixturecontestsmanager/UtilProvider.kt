package com.example.code_fixturecontestsmanager

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object UtilProvider {

    fun istProvider(dateTimeString: String?): String {
        val utcFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        val utcDate: Date = utcFormat.parse(dateTimeString)
        val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata") // Or whatever IST is supposed to be
        return formatter.format(utcDate)
    }

    fun intoHoursAndMinutes(timeInSeconds: Long) : String {
        val hours: Long = timeInSeconds / 3600L
        val minutes: Long = (timeInSeconds % 3600L) / 60L
        val duration = hours.toString() + " hours " + minutes.toString() + " minutes"
        return duration
    }
}