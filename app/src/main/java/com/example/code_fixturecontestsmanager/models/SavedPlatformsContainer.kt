package com.example.code_fixturecontestsmanager.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavedPlatformsContainer(
    val listOfSavedSites: ArrayList<String>? = null
): Parcelable