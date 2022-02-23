package com.woobu.countries.ui.countryDetails

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.woobu.countries.R
import com.woobu.countries.databinding.ScreenCountryDetailBinding
import com.woobu.countries.extensions.getCountryByName
import com.woobu.countries.extensions.readJsonFileAndReturnNames
import com.woobu.countries.model.CountriesResponse
import com.woobu.countries.session.SharedPreferenceSession
import com.google.android.gms.maps.model.PolylineOptions


class CountryDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: CountryDetailViewModel by viewModels()
    private lateinit var mMap: GoogleMap
    private lateinit var country: CountriesResponse
    lateinit var binding: ScreenCountryDetailBinding

    private var myHome: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScreenCountryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        country = intent.getSerializableExtra("country") as CountriesResponse

        initUI()
        initObservers()

        viewModel.checkHome()
    }

    private fun initUI() {
        binding.txtCountryName.text = "Country - ${country.name}"
        binding.txtCapital.text = "Capital - ${country.capital}"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initSpinner()

        binding.buttonMyHome.setOnClickListener { viewModel.makeMyHome(country.name) }
        binding.calculateDistance.setOnClickListener { calculateDistance() }
    }

    private fun initSpinner() {
        val spinnerArrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            this.assets.readJsonFileAndReturnNames(SharedPreferenceSession.FILE)
        )
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = spinnerArrayAdapter
    }

    private fun initObservers() {
        viewModel.makeMyHomeObserver.observe(this, {
            Toast.makeText(this, "You have set this as your home.", Toast.LENGTH_SHORT).show()
            viewModel.checkHome()
        })

        viewModel.checkMyHomeObserver.observe(this, {
            binding.buttonMyHome.isGone = it == country.name
            binding.textviewYourHome.isGone = it != country.name

            it.apply {
                if (it == country.name) {
                    binding.edittextHome.setText(this)
                    myHome = it
                } else {
                    binding.edittextHome.setText("")
                    myHome = ""
                }
            }
        })

        viewModel.calcDistanceObserver.observe(this, {
            binding.textviewTotalDistance.text = "Total Distance: $it KM"
        })
    }

    private fun calculateDistance() {
        val myCountry = assets.getCountryByName(SharedPreferenceSession.FILE, myHome)
        val countrySelected = assets.getCountryByName(
            SharedPreferenceSession.FILE,
            binding.spinnerCountry.selectedItem.toString()
        )
        myCountry.let {
            if (it == null) {
                Toast.makeText(this, "You need to set as your home first", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.calculateDistance(myCountry!!, countrySelected!!)
                addNewMarkerOnTheMap(
                    LatLng(myCountry.latlng[0], myCountry.latlng[1]),
                    LatLng(countrySelected.latlng[0], countrySelected.latlng[1])
                )
            }
        }
    }

    private fun addNewMarkerOnTheMap(myHome: LatLng, countrySelected: LatLng) {
        mMap.addMarker(MarkerOptions().position(countrySelected).title(country.name))
        mMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(myHome, countrySelected)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(countrySelected, 3f))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (country.latlng.isNotEmpty()) {
            val location = LatLng(country.latlng[0], country.latlng[1])
            mMap.addMarker(MarkerOptions().position(location).title(country.name))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 6f))
        }
    }
}