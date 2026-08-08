package com.example.ui.modals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.printer.PrinterDevice
import com.example.printer.ThermalPrinterManager
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrinterSetupModal(
    onDismiss: () -> Unit,
    printerConnected: Boolean,
    connectedDeviceName: String,
    onConnectDevice: (PrinterDevice) -> Unit,
    onDisconnectPrinter: () -> Unit,
    onPrintTestPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isScanning by remember { mutableStateOf(false) }
    var paperSize by remember { mutableStateOf("58mm") }
    var printMode by remember { mutableStateOf("صورة Raster (يدعم العربية 100%)") }
    val devices = remember { mutableStateOf(ThermalPrinterManager.getPairedBluetoothDevices()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = BackgroundSoft,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
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
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(DarkForestGreen.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Rounded.Print,
                            contentDescription = null,
                            tint = DarkForestGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            "اقتران طابعة الفواتير الحرارية",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            "متوافق مع طابعات 58mm / 80mm المحمولة (ESC/POS)",
                            fontSize = 11.sp,
                            color = TextSecondaryMuted
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Rounded.Close, contentDescription = "إغلاق", tint = TextSecondaryMuted)
                }
            }

            Divider(color = GlassBorder)

            // Current Connection Status Banner
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(16.dp)),
                color = if (printerConnected) EmeraldSuccessLight else RedWarningLight,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (printerConnected) EmeraldSuccess.copy(alpha = 0.3f) else RedWarning.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = if (printerConnected) Icons.Rounded.BluetoothConnected else Icons.Rounded.BluetoothDisabled,
                            contentDescription = null,
                            tint = if (printerConnected) EmeraldSuccess else RedWarning,
                            modifier = Modifier.size(26.dp)
                        )
                        Column {
                            Text(
                                text = if (printerConnected) "متصل الآن: $connectedDeviceName" else "غير متصل بأي طابعة",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (printerConnected) EmeraldSuccess else RedWarning
                            )
                            Text(
                                text = if (printerConnected) "جاهز لطباعة الفواتير مباشرة عبر البلوتوث" else "يرجى اختيار طابعة من القائمة أدناه للاقتراَن",
                                fontSize = 11.sp,
                                color = TextSecondaryMuted
                            )
                        }
                    }

                    if (printerConnected) {
                        Button(
                            onClick = onDisconnectPrinter,
                            colors = ButtonDefaults.buttonColors(containerColor = RedWarning),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("قطع الاتصال", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }

            // Settings Configuration (Paper width & Print Mode)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("إعدادات الورق ونمط الطباعة:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = paperSize == "58mm",
                            onClick = { paperSize = "58mm" },
                            label = { Text("ورق حراري 58mm (محمول)") },
                            leadingIcon = {
                                if (paperSize == "58mm") Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            },
                            modifier = Modifier.weight(1f)
                        )

                        FilterChip(
                            selected = paperSize == "80mm",
                            onClick = { paperSize = "80mm" },
                            label = { Text("ورق حراري 80mm (قياسي)") },
                            leadingIcon = {
                                if (paperSize == "80mm") Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Rounded.Info, contentDescription = null, tint = SkyBlueInfo, modifier = Modifier.size(18.dp))
                        Text(
                            "تستخدم التطبيق معالجة Bitmap ESC/POS لضمان طباعة النص العربي بوضوح وبدون أحرف مشوهة على جميع الطابعات الصينية.",
                            fontSize = 10.sp,
                            color = TextSecondaryMuted,
                            lineHeight = 14.sp
                        )
                    }
                }
            }

            // Devices Section Header with Scan Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "الطابعات المتاحة والمقترنة:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )

                TextButton(
                    onClick = {
                        isScanning = true
                        devices.value = ThermalPrinterManager.getPairedBluetoothDevices()
                        isScanning = false
                    }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(16.dp), tint = MediumForestGreen)
                        Text(if (isScanning) "جاري تحديث القائمة..." else "تحديث قوالب البلوتوث", fontSize = 12.sp, color = MediumForestGreen)
                    }
                }
            }

            // Device List
            if (devices.value.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CardSurfaceWhite,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Rounded.BluetoothSearching, contentDescription = null, tint = TextSecondaryMuted, modifier = Modifier.size(32.dp))
                        Text(
                            "لم يتم العثور على طابعات بلوتوث مقترنة",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            "يرجى تشغيل البلوتوث واقتران الطابعة الحرارية من إعدادات النظام أولاً، ثم اضغط تحديث.",
                            fontSize = 11.sp,
                            color = TextSecondaryMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(devices.value) { dev ->
                    val isThisConnected = printerConnected && connectedDeviceName.contains(dev.name.take(6))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onConnectDevice(dev)
                            },
                        color = if (isThisConnected) EmeraldSuccessLight else CardSurfaceWhite,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isThisConnected) EmeraldSuccess else GlassBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isThisConnected) EmeraldSuccess else DarkForestGreen.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Rounded.Bluetooth,
                                        contentDescription = null,
                                        tint = if (isThisConnected) Color.White else DarkForestGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        dev.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryDark
                                    )
                                    Text(
                                        "العنوان: ${dev.address} | ${dev.paperSize}",
                                        fontSize = 11.sp,
                                        color = TextSecondaryMuted
                                    )
                                }
                            }

                            if (isThisConnected) {
                                Surface(
                                    color = EmeraldSuccess,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        "متصل",
                                        fontSize = 10.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            } else {
                                OutlinedButton(
                                    onClick = { onConnectDevice(dev) },
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                ) {
                                    Text("اتصال", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onPrintTestPage,
                    enabled = printerConnected,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkForestGreen),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Print, contentDescription = null, tint = GoldLicense, modifier = Modifier.size(18.dp))
                        Text("طباعة صفحة اختبار", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("تم الإعداد", fontSize = 12.sp, color = TextPrimaryDark)
                }
            }
        }
    }
}
