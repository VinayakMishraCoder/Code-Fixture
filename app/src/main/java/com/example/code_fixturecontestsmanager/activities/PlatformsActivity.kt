package com.example.code_fixturecontestsmanager.activities

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.code_fixturecontestsmanager.Constants
import com.example.code_fixturecontestsmanager.R
import com.example.code_fixturecontestsmanager.UtilProvider
import com.example.code_fixturecontestsmanager.adapters.PlatformAdapter
import com.example.code_fixturecontestsmanager.databinding.ActivityPlatformsBinding
import com.example.code_fixturecontestsmanager.models.SavedPlatformsContainer
import com.example.code_fixturecontestsmanager.viewmodels.AvailablePlatformsViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class PlatformsActivity : AppCompatActivity(), PlatformAdapter.onPlatformClickListeners {

    lateinit var binding: ActivityPlatformsBinding
    lateinit var adapter: PlatformAdapter
    private val viewModel: AvailablePlatformsViewModel by lazy {
        ViewModelProvider(this).get(AvailablePlatformsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_platforms)
        UtilProvider.getGradientSilverDescent(binding.toolbarTitle, ("Available Platforms"))
        supportActionBar?.hide()
        binding.backButton.setOnClickListener { onBackPressed() }

        adapter = PlatformAdapter()
        binding.recyclerView.adapter = adapter
        viewModel.getAvailablePlatforms()
        viewModel.retrieveSelectedSitesList()
        viewModel.equaliseLists()

        viewModel.platformList.observe(this) { platformData ->
            viewModel.listSize.observe(this) { listSize ->
                val visibility = if (listSize > 0) INVISIBLE else VISIBLE
                binding.progressBarCforces.visibility = visibility
                binding.progressBarNumberOfPlatforms.visibility = visibility
                if (listSize > 0) {
                    binding.listSize.text = listSize.toString()
                    binding.listSize.visibility = VISIBLE
                    adapter.setUp(platformData, this@PlatformsActivity, viewModel.listOfAlreadySelectedSites)
                }
            }
        }
        binding.saveSitesButton.setOnClickListener {
            viewModel.onSaveClick()
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
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

    override fun onCheckBoxClick(siteName: String, isChecked: Boolean) {
        viewModel.onCheckBoxStateChanged(siteName, isChecked)
    }
}