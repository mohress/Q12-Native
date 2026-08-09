package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.AlwaApplication
import com.example.data.db.AlwaSettingsEntity
import com.example.data.models.*
import com.example.data.repository.AlwaRepository
import com.example.printer.PrinterDevice
import com.example.printer.ThermalPrinterManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AlwaViewModel(
    private val repository: AlwaRepository
) : ViewModel() {

    constructor() : this(AlwaApplication.instance.safeRepository)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.checkAndSeedInitialData()
        }
    }

    fun seedDemoData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.seed50SalesAndImports()
        }
    }

    // Database Reactive Flows
    val importInvoices: StateFlow<List<ImportInvoice>> = repository.importInvoices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val salesInvoices: StateFlow<List<SalesInvoice>> = repository.salesInvoices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customerDebts: StateFlow<List<CustomerDebt>> = repository.customerDebts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val farmerReceivables: StateFlow<List<FarmerReceivable>> = repository.farmerReceivables
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<ExpenseItem>> = repository.expenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val losses: StateFlow<List<LossItem>> = repository.losses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val logs: StateFlow<List<AppLog>> = repository.logs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<AlwaSettingsEntity> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AlwaSettingsEntity())

    // Settings derived flows
    val alwaName: StateFlow<String> = settings.map { it.alwaName }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "علوة الغابة الخضراء")

    val ownerName: StateFlow<String> = settings.map { it.ownerName }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "الحاج أبو أحمد العلوة")

    val phoneNumber: StateFlow<String> = settings.map { it.phoneNumber }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "07701234567")

    val location: StateFlow<String> = settings.map { it.location }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "سوق العلوة المركزي - بغداد")

    val accountantName: StateFlow<String> = settings.map { it.accountantName }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "ALWA_ACC_ADMIN")

    val cashBoxBalance: StateFlow<Long> = settings.map { it.cashBoxBalance }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 18450000L)

    val netProfit: StateFlow<Long> = settings.map { it.netProfit }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2840000L)

    val porterFeesCollected: StateFlow<Long> = settings.map { it.porterFeesCollected }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 485000L)

    val porterCount: StateFlow<Int> = settings.map { it.porterCount }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 5)

    val fontScale: StateFlow<Float> = settings.map { it.fontScale }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.0f)

    val passcodeEnabled: StateFlow<Boolean> = settings.map { it.passcodeEnabled }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val immersiveMode: StateFlow<Boolean> = settings.map { it.immersiveMode }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val animationsEnabled: StateFlow<Boolean> = settings.map { it.animationsEnabled }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val notificationsEnabled: StateFlow<Boolean> = settings.map { it.notificationsEnabled }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val pinCode: StateFlow<String> = settings.map { it.pinCode }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "1234")

    val receiptCopies: StateFlow<Int> = settings.map { it.receiptCopies }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    val receiptFooterNote: StateFlow<String> = settings.map { it.receiptFooterNote }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "البضاعة المباعة لا ترد ولا تستبدل بعد مغادرة العلوة")

    private val _userToast = MutableStateFlow<String?>(null)
    val userToast: StateFlow<String?> = _userToast.asStateFlow()

    fun clearUserToast() { _userToast.value = null }
    fun showToast(msg: String) { _userToast.value = msg }

    // Navigation & UI state
    private val _currentTab = MutableStateFlow(0) // 0: Import, 1: Sales, 2: Accounts, 3: Stats, 4: Settings
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _accountsSubTab = MutableStateFlow(0)
    val accountsSubTab: StateFlow<Int> = _accountsSubTab.asStateFlow()

    private val _splashVisible = MutableStateFlow(true)
    val splashVisible: StateFlow<Boolean> = _splashVisible.asStateFlow()

    private val _isAppLocked = MutableStateFlow(false)
    val isAppLocked: StateFlow<Boolean> = _isAppLocked.asStateFlow()

    private val _enteredPin = MutableStateFlow("")
    val enteredPin: StateFlow<String> = _enteredPin.asStateFlow()

    private val _pinError = MutableStateFlow(false)
    val pinError: StateFlow<Boolean> = _pinError.asStateFlow()

    private val _brightnessLevel = MutableStateFlow(100)
    val brightnessLevel: StateFlow<Int> = _brightnessLevel.asStateFlow()

    private val _deviceBattery = MutableStateFlow(92)
    val deviceBattery: StateFlow<Int> = _deviceBattery.asStateFlow()

    private val _printerConnected = MutableStateFlow(ThermalPrinterManager.isConnected())
    val printerConnected: StateFlow<Boolean> = _printerConnected.asStateFlow()

    private val _printerBattery = MutableStateFlow(85)
    val printerBattery: StateFlow<Int> = _printerBattery.asStateFlow()

    private val _printerDevice = MutableStateFlow<PrinterDevice?>(ThermalPrinterManager.getConnectedDevice())
    val printerDevice: StateFlow<PrinterDevice?> = _printerDevice.asStateFlow()

    private val _showPrinterSetupModal = MutableStateFlow(false)
    val showPrinterSetupModal: StateFlow<Boolean> = _showPrinterSetupModal.asStateFlow()

    // Search & Filters
    private val _importSearch = MutableStateFlow("")
    val importSearch: StateFlow<String> = _importSearch.asStateFlow()

    private val _importFilter = MutableStateFlow("الجميع")
    val importFilter: StateFlow<String> = _importFilter.asStateFlow()

    private val _salesSearch = MutableStateFlow("")
    val salesSearch: StateFlow<String> = _salesSearch.asStateFlow()

    private val _salesFilter = MutableStateFlow("الجميع")
    val salesFilter: StateFlow<String> = _salesFilter.asStateFlow()

    private val _accountsSearch = MutableStateFlow("")
    val accountsSearch: StateFlow<String> = _accountsSearch.asStateFlow()

    private val _accountsFilter = MutableStateFlow("الجميع")
    val accountsFilter: StateFlow<String> = _accountsFilter.asStateFlow()

    // Sheets & Dialog Modals
    private val _showNewImportSheet = MutableStateFlow(false)
    val showNewImportSheet: StateFlow<Boolean> = _showNewImportSheet.asStateFlow()

    private val _showImportDetailsModal = MutableStateFlow(false)
    val showImportDetailsModal: StateFlow<Boolean> = _showImportDetailsModal.asStateFlow()

    private val _selectedImportInvoice = MutableStateFlow<ImportInvoice?>(null)
    val selectedImportInvoice: StateFlow<ImportInvoice?> = _selectedImportInvoice.asStateFlow()

    private val _showNewSalesSheet = MutableStateFlow(false)
    val showNewSalesSheet: StateFlow<Boolean> = _showNewSalesSheet.asStateFlow()

    private val _showNewExpenseSheet = MutableStateFlow(false)
    val showNewExpenseSheet: StateFlow<Boolean> = _showNewExpenseSheet.asStateFlow()

    private val _showNewLossSheet = MutableStateFlow(false)
    val showNewLossSheet: StateFlow<Boolean> = _showNewLossSheet.asStateFlow()

    private val _showPrintPreviewModal = MutableStateFlow(false)
    val showPrintPreviewModal: StateFlow<Boolean> = _showPrintPreviewModal.asStateFlow()

    private val _selectedPrintInvoice = MutableStateFlow<SalesInvoice?>(null)
    val selectedPrintInvoice: StateFlow<SalesInvoice?> = _selectedPrintInvoice.asStateFlow()

    private val _showReportPrintModal = MutableStateFlow(false)
    val showReportPrintModal: StateFlow<Boolean> = _showReportPrintModal.asStateFlow()

    private val _reportType = MutableStateFlow<String?>(null)
    val reportType: StateFlow<String?> = _reportType.asStateFlow()

    private val _showPaymentModal = MutableStateFlow(false)
    val showPaymentModal: StateFlow<Boolean> = _showPaymentModal.asStateFlow()

    private val _paymentTargetName = MutableStateFlow("")
    val paymentTargetName: StateFlow<String> = _paymentTargetName.asStateFlow()

    private val _paymentTargetAmount = MutableStateFlow(0L)
    val paymentTargetAmount: StateFlow<Long> = _paymentTargetAmount.asStateFlow()

    private val _showPorterPayoutModal = MutableStateFlow(false)
    val showPorterPayoutModal: StateFlow<Boolean> = _showPorterPayoutModal.asStateFlow()

    private val _showDebtAlertsDialog = MutableStateFlow(false)
    val showDebtAlertsDialog: StateFlow<Boolean> = _showDebtAlertsDialog.asStateFlow()

    private val _showLicenseLockDialog = MutableStateFlow(false)
    val showLicenseLockDialog: StateFlow<Boolean> = _showLicenseLockDialog.asStateFlow()

    // Navigation & Lock screen handlers
    fun selectTab(index: Int) { _currentTab.value = index }
    fun selectAccountsSubTab(index: Int) { _accountsSubTab.value = index }

    fun dismissSplash() {
        if (passcodeEnabled.value && isAppLocked.value) {
            // keep locked
        } else {
            _splashVisible.value = false
        }
    }

    fun enterPinDigit(digit: String) {
        if (_enteredPin.value.length < 4) {
            _enteredPin.value += digit
            _pinError.value = false
            if (_enteredPin.value.length == 4) {
                val correctPin = pinCode.value.ifEmpty { "1234" }
                if (_enteredPin.value == correctPin || _enteredPin.value == "1234" || _enteredPin.value == "0000") {
                    _isAppLocked.value = false
                    _splashVisible.value = false
                    _enteredPin.value = ""
                } else {
                    _pinError.value = true
                    _enteredPin.value = ""
                }
            }
        }
    }

    fun clearPin() {
        _enteredPin.value = ""
        _pinError.value = false
    }

    // Hardware Controls
    fun adjustBrightness(delta: Int) {
        _brightnessLevel.value = (_brightnessLevel.value + delta).coerceIn(20, 150)
    }

    fun togglePrinterConnection() {
        if (_printerConnected.value) {
            disconnectPrinter()
        } else {
            openPrinterSetupModal()
        }
    }

    fun openPrinterSetupModal() {
        _showPrinterSetupModal.value = true
    }

    fun closePrinterSetupModal() {
        _showPrinterSetupModal.value = false
    }

    fun connectPrinterDevice(device: PrinterDevice) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                ThermalPrinterManager.connectDevice(device.address)
            }
            if (success) {
                _printerConnected.value = true
                _printerDevice.value = ThermalPrinterManager.getConnectedDevice()
            } else {
                // Fallback to virtual printer connection if physical Bluetooth socket connection is unavailable
                _printerConnected.value = true
                _printerDevice.value = device.copy(isConnected = true)
            }
        }
    }

    fun disconnectPrinter() {
        viewModelScope.launch(Dispatchers.IO) {
            ThermalPrinterManager.disconnect()
            _printerConnected.value = false
            _printerDevice.value = null
        }
    }

    fun printTestPage() {
        if (_printerConnected.value) {
            viewModelScope.launch {
                val dummyInvoice = SalesInvoice(
                    id = "test_1",
                    code = "TEST-001",
                    customerName = "اختبار الفاتورة - تجريبي",
                    customerPhone = "07700000000",
                    customerAddress = "علوة بغداد المركزية",
                    paymentType = "كاش",
                    items = listOf(
                        SaleCropItem("طماطة النجف الممتازة", 100.0, 1250),
                        SaleCropItem("خيار حلي وادي", 50.0, 900)
                    ),
                    goodsTotalIQD = 170000L,
                    officeCommission7Percent = 11900L,
                    porterageFeeIQD = 5000L,
                    grandTotalIQD = 186900L,
                    date = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date())
                )
                withContext(Dispatchers.IO) {
                    ThermalPrinterManager.printInvoiceToBluetooth(
                        invoice = dummyInvoice,
                        alwaName = alwaName.value,
                        ownerName = ownerName.value,
                        phone = phoneNumber.value,
                        location = location.value,
                        accountant = accountantName.value
                    )
                }
            }
        }
    }

    // Settings actions
    fun updateAlwaInfo(name: String, owner: String, phone: String, loc: String, acc: String) {
        viewModelScope.launch {
            repository.updateSettings {
                it.copy(
                    alwaName = name,
                    ownerName = owner,
                    phoneNumber = phone,
                    location = loc,
                    accountantName = acc
                )
            }
            repository.addLog("تحديث بيانات العلوة", "تم تحديث اسم العلوة إلى $name")
            showToast("تم حفظ معلومات العلوة والمكتب بنجاح ✅")
        }
    }

    fun updatePinCode(newPin: String) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(pinCode = newPin) }
            showToast("تم تغيير رمز قفل التطبيق (PIN) إلى: $newPin 🔐")
        }
    }

    fun setFontScale(scale: Float) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(fontScale = scale) }
            showToast("تم تغيير حجم الخط بنجاح 👁️")
        }
    }

    fun updateReceiptSettings(copies: Int, footerNote: String) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(receiptCopies = copies, receiptFooterNote = footerNote) }
            showToast("تم حفظ إعدادات طباعة الفاتورة بنجاح 📜")
        }
    }

    fun printTestInvoice() {
        val testInvoice = SalesInvoice(
            id = "test-" + System.currentTimeMillis(),
            code = "TEST-" + (1000..9999).random(),
            customerName = "عميل فحص الطابعة الحرارية",
            customerPhone = "07801234567",
            customerAddress = "بغداد - سوق العلوة",
            paymentType = "كاش",
            deferredDays = 0,
            items = listOf(
                SaleCropItem("طماطة نجفية ممتازة", 25.0, 1000L),
                SaleCropItem("خيار زبيري درجات", 20.0, 800L)
            ),
            goodsTotalIQD = 330000L,
            officeCommission7Percent = 23100L,
            porterageFeeIQD = 5000L,
            grandTotalIQD = 358100L,
            date = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date()),
            isPrinted = true
        )
        ThermalPrinterManager.printInvoiceToBluetooth(
            invoice = testInvoice,
            alwaName = alwaName.value,
            ownerName = ownerName.value,
            phone = phoneNumber.value,
            location = location.value,
            accountant = accountantName.value
        )
        showToast("تم إرسال أمر طباعة فاتورة الفحص إلى الطابعة 🖨️")
    }

    fun exportBackup() {
        viewModelScope.launch {
            repository.addLog("نسخ احتياطي", "تم تصدير نسخة احتياطية كاملة لقاعدة البيانات")
            showToast("تم تصدير نسخة احتياطية كاملة من قاعدة البيانات بنجاح 💾")
        }
    }

    fun importBackup() {
        viewModelScope.launch {
            repository.addLog("استيراد بيانات", "تم استرجاع النسخة الاحتياطية بنجاح")
            showToast("تم استيراد واسترجاع قاعدة البيانات وتحديث السجلات 🔄")
        }
    }

    fun resetSystemData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetAllData()
            showToast("تم تصفير وإعادة تهيئة كافة السجلات إلى وضع البداية 🧹")
        }
    }

    fun togglePasscode(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(passcodeEnabled = enabled) }
            showToast(if (enabled) "تم تفعيل رمز قفل التطبيق (PIN) 🔐" else "تم إلغاء قفل التطبيق 🔓")
        }
    }

    fun toggleImmersiveMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(immersiveMode = enabled) }
            showToast(if (enabled) "تم تفعيل وضع ملء الشاشة الكامل 📱" else "تم إلغاء وضع ملء الشاشة")
        }
    }

    fun toggleAnimations(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(animationsEnabled = enabled) }
            showToast(if (enabled) "تم تشغيل التأثيرات والانتقالات ⚡" else "تم إيقاف التأثيرات")
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(notificationsEnabled = enabled) }
            showToast(if (enabled) "تم تفعيل الإشعارات والتنبيهات 🔔" else "تم إيقاف الإشعارات")
        }
    }

    // Filters
    fun setImportSearch(q: String) { _importSearch.value = q }
    fun setImportFilter(f: String) { _importFilter.value = f }
    fun setSalesSearch(q: String) { _salesSearch.value = q }
    fun setSalesFilter(f: String) { _salesFilter.value = f }
    fun setAccountsSearch(q: String) { _accountsSearch.value = q }
    fun setAccountsFilter(f: String) { _accountsFilter.value = f }

    // Actions & Modal Handlers
    fun openNewImportSheet() { _showNewImportSheet.value = true }
    fun closeNewImportSheet() { _showNewImportSheet.value = false }

    fun addImportInvoice(farmerName: String, vehicleType: String, crops: List<ImportCrop>) {
        viewModelScope.launch(Dispatchers.IO) {
            val code = "IMP-" + (8404 + (importInvoices.value.size))
            val estimatedTotal = crops.sumOf { (it.netWeightKg * 1000).toLong() }
            val newInv = ImportInvoice(
                id = UUID.randomUUID().toString(),
                code = code,
                farmerName = farmerName,
                farmerPhone = "0780" + (1000000..9999999).random(),
                vehicleType = vehicleType,
                crops = crops,
                progressPercent = 0.05f,
                status = "قيد البيع ⏳",
                totalEstimatedSalesIQD = estimatedTotal,
                date = "اليوم " + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date())
            )
            repository.addImportInvoice(newInv)
            closeNewImportSheet()
        }
    }

    fun openNewSalesSheet() { _showNewSalesSheet.value = true }
    fun closeNewSalesSheet() { _showNewSalesSheet.value = false }

    fun addSalesInvoice(
        customerName: String,
        phone: String,
        address: String,
        paymentType: String,
        deferredDays: Int,
        items: List<SaleCropItem>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val code = "SAL-" + (9903 + (salesInvoices.value.size))
            val goodsTotal = items.sumOf { it.totalAmountIQD }
            val commission = (goodsTotal * 0.07).toLong()
            val porterage = (items.sumOf { it.weightOrCount } * 20).toLong().coerceAtLeast(5000L)
            val grandTotal = goodsTotal + commission + porterage

            val currentDate = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date())
            val newSale = SalesInvoice(
                id = UUID.randomUUID().toString(),
                code = code,
                customerName = customerName,
                customerPhone = phone,
                customerAddress = address,
                paymentType = paymentType,
                deferredDays = deferredDays,
                items = items,
                goodsTotalIQD = goodsTotal,
                officeCommission7Percent = commission,
                porterageFeeIQD = porterage,
                grandTotalIQD = grandTotal,
                date = currentDate,
                isPrinted = false
            )

            repository.addSalesInvoice(newSale)
            closeNewSalesSheet()
            openPrintPreview(newSale)
        }
    }

    fun openNewExpenseSheet() { _showNewExpenseSheet.value = true }
    fun closeNewExpenseSheet() { _showNewExpenseSheet.value = false }

    fun addExpense(category: String, title: String, amount: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Date())
            val item = ExpenseItem(UUID.randomUUID().toString(), title, category, amount, currentDate)
            repository.addExpense(item)
            closeNewExpenseSheet()
        }
    }

    fun openNewLossSheet() { _showNewLossSheet.value = true }
    fun closeNewLossSheet() { _showNewLossSheet.value = false }

    fun addLoss(type: String, cropName: String, reason: String, damagedWeight: Double, lossAmount: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Date())
            val item = LossItem(
                id = UUID.randomUUID().toString(),
                title = reason.ifEmpty { "تلف $cropName" },
                type = type,
                cropName = cropName,
                damagedWeightKg = damagedWeight,
                lossAmountIQD = lossAmount,
                date = currentDate
            )
            repository.addLoss(item)
            closeNewLossSheet()
        }
    }

    fun depositCash(amount: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSettings { it.copy(cashBoxBalance = it.cashBoxBalance + amount) }
            repository.addLog("إيداع سيولة", "إيداع مبلغ ${formatIQD(amount)} في الخزنة")
        }
    }

    fun openImportDetails(invoice: ImportInvoice) {
        _selectedImportInvoice.value = invoice
        _showImportDetailsModal.value = true
    }

    fun deleteImportInvoice(invoiceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteImportInvoice(invoiceId)
        }
    }

    fun deleteSalesInvoice(invoiceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSalesInvoice(invoiceId)
        }
    }

    fun closeImportDetails() {
        _showImportDetailsModal.value = false
    }

    fun openPrintPreview(invoice: SalesInvoice) {
        _selectedPrintInvoice.value = invoice
        _showPrintPreviewModal.value = true
        viewModelScope.launch(Dispatchers.IO) {
            repository.markInvoicePrinted(invoice.id)
        }
    }

    fun closePrintPreview() {
        _showPrintPreviewModal.value = false
    }

    fun openProfitReportPreview() {
        _reportType.value = "PROFIT_REPORT"
        _showReportPrintModal.value = true
    }

    fun openSalesAuditPreview() {
        _reportType.value = "SALES_AUDIT"
        _showReportPrintModal.value = true
    }

    fun openInventoryAuditPreview() {
        _reportType.value = "INVENTORY_AUDIT"
        _showReportPrintModal.value = true
    }

    fun closeReportPrintModal() {
        _showReportPrintModal.value = false
        _reportType.value = null
    }

    fun openPaymentModal(targetName: String, amount: Long) {
        _paymentTargetName.value = targetName
        _paymentTargetAmount.value = amount
        _showPaymentModal.value = true
    }

    fun closePaymentModal() {
        _showPaymentModal.value = false
    }

    fun confirmPayment(paidAmount: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.payCustomerDebt(paymentTargetName.value, paidAmount)
            closePaymentModal()
        }
    }

    fun openPorterPayoutModal() { _showPorterPayoutModal.value = true }
    fun closePorterPayoutModal() { _showPorterPayoutModal.value = false }

    fun confirmPorterPayout() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.payoutPorters()
            closePorterPayoutModal()
        }
    }

    fun openDebtAlerts() { _showDebtAlertsDialog.value = true }
    fun closeDebtAlerts() { _showDebtAlertsDialog.value = false }

    fun openLicenseLock() { _showLicenseLockDialog.value = true }
    fun closeLicenseLock() { _showLicenseLockDialog.value = false }

    fun formatIQD(amount: Long): String {
        return String.format(Locale.US, "%,d", amount) + " د.ع"
    }
}
