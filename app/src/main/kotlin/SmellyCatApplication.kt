package com.martafoderaro.smellycat

import android.app.Application
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.util.TimberTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SmellyCatApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeLogger()
    }

    private fun initializeLogger() {
        Timber.plant(when {
            BuildConfig.DEBUG -> Timber.DebugTree()
            else -> TimberTree()
        })
    }
}