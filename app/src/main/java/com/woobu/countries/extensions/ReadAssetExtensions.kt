package com.woobu.countries.extensions

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.woobu.countries.model.CountriesResponse

fun AssetManager.readJsonFile(fileName: String): ArrayList<CountriesResponse> {
    val myType = object : TypeToken<List<CountriesResponse>>() {}.type
    return Gson().fromJson(
        this.open(fileName).bufferedReader().use { it.readText() }, myType
    )
}

fun AssetManager.readJsonFileAndReturnNames(fileName: String): ArrayList<String> {
    val myType = object : TypeToken<List<CountriesResponse>>() {}.type
    val response = Gson().fromJson(
        this.open(fileName).bufferedReader().use { it.readText() }, myType
    ) as ArrayList<CountriesResponse>

    val namesArray = arrayListOf<String>()
    response.forEach {
        namesArray.add(it.name)
    }

    return namesArray
}

fun AssetManager.getCountryByName(fileName: String, countryName: String): CountriesResponse? {
    val myType = object : TypeToken<List<CountriesResponse>>() {}.type
    val response = Gson().fromJson(
        this.open(fileName).bufferedReader().use { it.readText() }, myType
    ) as ArrayList<CountriesResponse>

    response.find { it.name == countryName }.let {
        return it
    }
}