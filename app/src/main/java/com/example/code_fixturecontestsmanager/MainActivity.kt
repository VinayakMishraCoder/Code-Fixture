package com.example.code_fixturecontestsmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.code_fixturecontestsmanager.activities.AllOtherPlatformActivity
import com.example.code_fixturecontestsmanager.activities.SinglePlatformContestsActivity
import com.example.code_fixturecontestsmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()

        binding.lottieAnimationView.playAnimation()
        binding.lottieAnimationView.loop(true)


        binding.apply {
            cfContestButton.setOnClickListener {
                val intent = Intent(this@MainActivity, SinglePlatformContestsActivity::class.java)
                intent.putExtra(ACTIVITY_SHIFTER, CODE_FORCES)
                startActivity(intent)
            }
            cchefContestButton.setOnClickListener {
                val intent = Intent(this@MainActivity, SinglePlatformContestsActivity::class.java)
                intent.putExtra(ACTIVITY_SHIFTER, CODE_CHEF)
                startActivity(intent)
            }
            leetContestButton.setOnClickListener {
                val intent = Intent(this@MainActivity, SinglePlatformContestsActivity::class.java)
                intent.putExtra(ACTIVITY_SHIFTER, LEET_CODE)
                startActivity(intent)
            }
            allContestButton.setOnClickListener {
                val intent = Intent(this@MainActivity, AllOtherPlatformActivity::class.java)
                intent.putExtra(ACTIVITY_SHIFTER, OTHER_PLATFORMS)
                startActivity(intent)
            }
        }
    }

    companion object {
        val ACTIVITY_SHIFTER:String = "ACTIVITY_SHIFTER";
        val CODE_FORCES = "CodeForces";
        val CODE_CHEF = "CodeChef";
        val LEET_CODE = "LeetCode";
        val OTHER_PLATFORMS = "OtherPlatforms";
    }
}