package com.example.countriesoftheworld.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesoftheworld.data.model.CountryItem
import com.example.countriesoftheworld.data.repository.AllCountriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CountriesByLanguageUiState(
    val countries: List<CountryItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class CountriesByLanguageViewModel
    @Inject
    constructor(
        private val countriesRepository: AllCountriesRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CountriesByLanguageUiState())
        val uiState: StateFlow<CountriesByLanguageUiState> = _uiState.asStateFlow()

        private var currentLanguageCode: String = ""

        fun loadCountriesByLanguage(languageCode: String) {
            // Skip if already loading or same language
            if (_uiState.value.isLoading || currentLanguageCode == languageCode) {
                return
            }

            currentLanguageCode = languageCode

            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                try {
                    val countries = countriesRepository.getCountriesByLanguage(languageCode)
                    _uiState.value =
                        _uiState.value.copy(
                            countries = countries,
                            isLoading = false,
                            error = null,
                        )
                } catch (e: Exception) {
                    _uiState.value =
                        _uiState.value.copy(
                            countries = emptyList(),
                            isLoading = false,
                            error = e.message ?: "Unknown error occurred",
                        )
                }
            }
        }
    }
