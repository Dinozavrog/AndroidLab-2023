package com.example.androidsecondsem.di

import android.content.Context
import com.example.androidsecondsem.presentation.fragments.CityFragment
import com.example.androidsecondsem.presentation.fragments.SearchFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [WeatherModule::class, NetworkModule::class, LocationModule::class])
@Singleton
interface AppComponent {

    fun injectSearchFragment(searchFragment: SearchFragment)

    fun injectCityFragment(cityFragment: CityFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}
