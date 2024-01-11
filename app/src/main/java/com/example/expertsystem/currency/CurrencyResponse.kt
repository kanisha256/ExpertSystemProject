package com.example.expertsystem.currency

import com.example.expertsystem.currency.CurrencyError

data class CurrencyResponse(
    val success: Boolean,
    val quotes: Map<String, Double>,
    val error: CurrencyError?
)