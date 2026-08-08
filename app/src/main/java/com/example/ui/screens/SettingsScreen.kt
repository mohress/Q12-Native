package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit,
    passcodeEnabled: Boolean,
    onTogglePasscode: (Boolean) -> Unit,
    immersiveMode: Boolean,
    onToggleImmersive: (Boolean) -> Unit,
    animationsEnabled: Boolean,
    onToggleAnimations: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onToggleNotifications: (Boolean) -> Unit,
    onOpenLicenseModal: () -> Unit,
    modifier: Modifier = Modifier
) {
    var editAlwaName by remember { mutableStateOf(alwaName) }
    var editOwnerName by remember { mutableStateOf(ownerName) }
    var editPhone by remember { mutableStateOf(phoneNumber) }
    var editLocation by remember { mutableStateOf(location) }
    var editAccountant by remember { mutableStateOf(accountantName) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Bluetooth BLE Printer Settings Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                BoxWithConstraints(modifier = Modifier.padding(18.dp)) {
                    val isTablet = maxWidth > 600.dp

                    if (isTablet) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1.5f)
                            ) {
                                Icon(Icons.Rounded.Bluetooth, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(28.dp))
                                Column {
                                    Text("إعدادات طابعة الفواتير (Bluetooth BLE)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                    Text("الاتصال المباشر بطابعات الفواتير الحرارية 80mm/58mm", fontSize = 11.sp, color = TextSecondaryMuted)
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    color = if (printerConnected) EmeraldSuccessLight else RedWarningLight,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (printerConnected) "متصل بطابعة RPP02N" else "الطابعة غير متصلة",
                                        color = if (printerConnected) EmeraldSuccess else RedWarning,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                Button(
                                    onClick = onTogglePrinter,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (printerConnected) RedWarning else MediumForestGreen
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(if (printerConnected) "قطع الاتصال" else "اقتران وفحص", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Rounded.Bluetooth, contentDescription = null, tint = DarkForestGreen)
                                    Text("إعدادات طابعة الفواتير (Bluetooth BLE)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                }

                                Surface(
                                    color = if (printerConnected) EmeraldSuccessLight else RedWarningLight,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (printerConnected) "متصل بطابعة RPP02N" else "غير متصل",
                                        color = if (printerConnected) EmeraldSuccess else RedWarning,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = onTogglePrinter,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (printerConnected) RedWarning else MediumForestGreen
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(if (printerConnected) "قطع الاتصال" else "اقتران وفحص", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { /* Test print */ },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("طباعة فحص", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Alwa Info Form (تنسيق الحقول التفاعلي العرضي على التابلت)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                BoxWithConstraints(modifier = Modifier.padding(18.dp)) {
                    val isTablet = maxWidth > 600.dp

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("معلومات العلوة والمكتب", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)

                        if (isTablet) {
                            // Responsive Horizontal 2-Column Grid for Tablet Landscape
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    OutlinedTextField(
                                        value = editAlwaName,
                                        onValueChange = { editAlwaName = it },
                                        label = { Text("اسم العلوة (يظهر بالفاتورة)") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    OutlinedTextField(
                                        value = editOwnerName,
                                        onValueChange = { editOwnerName = it },
                                        label = { Text("اسم صاحب العلوة") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    OutlinedTextField(
                                        value = editPhone,
                                        onValueChange = { editPhone = it },
                                        label = { Text("رقم الهاتف") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    OutlinedTextField(
                                        value = editLocation,
                                        onValueChange = { editLocation = it },
                                        label = { Text("موقع العلوة (العنوان)") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                }

                                OutlinedTextField(
                                    value = editAccountant,
                                    onValueChange = { editAccountant = it },
                                    label = { Text("اسم المحاسب (بالإنجليزي حصراً)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        } else {
                            OutlinedTextField(
                                value = editAlwaName,
                                onValueChange = { editAlwaName = it },
                                label = { Text("اسم العلوة (يظهر بالفاتورة)") },
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
                                label = { Text("رقم الهاتف") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            OutlinedTextField(
                                value = editLocation,
                                onValueChange = { editLocation = it },
                                label = { Text("موقع العلوة (العنوان)") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            OutlinedTextField(
                                value = editAccountant,
                                onValueChange = { editAccountant = it },
                                label = { Text("اسم المحاسب (بالإنجليزي حصراً)") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Button(
                            onClick = { onSaveAlwaInfo(editAlwaName, editOwnerName, editPhone, editLocation, editAccountant) },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkForestGreen),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("حفظ المعلومات", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. Accessibility & Vision Panel
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
                            Text("تسهيل الاستخدام وضعاف البصر", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
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

                    Text("تكبير خط النصوص والأرقام لسهولة القراءة في السوق:", fontSize = 12.sp, color = TextSecondaryMuted)

                    Slider(
                        value = fontScale,
                        onValueChange = onFontScaleChange,
                        valueRange = 1.0f..1.5f,
                        steps = 4,
                        colors = SliderDefaults.colors(thumbColor = MediumForestGreen, activeTrackColor = MediumForestGreen)
                    )
                }
            }
        }

        // 4. Switches Panel
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
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("رمز مرور قفل التطبيق (PIN)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                        Switch(checked = passcodeEnabled, onCheckedChange = onTogglePasscode)
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
                        Text("الإشعارات والنظام", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                        Switch(checked = notificationsEnabled, onCheckedChange = onToggleNotifications)
                    }
                }
            }
        }

        // 5. Offline Backup Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { /* Export */ },
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBlueInfo),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Download, contentDescription = null, tint = Color.White)
                            Text("تصدير قاعدة البيانات", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedButton(
                        onClick = { /* Import backup */ },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Upload, contentDescription = null, tint = DarkForestGreen)
                            Text("استيراد ملف backup", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // 6. Golden Lifetime Subscription Status Card
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

        // 7. Development Rights & Technical Support Footer
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
}
