package com.example.covid19.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.covid19.ViewModel.ApiViewModel
import com.example.covid19.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: ApiViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(ApiViewModel::class.java)

        binding.dateEditText.setOnClickListener {
            viewModel.showDatePickerDialog(this, binding.dateEditText)
        }

        binding.searchButton.setOnClickListener {
            val selectedDate = binding.dateEditText.text.toString()
            if (selectedDate.isNotBlank()) {
                viewModel.getDailyHistory(selectedDate)

            }

        }

        viewModel.historyList.observe(this) { historyList ->
            // This block will be triggered when historyList LiveData updates
            val intent = Intent(this, DetailActivity::class.java)
            intent.putStringArrayListExtra("historyList", ArrayList(historyList))
            startActivity(intent)
        }

    }
}

