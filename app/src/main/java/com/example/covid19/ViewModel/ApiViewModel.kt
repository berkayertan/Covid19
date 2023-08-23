    package com.example.covid19.ViewModel

    import android.app.DatePickerDialog
    import android.content.Context
    import android.content.Intent
    import android.util.Log
    import android.widget.TextView
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
    import java.util.Calendar

    class ApiViewModel : ViewModel() {

        private val TAG = "ApiViewModel"
        private val countriesService = Countries()
        private val disposable = CompositeDisposable()
        val detailedData = MutableLiveData<Response>()

        val combinedData = MutableLiveData<List<Pair<String, Response>>>() // Use Pair to hold country name and response
        val historyList = MutableLiveData<List<String>>()

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
        fun getDailyHistory(selectedDate: String) {
            disposable.add(
                countriesService.getDailyHistory(selectedDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ dailyData ->
                        val historyList = dailyData.response.map { it.day }
                        this.historyList.value = historyList // Update LiveData with history data
                    }, { error ->
                        error.printStackTrace()
                    })
            )
        }

        fun fetchDetailedDataForCountry(country: String) {
            disposable.add(
                countriesService.getDetailedData(country)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ detailedResponse ->
                        detailedData.value = detailedResponse
                    }, { error ->
                        Log.e(TAG, "Error fetching detailed data for $country: ${error.message}")
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



        fun showDatePickerDialog(context: Context, editText: TextView) {
            Log.d("ApiViewModel", "showDatePickerDialog called")

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear)
                    editText.setText(formattedDate)
                    Log.d("ApiViewModel", "Selected date: $formattedDate")
                },
                year, month, day
            )

            datePickerDialog.show()
        }
        fun shareDetails(context: Context, response: Response) {
            val stringBuilder = StringBuilder()

            // Append the text from each property to the StringBuilder
            stringBuilder.append("Country: ${response.country}\n")
            response.cases?.let { cases ->
                stringBuilder.append("Cases: ${cases.new}\n")
            }
            response.deaths?.let { deaths ->
                stringBuilder.append("Deaths: ${deaths.total}\n")
            }
            response.tests?.let { tests ->
                stringBuilder.append("Tests: ${tests.total}\n")
            }

            val detailsText = stringBuilder.toString()

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, detailsText)
            context.startActivity(Intent.createChooser(intent, "Share Details"))
        }


        override fun onCleared() {
            super.onCleared()
            disposable.clear()
        }
    }
