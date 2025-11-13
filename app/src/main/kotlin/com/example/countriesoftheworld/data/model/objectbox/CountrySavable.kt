package com.example.countriesoftheworld.data.model.objectbox

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class CountrySavable(
    @Id var id: Long = 0,
    var name: String? = null,
    var age: Int = 0,
)
