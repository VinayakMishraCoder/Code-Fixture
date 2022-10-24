package com.example.code_fixturecontestsmanager.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.code_fixturecontestsmanager.MainActivity
import com.example.code_fixturecontestsmanager.activities.login.LoginActivity

class SplashScreenOne: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}