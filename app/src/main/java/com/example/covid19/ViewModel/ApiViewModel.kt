package com.example.covid19.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covid19.Model.CountryData
import com.example.covid19.Model.DailyData
import com.example.covid19.Model.Response
import com.example.covid19.Service.Countries
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
class ApiViewModel : ViewModel() {

    private val countriesService = Countries()
    private val disposable = CompositeDisposable()

    val combinedData = MutableLiveData<List<Pair<String, Response>>>() // Use Pair to hold country name and response

    fun getDataFromAPI() {
        disposable.add(
            countriesService.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ countryData ->
                    // Fetch the daily statistics after fetching the country data
                    fetchDailyStatistics(countryData.response)
                }, { error ->
                    error.printStackTrace()
                })
        )
    }

    private fun fetchDailyStatistics(countries: List<String>) {
        disposable.add(
            countriesService.getDailyStatistics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ dailyData ->
                    val combinedList = countries.mapNotNull { country ->
                        val dailyResponse = dailyData.response.find { it.country == country }
                        if (dailyResponse != null) {
                            country to dailyResponse
                        } else {
                            null
                        }
                    }
                    combinedData.value = combinedList
                }, { error ->
                    error.printStackTrace()
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
