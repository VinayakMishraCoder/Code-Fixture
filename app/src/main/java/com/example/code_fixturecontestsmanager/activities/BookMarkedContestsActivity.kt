package com.example.code_fixturecontestsmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.code_fixturecontestsmanager.R
import com.example.code_fixturecontestsmanager.databinding.ActivityBookmarkedContestsBinding
class BookMarkedContestsActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookmarkedContestsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bookmarked_contests)
        supportActionBar?.hide()

    }
}