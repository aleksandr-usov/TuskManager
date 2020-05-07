package com.example.tuskmanager

import android.app.Application
import com.example.tuskmanager.di.AppComponent
import com.example.tuskmanager.di.DaggerAppComponent
import com.facebook.stetho.Stetho

class TaskApplication : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        component = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}