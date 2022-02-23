package com.woobu.countries.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

class CountriesResponse : Serializable {
    val timezones: List<String> = arrayListOf()
    val latlng: List<Double> = arrayListOf()
    val name: String = ""

    @SerializedName("country_code")
    val countryCode: String = ""
    val capital: String = ""
}