package com.woobu.countries.ui.countriesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountriesViewModel : ViewModel() {

    private val _countriesObserver = MutableLiveData<String>()
    val countriesObserver: LiveData<String> = _countriesObserver

    fun fetchCountries(fileName: String) {
        _countriesObserver.value = fileName
    }
}