package com.example.covid19.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covid19.Adapters.CountryAdapter
import com.example.covid19.ViewModel.ApiViewModel
import com.example.covid19.databinding.ActivityMainBinding
import java.util.Locale



    class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding
        private lateinit var viewModel: ApiViewModel
        private lateinit var countryAdapter: CountryAdapter
        private lateinit var searchView: SearchView
        private lateinit var countryList: List<String>

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)

            viewModel = ViewModelProvider(this).get(ApiViewModel::class.java)

            binding.countryRecyclerView.layoutManager = LinearLayoutManager(this)
            countryAdapter = CountryAdapter()
            binding.countryRecyclerView.adapter = countryAdapter

            viewModel.getDataFromAPI()
            viewModel.countries.observe(this) { countries ->
                countryList = countries // Update the countryList
                countryAdapter.setData(countries)
            }

            searchView = binding.searchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val searchText = newText?.lowercase(Locale.getDefault())
                    val filteredCountries = if (searchText.isNullOrEmpty()) {
                        countryList
                    } else {
                        countryList.filter {
                            it.lowercase(Locale.getDefault()).contains(searchText)
                        }
                    }
                    countryAdapter.setData(filteredCountries)
                    return true
                }
            })
        }

    }

