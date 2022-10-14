package com.example.code_fixture.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MultiplatformContestsItem(

    var duration: Long? = null,

    var end_time: String? = null,

    var in_24_hours: String? = null,

    var name: String? = null,

    var site: String? = null,

    var start_time: String? = null,

    var status: String? = null,

    var url: String? = null

): Parcelable