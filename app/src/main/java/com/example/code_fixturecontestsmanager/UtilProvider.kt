package com.example.code_fixturecontestsmanager

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import com.google.android.material.textview.MaterialTextView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object UtilProvider {
    fun istProvider(dateTimeString: String?): String {
        val utcFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        val utcDate: Date = utcFormat.parse(dateTimeString)
        val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata") // Enter time zone you want.
        return formatter.format(utcDate)
    }

    fun istProviderForCodeChef(dateTimeString: String?): String {
        val utcFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss 'UTC'")
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        val utcDate: Date = utcFormat.parse(dateTimeString)
        val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata") // Enter time zone you want.
        return formatter.format(utcDate)
    }

    fun intoHoursAndMinutes(timeInSeconds: Long) : String {
        val hours: Long = timeInSeconds / 3600L
        val minutes: Long = (timeInSeconds % 3600L) / 60L
        val duration = ((hours.toString() + " hours ") + (if(minutes!=0L) minutes.toString() + " minutes" else "") )
        return duration
    }

    fun getGradientSilverDescent(textView: MaterialTextView, activityName: String) {
        val paint = textView.paint
        val width = paint.measureText((activityName))
        val textShader: Shader = LinearGradient(
            0f, 0f, width, textView.textSize, intArrayOf(
                Color.parseColor("#E1F5FE"), // start color
                Color.parseColor("#FAFAFA"), // middle color
                Color.parseColor("#E8EAF6"), // end color
            ), null, Shader.TileMode.CLAMP
        )
        textView.paint.shader = textShader
    }

    fun getGradientBlueDescent(textView: MaterialTextView, activityName: String) {
        val paint = textView.paint
        val width = paint.measureText((activityName+ " Contests"))
        val textShader: Shader = LinearGradient(
            0f, 0f, width, textView.textSize, intArrayOf(
                Color.parseColor("#1A237E"), // start color
                Color.parseColor("#4FC3F7"), // end color
            ), null, Shader.TileMode.CLAMP
        )
        textView.paint.shader = textShader
    }
}