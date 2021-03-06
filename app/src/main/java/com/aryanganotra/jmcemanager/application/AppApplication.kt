package com.aryanganotra.jmcemanager.application

import android.app.Application

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        var context: AppApplication? = null
    }

    fun getContext(): AppApplication? {
        return context
    }
}