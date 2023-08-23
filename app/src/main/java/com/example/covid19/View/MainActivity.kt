package com.example.covid19.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.covid19.Adapters.CountryAdapter
import com.example.covid19.ViewModel.ApiViewModel
import com.example.covid19.databinding.ActivityMainBinding
import java.util.Locale



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ApiViewModel
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var searchView: SearchView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        countryAdapter = CountryAdapter { countryName ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("countryName", countryName)
            startActivity(intent)
        }


        viewModel = ViewModelProvider(this).get(ApiViewModel::class.java)

        swipeRefreshLayout = binding.swipeRefresh
        binding.countryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.countryRecyclerView.adapter = countryAdapter

        swipeRefreshLayout.setOnRefreshListener {
            // Refresh data here
             viewModel.getDataFromAPI()
             swipeRefreshLayout.isRefreshing = false // Stop the refreshing animation
        }

        viewModel.getDataFromAPI()


        viewModel.combinedData.observe(this) { combinedDataList ->
            countryAdapter.setData(combinedDataList)
        }

        searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText?.lowercase(Locale.getDefault())
                val filteredData = if (searchText.isNullOrEmpty()) {
                    viewModel.combinedData.value ?: emptyList()
                } else {
                    viewModel.combinedData.value?.filter { (country, _) ->
                        country.lowercase(Locale.getDefault()).contains(searchText)
                    } ?: emptyList()
                }
                countryAdapter.setData(filteredData)
                return true
            }
        })
    }
}