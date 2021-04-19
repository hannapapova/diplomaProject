package com.example.weatherapplication.koin

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val viewModelModule = module {
    single {
        WeatherViewModel(androidApplication())
    }
}