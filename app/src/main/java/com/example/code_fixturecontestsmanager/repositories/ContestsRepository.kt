package com.example.code_fixturecontestsmanager.repositories

import com.example.code_fixture.models.Contests
import com.example.code_fixturecontestsmanager.MainActivity
import com.example.code_fixturecontestsmanager.retrofitAPI.RetroFit

class ContestsRepository {

    suspend fun getSinglePlatformContests(activityId: String) : Contests {
        return when(activityId) {
            MainActivity.CODE_FORCES -> {
                RetroFit.apiInstance.getCodeForcesContests()
            }
            MainActivity.CODE_CHEF -> {
                RetroFit.apiInstance.getCodeChefContests()
            }
            MainActivity.LEET_CODE -> {
                RetroFit.apiInstance.getLeetCodeContests()
            }
            else -> {
                RetroFit.apiInstance.getAllContests()
            }
        }
    }
}