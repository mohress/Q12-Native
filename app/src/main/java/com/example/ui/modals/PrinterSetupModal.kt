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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()
    var isScanning by remember { mutableStateOf(false) }
    var paperSize by remember { mutableStateOf("58mm") }
    var printMode by remember { mutableStateOf("صورة Raster (يدعم العربية 100%)") }
    val devices = remember { mutableStateOf(ThermalPrinterManager.getPairedBluetoothDevices()) }
    var showManualAdd by remember { mutableStateOf(false) }
    var manualName by remember { mutableStateOf("") }
    var manualAddress by remember { mutableStateOf("") }

    fun runScan() {
        coroutineScope.launch {
            isScanning = true
            ThermalPrinterManager.startDiscovery()
            delay(1200)
            devices.value = ThermalPrinterManager.getPairedBluetoothDevices()
            isScanning = false
        }
    }

    LaunchedEffect(Unit) {
        runScan()
    }

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

            // Devices Section Header with Active Search Button
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

                Button(
                    onClick = { runScan() },
                    enabled = !isScanning,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkForestGreen),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isScanning) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Rounded.BluetoothSearching, contentDescription = null, modifier = Modifier.size(16.dp), tint = GoldLicense)
                        }
                        Text(if (isScanning) "جاري البحث..." else "البحث عن الطابعات", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Scanning progress indicator
            if (isScanning) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MediumForestGreen,
                        trackColor = LightForestGreen.copy(alpha = 0.3f)
                    )
                    Text(
                        "جاري فحص الإشارة ومسح طابعات البلوتوث الحرارية المجاورة...",
                        fontSize = 11.sp,
                        color = MediumForestGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Device List
            if (devices.value.isEmpty() && !isScanning) {
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
                            "لم يتم العثور على طابعات بلوتوث",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            "اضغط على زر البحث أعلاه لإعادة فحص الأجهزة أو تأكد من تشغيل البلوتوث.",
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
                        .heightIn(max = 220.dp),
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
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
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
                                            "متصل الآن",
                                            fontSize = 11.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                } else {
                                    Button(
                                        onClick = { onConnectDevice(dev) },
                                        colors = ButtonDefaults.buttonColors(containerColor = MediumForestGreen),
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text("اقتران وتوصيل", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Manual printer addition option
            OutlinedButton(
                onClick = { showManualAdd = !showManualAdd },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (showManualAdd) Icons.Rounded.ExpandLess else Icons.Rounded.AddCircleOutline,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text("إضافة طابعة حرارية برقم MAC أو اسم يدوياً", fontSize = 11.sp)
                }
            }

            AnimatedVisibility(visible = showManualAdd) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CardSurfaceWhite,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = manualName,
                            onValueChange = { manualName = it },
                            label = { Text("اسم الطابعة (مثال: RPP02N)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = manualAddress,
                            onValueChange = { manualAddress = it },
                            label = { Text("عنوان MAC (مثال: 00:11:22:33:44:55)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = {
                                if (manualName.isNotBlank() && manualAddress.isNotBlank()) {
                                    val customDev = PrinterDevice(manualName, manualAddress, isPaired = true, paperSize = paperSize)
                                    devices.value = devices.value + customDev
                                    onConnectDevice(customDev)
                                    manualName = ""
                                    manualAddress = ""
                                    showManualAdd = false
                                }
                            },
                            enabled = manualName.isNotBlank() && manualAddress.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkForestGreen),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("اقتران الطابعة اليدوية", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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

