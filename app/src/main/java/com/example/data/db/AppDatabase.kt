package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ImportInvoiceEntity::class,
        SalesInvoiceEntity::class,
        CustomerDebtEntity::class,
        FarmerReceivableEntity::class,
        ExpenseItemEntity::class,
        LossItemEntity::class,
        AppLogEntity::class,
        AlwaSettingsEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun importInvoiceDao(): ImportInvoiceDao
    abstract fun salesInvoiceDao(): SalesInvoiceDao
    abstract fun customerDebtDao(): CustomerDebtDao
    abstract fun farmerReceivableDao(): FarmerReceivableDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun lossDao(): LossDao
    abstract fun appLogDao(): AppLogDao
    abstract fun alwaSettingsDao(): AlwaSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "alwa_database"
                )
                .fallbackToDestructiveMigration(true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
