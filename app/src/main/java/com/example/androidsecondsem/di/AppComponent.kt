package com.example.androidsecondsem.di

import android.app.Application
import android.content.Context
import com.example.androidsecondsem.presentation.fragments.CityFragment
import com.example.androidsecondsem.presentation.fragments.SearchFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppComponent {

    @Provides
    fun provideContext(app:Application): Context = app.applicationContext

    @Provides
    fun provideSearchFragment(): SearchFragment = SearchFragment()

    @Provides
    fun provideCityFragment(): CityFragment = CityFragment()

}
