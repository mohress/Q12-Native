package com.example.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ImportInvoiceDao {
    @Query("SELECT * FROM import_invoices ORDER BY id DESC")
    fun getAll(): Flow<List<ImportInvoiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(invoice: ImportInvoiceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(invoices: List<ImportInvoiceEntity>)

    @Query("DELETE FROM import_invoices WHERE id = :id")
    suspend fun deleteById(id: String)
}

@Dao
interface SalesInvoiceDao {
    @Query("SELECT * FROM sales_invoices ORDER BY id DESC")
    fun getAll(): Flow<List<SalesInvoiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(invoice: SalesInvoiceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(invoices: List<SalesInvoiceEntity>)

    @Query("UPDATE sales_invoices SET isPrinted = 1 WHERE id = :id")
    suspend fun markPrinted(id: String)

    @Query("DELETE FROM sales_invoices WHERE id = :id")
    suspend fun deleteById(id: String)
}

@Dao
interface CustomerDebtDao {
    @Query("SELECT * FROM customer_debts ORDER BY id DESC")
    fun getAll(): Flow<List<CustomerDebtEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(debt: CustomerDebtEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(debts: List<CustomerDebtEntity>)

    @Query("DELETE FROM customer_debts WHERE customerName = :customerName")
    suspend fun deleteByCustomerName(customerName: String)

    @Query("UPDATE customer_debts SET totalDebtIQD = :newAmount WHERE customerName = :customerName")
    suspend fun updateDebtAmount(customerName: String, newAmount: Long)
}

@Dao
interface FarmerReceivableDao {
    @Query("SELECT * FROM farmer_receivables ORDER BY id DESC")
    fun getAll(): Flow<List<FarmerReceivableEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(receivable: FarmerReceivableEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(receivables: List<FarmerReceivableEntity>)

    @Query("DELETE FROM farmer_receivables WHERE id = :id")
    suspend fun deleteById(id: String)
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAll(): Flow<List<ExpenseItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<ExpenseItemEntity>)
}

@Dao
interface LossDao {
    @Query("SELECT * FROM losses ORDER BY id DESC")
    fun getAll(): Flow<List<LossItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(loss: LossItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(losses: List<LossItemEntity>)
}

@Dao
interface AppLogDao {
    @Query("SELECT * FROM logs ORDER BY id DESC")
    fun getAll(): Flow<List<AppLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: AppLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<AppLogEntity>)
}

@Dao
interface AlwaSettingsDao {
    @Query("SELECT * FROM alwa_settings WHERE id = 1")
    fun getSettings(): Flow<AlwaSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSettings(settings: AlwaSettingsEntity)
}
