package com.example.code_fixturecontestsmanager.activities

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.code_fixturecontestsmanager.R
import com.example.code_fixturecontestsmanager.adapters.PlatformAdapter
import com.example.code_fixturecontestsmanager.databinding.ActivityPlatformsBinding
import com.example.code_fixturecontestsmanager.viewmodels.AvailablePlatformsViewModel


class PlatformsActivity : AppCompatActivity(), PlatformAdapter.onPlatformClickListeners {
    lateinit var binding: ActivityPlatformsBinding
    lateinit var adapter: PlatformAdapter
    private val viewModel: AvailablePlatformsViewModel by lazy {
        ViewModelProvider(this).get(AvailablePlatformsViewModel::class.java)
    }
    private var GFG_URI = "https://www.geeksforgeeks.org"
    private var package_name = "com.example.code_fixturecontestsmanager.activities";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_platforms)

        supportActionBar?.hide()
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        adapter = PlatformAdapter()
        binding.recyclerView.adapter = adapter
        viewModel.getAvailablePlatforms()
        viewModel.platformList.observe(this) { platformData ->
            viewModel.listSize.observe(this) { listSize ->
                if(listSize > 0) {
                    adapter.setUp(platformData, this@PlatformsActivity)
                } else {

                }
            }
        }
    }

    override fun onPlatformClick(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        val colorInt: Int = Color.parseColor("#1565C0")
        val defaultColors = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(colorInt)
            .build()
        builder.setDefaultColorSchemeParams(defaultColors)
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }



}