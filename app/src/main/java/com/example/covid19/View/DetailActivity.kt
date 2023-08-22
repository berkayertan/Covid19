package com.example.covid19.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.covid19.Model.Response
import com.example.covid19.R
import com.example.covid19.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.historyButton.setOnClickListener {
            val intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.toolbar.inflateMenu(R.menu.share_menu)
        binding.toolbar.setOnMenuItemClickListener {
            if(it.itemId==R.id.share) {
                shareDetails()
            }
            true
        }


        val response = intent.getSerializableExtra("response") as? Response
        if (response != null) {
            binding.apply {


                newCases.text = response.cases.new ?: "0"
                activeCases.text = response.cases.active.toString()
                criticalCases.text = response.cases.critical.toString()
                recoveredCases.text = response.cases.recovered.toString()
                totalCases.text = response.cases.total.toString()

                totalDeaths.text = response.deaths.total.toString()

                totalTests.text = response.tests.total.toString()
            }

        }
    }

    private fun shareDetails() {
        val stringBuilder = StringBuilder()

        // Append the text from each TextView to the StringBuilder
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
    }
}

