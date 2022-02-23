package com.woobu.countries.ui.countriesList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.woobu.countries.databinding.ScreenCountriesBinding
import com.woobu.countries.extensions.readJsonFile
import com.woobu.countries.model.CountriesResponse
import com.woobu.countries.session.SharedPreferenceSession

class CountriesActivity : AppCompatActivity() {

    private val countriesViewModel: CountriesViewModel by viewModels()
    lateinit var countriesAdapter: CountriesAdapter
    lateinit var binding: ScreenCountriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScreenCountriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initObservers()

        countriesViewModel.fetchCountries(SharedPreferenceSession.FILE)
    }

    private fun initObservers() {
        countriesViewModel.countriesObserver.observe(this, {
            iniRecyclerView(this.assets.readJsonFile(it))
        })
    }

    private fun initUI() {
        binding.editSearch.addTextChangedListener(filterWatcher)
    }

    private fun iniRecyclerView(list: ArrayList<CountriesResponse>) {
        countriesAdapter = CountriesAdapter()
        countriesAdapter.loadCountries(list)
        binding.recyclerView.adapter = countriesAdapter
    }

    private val filterWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            countriesAdapter.filter.filter(s)
        }
    }

}