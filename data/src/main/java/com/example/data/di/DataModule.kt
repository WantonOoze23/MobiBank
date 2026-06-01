package com.example.data.di

import com.example.data.local.datastore.TokenManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { TokenManager(androidContext()) }


}