package com.septianen.imagemachine

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MachineApplication: Application() {

    override fun onCreate() {
        super.onCreate()

    }
}