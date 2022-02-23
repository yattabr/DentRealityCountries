package com.woobu.countries.ui.countriesList

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.woobu.countries.databinding.ItemCountriesBinding
import com.woobu.countries.model.CountriesResponse
import com.woobu.countries.ui.countryDetails.CountryDetailActivity
import java.util.*


class CountriesAdapter() :
    RecyclerView.Adapter<CountriesAdapter.CustomViewHolder>(), Filterable {

    private var filteredList: ArrayList<CountriesResponse> = arrayListOf()
    private var countriesList: ArrayList<CountriesResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view =
            ItemCountriesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        return CustomViewHolder(view)
    }

    fun loadCountries(items: ArrayList<CountriesResponse>) {
        filteredList = items
        countriesList = items
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        filteredList.sortBy { it.name }
        holder.bind(filteredList[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                filteredList = if (charString.isEmpty()) {
                    countriesList
                } else {
                    val list = countriesList.filter { item ->
                        item.name.toLowerCase(Locale.getDefault())
                            .contains(charString.toLowerCase(Locale.getDefault()))
                    }
                    list as ArrayList<CountriesResponse>
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results!!.values as ArrayList<CountriesResponse>
                notifyDataSetChanged()
            }

        }
    }

    class CustomViewHolder(itemView: ItemCountriesBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val context = itemView.root.context
        private val textViewMame = itemView.textviewName
        private val textViewCountry = itemView.textviewCountry

        fun bind(item: CountriesResponse) {
            textViewMame.text = item.name
            textViewCountry.text = "${item.capital} - ${item.countryCode}"

            itemView.setOnClickListener {
                val intent = Intent(context, CountryDetailActivity::class.java)
                intent.putExtra("country", item)
                context.startActivity(intent)
            }
        }
    }
}