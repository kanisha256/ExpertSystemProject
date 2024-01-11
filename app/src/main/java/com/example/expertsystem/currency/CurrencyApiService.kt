package com.example.expertsystem.currency

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("live")
    suspend fun getCurrencyRates(
        @Query("access_key") accessKey: String,
        @Query("currencies") currencies: String,
        @Query("source") source: String,
        @Query("format") format: Int
    ): CurrencyResponse
}
