package com.example.countriesoftheworld.data.model

data class Country(
    val name: String,
    val flagUrl: String,
    val isVisited: Boolean = false,
)
