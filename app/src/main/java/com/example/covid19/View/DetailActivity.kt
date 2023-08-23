package com.example.covid19.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covid19.Adapters.DetailedAdapter
import com.example.covid19.Model.Response
import com.example.covid19.R
import com.example.covid19.ViewModel.ApiViewModel
import com.example.covid19.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: ApiViewModel
    private lateinit var adapter: DetailedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ApiViewModel::class.java)

        // Get the country name passed from MainActivity
        val countryName = intent.getStringExtra("countryName") ?: ""

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val responseList = mutableListOf<Response>() // Create a mutable list
        adapter = DetailedAdapter(responseList)
        recyclerView.adapter = adapter

        viewModel.fetchDetailedDataForCountry(countryName)

        viewModel.detailedData.observe(this, Observer { detailedResponse ->
            // Clear the list and add the detailed response
            responseList.clear()
            responseList.add(detailedResponse)
            adapter.notifyDataSetChanged()
        })
        binding.historyButton.setOnClickListener {
            val intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.toolbar.inflateMenu(R.menu.share_menu)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.share) {
                viewModel.detailedData.value?.let { detailedResponse ->
                    viewModel.shareDetails(this, detailedResponse)
                }
            }
            true
        }



    }
    /*private fun shareDetails() {
        val stringBuilder = StringBuilder()

        // Append the text from each TextView to the StringBuilder
        stringBuilder.append("Country: ${binding.countryName.text}\n")
        stringBuilder.append("Cases: ${binding.cases.text}\n")
        stringBuilder.append("New Cases: ${binding.newCases.text}\n")
        stringBuilder.append("Active Cases: ${binding.activeCases.text}\n")
        stringBuilder.append("Critical Cases: ${binding.criticalCases.text}\n")
        stringBuilder.append("Recovered Cases: ${binding.recoveredCases.text}\n")
        stringBuilder.append("Total Cases: ${binding.totalCases.text}\n")
        stringBuilder.append("Deaths: ${binding.deaths.text}\n")
        stringBuilder.append("Total Deaths: ${binding.totalDeaths.text}\n")
        stringBuilder.append("Total Tests: ${binding.totalTests.text}\n")

        val detailsText = stringBuilder.toString()

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, detailsText)
        startActivity(Intent.createChooser(intent, "Share Details"))
    }*/

}

