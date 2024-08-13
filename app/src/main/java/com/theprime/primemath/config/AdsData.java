package com.theprime.primemath.config;

import com.adsmedia.adsmodul.utils.AdsConfig;

public class AdsData {
    /*
    change placement ID
    AdsConfig.Banner_ID = "your_placement_banner";
    AdsConfig.Interstitial_ID ="your_placement_interstitial";
    AdsConfig.Open_App_ID = "your_placement_open_app";
     */

    /*
    Change Admob ads to other ads https://codegith.com/support
     */
    public static void getIDAds(){
        AdsConfig.Banner_ID = "ca-app-pub-3940256099942544/9214589741";
        AdsConfig.Interstitial_ID ="ca-app-pub-3940256099942544/1033173712";
        AdsConfig.Open_App_ID = "ca-app-pub-3940256099942544/9257395921";// only for Admob, Applovin, and Pangle
        AdsConfig.Game_App_ID = ""; // for Unity and Pangle
        AdsConfig.Interval = 3; // change number for interstitial
    }
}
