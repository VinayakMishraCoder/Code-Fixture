package com.example.code_fixturecontestsmanager.models

import android.os.Parcelable
import com.example.code_fixture.models.ContestsItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailsContainer(
    var listOfSavedSites: ArrayList<String> = ArrayList(),
    var bookMarkedContests: ArrayList<ContestsItem> = ArrayList()
) : Parcelable