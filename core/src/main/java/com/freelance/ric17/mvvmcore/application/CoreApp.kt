package com.freelance.ric17.mvvmcore.application

import android.app.Application
import com.facebook.stetho.Stetho
import com.freelance.ric17.mvvmcore.BuildConfig
import com.freelance.ric17.mvvmcore.di.AppModule
import com.freelance.ric17.mvvmcore.di.CoreComponent
import com.freelance.ric17.mvvmcore.di.DaggerCoreComponent

open class CoreApp : Application() {

    companion object {
        lateinit var coreComponent : CoreComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
        initStetho()
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }

    private fun initDI() {
        coreComponent = DaggerCoreComponent.builder().appModule(AppModule(this)).build()
    }


}