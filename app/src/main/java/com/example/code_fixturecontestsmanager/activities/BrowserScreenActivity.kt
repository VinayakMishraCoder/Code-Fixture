package com.example.code_fixturecontestsmanager.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.code_fixturecontestsmanager.R
import com.example.code_fixturecontestsmanager.UtilProvider
import com.example.code_fixturecontestsmanager.databinding.ActivityBrowserScreenBinding

class BrowserScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivityBrowserScreenBinding
    lateinit var contestUrl: String
    private var TOOLBAR_TITLE = "Register"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_browser_screen)
        supportActionBar?.hide()
        SinglePlatformContestsActivity.URL_KEY?.let {
            contestUrl = intent.getStringExtra(it) as String
        }

        var mWebView = binding.contestWebView as WebView
        contestUrl?.let { mWebView?.loadUrl(it) }
            ?: kotlin.run {
                // Handle issues of url
            }

        binding.apply {
            filterSortBut.visibility = View.INVISIBLE
            backButton.setOnClickListener {
                onBackPressed()
            }
            toolbarTitle.text = TOOLBAR_TITLE
            UtilProvider.getGradientSilverDescent(toolbarTitle, TOOLBAR_TITLE)
        }

        val webSettings = mWebView?.getSettings()
        webSettings?.setJavaScriptEnabled(true)
        webSettings?.safeBrowsingEnabled = true
        mWebView?.setWebViewClient(WebViewClient())
        mWebView.getSettings().setLoadWithOverviewMode(true)
        mWebView.getSettings().setUseWideViewPort(true)

        mWebView?.canGoBack()
        mWebView?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP && mWebView.canGoBack()) {
                mWebView.goBack()
                return@OnKeyListener true
            }
            false
        })

        /**
         * Give options for opening in custom tabs/ delete Browsing Activity.
         * Add all platforms lists/ adapter and activity.
         * Add filtering.
         * Create Login UI.
         * */

    }
}