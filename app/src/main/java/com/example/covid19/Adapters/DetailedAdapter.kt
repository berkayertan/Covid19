package com.example.covid19.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19.Model.Response
import com.example.covid19.databinding.DetailItemsBinding

class DetailedAdapter(private val data: MutableList<Response>) :
    RecyclerView.Adapter<DetailedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DetailItemsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
}

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val responseData = data[position]
        holder.bind(responseData)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(private val binding: DetailItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: Response) {
            Log.d("Adapter", "Country: ${response.country}")
            Log.d("Adapter", "Cases: ${response.cases}")
            Log.d("Adapter", "Deaths: ${response.deaths}")
            Log.d("Adapter", "Tests: ${response.tests}")
            binding.apply {
                response.deaths?.let { deaths ->
                    totalDeaths.text = "Deaths: ${deaths.total}"
                    // Handle other properties of deaths if needed
                }
                response.cases?.let { cases ->
                    newCases.text = "Cases: ${cases.new}"
                    // Handle other properties of cases if needed
                }
                response.tests?.let { tests ->
                    totalTests.text = "Tests: ${tests.total}"
                    // Handle other properties of tests if needed
                }
            }
        }
    }
}
