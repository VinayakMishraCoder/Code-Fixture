package com.example.code_fixturecontestsmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.code_fixturecontestsmanager.R
import com.example.code_fixturecontestsmanager.databinding.ActivityPlatformsBinding

class PlatformsActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlatformsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_platforms)

    }
}