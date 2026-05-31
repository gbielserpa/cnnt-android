package com.cnnt.app

import android.app.Application
import com.cnnt.app.data.dao.CnntDatabase
import com.cnnt.app.data.repository.CnntRepository

class CnntApplication : Application() {

    val database: CnntDatabase by lazy { CnntDatabase.getDatabase(this) }
    val repository: CnntRepository by lazy { CnntRepository(database) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CnntApplication
            private set
    }
}
