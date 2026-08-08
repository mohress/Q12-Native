package com.example.data.repository

import com.example.data.SampleDataGenerator
import com.example.data.db.*
import com.example.data.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class AlwaRepository(private val database: AppDatabase) {

    private val importDao = database.importInvoiceDao()
    private val salesDao = database.salesInvoiceDao()
    private val debtDao = database.customerDebtDao()
    private val receivableDao = database.farmerReceivableDao()
    private val expenseDao = database.expenseDao()
    private val lossDao = database.lossDao()
    private val logDao = database.appLogDao()
    private val settingsDao = database.alwaSettingsDao()

    val importInvoices: Flow<List<ImportInvoice>> = importDao.getAll().map { list ->
        list.map { it.toDomain() }
    }

    val salesInvoices: Flow<List<SalesInvoice>> = salesDao.getAll().map { list ->
        list.map { it.toDomain() }
    }

    val customerDebts: Flow<List<CustomerDebt>> = debtDao.getAll().map { list ->
        list.map { it.toDomain() }
    }

    val farmerReceivables: Flow<List<FarmerReceivable>> = receivableDao.getAll().map { list ->
        list.map { it.toDomain() }
    }

    val expenses: Flow<List<ExpenseItem>> = expenseDao.getAll().map { list ->
        list.map { it.toDomain() }
    }

    val losses: Flow<List<LossItem>> = lossDao.getAll().map { list ->
        list.map { it.toDomain() }
    }

    val logs: Flow<List<AppLog>> = logDao.getAll().map { list ->
        list.map { it.toDomain() }
    }

    val settings: Flow<AlwaSettingsEntity> = settingsDao.getSettings().map {
        it ?: AlwaSettingsEntity()
    }

    suspend fun seed50SalesAndImports() {
        val sampleImports = SampleDataGenerator.generate50ImportInvoices()
        importDao.insertAll(sampleImports.map { it.toEntity() })

        val sampleSales = SampleDataGenerator.generate50SalesInvoices()
        salesDao.insertAll(sampleSales.map { it.toEntity() })

        addLog("تغذية البيانات التجريبية", "تم شحن 50 إرسالية استيراد و 50 فاتورة بيع جديدة بنجاح")
    }

    suspend fun checkAndSeedInitialData() {
        val currentSettings = settingsDao.getSettings().firstOrNull()
        if (currentSettings == null) {
            settingsDao.updateSettings(AlwaSettingsEntity())
        }

        val existingImports = importDao.getAll().firstOrNull()
        if (existingImports.isNullOrEmpty() || existingImports.size < 50) {
            val sampleImports = SampleDataGenerator.generate50ImportInvoices()
            importDao.insertAll(sampleImports.map { it.toEntity() })
        }

        val existingSales = salesDao.getAll().firstOrNull()
        if (existingSales.isNullOrEmpty() || existingSales.size < 50) {
            val sampleSales = SampleDataGenerator.generate50SalesInvoices()
            salesDao.insertAll(sampleSales.map { it.toEntity() })
        }

        val existingDebts = debtDao.getAll().firstOrNull()
        if (existingDebts.isNullOrEmpty()) {
            val sampleDebts = listOf(
                CustomerDebt("d1", "أبو حسين للعمولة - الكرخ", "07718889900", 3850000L, "2026-08-10", "متأخرة"),
                CustomerDebt("d2", "أسواق الفردوس - المنصور", "07803332211", 1420000L, "2026-08-05", "قادمة"),
                CustomerDebt("d3", "محل النور للفضليات - الأعظمية", "07904445566", 890000L, "2026-08-15", "قادمة")
            )
            debtDao.insertAll(sampleDebts.map { it.toEntity() })
        }

        val existingReceivables = receivableDao.getAll().firstOrNull()
        if (existingReceivables.isNullOrEmpty()) {
            val sampleReceivables = listOf(
                FarmerReceivable("r1", "أبو علي الكربلائي", "07809988771", 5800000L, 5684000L, "اليوم", "مستحقات اليوم"),
                FarmerReceivable("r2", "جاسم المحمدي", "07801234567", 9200000L, 9016000L, "اليوم", "مستحقات اليوم"),
                FarmerReceivable("r3", "سعدون البصري", "07705544332", 7200000L, 7056000L, "الأمس", "مستحقات سابقة")
            )
            receivableDao.insertAll(sampleReceivables.map { it.toEntity() })
        }

        val existingExpenses = expenseDao.getAll().firstOrNull()
        if (existingExpenses.isNullOrEmpty()) {
            val sampleExpenses = listOf(
                ExpenseItem("e1", "أجور ضيافة وشاي العلوة", "مصاريف يومية", 45000L, "اليوم 08:00 ص"),
                ExpenseItem("e2", "بنزين مولدة العلوة الرئيسية", "مصاريف يومية", 120000L, "اليوم 09:30 ص"),
                ExpenseItem("e3", "سلفة محاسب العلوة", "رواتب", 250000L, "الأمس")
            )
            expenseDao.insertAll(sampleExpenses.map { it.toEntity() })
        }

        val existingLosses = lossDao.getAll().firstOrNull()
        if (existingLosses.isNullOrEmpty()) {
            val sampleLosses = listOf(
                LossItem("l1", "تلف صندوقين طماطة في النقل", "تلف محصول", "طماطة النجف", 45.0, 65000L, "اليوم 09:00 ص"),
                LossItem("l2", "كسر في ميزان القباني الإلكتروني", "خسائر أخرى", "-", 0.0, 85000L, "الأمس")
            )
            lossDao.insertAll(sampleLosses.map { it.toEntity() })
        }

        val existingLogs = logDao.getAll().firstOrNull()
        if (existingLogs.isNullOrEmpty()) {
            val sampleLogs = listOf(
                AppLog("g1", "تسجيل وصول شحنة", "تم استلام ارسالية طماطة النجف - جاسم المحمدي", "07:30 ص"),
                AppLog("g2", "إصدار فاتورة بيع", "فاتورة INV-9021 بقيمة 2,121,500 د.ع", "10:15 ص"),
                AppLog("g3", "طباعة وصل حراري", "تمت طباعة الفاتورة INV-9021 بنجاح", "10:16 ص"),
                AppLog("g4", "تسجيل مصاريف", "أجور ضيافة بقيمة 45,000 د.ع", "08:00 ص")
            )
            logDao.insertAll(sampleLogs.map { it.toEntity() })
        }
    }

    suspend fun addImportInvoice(invoice: ImportInvoice) {
        importDao.insert(invoice.toEntity())
        addLog("تسجيل إرسالية جديدة", "تم تسجيل إرسالية ${invoice.farmerName} - ${invoice.code}")
    }

    suspend fun addSalesInvoice(invoice: SalesInvoice) {
        salesDao.insert(invoice.toEntity())
        addLog("إصدار فاتورة بيع", "فاتورة ${invoice.code} بقيمة ${invoice.grandTotalIQD} د.ع")

        // Update settings balances (cash box or debt)
        val currentSettings = settingsDao.getSettings().firstOrNull() ?: AlwaSettingsEntity()
        if (invoice.paymentType == "كاش") {
            val updatedCash = currentSettings.cashBoxBalance + invoice.grandTotalIQD
            val updatedNet = currentSettings.netProfit + invoice.officeCommission7Percent
            val updatedPorter = currentSettings.porterFeesCollected + invoice.porterageFeeIQD
            settingsDao.updateSettings(
                currentSettings.copy(
                    cashBoxBalance = updatedCash,
                    netProfit = updatedNet,
                    porterFeesCollected = updatedPorter
                )
            )
        } else {
            // Debt
            val existingDebt = customerDebts.firstOrNull()?.find { it.customerName == invoice.customerName }
            val newTotal = (existingDebt?.totalDebtIQD ?: 0L) + invoice.grandTotalIQD
            val debtItem = CustomerDebt(
                id = existingDebt?.id ?: UUID.randomUUID().toString(),
                customerName = invoice.customerName,
                customerPhone = invoice.customerPhone,
                totalDebtIQD = newTotal,
                dueDate = "بعد ${invoice.deferredDays} أيام",
                status = "قادمة"
            )
            debtDao.insert(debtItem.toEntity())
        }
    }

    suspend fun addExpense(expense: ExpenseItem) {
        expenseDao.insert(expense.toEntity())
        val currentSettings = settingsDao.getSettings().firstOrNull() ?: AlwaSettingsEntity()
        val newCash = (currentSettings.cashBoxBalance - expense.amountIQD).coerceAtLeast(0L)
        val newProfit = currentSettings.netProfit - expense.amountIQD
        settingsDao.updateSettings(
            currentSettings.copy(
                cashBoxBalance = newCash,
                netProfit = newProfit
            )
        )
        addLog("تسجيل مصروفات", "${expense.title} بقيمة ${expense.amountIQD} د.ع")
    }

    suspend fun addLoss(loss: LossItem) {
        lossDao.insert(loss.toEntity())
        val currentSettings = settingsDao.getSettings().firstOrNull() ?: AlwaSettingsEntity()
        val newProfit = currentSettings.netProfit - loss.lossAmountIQD
        settingsDao.updateSettings(currentSettings.copy(netProfit = newProfit))
        addLog("تسجيل خسائر/تلف", "${loss.title} بقيمة ${loss.lossAmountIQD} د.ع")
    }

    suspend fun payCustomerDebt(customerName: String, amount: Long) {
        val currentDebts = customerDebts.firstOrNull() ?: emptyList()
        val target = currentDebts.find { it.customerName == customerName }
        if (target != null) {
            val remaining = target.totalDebtIQD - amount
            if (remaining <= 0) {
                debtDao.deleteByCustomerName(customerName)
            } else {
                debtDao.updateDebtAmount(customerName, remaining)
            }
            val currentSettings = settingsDao.getSettings().firstOrNull() ?: AlwaSettingsEntity()
            settingsDao.updateSettings(
                currentSettings.copy(
                    cashBoxBalance = currentSettings.cashBoxBalance + amount
                )
            )
            addLog("تسديد دين", "تم استلام $amount د.ع من $customerName")
        }
    }

    suspend fun payoutPorters() {
        val currentSettings = settingsDao.getSettings().firstOrNull() ?: AlwaSettingsEntity()
        val collected = currentSettings.porterFeesCollected
        if (collected > 0) {
            val newCash = (currentSettings.cashBoxBalance - collected).coerceAtLeast(0L)
            settingsDao.updateSettings(
                currentSettings.copy(
                    cashBoxBalance = newCash,
                    porterFeesCollected = 0L
                )
            )
            addLog("توزيع أجور الحمالية", "تم توزيع $collected د.ع على ${currentSettings.porterCount} حمالين")
        }
    }

    suspend fun updateSettings(update: (AlwaSettingsEntity) -> AlwaSettingsEntity) {
        val current = settingsDao.getSettings().firstOrNull() ?: AlwaSettingsEntity()
        val updated = update(current)
        settingsDao.updateSettings(updated)
    }

    suspend fun markInvoicePrinted(invoiceId: String) {
        salesDao.markPrinted(invoiceId)
        addLog("طباعة فاتورة", "تمت طباعة الفاتورة ID: $invoiceId")
    }

    suspend fun addLog(action: String, details: String) {
        val dateStr = SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(Date())
        val log = AppLog(
            id = UUID.randomUUID().toString(),
            action = action,
            details = details,
            timestamp = dateStr
        )
        logDao.insert(log.toEntity())
    }
}
