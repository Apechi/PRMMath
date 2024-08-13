package com.theprime.primemath.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adsmedia.adsmodul.AdsHelper
import com.adsmedia.adsmodul.utils.AdsConfig
import com.theprime.primemath.BuildConfig
import com.theprime.primemath.R
import com.theprime.primemath.config.AdsData
import com.theprime.primemath.data.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import io.reactivex.disposables.CompositeDisposable
import java.util.*

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
        AdsHelper.initializeAds(this, BuildConfig.APPLICATION_ID, AdsConfig.Game_App_ID)
        AdsHelper.loadInterstitial(
            this,
            AdsConfig.Interstitial_ID
        )
        if (BuildConfig.DEBUG) {
            AdsHelper.debugMode(true)
        }
        AdsHelper.showBanner(
            this, findViewById(R.id.adView),
            AdsConfig.Banner_ID
        )

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
}
