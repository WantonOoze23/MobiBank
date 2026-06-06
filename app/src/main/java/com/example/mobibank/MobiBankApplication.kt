package com.example.mobibank

import android.app.Application
import com.example.data.di.dataModule
import com.example.mobibank.di.appModule
import com.example.mobibank.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MobiBankApplication : Application() {
     override fun onCreate() {
         super.onCreate()

         startKoin {
             androidLogger()
             androidContext(this@MobiBankApplication)
             modules(
                 listOf(appModule, domainModule, dataModule)
             )
         }
     }
}