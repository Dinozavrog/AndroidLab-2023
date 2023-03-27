package com.example.androidsecondsem.presentation.utils

import android.app.Application
import com.example.androidsecondsem.BuildConfig
import com.example.androidsecondsem.data.response.Container
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        Container.provideFusedLocation(applicationContext = this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
