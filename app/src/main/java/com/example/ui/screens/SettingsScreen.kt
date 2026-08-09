package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.printer.PrinterDevice
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    alwaName: String,
    ownerName: String,
    phoneNumber: String,
    location: String,
    accountantName: String,
    onSaveAlwaInfo: (String, String, String, String, String) -> Unit,
    printerConnected: Boolean,
    printerDevice: PrinterDevice? = null,
    autoConnectPrinter: Boolean = true,
    onToggleAutoConnect: (Boolean) -> Unit = {},
    autoConnectStatus: String? = null,
    autoConnectSecondsLeft: Int = 0,
    onTogglePrinter: () -> Unit,
    onPrintTestPage: () -> Unit,
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit,
    passcodeEnabled: Boolean,
    onTogglePasscode: (Boolean) -> Unit,
    onLockApp: () -> Unit = {},
    pinCode: String,
    onUpdatePinCode: (String) -> Unit,
    immersiveMode: Boolean,
    onToggleImmersive: (Boolean) -> Unit,
    animationsEnabled: Boolean,
    onToggleAnimations: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onToggleNotifications: (Boolean) -> Unit,
    receiptCopies: Int,
    receiptFooterNote: String,
    onSaveReceiptSettings: (Int, String) -> Unit,
    onExportBackup: () -> Unit,
    onImportBackup: () -> Unit,
    onResetData: () -> Unit,
    onOpenLicenseModal: () -> Unit,
    modifier: Modifier = Modifier
) {
    var editAlwaName by remember(alwaName) { mutableStateOf(alwaName) }
    var editOwnerName by remember(ownerName) { mutableStateOf(ownerName) }
    var editPhone by remember(phoneNumber) { mutableStateOf(phoneNumber) }
    var editLocation by remember(location) { mutableStateOf(location) }
    var editAccountant by remember(accountantName) { mutableStateOf(accountantName) }

    var editReceiptCopies by remember(receiptCopies) { mutableStateOf(receiptCopies) }
    var editFooterNote by remember(receiptFooterNote) { mutableStateOf(receiptFooterNote) }

    var showPinDialog by remember { mutableStateOf(false) }
    var newPinInput by remember { mutableStateOf("") }
    var confirmPinInput by remember { mutableStateOf("") }
    var pinErrorText by remember { mutableStateOf<String?>(null) }

    var showResetConfirmDialog by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isTabletLandscape = maxWidth > 600.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "لوحة التحكم والإعدادات الشاملة",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimaryDark,
                            fontFamily = CairoFontFamily
                        )
                        Text(
                            text = "تخصيص النظام، الطابعة الحرارية، الأمان والنسخ الاحتياطي",
                            fontSize = 12.sp,
                            color = TextSecondaryMuted,
                            fontFamily = CairoFontFamily
                        )
                    }

                    Surface(
                        color = DarkForestGreen.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Rounded.Dashboard, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(16.dp))
                            Text(
                                text = if (isTabletLandscape) "نمط تابلت Bento Grid" else "إعدادات النظام",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkForestGreen,
                                fontFamily = CairoFontFamily
                            )
                        }
                    }
                }
            }

            if (isTabletLandscape) {
                // -------------------------------------------------------------
                // TABLET BENTO GRID LAYOUT (10-Inch Landscape Optimization)
                // -------------------------------------------------------------

                // Bento Row 1: Printer & Golden License
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(modifier = Modifier.weight(1.3f)) {
                            BentoPrinterCard(
                                printerConnected = printerConnected,
                                printerDevice = printerDevice,
                                autoConnectPrinter = autoConnectPrinter,
                                onToggleAutoConnect = onToggleAutoConnect,
                                autoConnectStatus = autoConnectStatus,
                                autoConnectSecondsLeft = autoConnectSecondsLeft,
                                onTogglePrinter = onTogglePrinter,
                                onPrintTestPage = onPrintTestPage
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            BentoGoldenLicenseCard(onOpenLicenseModal = onOpenLicenseModal)
                        }
                    }
                }

                // Bento Row 2: Alwa Info & Receipt Customization
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(modifier = Modifier.weight(1.2f)) {
                            BentoAlwaInfoCard(
                                editAlwaName = editAlwaName,
                                onAlwaNameChange = { editAlwaName = it },
                                editOwnerName = editOwnerName,
                                onOwnerNameChange = { editOwnerName = it },
                                editPhone = editPhone,
                                onPhoneChange = { editPhone = it },
                                editLocation = editLocation,
                                onLocationChange = { editLocation = it },
                                editAccountant = editAccountant,
                                onAccountantChange = { editAccountant = it },
                                onSave = { onSaveAlwaInfo(editAlwaName, editOwnerName, editPhone, editLocation, editAccountant) }
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            BentoReceiptCard(
                                editReceiptCopies = editReceiptCopies,
                                onCopiesChange = { editReceiptCopies = it },
                                editFooterNote = editFooterNote,
                                onFooterNoteChange = { editFooterNote = it },
                                onSave = { onSaveReceiptSettings(editReceiptCopies, editFooterNote) }
                            )
                        }
                    }
                }

                // Bento Row 3: Security, Font Scale, Backup
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            BentoSecurityCard(
                                passcodeEnabled = passcodeEnabled,
                                onTogglePasscode = onTogglePasscode,
                                onLockApp = onLockApp,
                                pinCode = pinCode,
                                onChangePinClick = {
                                    newPinInput = ""
                                    confirmPinInput = ""
                                    pinErrorText = null
                                    showPinDialog = true
                                },
                                immersiveMode = immersiveMode,
                                onToggleImmersive = onToggleImmersive,
                                animationsEnabled = animationsEnabled,
                                onToggleAnimations = onToggleAnimations
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            BentoAccessibilityCard(
                                fontScale = fontScale,
                                onFontScaleChange = onFontScaleChange
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            BentoBackupDataCard(
                                onExportBackup = onExportBackup,
                                onResetDataClick = { showResetConfirmDialog = true }
                            )
                        }
                    }
                }

                // Footer Row
                item {
                    BentoFooterCard()
                }

            } else {
                // -------------------------------------------------------------
                // PHONE / PORTRAIT SINGLE COLUMN LAYOUT
                // -------------------------------------------------------------
                item {
                    BentoPrinterCard(
                        printerConnected = printerConnected,
                        printerDevice = printerDevice,
                        autoConnectPrinter = autoConnectPrinter,
                        onToggleAutoConnect = onToggleAutoConnect,
                        autoConnectStatus = autoConnectStatus,
                        autoConnectSecondsLeft = autoConnectSecondsLeft,
                        onTogglePrinter = onTogglePrinter,
                        onPrintTestPage = onPrintTestPage
                    )
                }

                item {
                    BentoAlwaInfoCard(
                        editAlwaName = editAlwaName,
                        onAlwaNameChange = { editAlwaName = it },
                        editOwnerName = editOwnerName,
                        onOwnerNameChange = { editOwnerName = it },
                        editPhone = editPhone,
                        onPhoneChange = { editPhone = it },
                        editLocation = editLocation,
                        onLocationChange = { editLocation = it },
                        editAccountant = editAccountant,
                        onAccountantChange = { editAccountant = it },
                        onSave = { onSaveAlwaInfo(editAlwaName, editOwnerName, editPhone, editLocation, editAccountant) }
                    )
                }

                item {
                    BentoReceiptCard(
                        editReceiptCopies = editReceiptCopies,
                        onCopiesChange = { editReceiptCopies = it },
                        editFooterNote = editFooterNote,
                        onFooterNoteChange = { editFooterNote = it },
                        onSave = { onSaveReceiptSettings(editReceiptCopies, editFooterNote) }
                    )
                }

                item {
                    BentoAccessibilityCard(
                        fontScale = fontScale,
                        onFontScaleChange = onFontScaleChange
                    )
                }

                item {
                    BentoSecurityCard(
                        passcodeEnabled = passcodeEnabled,
                        onTogglePasscode = onTogglePasscode,
                        onLockApp = onLockApp,
                        pinCode = pinCode,
                        onChangePinClick = {
                            newPinInput = ""
                            confirmPinInput = ""
                            pinErrorText = null
                            showPinDialog = true
                        },
                        immersiveMode = immersiveMode,
                        onToggleImmersive = onToggleImmersive,
                        animationsEnabled = animationsEnabled,
                        onToggleAnimations = onToggleAnimations
                    )
                }

                item {
                    BentoBackupDataCard(
                        onExportBackup = onExportBackup,
                        onResetDataClick = { showResetConfirmDialog = true }
                    )
                }

                item {
                    BentoGoldenLicenseCard(onOpenLicenseModal = onOpenLicenseModal)
                }

                item {
                    BentoFooterCard()
                }
            }
        }
    }

    // PIN Customization Dialog
    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text("تغيير رمز قفل التطبيق (PIN)", fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("أدخل رمز جديد مكون من 4 أرقام:", fontSize = 12.sp, color = TextSecondaryMuted, fontFamily = CairoFontFamily)

                    OutlinedTextField(
                        value = newPinInput,
                        onValueChange = { if (it.length <= 4 && it.all { char -> char.isDigit() }) newPinInput = it },
                        label = { Text("الرمز الجديد (4 أرقام)", fontFamily = CairoFontFamily) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = confirmPinInput,
                        onValueChange = { if (it.length <= 4 && it.all { char -> char.isDigit() }) confirmPinInput = it },
                        label = { Text("تأكيد الرمز الجديد", fontFamily = CairoFontFamily) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (pinErrorText != null) {
                        Text(pinErrorText!!, color = RedWarning, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPinInput.length != 4) {
                            pinErrorText = "يجب أن يتكون الرمز من 4 أرقام بالضبط"
                        } else if (newPinInput != confirmPinInput) {
                            pinErrorText = "الرمزان غير متطابقين"
                        } else {
                            onUpdatePinCode(newPinInput)
                            showPinDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkForestGreen)
                ) {
                    Text("تأكيد وحفظ", fontFamily = CairoFontFamily)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) {
                    Text("إلغاء", fontFamily = CairoFontFamily)
                }
            }
        )
    }

    // Reset Confirmation Dialog
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = { Text("تأكيد إعادة ضبط وتصفير السجلات ⚠️", fontWeight = FontWeight.Bold, color = RedWarning, fontFamily = CairoFontFamily) },
            text = {
                Text(
                    "هل أنت متأكد من تصفير وإعادة تهيئة السجلات وقاعدة البيانات؟ سيتم إعادة تحميل البيانات إلى وضعها الافتراضي.",
                    fontSize = 13.sp,
                    color = TextPrimaryDark,
                    fontFamily = CairoFontFamily
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetData()
                        showResetConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedWarning)
                ) {
                    Text("نعم، تصفير السجلات", fontFamily = CairoFontFamily)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("إلغاء", fontFamily = CairoFontFamily)
                }
            }
        )
    }
}

// =============================================================================
// BENTO GRID CARDS COMPONENTS
// =============================================================================

@Composable
fun BentoPrinterCard(
    printerConnected: Boolean,
    printerDevice: PrinterDevice?,
    autoConnectPrinter: Boolean,
    onToggleAutoConnect: (Boolean) -> Unit,
    autoConnectStatus: String?,
    autoConnectSecondsLeft: Int,
    onTogglePrinter: () -> Unit,
    onPrintTestPage: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(22.dp)),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Card Title Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkForestGreen.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Print, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(24.dp))
                    }
                    Column {
                        Text("الطابعة الحرارية (Bluetooth POS)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
                        Text(printerDevice?.name ?: "طابعة إيصالات 58mm / 80mm", fontSize = 11.sp, color = TextSecondaryMuted, fontFamily = CairoFontFamily)
                    }
                }

                Surface(
                    color = if (printerConnected) EmeraldSuccessLight else RedWarningLight,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (printerConnected) EmeraldSuccess else RedWarning)
                        )
                        Text(
                            text = if (printerConnected) "متصلة الآن 🖨️" else "غير متصلة 🔌",
                            color = if (printerConnected) EmeraldSuccess else RedWarning,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = CairoFontFamily
                        )
                    }
                }
            }

            HorizontalDivider(color = GlassBorder)

            // Auto-Connect Control Section
            Surface(
                color = BackgroundSoft,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Rounded.Autorenew, contentDescription = null, tint = MediumForestGreen, modifier = Modifier.size(18.dp))
                            Column {
                                Text("الاتصال التلقائي عند فتح التطبيق", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
                                Text("يبحث عن آخر طابعة لمدة 60 ثانية فور تشغيل الجهاز", fontSize = 10.5.sp, color = TextSecondaryMuted, fontFamily = CairoFontFamily)
                            }
                        }
                        Switch(checked = autoConnectPrinter, onCheckedChange = onToggleAutoConnect)
                    }

                    if (autoConnectPrinter && autoConnectStatus != null) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = autoConnectStatus,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkForestGreen,
                                fontFamily = CairoFontFamily
                            )

                            if (autoConnectSecondsLeft > 0) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    LinearProgressIndicator(
                                        progress = { (60 - autoConnectSecondsLeft) / 60f },
                                        modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                                        color = MediumForestGreen,
                                        trackColor = GlassBorder
                                    )
                                    Text("${autoConnectSecondsLeft} ثانية", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondaryMuted, fontFamily = CairoFontFamily)
                                }
                            }
                        }
                    }
                }
            }

            // Buttons Row
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onTogglePrinter,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (printerConnected) RedWarning else MediumForestGreen
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.2f)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.BluetoothSearching, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Text(if (printerConnected) "إدارة الطابعة" else "البحث والربط 🔍", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                    }
                }

                OutlinedButton(
                    onClick = onPrintTestPage,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Receipt, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(16.dp))
                        Text("طباعة فحص 📄", fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                    }
                }
            }
        }
    }
}

@Composable
fun BentoAlwaInfoCard(
    editAlwaName: String,
    onAlwaNameChange: (String) -> Unit,
    editOwnerName: String,
    onOwnerNameChange: (String) -> Unit,
    editPhone: String,
    onPhoneChange: (String) -> Unit,
    editLocation: String,
    onLocationChange: (String) -> Unit,
    editAccountant: String,
    onAccountantChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(22.dp)),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkForestGreen.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Store, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(20.dp))
                }
                Text("بيانات العلوة والمكتب", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
            }

            OutlinedTextField(
                value = editAlwaName,
                onValueChange = onAlwaNameChange,
                label = { Text("اسم العلوة (رأس الفاتورة)", fontFamily = CairoFontFamily) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = editOwnerName,
                onValueChange = onOwnerNameChange,
                label = { Text("اسم صاحب العلوة", fontFamily = CairoFontFamily) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = editPhone,
                onValueChange = onPhoneChange,
                label = { Text("رقم الهاتف للتواصل", fontFamily = CairoFontFamily) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = editLocation,
                onValueChange = onLocationChange,
                label = { Text("موقع العلوة والعنوان", fontFamily = CairoFontFamily) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = editAccountant,
                onValueChange = onAccountantChange,
                label = { Text("اسم المحاسب المسؤول", fontFamily = CairoFontFamily) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = DarkForestGreen),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Save, contentDescription = null, tint = GoldLicense)
                    Text("حفظ معلومات العلوة 💾", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                }
            }
        }
    }
}

@Composable
fun BentoReceiptCard(
    editReceiptCopies: Int,
    onCopiesChange: (Int) -> Unit,
    editFooterNote: String,
    onFooterNoteChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(22.dp)),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MediumForestGreen.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.ReceiptLong, contentDescription = null, tint = MediumForestGreen, modifier = Modifier.size(20.dp))
                }
                Text("تخصيص ورقة الفاتورة والطباعة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("عدد النسخ المطبوعة تلقائياً:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = editReceiptCopies == 1,
                        onClick = { onCopiesChange(1) },
                        label = { Text("نسخة واحدة", fontFamily = CairoFontFamily, fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = editReceiptCopies == 2,
                        onClick = { onCopiesChange(2) },
                        label = { Text("نسختان (زبون + مكتب)", fontFamily = CairoFontFamily, fontSize = 11.sp) }
                    )
                }
            }

            OutlinedTextField(
                value = editFooterNote,
                onValueChange = onFooterNoteChange,
                label = { Text("ملاحظات أسفل الفاتورة (مطبوعة)", fontFamily = CairoFontFamily) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = MediumForestGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("حفظ إعدادات الفاتورة 📑", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
            }
        }
    }
}

@Composable
fun BentoAccessibilityCard(
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(22.dp)),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SkyBlueInfo.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Accessibility, contentDescription = null, tint = SkyBlueInfo, modifier = Modifier.size(20.dp))
                    }
                    Text("تكبير الخط وقراءة السوق", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
                }

                Surface(color = SkyBlueInfoLight, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "${(fontScale * 100).toInt()}%",
                        color = SkyBlueInfo,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontFamily = CairoFontFamily
                    )
                }
            }

            Text("تكبير خط النصوص والأرقام لسهولة القراءة في السوق والموقع:", fontSize = 11.5.sp, color = TextSecondaryMuted, fontFamily = CairoFontFamily)

            Slider(
                value = fontScale,
                onValueChange = onFontScaleChange,
                valueRange = 1.0f..1.5f,
                steps = 4,
                colors = SliderDefaults.colors(thumbColor = MediumForestGreen, activeTrackColor = MediumForestGreen)
            )

            // Live preview
            Surface(
                color = BackgroundSoft,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "معاينة: طماطة نجفية - 1,250,000 د.ع",
                    fontSize = (13 * fontScale).sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForestGreen,
                    fontFamily = CairoFontFamily,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun BentoSecurityCard(
    passcodeEnabled: Boolean,
    onTogglePasscode: (Boolean) -> Unit,
    onLockApp: () -> Unit,
    pinCode: String,
    onChangePinClick: () -> Unit,
    immersiveMode: Boolean,
    onToggleImmersive: (Boolean) -> Unit,
    animationsEnabled: Boolean,
    onToggleAnimations: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(22.dp)),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(RedWarning.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Security, contentDescription = null, tint = RedWarning, modifier = Modifier.size(20.dp))
                    }
                    Text("الأمان وقفل النظام", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
                }

                Switch(checked = passcodeEnabled, onCheckedChange = onTogglePasscode)
            }

            if (passcodeEnabled) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("رمز الـ PIN الحالي: $pinCode", fontSize = 11.5.sp, color = TextSecondaryMuted, fontFamily = CairoFontFamily)

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = onLockApp,
                            colors = ButtonDefaults.buttonColors(containerColor = RedWarning, contentColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Rounded.Lock, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("قفل الآن", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                        }

                        TextButton(onClick = onChangePinClick) {
                            Text("تغيير الرمز", fontSize = 11.sp, color = DarkForestGreen, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                        }
                    }
                }
            }

            HorizontalDivider(color = GlassBorder)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("وضع ملء الشاشة الكامل", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
                Switch(checked = immersiveMode, onCheckedChange = onToggleImmersive)
            }

            HorizontalDivider(color = GlassBorder)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("تأثيرات الانتقالات والإنيميشن", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
                Switch(checked = animationsEnabled, onCheckedChange = onToggleAnimations)
            }
        }
    }
}

@Composable
fun BentoBackupDataCard(
    onExportBackup: () -> Unit,
    onResetDataClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(22.dp)),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkForestGreen.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.SdCard, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(20.dp))
                }
                Text("النسخ الاحتياطي والبيانات", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
            }

            Button(
                onClick = onExportBackup,
                colors = ButtonDefaults.buttonColors(containerColor = DarkForestGreen),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(46.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Save, contentDescription = null, tint = GoldLicense, modifier = Modifier.size(18.dp))
                    Text("حفظ نسخة احتياطية فورية 💾", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                }
            }

            OutlinedButton(
                onClick = onResetDataClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = RedWarning),
                border = BorderStroke(1.dp, RedWarning.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.DeleteSweep, contentDescription = null, tint = RedWarning, modifier = Modifier.size(18.dp))
                    Text("تصفير البيانات وإعادة الضبط ⚠️", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = RedWarning, fontFamily = CairoFontFamily)
                }
            }
        }
    }
}

@Composable
fun BentoGoldenLicenseCard(
    onOpenLicenseModal: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp)),
        color = GoldLicense,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Rounded.Verified, contentDescription = null, tint = TextPrimaryDark, modifier = Modifier.size(24.dp))
                    Text("ترخيص النظام", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
                }

                Surface(color = TextPrimaryDark, shape = RoundedCornerShape(12.dp)) {
                    Text("مفعل مدى الحياة 💎", color = GoldLicense, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontFamily = CairoFontFamily)
                }
            }

            Text("الاشتراك الذهبي المميز مدى الحياة 💎", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
            Text("رقم المشترك: ALWA-HWID-84091-IQ99", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)

            HorizontalDivider(color = TextPrimaryDark.copy(alpha = 0.2f))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("✓ طباعة فورية للطابعات الحرارية بدون انترنت", fontSize = 11.sp, color = TextPrimaryDark, fontFamily = CairoFontFamily)
                Text("✓ دعم مفتوح لأقسام الاستيراد والمبيعات والديون", fontSize = 11.sp, color = TextPrimaryDark, fontFamily = CairoFontFamily)
            }

            Button(
                onClick = onOpenLicenseModal,
                colors = ButtonDefaults.buttonColors(containerColor = TextPrimaryDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("تفاصيل التوثيق والترخيص 📜", color = GoldLicense, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
            }
        }
    }
}

@Composable
fun BentoFooterCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(20.dp)),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("جميع الحقوق محفوظة لصالح شركة برايم™ للحلول البرمجية © 2026", fontSize = 11.sp, color = TextSecondaryMuted, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("بغداد: 07749883474", fontSize = 11.sp, color = DarkForestGreen, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                Text("الموصل: 07883885156", fontSize = 11.sp, color = DarkForestGreen, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
            }
        }
    }
}
