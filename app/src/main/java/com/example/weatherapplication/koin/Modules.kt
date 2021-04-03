package com.example.weatherapplication.koin

import org.koin.dsl.module

val viewModelModule = module {
    single {
        WeatherViewModel()
    }
}