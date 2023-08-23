package com.example.covid19.Service

import com.example.covid19.Model.CountryData
import com.example.covid19.Model.DailyData
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Countries {

    private val BASE_URL = "https://covid-193.p.rapidapi.com/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                        .newBuilder()
                        .addHeader("x-rapidapi-key", "40cfea8fc6msh4e63f74aa9f16bfp1cdc3djsn28a6708d4c15")
                        .build()
                    chain.proceed(request)
                }
                .build()
        )
        .build()
        .create(CountryAPI::class.java)


    fun getData(): Single<CountryData> {
        return api.getCountries()
    }

    fun getDailyStatistics(): Single<DailyData> {
        return api.getStatistics()
    }
    fun getDailyHistory(selectedDate: String): Single<DailyData> {
        return api.getHistory(selectedDate)
    }
    }
