package com.example.covid19.Service

import com.example.covid19.Model.CountryData
import com.example.covid19.Model.DailyData
import com.example.covid19.Model.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountryAPI {

    @GET("countries/")
    fun getCountries(): Single<CountryData>

    @GET("statistics/")
    fun getStatistics(): Single<DailyData>

    @GET("statistics")
    fun getDetailedData(@Query("country") country: String): Single<Response>

    @GET("history/{date}/")
    fun getHistory(@Path("date") date: String): Single<DailyData>
}