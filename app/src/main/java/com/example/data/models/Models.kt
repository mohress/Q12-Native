package com.example.data.models

data class ImportCrop(
    val cropName: String,
    val boxCount: Int,
    val grossWeightKg: Double,
    val tareWeightKg: Double
) {
    val netWeightKg: Double get() = (grossWeightKg - tareWeightKg).coerceAtLeast(0.0)
}

data class ImportInvoice(
    val id: String,
    val code: String,
    val farmerName: String,
    val farmerPhone: String = "",
    val vehicleType: String,
    val crops: List<ImportCrop>,
    val progressPercent: Float, // 0.0f to 1.0f
    val status: String, // "قيد البيع ⏳" or "جاهز للتسوية ⚠️" or "مكتمل ✅"
    val totalEstimatedSalesIQD: Long,
    val date: String
)

data class SaleCropItem(
    val cropName: String,
    val weightOrCount: Double,
    val unitPriceIQD: Long
) {
    val totalAmountIQD: Long get() = (weightOrCount * unitPriceIQD).toLong()
}

data class SalesInvoice(
    val id: String,
    val code: String,
    val customerName: String,
    val customerPhone: String = "",
    val customerAddress: String = "",
    val paymentType: String, // "كاش" or "آجل"
    val deferredDays: Int = 0,
    val items: List<SaleCropItem>,
    val goodsTotalIQD: Long,
    val officeCommission7Percent: Long,
    val porterageFeeIQD: Long,
    val grandTotalIQD: Long,
    val date: String,
    val isPrinted: Boolean = false
)

data class CustomerDebt(
    val id: String,
    val customerName: String,
    val customerPhone: String,
    val totalDebtIQD: Long,
    val dueDate: String,
    val status: String // "متأخرة" or "قادمة"
)

data class FarmerReceivable(
    val id: String,
    val farmerName: String,
    val farmerPhone: String,
    val totalSalesIQD: Long,
    val netAmountIQD: Long, // After 2% deduction
    val date: String,
    val status: String // "مستحقات اليوم" or "مستحقات سابقة"
)

data class ExpenseItem(
    val id: String,
    val title: String,
    val category: String, // "مصاريف يومية", "رواتب", "مصاريف شخصية"
    val amountIQD: Long,
    val date: String
)

data class LossItem(
    val id: String,
    val title: String,
    val type: String, // "تلف محصول", "خسائر أخرى"
    val cropName: String = "",
    val damagedWeightKg: Double = 0.0,
    val lossAmountIQD: Long,
    val date: String
)

data class AppLog(
    val id: String,
    val action: String,
    val details: String,
    val timestamp: String
)

data class PrinterDevice(
    val name: String,
    val address: String,
    val isConnected: Boolean,
    val batteryPercent: Int
)
