package com.example.code_fixturecontestsmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.code_fixturecontestsmanager.activities.CodeForcesActivity
import com.example.code_fixturecontestsmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()

        binding.lottieAnimationView.playAnimation()
        binding.lottieAnimationView.loop(true)

        binding.cfContestButton.setOnClickListener {
            val intent = Intent(this, CodeForcesActivity::class.java)
            startActivity(intent)
        }
    }
}