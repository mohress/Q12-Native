package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.data.models.SalesInvoice
import com.example.ui.AlwaViewModel
import com.example.ui.components.AppHeader
import com.example.ui.components.BottomNavBar
import com.example.ui.components.ThermalReceiptPreview
import com.example.ui.modals.*
import com.example.ui.screens.*
import com.example.ui.theme.BackgroundSoft
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: AlwaViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

                    val splashVisible by viewModel.splashVisible.collectAsState()
                    val isAppLocked by viewModel.isAppLocked.collectAsState()
                    val enteredPin by viewModel.enteredPin.collectAsState()
                    val pinError by viewModel.pinError.collectAsState()

                    val currentTab by viewModel.currentTab.collectAsState()
                    val accountsSubTab by viewModel.accountsSubTab.collectAsState()

                    val alwaName by viewModel.alwaName.collectAsState()
                    val ownerName by viewModel.ownerName.collectAsState()
                    val phoneNumber by viewModel.phoneNumber.collectAsState()
                    val location by viewModel.location.collectAsState()
                    val accountantName by viewModel.accountantName.collectAsState()

                    val brightnessLevel by viewModel.brightnessLevel.collectAsState()
                    val deviceBattery by viewModel.deviceBattery.collectAsState()
                    val printerConnected by viewModel.printerConnected.collectAsState()

                    val importSearch by viewModel.importSearch.collectAsState()
                    val importFilter by viewModel.importFilter.collectAsState()
                    val salesSearch by viewModel.salesSearch.collectAsState()
                    val salesFilter by viewModel.salesFilter.collectAsState()
                    val accountsSearch by viewModel.accountsSearch.collectAsState()
                    val accountsFilter by viewModel.accountsFilter.collectAsState()

                    val importInvoices by viewModel.importInvoices.collectAsState()
                    val salesInvoices by viewModel.salesInvoices.collectAsState()
                    val customerDebts by viewModel.customerDebts.collectAsState()
                    val farmerReceivables by viewModel.farmerReceivables.collectAsState()
                    val porterFeesCollected by viewModel.porterFeesCollected.collectAsState()
                    val porterCount by viewModel.porterCount.collectAsState()

                    val cashBoxBalance by viewModel.cashBoxBalance.collectAsState()
                    val netProfit by viewModel.netProfit.collectAsState()
                    val expenses by viewModel.expenses.collectAsState()
                    val losses by viewModel.losses.collectAsState()
                    val logs by viewModel.logs.collectAsState()

                    val fontScale by viewModel.fontScale.collectAsState()
                    val passcodeEnabled by viewModel.passcodeEnabled.collectAsState()
                    val immersiveMode by viewModel.immersiveMode.collectAsState()
                    val animationsEnabled by viewModel.animationsEnabled.collectAsState()
                    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()

                    // Modal states
                    val showNewImportSheet by viewModel.showNewImportSheet.collectAsState()
                    val showImportDetailsModal by viewModel.showImportDetailsModal.collectAsState()
                    val selectedImportInvoice by viewModel.selectedImportInvoice.collectAsState()
                    val showNewSalesSheet by viewModel.showNewSalesSheet.collectAsState()
                    val showNewExpenseSheet by viewModel.showNewExpenseSheet.collectAsState()
                    val showNewLossSheet by viewModel.showNewLossSheet.collectAsState()
                    val showPaymentModal by viewModel.showPaymentModal.collectAsState()
                    val paymentTargetName by viewModel.paymentTargetName.collectAsState()
                    val paymentTargetAmount by viewModel.paymentTargetAmount.collectAsState()
                    val showPorterPayoutModal by viewModel.showPorterPayoutModal.collectAsState()
                    val showDebtAlertsDialog by viewModel.showDebtAlertsDialog.collectAsState()
                    val showLicenseLockDialog by viewModel.showLicenseLockDialog.collectAsState()
                    val showPrintPreviewModal by viewModel.showPrintPreviewModal.collectAsState()
                    val selectedPrintInvoice by viewModel.selectedPrintInvoice.collectAsState()
                    val showPrinterSetupModal by viewModel.showPrinterSetupModal.collectAsState()
                    val printerDevice by viewModel.printerDevice.collectAsState()

                    Box(modifier = Modifier.fillMaxSize()) {
                        if (splashVisible || isAppLocked) {
                            SplashScreen(
                                alwaName = alwaName,
                                isLocked = isAppLocked,
                                enteredPin = enteredPin,
                                pinError = pinError,
                                onPinDigit = viewModel::enterPinDigit,
                                onClearPin = viewModel::clearPin,
                                onDismissSplash = viewModel::dismissSplash
                            )
                        } else {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                containerColor = BackgroundSoft,
                                topBar = {
                                    AppHeader(
                                        alwaName = alwaName,
                                        brightness = brightnessLevel,
                                        onAdjustBrightness = viewModel::adjustBrightness,
                                        deviceBattery = deviceBattery,
                                        printerConnected = printerConnected,
                                        onTogglePrinter = viewModel::togglePrinterConnection,
                                        debtAlertsCount = customerDebts.count { it.status == "متأخرة" },
                                        onOpenDebtAlerts = viewModel::openDebtAlerts
                                    )
                                },
                                bottomBar = {
                                    BottomNavBar(
                                        selectedTab = currentTab,
                                        onTabSelected = viewModel::selectTab
                                    )
                                }
                            ) { innerPadding ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                ) {
                                    when (currentTab) {
                                        0 -> ImportScreen(
                                            searchQuery = importSearch,
                                            onSearchChange = viewModel::setImportSearch,
                                            activeFilter = importFilter,
                                            onFilterChange = viewModel::setImportFilter,
                                            importInvoices = importInvoices,
                                            onNewImportClick = viewModel::openNewImportSheet,
                                            onViewDetails = viewModel::openImportDetails,
                                            formatIQD = viewModel::formatIQD
                                        )
                                        1 -> SalesScreen(
                                            searchQuery = salesSearch,
                                            onSearchChange = viewModel::setSalesSearch,
                                            activeFilter = salesFilter,
                                            onFilterChange = viewModel::setSalesFilter,
                                            salesInvoices = salesInvoices,
                                            onNewSaleClick = viewModel::openNewSalesSheet,
                                            onPrintInvoice = viewModel::openPrintPreview,
                                            formatIQD = viewModel::formatIQD
                                        )
                                        2 -> AccountsScreen(
                                            activeSubTab = accountsSubTab,
                                            onSubTabSelect = viewModel::selectAccountsSubTab,
                                            searchQuery = accountsSearch,
                                            onSearchChange = viewModel::setAccountsSearch,
                                            activeFilter = accountsFilter,
                                            onFilterChange = viewModel::setAccountsFilter,
                                            customerDebts = customerDebts,
                                            farmerReceivables = farmerReceivables,
                                            porterFeesCollected = porterFeesCollected,
                                            porterCount = porterCount,
                                            onOpenPaymentModal = viewModel::openPaymentModal,
                                            onOpenPorterPayoutModal = viewModel::openPorterPayoutModal,
                                            formatIQD = viewModel::formatIQD
                                        )
                                        3 -> StatsScreen(
                                            cashBoxBalance = cashBoxBalance,
                                            netProfit = netProfit,
                                            expenses = expenses,
                                            losses = losses,
                                            logs = logs,
                                            onOpenNewExpense = viewModel::openNewExpenseSheet,
                                            onOpenNewLoss = viewModel::openNewLossSheet,
                                            onDepositCash = { viewModel.depositCash(5000000L) },
                                            formatIQD = viewModel::formatIQD
                                        )
                                        4 -> SettingsScreen(
                                            alwaName = alwaName,
                                            ownerName = ownerName,
                                            phoneNumber = phoneNumber,
                                            location = location,
                                            accountantName = accountantName,
                                            onSaveAlwaInfo = viewModel::updateAlwaInfo,
                                            printerConnected = printerConnected,
                                            onTogglePrinter = viewModel::togglePrinterConnection,
                                            fontScale = fontScale,
                                            onFontScaleChange = viewModel::setFontScale,
                                            passcodeEnabled = passcodeEnabled,
                                            onTogglePasscode = viewModel::togglePasscode,
                                            immersiveMode = immersiveMode,
                                            onToggleImmersive = viewModel::toggleImmersiveMode,
                                            animationsEnabled = animationsEnabled,
                                            onToggleAnimations = viewModel::toggleAnimations,
                                            notificationsEnabled = notificationsEnabled,
                                            onToggleNotifications = viewModel::toggleNotifications,
                                            onOpenLicenseModal = viewModel::openLicenseLock
                                        )
                                    }
                                }
                            }
                        }

                        // Modal Bottom Sheets & Dialogs
                        if (showNewImportSheet) {
                            NewImportInvoiceSheet(
                                onDismiss = viewModel::closeNewImportSheet,
                                onSubmit = viewModel::addImportInvoice
                            )
                        }

                        if (showImportDetailsModal && selectedImportInvoice != null) {
                            ImportInvoiceDetailsSheet(
                                invoice = selectedImportInvoice!!,
                                onDismiss = viewModel::closeImportDetails,
                                onSettleAccount = { invoice ->
                                    viewModel.openPaymentModal(invoice.farmerName, (invoice.totalEstimatedSalesIQD * 0.98).toLong())
                                },
                                formatIQD = viewModel::formatIQD
                            )
                        }

                        if (showNewSalesSheet) {
                            NewSalesInvoiceSheet(
                                onDismiss = viewModel::closeNewSalesSheet,
                                onSubmit = viewModel::addSalesInvoice,
                                formatIQD = viewModel::formatIQD
                            )
                        }

                        if (showNewExpenseSheet) {
                            NewExpenseSheet(
                                onDismiss = viewModel::closeNewExpenseSheet,
                                onSubmit = viewModel::addExpense
                            )
                        }

                        if (showNewLossSheet) {
                            NewLossSheet(
                                onDismiss = viewModel::closeNewLossSheet,
                                onSubmit = { type, crop, wt, reason, amt ->
                                    viewModel.addLoss(type, crop, reason, wt, amt)
                                }
                            )
                        }

                        if (showPaymentModal) {
                            PaymentModal(
                                customerName = paymentTargetName,
                                initialAmount = paymentTargetAmount,
                                onDismiss = viewModel::closePaymentModal,
                                onSubmitPayment = viewModel::confirmPayment,
                                formatIQD = viewModel::formatIQD
                            )
                        }

                        if (showPorterPayoutModal) {
                            PorterPayoutModal(
                                totalCollected = porterFeesCollected,
                                porterCount = porterCount,
                                onDismiss = viewModel::closePorterPayoutModal,
                                onSubmitPayout = viewModel::confirmPorterPayout,
                                formatIQD = viewModel::formatIQD
                            )
                        }

                        if (showDebtAlertsDialog) {
                            DebtAlertsDialog(
                                debts = customerDebts,
                                onDismiss = viewModel::closeDebtAlerts,
                                onOpenPaymentModal = viewModel::openPaymentModal,
                                formatIQD = viewModel::formatIQD
                            )
                        }

                        if (showLicenseLockDialog) {
                            LicenseLockDialog(
                                onDismiss = viewModel::closeLicenseLock
                            )
                        }

                        if (showPrinterSetupModal) {
                            PrinterSetupModal(
                                onDismiss = viewModel::closePrinterSetupModal,
                                printerConnected = printerConnected,
                                connectedDeviceName = printerDevice?.name ?: "RPP02N Thermal POS",
                                onConnectDevice = viewModel::connectPrinterDevice,
                                onDisconnectPrinter = viewModel::disconnectPrinter,
                                onPrintTestPage = viewModel::printTestPage
                            )
                        }

                        // Thermal Receipt Print Modal Sheet
                        if (showPrintPreviewModal && selectedPrintInvoice != null) {
                            val invoice = selectedPrintInvoice!!
                            ModalBottomSheet(
                                onDismissRequest = viewModel::closePrintPreview,
                                containerColor = BackgroundSoft
                            ) {
                                ThermalReceiptPreview(
                                    invoice = invoice,
                                    alwaName = alwaName,
                                    ownerName = ownerName,
                                    phoneNumber = phoneNumber,
                                    location = location,
                                    accountantName = accountantName,
                                    printerConnected = printerConnected,
                                    onOpenPrinterSetup = viewModel::openPrinterSetupModal,
                                    onPrintBluetooth = viewModel::closePrintPreview,
                                    onPrintSystem = viewModel::closePrintPreview,
                                    onShareImage = viewModel::closePrintPreview
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
