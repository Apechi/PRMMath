package com.theprime.primemath.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adsmedia.adsmodul.AdsHelper
import com.adsmedia.adsmodul.OpenAds
import com.adsmedia.adsmodul.utils.AdsConfig
import com.theprime.primemath.BuildConfig
import com.theprime.primemath.R
import com.theprime.primemath.config.AdsData

@SuppressLint("CustomSplashScreen")
class   SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        AdsHelper.initializeAdsPrime(this, BuildConfig.APPLICATION_ID, AdsConfig.Game_App_ID)
        if (BuildConfig.DEBUG) {
            AdsHelper.debugModePrime(true)
        }
        AdsData.getIDAds();
        OpenAds.LoadOpenAds(AdsConfig.Open_App_ID)
        OpenAds.AppOpenAdManager.showAdIfAvailable(this) {
            val intent = Intent(this, MainActivity::class.java)

            object : CountDownTimer(3000, 1000) {
                override fun onFinish() {
                    startActivity(intent)
                    finish()
                }
                override fun onTick(millisUntilFinished: Long) {
                    //return Unit
                }
            }.start()
        }

    }
}