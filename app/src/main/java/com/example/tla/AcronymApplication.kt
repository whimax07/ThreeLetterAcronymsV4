package com.example.tla

import android.app.Application
import com.example.tla.data.AppContainer
import com.example.tla.data.AppDataContainer

class AcronymApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}