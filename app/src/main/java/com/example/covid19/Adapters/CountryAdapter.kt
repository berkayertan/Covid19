package com.example.covid19.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19.databinding.CountryItemsBinding

class CountryAdapter : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {


    private val countryList = mutableListOf<String>()


    fun setData(data: List<String>) {
        countryList.clear()
        countryList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CountryItemsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val countryName = countryList[position]
        holder.bind(countryName)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    class ViewHolder(private val binding: CountryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(country: String) {
            binding.apply {
                countryText.text = country
            }
        }
    }

}