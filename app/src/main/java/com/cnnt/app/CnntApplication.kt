package com.cnnt.app

import android.app.Application
import android.util.Log
import com.cnnt.app.data.dao.CnntDatabase
import com.cnnt.app.data.repository.CnntRepository

class CnntApplication : Application() {

    val database: CnntDatabase by lazy { CnntDatabase.getDatabase(this) }
    val repository: CnntRepository by lazy { CnntRepository(database) }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Global exception handler to prevent silent crashes
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("CNNT", "Uncaught exception in ${thread.name}", throwable)
            // Let the default handler run (shows crash dialog or restarts)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    companion object {
        lateinit var instance: CnntApplication
            private set
    }
}
