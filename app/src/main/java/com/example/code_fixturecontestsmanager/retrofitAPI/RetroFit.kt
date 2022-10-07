package com.example.code_fixturecontestsmanager.retrofitAPI
import com.example.code_fixturecontestsmanager.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFit {
    val apiInstance: APIservices by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIservices::class.java)
    }
}