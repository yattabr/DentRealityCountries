package com.woobu.countries.ui.countryDetails

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.woobu.countries.R
import com.woobu.countries.model.CountriesResponse
import com.woobu.countries.session.SharedPreferenceSession
import java.text.NumberFormat

class CountryDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _makeMyHomeObserver = MutableLiveData<String>()
    val makeMyHomeObserver: LiveData<String> = _makeMyHomeObserver

    private val _checkMyHomeObserver = MutableLiveData<String>()
    val checkMyHomeObserver: LiveData<String> = _checkMyHomeObserver

    private val _calcDistanceObserver = MutableLiveData<String>()
    val calcDistanceObserver: LiveData<String> = _calcDistanceObserver

    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedPreferenceSession: SharedPreferenceSession

    init {
        sharedPref = application.getSharedPreferences("sharedPRef", Context.MODE_PRIVATE)
        sharedPreferenceSession = SharedPreferenceSession(sharedPref)
    }

    fun makeMyHome(countryName: String) {
        sharedPreferenceSession.myHome = countryName
        _makeMyHomeObserver.value = countryName
    }

    fun checkHome() {
        _checkMyHomeObserver.value = sharedPreferenceSession.myHome
    }

    fun calculateDistance(myHome: CountriesResponse, country: CountriesResponse) {
        val startPoint = Location("home")
        startPoint.latitude = myHome.latlng[0]
        startPoint.longitude = myHome.latlng[1]

        val endPoint = Location("country")
        endPoint.latitude = country.latlng[0]
        endPoint.longitude = country.latlng[1]

        val distance: Float = startPoint.distanceTo(endPoint) / 1000

        _calcDistanceObserver.value = NumberFormat.getInstance().format(distance)
    }
}