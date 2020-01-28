package com.androidapp.trendingrepoclient

import android.app.Application
import com.androidapp.trendingrepoclient.di.module
import org.koin.android.ext.android.startKoin

/**
 * Created by S.Nur Uysal on 2020-01-26.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(module))
    }
}