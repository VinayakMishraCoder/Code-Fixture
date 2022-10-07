package com.example.code_fixturecontestsmanager.retrofitAPI

import com.example.code_fixture.models.Contests
import com.example.code_fixture.models.MultiplatformContests
import retrofit2.http.GET

interface APIservices {

    @GET("/api/v1/codeforces")
    suspend fun getCodeForcesContests(): Contests

    @GET("/api/v1/code_chef")
    suspend fun getCodeChefContests(): Contests

    @GET("/api/v1/leet_code")
    suspend fun getLeetCodeContests(): Contests

    @GET("/api/v1/all")
    suspend fun getAllContests(): MultiplatformContests

}