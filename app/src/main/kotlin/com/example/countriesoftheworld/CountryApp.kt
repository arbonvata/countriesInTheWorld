package com.example.countriesoftheworld

import android.app.Application
import com.example.countriesoftheworld.data.model.objectbox.ObjectBox

// import io.realm.gradle
class CountryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }
}
