package com.example.countriesoftheworld.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.countriesoftheworld.data.model.Country
import com.example.countriesoftheworld.presentation.viewmodel.AllCountriesUiState
import com.example.countriesoftheworld.presentation.viewmodel.CountryViewModel
import com.example.countriesoftheworld.ui.theme.CountriesOfTheWorldTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllCountries(
    modifier: Modifier = Modifier,
    countryViewModel: CountryViewModel = hiltViewModel(),
    navController: NavController? = null,
) {
    val countriesState by countryViewModel.filteredCountriesState.collectAsState()
    val searchQuery by countryViewModel.searchQuery.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // 2. Add state to track the current selected route for the drawer
    var currentRoute by remember { mutableStateOf(AppRoutes.ALL_COUNTRIES) }

    LaunchedEffect(Unit) {
        countryViewModel.fetchAllCountries()
    }

    // 3. Pass new parameters to the Navigation Drawer
    AllCountriesNavigationDrawer(
        drawerState = drawerState,
        scope = scope,
        currentRoute = currentRoute,
        onNavigate = { route ->
            currentRoute = route // Update the selected item
            // Here you can add logic to filter the list based on the route, e.g.:
            // countryViewModel.setFilter(route)
            // Or navigate to a different screen if you have separate composables for each
            scope.launch {
                drawerState.close()
            }
        },
    ) {
        Scaffold(
            topBar = {
                AllCountriesTopAppBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                )
            },
        ) { innerPadding ->
            Column(modifier = modifier.padding(innerPadding)) {
                TextField(
                    value = searchQuery,
                    onValueChange = { countryViewModel.onSearchQueryChanged(it) },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    placeholder = { Text("Search for a country") },
                )
                when (val state = countriesState) {
                    is AllCountriesUiState.Success -> {
                        val allCountries = state.countries.toCountries()
                        AllCountriesList(
                            modifier = Modifier,
                            countries = allCountries,
                            onCountryClicked = { countryName ->
                                navController?.navigate("countryInfo/$countryName")
                            },
                        )
                    }

                    is AllCountriesUiState.Error -> {
                        ErrowWhenFetching(modifier, innerPadding, countryViewModel)
                    }

                    is AllCountriesUiState.Loading -> {
                        Box(
                            modifier =
                                modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

private fun List<com.example.countriesoftheworld.data.model.CountryItem>.toCountries(): List<Country> =
    this.mapNotNull { countryItem ->
        countryItem.name?.let { name ->
            countryItem.flag?.let { flag ->
                Country(name = name, flagUrl = flag)
            }
        }
    }

@Composable
private fun ErrowWhenFetching(
    modifier: Modifier,
    innerPadding: PaddingValues,
    countryViewModel: CountryViewModel,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(innerPadding),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Error loading countries. Please check your connection and try again.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = { countryViewModel.fetchAllCountries() },
            ) {
                Text("Retry")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllCountriesTopAppBar(onMenuClick: () -> Unit = {}) {
    TopAppBar(
        title = {
            Text(
                text = "All Countries",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllCountriesNavigationDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope,
    // 4. Accept currentRoute and a navigation callback
    currentRoute: String,
    onNavigate: (String) -> Unit,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Countries of the World",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp),
                )
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // 5. Update NavigationDrawerItems to be dynamic
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Public, contentDescription = "All Countries") },
                    label = { Text("All Countries") },
                    selected = currentRoute == AppRoutes.ALL_COUNTRIES,
                    onClick = { onNavigate(AppRoutes.ALL_COUNTRIES) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Visited by me") },
                    label = { Text("Visited by me") },
                    selected = currentRoute == AppRoutes.VISITED_COUNTRIES,
                    onClick = { onNavigate(AppRoutes.VISITED_COUNTRIES) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.HighlightOff, contentDescription = "Not visited by me") },
                    label = { Text("Not visited by me") },
                    selected = currentRoute == AppRoutes.NOT_VISITED_COUNTRIES,
                    onClick = { onNavigate(AppRoutes.NOT_VISITED_COUNTRIES) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Public, contentDescription = "List of continents") },
                    label = { Text("Continents") },
                    selected = currentRoute == AppRoutes.CONTINENTS,
                    onClick = { onNavigate(AppRoutes.CONTINENTS) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                )
            }
        },
    ) {
        content()
    }
}

@Composable
private fun AllCountriesList(
    modifier: Modifier,
    countries: List<Country>,
    onCheckedChange: (Boolean) -> Unit = {},
    onCountryClicked: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items = countries) { country ->

            CountryWithFlag(
                country = country,
                modifier = Modifier.padding(8.dp),
                onCheckedChange = onCheckedChange,
                onCountryClicked = onCountryClicked,
            )
        }
    }
}

@Composable
fun CountryWithFlag(
    modifier: Modifier,
    country: Country,
    onCheckedChange: (Boolean) -> Unit = {},
    onCountryClicked: (country: String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }

    Row(
        modifier =
            modifier
                .clickable { onCountryClicked(country.name) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = country.flagUrl,
            contentDescription = "Flag of ${country.name}",
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(44.dp)
                    .aspectRatio(1f),
        )

        Text(
            text = country.name,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
        // Add a spacer to push the icon to the right
        Spacer(modifier = Modifier.weight(1f))
        // Add the three-dot icon
        CountryOptionsMenu(
            expanded = expanded,
            checked = checked,
            onExpandedChange = { expanded = it },
            onCheckedChange = { newState ->
                checked = newState
                onCheckedChange(checked)
            },
        )
    }
}

@Composable
fun CountryOptionsMenu(
    expanded: Boolean,
    checked: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
) {
    Box {
        IconButton(onClick = { onExpandedChange(true) }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
        ) {
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { newState ->
                                onCheckedChange(newState)
                                onExpandedChange(false) // Close menu after selection
                            },
                            modifier = Modifier.padding(end = 8.dp),
                        )
                        Text(text = "Select Country")
                    }
                },
                onClick = { /* Handled by Checkbox */ },
            )
        }
    }
}

@Composable
@Preview(showBackground = true, name = "Country with flag")
fun CountryWithFlagPreview() {
    CountriesOfTheWorldTheme {
        CountryWithFlag(
            country =
                Country(
                    "United States",
                    "https://flagcdn.com/w320/us.png",
                ),
            modifier = Modifier.padding(8.dp),
            onCountryClicked = {
            },
        )
    }
}

@Preview(showBackground = true, name = "All countries")
@Composable
fun AllCountriesPreview() {
    val sampleCountries =
        listOf(
            Country(name = "United States", flagUrl = "https://flagcdn.com/w320/us.png"),
            Country(name = "Canada", flagUrl = "https://flagcdn.com/w320/ca.png"),
            Country(name = "Mexico", flagUrl = "https://flagcdn.com/w320/mx.png"),
            Country(name = "Brazil", flagUrl = "https://flagcdn.com/w320/br.png"),
            Country(name = "Argentina", flagUrl = "https://flagcdn.com/w320/ar.png"),
            Country(name = "United Kingdom", flagUrl = "https://flagcdn.com/w320/gb.png"),
            Country(name = "Germany", flagUrl = "https://flagcdn.com/w320/de.png"),
            Country(name = "France", flagUrl = "https://flagcdn.com/w320/fr.png"),
            Country(name = "Spain", flagUrl = "https://flagcdn.com/w320/es.png"),
            Country(name = "Italy", flagUrl = "https://flagcdn.com/w320/it.png"),
            Country(name = "Japan", flagUrl = "https://flagcdn.com/w320/jp.png"),
            Country(name = "China", flagUrl = "https://flagcdn.com/w320/cn.png"),
            Country(name = "India", flagUrl = "https://flagcdn.com/w320/in.png"),
            Country(name = "Australia", flagUrl = "https://flagcdn.com/w320/au.png"),
            Country(name = "New Zealand", flagUrl = "https://flagcdn.com/w320/nz.png"),
            Country(name = "South Africa", flagUrl = "https://flagcdn.com/w320/za.png"),
            Country(name = "Egypt", flagUrl = "https://flagcdn.com/w320/eg.png"),
            Country(name = "Nigeria", flagUrl = "https://flagcdn.com/w320/ng.png"),
            Country(name = "Kenya", flagUrl = "https://flagcdn.com/w320/ke.png"),
            Country(name = "Russia", flagUrl = "https://flagcdn.com/w320/ru.png"),
        )
    CountriesOfTheWorldTheme {
        AllCountriesList(
            modifier = Modifier,
            countries = sampleCountries,
            onCheckedChange = {},
            onCountryClicked = {},
        )
    }
}
