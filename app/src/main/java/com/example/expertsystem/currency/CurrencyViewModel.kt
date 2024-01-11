package com.example.expertsystem.currency

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyViewModel : ViewModel() {

    private val _currencyList = MutableLiveData<List<Currency>>()
    val currencyList: LiveData<List<Currency>> get() = _currencyList

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://apilayer.net/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(CurrencyApiService::class.java)
    private val apiKey = "113f31aaa5dc6ba020a51cd33336e08f"

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            while (true) {
                try {
                    val response = apiService.getCurrencyRates(
                        apiKey,
                        "EUR,GBP,CAD,PLN,RUB",
                        "USD",
                        1
                    )

                    if (response.success) {
                        val currencies = response.quotes.map { Currency(it.key, it.value) }
                        _currencyList.postValue(currencies)
                    } else {
                        // Обработка ошибок, например, вывод ошибки в лог
                        println("API Error: ${response.error?.info}")
                    }

                } catch (e: Exception) {
                    // Обработка ошибок, например, вывод ошибки в лог
                    println("Exception: ${e.message}")
                }

                delay(10000) // задержка для имитации обновления в реальном времени
            }
        }
    }
}
