package com.example.code_fixture.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContestsItem(

    @SerializedName("duration")
    var duration: Long? = null,

    @SerializedName("end_time")
    var end_time: String? = null,

    @SerializedName("in_24_hours")
    var in_24_hours: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("start_time")
    var start_time: String? = null,

    @SerializedName("status")
    var status: String? = null,

    @SerializedName("url")
    var url: String? = null,

    @SerializedName("site")
    var site: String? = null

): Parcelable