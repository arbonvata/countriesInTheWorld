package com.example.countriesoftheworld.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesoftheworld.data.model.Country
import com.example.countriesoftheworld.data.model.CountryItem
import com.example.countriesoftheworld.data.model.flagImageUrl
import com.example.countriesoftheworld.data.model.objectbox.CountrySavable
import com.example.countriesoftheworld.data.model.objectbox.CountrySavable_
import com.example.countriesoftheworld.data.repository.AllCountriesRepository
import com.example.countriesoftheworld.presentation.compose.Continent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ClientRequestException
import io.objectbox.Box
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
        val countries: List<Country>,
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
        private val countryBox: Box<CountrySavable>,
    ) : ViewModel() {
        private val _allCountriesState =
            MutableStateFlow<AllCountriesUiState>(AllCountriesUiState.Loading)
        val allCountriesState: StateFlow<AllCountriesUiState> = _allCountriesState.asStateFlow()

        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

        private val _visitedFilter = MutableStateFlow<Boolean?>(null)
        val visitedFilter: StateFlow<Boolean?> = _visitedFilter.asStateFlow()

        private val _singleCountryState =
            MutableStateFlow<SingleCountryUiState>(SingleCountryUiState.Loading)
        val singleCountryState: StateFlow<SingleCountryUiState> = _singleCountryState.asStateFlow()

        val filteredCountriesState: StateFlow<AllCountriesUiState> =
            allCountriesState
                .combine(searchQuery) { state, query ->
                    when (state) {
                        is AllCountriesUiState.Success -> {
                            val filteredCountries =
                                state.countries.filter {
                                    it.name.contains(query, ignoreCase = true)
                                }
                            AllCountriesUiState.Success(filteredCountries)
                        }
                        else -> state
                    }
                }.combine(visitedFilter) { state, visited ->
                    when (state) {
                        is AllCountriesUiState.Success -> {
                            val filteredCountries =
                                state.countries.filter {
                                    visited == null || it.isVisited == visited
                                }
                            AllCountriesUiState.Success(filteredCountries)
                        }
                        else -> state
                    }
                }.stateIn(viewModelScope, SharingStarted.Lazily, AllCountriesUiState.Loading)

        fun onSearchQueryChanged(query: String) {
            _searchQuery.value = query
        }

        fun setVisitedFilter(filter: Boolean?) {
            _visitedFilter.value = filter
        }

        fun fetchAllCountries(continent: Continent? = null) {
            viewModelScope.launch {
                _allCountriesState.value = AllCountriesUiState.Loading
                runCatching {
                    val countryItems = countriesRepository.getAllCountries(continent)

                    // Fetch all visited names from the database once
                    val query = countryBox.query(CountrySavable_.visitedByMe.equal(true)).build()
                    val visitedNames = query.find().mapNotNull { it.name }.toSet()
                    query.close()

                    countryItems.mapNotNull { item ->
                        item.name?.let { name ->
                            Country(
                                name = name,
                                flagUrl = item.flagImageUrl(),
                                isVisited = visitedNames.contains(name),
                            )
                        }
                    }
                }.onSuccess { mappedCountries ->
                    _allCountriesState.value = AllCountriesUiState.Success(countries = mappedCountries)
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

        fun toggleCountryVisited(
            country: Country,
            isVisited: Boolean,
        ) {
            viewModelScope.launch {
                val query = countryBox.query(CountrySavable_.name.equal(country.name)).build()
                val existing = query.findFirst()

                if (existing != null) {
                    existing.visitedByMe = isVisited
                    countryBox.put(existing)
                } else {
                    countryBox.put(CountrySavable(name = country.name, visitedByMe = isVisited))
                }
                query.close()

                // Update the state locally to avoid a full refresh if possible,
                // but for now, re-fetching or updating the current list is simpler.
                // To be reactive, we could use ObjectBox Flow/LiveData, but let's just update the state here.
                val currentState = _allCountriesState.value
                if (currentState is AllCountriesUiState.Success) {
                    val updatedList =
                        currentState.countries.map {
                            if (it.name == country.name) it.copy(isVisited = isVisited) else it
                        }
                    _allCountriesState.value = AllCountriesUiState.Success(updatedList)
                }
            }
        }

        fun isCountryVisited(countryName: String): Boolean {
            val query = countryBox.query(CountrySavable_.name.equal(countryName)).build()
            val country = query.findFirst()
            val result = country?.visitedByMe ?: false
            query.close()
            return result
        }
    }
