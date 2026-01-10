package com.example.countriesoftheworld

import android.app.Application
import com.example.countriesoftheworld.data.model.objectbox.ObjectBox
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CountryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }
}
