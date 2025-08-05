package com.example.countriesoftheworld.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesoftheworld.data.model.Countries
import com.example.countriesoftheworld.data.model.Country
import com.example.countriesoftheworld.data.repository.AllCountriesRepository
import io.ktor.client.plugins.ClientRequestException // Ktor exception for 404
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// UI State sealed interfaces remain the same
sealed interface AllCountriesUiState {
    object Loading : AllCountriesUiState

    data class Success(
        val countries: Countries,
    ) : AllCountriesUiState

    data class Error(
        val message: String,
    ) : AllCountriesUiState
}

sealed interface SingleCountryUiState {
    object Loading : SingleCountryUiState

    data class Success(
        val country: Country,
    ) : SingleCountryUiState

    data class Error(
        val message: String,
    ) : SingleCountryUiState

    object NotFound : SingleCountryUiState
}

class CountryViewModel(
    private val countriesRepository: AllCountriesRepository,
) : ViewModel() {
    private val _allCountriesState = MutableStateFlow<AllCountriesUiState>(AllCountriesUiState.Loading)
    val allCountriesState: StateFlow<AllCountriesUiState> = _allCountriesState.asStateFlow()

    private val _singleCountryState = MutableStateFlow<SingleCountryUiState>(SingleCountryUiState.Loading)
    val singleCountryState: StateFlow<SingleCountryUiState> = _singleCountryState.asStateFlow()

    init {
        fetchAllCountries()
    }

    fun fetchAllCountries() {
        viewModelScope.launch {
            _allCountriesState.value = AllCountriesUiState.Loading
            runCatching {
                // Use runCatching to get a kotlin.Result
                countriesRepository.getAllCountries()
            }.onSuccess { countries ->
                _allCountriesState.value = AllCountriesUiState.Success(countries)
            }.onFailure { throwable ->
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
                _singleCountryState.value = SingleCountryUiState.Success(country)
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
