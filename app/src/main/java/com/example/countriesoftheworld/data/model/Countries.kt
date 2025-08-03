package com.example.countriesoftheworld.data.model

class Countries : ArrayList<CountriesItem>()

data class CountriesItem(
    val alpha2Code: String,
    val alpha3Code: String,
    val altSpellings: List<String>,
    val area: Double,
    val borders: List<String>,
    val callingCodes: List<String>,
    val capital: String,
    val cioc: String,
    val currencies: List<Currency>,
    val demonym: String,
    val flag: String,
    val flags: Flags,
    val gini: Double,
    val independent: Boolean,
    val languages: List<Language>,
    val latlng: List<Double>,
    val name: String,
    val nativeName: String,
    val numericCode: String,
    val population: Int,
    val region: String,
    val regionalBlocs: List<RegionalBloc>,
    val subregion: String,
    val timezones: List<String>,
    val topLevelDomain: List<String>,
    val translations: Translations,
)

data class Currency(
    val code: String,
    val name: String,
    val symbol: String,
)

data class Flags(
    val png: String,
    val svg: String,
)

data class Language(
    val iso639_1: String,
    val iso639_2: String,
    val name: String,
    val nativeName: String,
)

data class RegionalBloc(
    val acronym: String,
    val name: String,
    val otherAcronyms: List<String>,
    val otherNames: List<String>,
)

data class Translations(
    val br: String,
    val de: String,
    val es: String,
    val fa: String,
    val fr: String,
    val hr: String,
    val hu: String,
    val `it`: String,
    val ja: String,
    val nl: String,
    val pt: String,
)
