package com.example.countriesoftheworld.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LanguageData(
    val code: String,
    val name: String,
    val native: String,
)
