package com.example.code_fixturecontestsmanager.repositories

import com.example.code_fixturecontestsmanager.models.AvailablePlatforms
import com.example.code_fixturecontestsmanager.retrofitAPI.RetroFit

class PlatformsRepository {
    suspend fun getAvailablePlatforms() : AvailablePlatforms {
        return RetroFit.apiInstance.getAllAvailablePlatforms()
    }
}