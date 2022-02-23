package com.woobu.countries.session

import android.content.SharedPreferences

class SharedPreferenceSession(private val sharedPreferences: SharedPreferences) {

    companion object {
        const val FILE = "countries.json"
        private const val MY_HOME = "my_home"
    }

    var myHome: String?
        get() {
            return getValue(MY_HOME)
        }
        set(value) {
            putValue(MY_HOME, value.toString())
        }

    private fun getValue(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    private fun putValue(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

}