package com.theprime.primemath.ui.activity

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adsmedia.adsmodul.AdsHelper
import com.adsmedia.adsmodul.utils.AdsConfig
import com.theprime.primemath.BuildConfig
import com.theprime.primemath.R
import com.theprime.primemath.config.AdsData
import com.theprime.primemath.data.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import java.util.Locale


var isRecreated = false
class MainActivity : AppCompatActivity() {
    private val database: AppDatabase by inject()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isRecreated){
            database.settingsDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { databaseSettings ->
                    if (databaseSettings.isNotEmpty()) {
                        setLocale(databaseSettings[0].language)
                        isRecreated = true
                        recreate()
                    }
                }.subscribe()
        }
        setContentView(R.layout.main_activity)

        AdsData.getIDAds();
        AdsHelper.gdprPrime(this, true, "", AdsConfig.Game_App_ID)
        AdsHelper.initializeAdsPrime(this, BuildConfig.APPLICATION_ID, AdsConfig.Game_App_ID)
        AdsHelper.loadInterstitialPrime(
            this,
            AdsConfig.Interstitial_ID
        )
        if (BuildConfig.DEBUG) {
            AdsHelper.debugModePrime(true)
        }
        AdsHelper.showBannerPrime(
            this, findViewById(R.id.adView),
            AdsConfig.Banner_ID
        )

        if (isNetworkAvailable()) {
            Toast.makeText(this, "Ada Internet", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "ga ada jir", Toast.LENGTH_LONG).show()
        }


    }

    /**
     * Sets the application language
     */
    private fun setLocale(language:String){
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(language))
        resources.updateConfiguration(conf,dm)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        Log.d("Network", "Active network info: $activeNetworkInfo")
        Log.d("Network", "Network state: ${activeNetworkInfo?.state}")
        Log.d("Network", "Network reason: ${activeNetworkInfo?.reason}")
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
