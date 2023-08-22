package com.example.covid19.Service

import com.example.covid19.Model.CountryData
import com.example.covid19.Model.DailyData
import io.reactivex.Single
import retrofit2.http.GET

interface CountryAPI {

    @GET("countries/")
    fun getCountries(): Single<CountryData>

    @GET("statistics/")
    fun getStatistics(): Single<DailyData>
}