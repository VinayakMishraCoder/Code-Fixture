package com.example.code_fixturecontestsmanager.repositories

import com.example.code_fixture.models.Contests
import com.example.code_fixturecontestsmanager.retrofitAPI.RetroFit

class CodeforcesContestsRepository {

    suspend fun getCodeForcesContests() : Contests {
        return RetroFit.apiInstance.getCodeForcesContests()
    }

}