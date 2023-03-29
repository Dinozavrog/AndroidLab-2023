package com.example.androidsecondsem.presentation.utils

import android.app.Application
import com.example.androidsecondsem.BuildConfig
import com.example.androidsecondsem.di.AppComponent
import com.example.androidsecondsem.di.Container
import com.example.androidsecondsem.di.DaggerAppComponent
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        Container.provideFusedLocation(applicationContext = this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        appComponent = DaggerAppComponent.builder()
            .context(applicationContext)
            .build()

    }
    companion object {
        lateinit var appComponent: AppComponent
    }
}
