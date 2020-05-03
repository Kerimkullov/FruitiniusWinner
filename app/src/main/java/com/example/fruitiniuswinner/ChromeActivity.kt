package com.example.fruitiniuswinner

import android.app.Activity
import android.content.ComponentName
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession


class ChromeActivity : Activity() {

    private val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";


    private var mCustomTabsServiceConnection: CustomTabsServiceConnection? = null
    private var mClient: CustomTabsClient? = null
    private var mCustomTabsSession: CustomTabsSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCustomTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(componentName: ComponentName, customTabsClient: CustomTabsClient) {
                //Pre-warming
                mClient = customTabsClient
                mClient?.warmup(0L)
                mCustomTabsSession = mClient?.newSession(null)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                mClient = null
            }
        }

        CustomTabsClient.bindCustomTabsService(this, CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);

        var targetUrl:String = intent.getStringExtra("targetUrl")
        loadCustomTabForSite("https://kalemika.club/osnova/$targetUrl")

    }

    override fun onResume() {
        super.onResume()
        val timer = object : CountDownTimer(1500, 500) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                var targetUrl:String = intent.getStringExtra("targetUrl")
                loadCustomTabForSite("https://kalemika.club/osnova/$targetUrl")
            }
        }
        timer.start()
    }

    private fun loadCustomTabForSite(url: String, color: Int = Color.GRAY) {
        val options = BitmapFactory.Options()
        options.outWidth = 24
        options.outHeight = 24
        options.inScaled = true

        val customTabsIntent = CustomTabsIntent.Builder(mCustomTabsSession)
            .setToolbarColor(color)
            .setShowTitle(true)
            .setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .setCloseButtonIcon(
            BitmapFactory.decodeResource(
                resources, R.drawable.arrow_back, options
            )
        ).build()

        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}