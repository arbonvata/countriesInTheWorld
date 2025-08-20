package com.example.countriesoftheworld.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesoftheworld.data.model.CountryItem
import com.example.countriesoftheworld.data.repository.AllCountriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AllCountriesUiState {
    object Loading : AllCountriesUiState

    data class Success(
        val countries: List<CountryItem>,
    ) : AllCountriesUiState

    data class Error(
        val message: String,
    ) : AllCountriesUiState
}

sealed interface SingleCountryUiState {
    object Loading : SingleCountryUiState

    data class Success(
        val country: CountryItem,
    ) : SingleCountryUiState

    data class Error(
        val message: String,
    ) : SingleCountryUiState

    object NotFound : SingleCountryUiState
}

@HiltViewModel
class CountryViewModel
    @Inject
    constructor(
        // Constructor injection
        private val countriesRepository: AllCountriesRepository,
    ) : ViewModel() {
        private val _allCountriesState =
            MutableStateFlow<AllCountriesUiState>(AllCountriesUiState.Loading)
        val allCountriesState: StateFlow<AllCountriesUiState> = _allCountriesState.asStateFlow()

        private val _singleCountryState =
            MutableStateFlow<SingleCountryUiState>(SingleCountryUiState.Loading)
        val singleCountryState: StateFlow<SingleCountryUiState> = _singleCountryState.asStateFlow()

        fun fetchAllCountries() {
            viewModelScope.launch {
                _allCountriesState.value = AllCountriesUiState.Loading
                runCatching {
                    // Use runCatching to get a kotlin.Result
                    countriesRepository.getAllCountries()
                }.onSuccess { countries ->
                    Log.d("ArbonVata", "onSuccess: $countries")
                    _allCountriesState.value = AllCountriesUiState.Success(countries = countries)
                }.onFailure { throwable ->
                    Log.d("ArbonVata", "onError: ${throwable.message}")
                    _allCountriesState.value =
                        AllCountriesUiState.Error(
                            throwable.message ?: "Failed to load countries",
                        )
                }
            }
        }

        fun fetchCountryByName(name: String) {
            viewModelScope.launch {
                _singleCountryState.value = SingleCountryUiState.Loading
                runCatching {
                    countriesRepository.getCountry(name)
                }.onSuccess { country ->
                    _singleCountryState.value = SingleCountryUiState.Success(country[0])
                }.onFailure { throwable ->
                    if (throwable is ClientRequestException && throwable.response.status.value == 404) {
                        _singleCountryState.value = SingleCountryUiState.NotFound
                    } else {
                        _singleCountryState.value =
                            SingleCountryUiState.Error(
                                throwable.message ?: "Failed to load details for $name",
                            )
                    }
                }
            }
        }

        fun clearSingleCountryState() {
            _singleCountryState.value = SingleCountryUiState.Loading
        }
    }
