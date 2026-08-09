package com.example

import android.app.Application
import android.util.Log
import com.example.data.db.AppDatabase
import com.example.data.repository.AlwaRepository

class AlwaApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var repository: AlwaRepository
        private set

    val safeRepository: AlwaRepository
        get() {
            if (!::repository.isInitialized) {
                database = AppDatabase.getInstance(this)
                repository = AlwaRepository(database)
            }
            return repository
        }

    override fun onCreate() {
        super.onCreate()
        instance = this

        try {
            database = AppDatabase.getInstance(this)
            repository = AlwaRepository(database)
        } catch (e: Exception) {
            Log.e("AlwaApplication", "Failed to initialize Database/Repository", e)
        }

        try {
            val clazz = Class.forName("com.google.firebase.FirebaseApp")
            val method = clazz.getMethod("initializeApp", android.content.Context::class.java)
            method.invoke(null, this)
        } catch (e: ClassNotFoundException) {
            Log.d("AlwaApplication", "Firebase SDK not included, offline mode active")
        } catch (e: Throwable) {
            Log.w("AlwaApplication", "Firebase initialization omitted", e)
        }
    }

    companion object {
        lateinit var instance: AlwaApplication
            private set
    }
}
