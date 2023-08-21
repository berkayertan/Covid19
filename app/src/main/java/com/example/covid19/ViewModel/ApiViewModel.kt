package com.example.covid19.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covid19.Model.CountryData
import com.example.covid19.Service.Countries
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ApiViewModel : ViewModel() {

    private val countriesService = Countries()
    private val disposable = CompositeDisposable()
    val countries = MutableLiveData<List<String>>() // Expecting a list of country names as strings

    fun getDataFromAPI() {
        disposable.add(
            countriesService.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ countryData ->
                    countries.value = countryData.response // Access the 'response' property of CountryData
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