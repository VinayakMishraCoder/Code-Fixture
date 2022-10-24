package com.example.code_fixturecontestsmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.code_fixturecontestsmanager.activities.AllOtherPlatformActivity
import com.example.code_fixturecontestsmanager.activities.PlatformsActivity
import com.example.code_fixturecontestsmanager.activities.SinglePlatformContestsActivity
import com.example.code_fixturecontestsmanager.activities.login.LoginActivity
import com.example.code_fixturecontestsmanager.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    /**
     * Change all platform activity to -> multiplatform.
     * Single Adapter to Contest Adapter.
     * Change UI/UX of Login Screen.
     * Think of modeling users's bookmarked contests and saved sites into one model.
     * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()

        window.setStatusBarColor(getResources().getColor(R.color.plo));

        binding.checkoutAnim.playAnimation()
        binding.checkoutAnim.loop(true)
        binding.horizontalScrollView.apply {
            isSmoothScrollingEnabled = true
        }

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
                startActivity(intent)
            }
            addMore.setOnClickListener {
                val intent = Intent(this@MainActivity, PlatformsActivity::class.java)
                startActivity(intent)
            }
        }

        binding.landerMenu.setOnClickListener {
            val inflater = binding.root.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.main_activity_popup, null) // pass custom layout
            val popupWindow = PopupWindow(view, 500, ConstraintLayout.LayoutParams.WRAP_CONTENT, true)
            popupWindow.elevation = 40.0f
            view.findViewById<CardView>(R.id.logout).setOnClickListener {
//                Firebase.auth.signOut()
//                GoogleSignIn.getClient(getApplicationContext(), GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
                Firebase.auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            popupWindow.showAsDropDown(binding.landerMenu) // view to attach with
        }
    }

    companion object {
        val ACTIVITY_SHIFTER: String = "ACTIVITY_SHIFTER";
        val CODE_FORCES = "CodeForces";
        val CODE_CHEF = "CodeChef";
        val LEET_CODE = "LeetCode";
        val OTHER_PLATFORMS = "OtherPlatforms";
    }
}






