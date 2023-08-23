package com.example.covid19.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19.Model.CountryData
import com.example.covid19.Model.Response
import com.example.covid19.View.DetailActivity
import com.example.covid19.databinding.CountryItemsBinding

class CountryAdapter(private val onItemClick: (String) -> Unit): RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    private val combinedDataList = mutableListOf<Pair<String, Response?>>()

    fun setData(data: List<Pair<String, Response>>) {
        combinedDataList.clear()
        combinedDataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CountryItemsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val countryName = combinedDataList[position]
        holder.bind(countryName)
        holder.itemView.setOnClickListener {
            onItemClick(countryName.first)
        }
    }

    override fun getItemCount(): Int {
        return combinedDataList.size
    }

    class ViewHolder(private val binding: CountryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(combinedData: Pair<String, Response?>) {
            val (country, dailyResponse) = combinedData
            binding.apply {
                countryText.text = country
                if (dailyResponse != null) {
                    caseText.text = dailyResponse.cases.new?: "0"
                    deathText.text = dailyResponse.deaths.new ?: "0"
                    testNumberText.text = dailyResponse.tests.total.toString()
                }
            }
        }
    }
}