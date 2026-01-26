package com.example.countriesoftheworld.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesoftheworld.data.model.LanguageData
import com.example.countriesoftheworld.data.repository.LanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel
    @Inject
    constructor(
        private val languageRepository: LanguageRepository,
    ) : ViewModel() {
        private val _languages = MutableStateFlow<List<LanguageData>>(emptyList())
        val languages: StateFlow<List<LanguageData>> = _languages.asStateFlow()
        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

        val filteredLanguages: StateFlow<List<LanguageData>> =
            combine(
                _languages,
                _searchQuery,
            ) { languages, query ->
                if (query.isBlank()) {
                    languages
                } else {
                    languages.filter {
                        it.name.contains(query, ignoreCase = true) ||
                            it.code.contains(query, ignoreCase = true) ||
                            it.native.contains(query, ignoreCase = true)
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

        init {
            loadLanguages()
        }

        private fun loadLanguages() {
            viewModelScope.launch {
                _languages.value = languageRepository.getLanguages()
            }
        }

        fun onSearchQueryChanged(newQuery: String) {
            _searchQuery.value = newQuery
        }
    }
