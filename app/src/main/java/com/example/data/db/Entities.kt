package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.models.*
import org.json.JSONArray
import org.json.JSONObject

@Entity(tableName = "import_invoices")
data class ImportInvoiceEntity(
    @PrimaryKey val id: String,
    val code: String,
    val farmerName: String,
    val farmerPhone: String,
    val vehicleType: String,
    val cropsJson: String,
    val progressPercent: Float,
    val status: String,
    val totalEstimatedSalesIQD: Long,
    val date: String
)

@Entity(tableName = "sales_invoices")
data class SalesInvoiceEntity(
    @PrimaryKey val id: String,
    val code: String,
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String,
    val paymentType: String,
    val deferredDays: Int,
    val itemsJson: String,
    val goodsTotalIQD: Long,
    val officeCommission7Percent: Long,
    val porterageFeeIQD: Long,
    val grandTotalIQD: Long,
    val date: String,
    val isPrinted: Boolean
)

@Entity(tableName = "customer_debts")
data class CustomerDebtEntity(
    @PrimaryKey val id: String,
    val customerName: String,
    val customerPhone: String,
    val totalDebtIQD: Long,
    val dueDate: String,
    val status: String
)

@Entity(tableName = "farmer_receivables")
data class FarmerReceivableEntity(
    @PrimaryKey val id: String,
    val farmerName: String,
    val farmerPhone: String,
    val totalSalesIQD: Long,
    val netAmountIQD: Long,
    val date: String,
    val status: String
)

@Entity(tableName = "expenses")
data class ExpenseItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val amountIQD: Long,
    val date: String
)

@Entity(tableName = "losses")
data class LossItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val type: String,
    val cropName: String,
    val damagedWeightKg: Double,
    val lossAmountIQD: Long,
    val date: String
)

@Entity(tableName = "logs")
data class AppLogEntity(
    @PrimaryKey val id: String,
    val action: String,
    val details: String,
    val timestamp: String
)

@Entity(tableName = "alwa_settings")
data class AlwaSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val alwaName: String = "علوة الغابة الخضراء",
    val ownerName: String = "الحاج أبو أحمد العلوة",
    val phoneNumber: String = "07701234567",
    val location: String = "سوق العلوة المركزي - بغداد",
    val accountantName: String = "ALWA_ACC_ADMIN",
    val cashBoxBalance: Long = 18450000L,
    val netProfit: Long = 2840000L,
    val porterFeesCollected: Long = 485000L,
    val porterCount: Int = 5,
    val passcodeEnabled: Boolean = true,
    val immersiveMode: Boolean = false,
    val animationsEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val fontScale: Float = 1.0f
)

// Helpers to convert domain models <-> entities & JSON string conversions
fun ImportCrop.toJsonObject(): JSONObject {
    return JSONObject().apply {
        put("cropName", cropName)
        put("boxCount", boxCount)
        put("grossWeightKg", grossWeightKg)
        put("tareWeightKg", tareWeightKg)
    }
}

fun JSONObject.toImportCrop(): ImportCrop {
    return ImportCrop(
        cropName = optString("cropName"),
        boxCount = optInt("boxCount"),
        grossWeightKg = optDouble("grossWeightKg"),
        tareWeightKg = optDouble("tareWeightKg")
    )
}

fun List<ImportCrop>.toCropsJson(): String {
    val array = JSONArray()
    forEach { array.put(it.toJsonObject()) }
    return array.toString()
}

fun String.toImportCropList(): List<ImportCrop> {
    if (isEmpty()) return emptyList()
    val list = mutableListOf<ImportCrop>()
    val array = JSONArray(this)
    for (i in 0 until array.length()) {
        list.add(array.getJSONObject(i).toImportCrop())
    }
    return list
}

fun SaleCropItem.toJsonObject(): JSONObject {
    return JSONObject().apply {
        put("cropName", cropName)
        put("weightOrCount", weightOrCount)
        put("unitPriceIQD", unitPriceIQD)
    }
}

fun JSONObject.toSaleCropItem(): SaleCropItem {
    return SaleCropItem(
        cropName = optString("cropName"),
        weightOrCount = optDouble("weightOrCount"),
        unitPriceIQD = optLong("unitPriceIQD")
    )
}

fun List<SaleCropItem>.toItemsJson(): String {
    val array = JSONArray()
    forEach { array.put(it.toJsonObject()) }
    return array.toString()
}

fun String.toSaleCropItemList(): List<SaleCropItem> {
    if (isEmpty()) return emptyList()
    val list = mutableListOf<SaleCropItem>()
    val array = JSONArray(this)
    for (i in 0 until array.length()) {
        list.add(array.getJSONObject(i).toSaleCropItem())
    }
    return list
}

// Domain mapper extensions
fun ImportInvoiceEntity.toDomain() = ImportInvoice(
    id = id,
    code = code,
    farmerName = farmerName,
    farmerPhone = farmerPhone,
    vehicleType = vehicleType,
    crops = cropsJson.toImportCropList(),
    progressPercent = progressPercent,
    status = status,
    totalEstimatedSalesIQD = totalEstimatedSalesIQD,
    date = date
)

fun ImportInvoice.toEntity() = ImportInvoiceEntity(
    id = id,
    code = code,
    farmerName = farmerName,
    farmerPhone = farmerPhone,
    vehicleType = vehicleType,
    cropsJson = crops.toCropsJson(),
    progressPercent = progressPercent,
    status = status,
    totalEstimatedSalesIQD = totalEstimatedSalesIQD,
    date = date
)

fun SalesInvoiceEntity.toDomain() = SalesInvoice(
    id = id,
    code = code,
    customerName = customerName,
    customerPhone = customerPhone,
    customerAddress = customerAddress,
    paymentType = paymentType,
    deferredDays = deferredDays,
    items = itemsJson.toSaleCropItemList(),
    goodsTotalIQD = goodsTotalIQD,
    officeCommission7Percent = officeCommission7Percent,
    porterageFeeIQD = porterageFeeIQD,
    grandTotalIQD = grandTotalIQD,
    date = date,
    isPrinted = isPrinted
)

fun SalesInvoice.toEntity() = SalesInvoiceEntity(
    id = id,
    code = code,
    customerName = customerName,
    customerPhone = customerPhone,
    customerAddress = customerAddress,
    paymentType = paymentType,
    deferredDays = deferredDays,
    itemsJson = items.toItemsJson(),
    goodsTotalIQD = goodsTotalIQD,
    officeCommission7Percent = officeCommission7Percent,
    porterageFeeIQD = porterageFeeIQD,
    grandTotalIQD = grandTotalIQD,
    date = date,
    isPrinted = isPrinted
)

fun CustomerDebtEntity.toDomain() = CustomerDebt(
    id = id,
    customerName = customerName,
    customerPhone = customerPhone,
    totalDebtIQD = totalDebtIQD,
    dueDate = dueDate,
    status = status
)

fun CustomerDebt.toEntity() = CustomerDebtEntity(
    id = id,
    customerName = customerName,
    customerPhone = customerPhone,
    totalDebtIQD = totalDebtIQD,
    dueDate = dueDate,
    status = status
)

fun FarmerReceivableEntity.toDomain() = FarmerReceivable(
    id = id,
    farmerName = farmerName,
    farmerPhone = farmerPhone,
    totalSalesIQD = totalSalesIQD,
    netAmountIQD = netAmountIQD,
    date = date,
    status = status
)

fun FarmerReceivable.toEntity() = FarmerReceivableEntity(
    id = id,
    farmerName = farmerName,
    farmerPhone = farmerPhone,
    totalSalesIQD = totalSalesIQD,
    netAmountIQD = netAmountIQD,
    date = date,
    status = status
)

fun ExpenseItemEntity.toDomain() = ExpenseItem(
    id = id,
    title = title,
    category = category,
    amountIQD = amountIQD,
    date = date
)

fun ExpenseItem.toEntity() = ExpenseItemEntity(
    id = id,
    title = title,
    category = category,
    amountIQD = amountIQD,
    date = date
)

fun LossItemEntity.toDomain() = LossItem(
    id = id,
    title = title,
    type = type,
    cropName = cropName,
    damagedWeightKg = damagedWeightKg,
    lossAmountIQD = lossAmountIQD,
    date = date
)

fun LossItem.toEntity() = LossItemEntity(
    id = id,
    title = title,
    type = type,
    cropName = cropName,
    damagedWeightKg = damagedWeightKg,
    lossAmountIQD = lossAmountIQD,
    date = date
)

fun AppLogEntity.toDomain() = AppLog(
    id = id,
    action = action,
    details = details,
    timestamp = timestamp
)

fun AppLog.toEntity() = AppLogEntity(
    id = id,
    action = action,
    details = details,
    timestamp = timestamp
)
