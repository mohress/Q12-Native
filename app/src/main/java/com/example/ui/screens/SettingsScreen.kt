package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onTogglePrinter: () -> Unit,
    onPrintTestPage: () -> Unit,
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit,
    passcodeEnabled: Boolean,
    onTogglePasscode: (Boolean) -> Unit,
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Bluetooth Thermal Printer Settings Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
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
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkForestGreen.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Rounded.Print, contentDescription = null, tint = DarkForestGreen)
                            }
                            Column {
                                Text("طابعة الفواتير (Bluetooth BLE)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                Text("الاتصال بطابعات الفواتير الحرارية (RPP02N / PT-210)", fontSize = 11.sp, color = TextSecondaryMuted)
                            }
                        }

                        Surface(
                            color = if (printerConnected) EmeraldSuccessLight else RedWarningLight,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (printerConnected) "متصلة الآن" else "غير متصلة",
                                color = if (printerConnected) EmeraldSuccess else RedWarning,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

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
                                Text(if (printerConnected) "إدارة الاتصال" else "البحث عن الطابعات", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        OutlinedButton(
                            onClick = onPrintTestPage,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Receipt, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(16.dp))
                                Text("طباعة فحص", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 2. Alwa Info Form
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Rounded.Store, contentDescription = null, tint = DarkForestGreen)
                        Text("معلومات العلوة والمكتب", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                    }

                    OutlinedTextField(
                        value = editAlwaName,
                        onValueChange = { editAlwaName = it },
                        label = { Text("اسم العلوة (يظهر برأس الفاتورة)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = editOwnerName,
                        onValueChange = { editOwnerName = it },
                        label = { Text("اسم صاحب العلوة") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("رقم الهاتف للتواصل") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = editLocation,
                        onValueChange = { editLocation = it },
                        label = { Text("موقع العلوة والعنوان") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = editAccountant,
                        onValueChange = { editAccountant = it },
                        label = { Text("اسم المحاسب المسؤول") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = { onSaveAlwaInfo(editAlwaName, editOwnerName, editPhone, editLocation, editAccountant) },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkForestGreen),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Save, contentDescription = null, tint = GoldLicense)
                            Text("حفظ معلومات العلوة", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. Receipt Design & Printing Settings
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Rounded.ReceiptLong, contentDescription = null, tint = DarkForestGreen)
                        Text("تخصيص ورقة الفاتورة والطباعة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("عدد النسخ المطبوعة تلقائياً:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = editReceiptCopies == 1,
                                onClick = { editReceiptCopies = 1 },
                                label = { Text("نسخة واحدة") }
                            )
                            FilterChip(
                                selected = editReceiptCopies == 2,
                                onClick = { editReceiptCopies = 2 },
                                label = { Text("نسختان (زبون + مكتب)") }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = editFooterNote,
                        onValueChange = { editFooterNote = it },
                        label = { Text("ملاحظات أسفل الفاتورة (تظهر مطبوعة)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = { onSaveReceiptSettings(editReceiptCopies, editFooterNote) },
                        colors = ButtonDefaults.buttonColors(containerColor = MediumForestGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("حفظ إعدادات ورقة الفاتورة", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 4. Accessibility & Vision Panel
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
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
                            Icon(Icons.Rounded.Accessibility, contentDescription = null, tint = MediumForestGreen)
                            Text("تكبير الخط وقراءة السوق", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                        }

                        Surface(color = SkyBlueInfoLight, shape = RoundedCornerShape(8.dp)) {
                            Text(
                                text = "${(fontScale * 100).toInt()}% (${if (fontScale <= 1.0f) "طبيعي" else "مكبر"})",
                                color = SkyBlueInfo,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text("تكبير خط النصوص والأرقام لسهولة القراءة في السوق والموقع:", fontSize = 12.sp, color = TextSecondaryMuted)

                    Slider(
                        value = fontScale,
                        onValueChange = onFontScaleChange,
                        valueRange = 1.0f..1.5f,
                        steps = 4,
                        colors = SliderDefaults.colors(thumbColor = MediumForestGreen, activeTrackColor = MediumForestGreen)
                    )

                    // Live font preview sample
                    Surface(
                        color = BackgroundSoft,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "معاينة: طماطة نجفية - 1,250,000 د.ع",
                            fontSize = (14 * fontScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkForestGreen,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        // 5. Switches & Security Panel
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("رمز مرور قفل التطبيق (PIN)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                            Text("الرمز الحالي: $pinCode", fontSize = 11.sp, color = TextSecondaryMuted)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (passcodeEnabled) {
                                TextButton(onClick = {
                                    newPinInput = ""
                                    confirmPinInput = ""
                                    pinErrorText = null
                                    showPinDialog = true
                                }) {
                                    Text("تغيير الرمز", fontSize = 11.sp, color = DarkForestGreen, fontWeight = FontWeight.Bold)
                                }
                            }
                            Switch(checked = passcodeEnabled, onCheckedChange = onTogglePasscode)
                        }
                    }

                    HorizontalDivider(color = GlassBorder)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("وضع ملء الشاشة الكامل (Immersive Mode)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                        Switch(checked = immersiveMode, onCheckedChange = onToggleImmersive)
                    }

                    HorizontalDivider(color = GlassBorder)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("تأثيرات الحركة والانتقالات (Animations)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                        Switch(checked = animationsEnabled, onCheckedChange = onToggleAnimations)
                    }

                    HorizontalDivider(color = GlassBorder)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("الإشعارات والتنبيهات الصوتية", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                        Switch(checked = notificationsEnabled, onCheckedChange = onToggleNotifications)
                    }
                }
            }
        }

        // 6. Offline Backup & Data Management Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Rounded.Storage, contentDescription = null, tint = DarkForestGreen)
                        Text("النسخ الاحتياطي وإدارة البيانات", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onExportBackup,
                            colors = ButtonDefaults.buttonColors(containerColor = SkyBlueInfo),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Download, contentDescription = null, tint = Color.White)
                                Text("تصدير نسخة احتياطية", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        OutlinedButton(
                            onClick = onImportBackup,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Upload, contentDescription = null, tint = DarkForestGreen)
                                Text("استيراد النسخة الاحتياطية", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = { showResetConfirmDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RedWarning),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RedWarning.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.DeleteSweep, contentDescription = null, tint = RedWarning)
                            Text("تصفير وإعادة ضبط السجلات والبيانات", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RedWarning)
                        }
                    }
                }
            }
        }

        // 7. Golden Lifetime Subscription Status Card
        item {
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
                            Text("اشتراك النظام", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = TextPrimaryDark)
                        }

                        Surface(color = TextPrimaryDark, shape = RoundedCornerShape(12.dp)) {
                            Text("مفعل مدى الحياة 💎", color = GoldLicense, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                        }
                    }

                    Text("الاشتراك الذهبي المميز مدى الحياة 💎", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimaryDark)
                    Text("الحالة: نشط ومفعل بالكامل مدى الحياة ♾️", fontSize = 13.sp, color = TextPrimaryDark)
                    Text("رقم المشترك (HWID): ALWA-HWID-84091-IQ99", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)

                    HorizontalDivider(color = TextPrimaryDark.copy(alpha = 0.2f))

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("✓ دعم مفتوح لأجهزة الفواتير والطباعة الحرارية", fontSize = 11.sp, color = TextPrimaryDark)
                        Text("✓ حماية وتشفير عالي السعة بدون انترنت", fontSize = 11.sp, color = TextPrimaryDark)
                        Text("✓ دعم فني مباشر وسريع من الشركة المجهزة", fontSize = 11.sp, color = TextPrimaryDark)
                    }

                    Button(
                        onClick = onOpenLicenseModal,
                        colors = ButtonDefaults.buttonColors(containerColor = TextPrimaryDark),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("عرض تفاصيل الترخيص والتوثيق", color = GoldLicense, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 8. Development Rights & Technical Support Footer
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(20.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("جميع الحقوق محفوظة لصالح شركة برايم™ للحلول البرمجية © 2026", fontSize = 11.sp, color = TextSecondaryMuted, fontWeight = FontWeight.Bold)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("مكتب بغداد: 07749883474", fontSize = 11.sp, color = DarkForestGreen, fontWeight = FontWeight.Bold)
                        Text("مكتب الموصل: 07883885156", fontSize = 11.sp, color = DarkForestGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // PIN Customization Dialog
    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text("تغيير رمز قفل التطبيق (PIN)", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("أدخل رمز جديد مكون من 4 أرقام:", fontSize = 12.sp, color = TextSecondaryMuted)

                    OutlinedTextField(
                        value = newPinInput,
                        onValueChange = { if (it.length <= 4 && it.all { char -> char.isDigit() }) newPinInput = it },
                        label = { Text("الرمز الجديد (4 أرقام)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = confirmPinInput,
                        onValueChange = { if (it.length <= 4 && it.all { char -> char.isDigit() }) confirmPinInput = it },
                        label = { Text("تأكيد الرمز الجديد") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (pinErrorText != null) {
                        Text(pinErrorText!!, color = RedWarning, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                    Text("تأكيد وحفظ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Reset Confirmation Dialog
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = { Text("تأكيد إعادة ضبط وتصفير السجلات ⚠️", fontWeight = FontWeight.Bold, color = RedWarning) },
            text = {
                Text(
                    "هل أنت متأكد من تصفير وإعادة تهيئة السجلات وقاعدة البيانات؟ سيتم إعادة تحميل البيانات إلى وضعها الافتراضي.",
                    fontSize = 13.sp,
                    color = TextPrimaryDark
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
                    Text("نعم، تصفير السجلات")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }
}
