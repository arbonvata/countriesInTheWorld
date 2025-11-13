package com.example.countriesoftheworld.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesoftheworld.data.model.CountryItem
import com.example.countriesoftheworld.data.repository.AllCountriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
        private val countriesRepository: AllCountriesRepository,
    ) : ViewModel() {
        private val _allCountriesState =
            MutableStateFlow<AllCountriesUiState>(AllCountriesUiState.Loading)
        val allCountriesState: StateFlow<AllCountriesUiState> = _allCountriesState.asStateFlow()

        private val _singleCountryState =
            MutableStateFlow<SingleCountryUiState>(SingleCountryUiState.Loading)
        val singleCountryState: StateFlow<SingleCountryUiState> = _singleCountryState.asStateFlow()

        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

        val filteredCountriesState: StateFlow<AllCountriesUiState> =
            allCountriesState
                .combine(searchQuery) { state, query ->
                    when (state) {
                        is AllCountriesUiState.Success -> {
                            val filteredCountries =
                                state.countries.filter {
                                    it.name?.contains(query, ignoreCase = true) == true
                                }
                            AllCountriesUiState.Success(filteredCountries)
                        }
                        else -> state
                    }
                }.stateIn(viewModelScope, SharingStarted.Lazily, AllCountriesUiState.Loading)

        fun onSearchQueryChanged(query: String) {
            _searchQuery.value = query
        }

        fun fetchAllCountries() {
            viewModelScope.launch {
                _allCountriesState.value = AllCountriesUiState.Loading
                runCatching {
                    countriesRepository.getAllCountries()
                }.onSuccess { countries ->
                    _allCountriesState.value = AllCountriesUiState.Success(countries = countries)
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
                }.onSuccess { countries ->
                    _singleCountryState.value =
                        if (countries.isNotEmpty()) {
                            SingleCountryUiState.Success(countries[0])
                        } else {
                            SingleCountryUiState.NotFound
                        }
                }.onFailure { throwable ->
                    _singleCountryState.value =
                        if (throwable is ClientRequestException && throwable.response.status.value == 404) {
                            SingleCountryUiState.NotFound
                        } else {
                            SingleCountryUiState.Error(
                                throwable.message ?: "Failed to load details for $name",
                            )
                        }
                }
            }
        }

        fun saveCountryToDatabase(country: com.example.countriesoftheworld.data.model.Country) {
            // Empty method to be implemented later when database is set up
            // This method will save the selected country to the database
        }
    }
