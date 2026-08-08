package com.example

import android.app.Application
import com.example.data.db.AppDatabase
import com.example.data.repository.AlwaRepository

class AlwaApplication : Application() {

    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { AlwaRepository(database) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: AlwaApplication
            private set
    }
}
