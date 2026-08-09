package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.SalesInvoice
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ThermalReceiptPreview(
    invoice: SalesInvoice,
    alwaName: String,
    ownerName: String,
    phoneNumber: String,
    location: String,
    accountantName: String,
    printerConnected: Boolean,
    onOpenPrinterSetup: () -> Unit,
    onPrintBluetooth: () -> Unit,
    onPrintSystem: () -> Unit,
    onShareImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var paperSize by remember { mutableStateOf("58mm") }
    var isPrintingByBt by remember { mutableStateOf(false) }

    val is58mm = paperSize == "58mm"
    val paperWidth = if (is58mm) 300.dp else 370.dp
    val textScale = if (is58mm) 1.25f else 1.40f

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        val isTabletLandscape = maxWidth >= 600.dp

        if (isTabletLandscape) {
            // Tablet Landscape Two-Column Layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 620.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Left Column: Control Panel & Actions (40% width)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(CardSurfaceWhite, RoundedCornerShape(16.dp))
                        .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Header Title
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = DarkForestGreen,
                                    shape = CircleShape,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Rounded.Print,
                                            contentDescription = null,
                                            tint = GoldLicense,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        "معاينة الفاتورة الحرارية",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = CairoFontFamily,
                                        color = TextPrimaryDark
                                    )
                                    Text(
                                        "جاهز للطباعة والمشاركة",
                                        fontSize = 11.sp,
                                        fontFamily = CairoFontFamily,
                                        color = TextSecondaryMuted
                                    )
                                }
                            }
                        }

                        Divider(color = GlassBorder)

                        // Paper Size Switcher
                        Text(
                            "حجم الورق الحراري:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = CairoFontFamily,
                            color = TextPrimaryDark
                        )
                        Surface(
                            color = BackgroundSoft,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp)),
                                    color = if (is58mm) DarkForestGreen else Color.Transparent,
                                    onClick = { paperSize = "58mm" }
                                ) {
                                    Text(
                                        "58 مم (طابعة محمولة)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = CairoFontFamily,
                                        color = if (is58mm) Color.White else TextSecondaryMuted,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp)),
                                    color = if (!is58mm) DarkForestGreen else Color.Transparent,
                                    onClick = { paperSize = "80mm" }
                                ) {
                                    Text(
                                        "80 مم (طابعة ثابتة)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = CairoFontFamily,
                                        color = if (!is58mm) Color.White else TextSecondaryMuted,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        // Printer Bluetooth Connection Status
                        Surface(
                            color = if (printerConnected) EmeraldSuccessLight else RedWarningLight,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (printerConnected) EmeraldSuccess.copy(alpha = 0.3f) else RedWarning.copy(alpha = 0.3f)
                            ),
                            onClick = onOpenPrinterSetup,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Rounded.Bluetooth,
                                        contentDescription = null,
                                        tint = if (printerConnected) EmeraldSuccess else RedWarning,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = if (printerConnected) "الطابعة متصلة (BT)" else "الطابعة غير متصلة",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = CairoFontFamily,
                                        color = if (printerConnected) EmeraldSuccess else RedWarning
                                    )
                                }
                                Text(
                                    "إعدادات >",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = CairoFontFamily,
                                    color = if (printerConnected) EmeraldSuccess else RedWarning
                                )
                            }
                        }

                        // Summary Info Card
                        Surface(
                            color = BackgroundSoft,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("الزبون:", fontSize = 11.sp, fontFamily = CairoFontFamily, color = TextSecondaryMuted)
                                    Text(invoice.customerName, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily, color = TextPrimaryDark)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("رقم الفاتورة:", fontSize = 11.sp, fontFamily = CairoFontFamily, color = TextSecondaryMuted)
                                    Text("#${invoice.code}", fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily, color = TextPrimaryDark)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("المبلغ الإجمالي:", fontSize = 11.sp, fontFamily = CairoFontFamily, color = TextSecondaryMuted)
                                    Text("${String.format("%,d", invoice.grandTotalIQD)} د.ع", fontSize = 13.sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily, color = DarkForestGreen)
                                }
                            }
                        }
                    }

                    // Bottom Action Buttons
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    isPrintingByBt = true
                                    withContext(Dispatchers.IO) { kotlinx.coroutines.delay(600) }
                                    isPrintingByBt = false
                                    onPrintBluetooth()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (printerConnected) DarkForestGreen else Color(0xFF334155)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Rounded.Bluetooth, contentDescription = null, tint = GoldLicense, modifier = Modifier.size(20.dp))
                                Text(
                                    if (isPrintingByBt) "جاري إرسال الأمر للطباعة..." else "طباعة حرارية مباشرة (BT)",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = CairoFontFamily
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onPrintSystem,
                                colors = ButtonDefaults.buttonColors(containerColor = SkyBlueInfo),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Rounded.Print, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Text("طابعة النظام", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                }
                            }

                            Button(
                                onClick = onShareImage,
                                colors = ButtonDefaults.buttonColors(containerColor = GoldLicense),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Rounded.Share, contentDescription = null, tint = TextPrimaryDark, modifier = Modifier.size(16.dp))
                                    Text("مشاركة", color = TextPrimaryDark, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                }
                            }
                        }
                    }
                }

                // Right Column: Thermal Paper Strip Box (Scrollable)
                Box(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight()
                        .background(Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    ReceiptPaperContent(
                        invoice = invoice,
                        alwaName = alwaName,
                        ownerName = ownerName,
                        phoneNumber = phoneNumber,
                        location = location,
                        accountantName = accountantName,
                        paperWidth = paperWidth,
                        textScale = textScale,
                        is58mm = is58mm
                    )
                }
            }
        } else {
            // Mobile Portrait Stacked Layout
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top Toolbar: Paper size toggle & Printer status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Paper Size Toggle
                    Surface(
                        color = CardSurfaceWhite,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                    ) {
                        Row(modifier = Modifier.padding(2.dp)) {
                            Surface(
                                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                                color = if (is58mm) DarkForestGreen else Color.Transparent,
                                onClick = { paperSize = "58mm" }
                            ) {
                                Text(
                                    "ورق 58mm",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (is58mm) Color.White else TextSecondaryMuted,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }

                            Surface(
                                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                                color = if (!is58mm) DarkForestGreen else Color.Transparent,
                                onClick = { paperSize = "80mm" }
                            ) {
                                Text(
                                    "ورق 80mm",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!is58mm) Color.White else TextSecondaryMuted,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // Connection Status Button
                    Surface(
                        color = if (printerConnected) EmeraldSuccessLight else RedWarningLight,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                        onClick = onOpenPrinterSetup
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Rounded.Bluetooth,
                                contentDescription = null,
                                tint = if (printerConnected) EmeraldSuccess else RedWarning,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = if (printerConnected) "متصلة 58mm" else "اقتران طابعة",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (printerConnected) EmeraldSuccess else RedWarning
                            )
                        }
                    }
                }

                ReceiptPaperContent(
                    invoice = invoice,
                    alwaName = alwaName,
                    ownerName = ownerName,
                    phoneNumber = phoneNumber,
                    location = location,
                    accountantName = accountantName,
                    paperWidth = paperWidth,
                    textScale = textScale,
                    is58mm = is58mm
                )

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                isPrintingByBt = true
                                withContext(Dispatchers.IO) { kotlinx.coroutines.delay(600) }
                                isPrintingByBt = false
                                onPrintBluetooth()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (printerConnected) DarkForestGreen else Color(0xFF334155)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.2f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.Bluetooth, contentDescription = null, tint = GoldLicense, modifier = Modifier.size(18.dp))
                            Text(
                                if (isPrintingByBt) "جاري الطباعة..." else "طباعة حرارية (BT)",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onPrintSystem,
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBlueInfo),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Print, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Text("طابعة النظام", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = onShareImage,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldLicense),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Share, contentDescription = null, tint = TextPrimaryDark, modifier = Modifier.size(16.dp))
                            Text("مشاركة", color = TextPrimaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptPaperContent(
    invoice: SalesInvoice,
    alwaName: String,
    ownerName: String,
    phoneNumber: String,
    location: String,
    accountantName: String,
    paperWidth: androidx.compose.ui.unit.Dp,
    textScale: Float,
    is58mm: Boolean
) {
    Surface(
        modifier = Modifier
            .width(paperWidth)
            .shadow(14.dp, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 2.dp, bottomEnd = 2.dp)),
        color = Color(0xFFFAFAFA),
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 2.dp, bottomEnd = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC0C0C0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = if (is58mm) 10.dp else 16.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Feed Notch
            Box(
                modifier = Modifier
                    .size(28.dp, 5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF777777))
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Store Icon Badge (Outlined Thermal Style)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Storefront,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Explicit RTL Layout for Receipt Information Block
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Alwa Header (Centered Title Block - Large & Bold for 58mm)
                    Text(
                        text = alwaName,
                        color = Color.Black,
                        fontSize = (20 * textScale).sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = CairoFontFamily,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "بإدارة: $ownerName",
                        color = Color.Black,
                        fontSize = (12 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "هاتف: $phoneNumber",
                        color = Color.Black,
                        fontSize = (11 * textScale).sp,
                        fontFamily = CairoFontFamily,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "العنوان: $location",
                        color = Color.Black,
                        fontSize = (11 * textScale).sp,
                        fontFamily = CairoFontFamily,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Dashed Line Separator
                    Text(
                        text = "---------------------------------------------------------------------------------------------------",
                        color = Color.Black,
                        fontSize = (9 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 2. Invoice Customer & Meta Info Block (RTL Alignment)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("رقم الفاتورة:", color = Color.Black, fontSize = (11.5 * textScale).sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                            Text("# ${invoice.code}", color = Color.Black, fontSize = (12 * textScale).sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("الزبون:", color = Color.Black, fontSize = (11.5 * textScale).sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                            Text(invoice.customerName, color = Color.Black, fontSize = (12.5 * textScale).sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("التاريخ:", color = Color.Black, fontSize = (11.5 * textScale).sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                            Text(invoice.date, color = Color.Black, fontSize = (11 * textScale).sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("طريقة الدفع:", color = Color.Black, fontSize = (11.5 * textScale).sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                            Text(
                                text = if (invoice.paymentType == "آجل") "(📋 بالأجل)" else "نقداً (كاش)",
                                color = Color.Black,
                                fontSize = (11.5 * textScale).sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = CairoFontFamily
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Dashed Line Separator
                    Text(
                        text = "---------------------------------------------------------------------------------------------------",
                        color = Color.Black,
                        fontSize = (9 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // 3. Items Table (Solid Black Border: الصنف | العدد | الوزن | السعر)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, Color.Black)
            ) {
                // Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1.3f)
                            .fillMaxHeight()
                            .padding(vertical = 5.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("الصنف", color = Color.Black, fontSize = (11 * textScale).sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily)
                    }

                    Box(modifier = Modifier.width(1.5.dp).fillMaxHeight().background(Color.Black))

                    Box(
                        modifier = Modifier
                            .weight(0.7f)
                            .fillMaxHeight()
                            .padding(vertical = 5.dp, horizontal = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("العدد", color = Color.Black, fontSize = (11 * textScale).sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily)
                    }

                    Box(modifier = Modifier.width(1.5.dp).fillMaxHeight().background(Color.Black))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(vertical = 5.dp, horizontal = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("الوزن", color = Color.Black, fontSize = (11 * textScale).sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily)
                    }

                    Box(modifier = Modifier.width(1.5.dp).fillMaxHeight().background(Color.Black))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(vertical = 5.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("السعر", color = Color.Black, fontSize = (11 * textScale).sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily)
                    }
                }

                HorizontalDivider(color = Color.Black, thickness = 1.5.dp)

                // Data Rows
                invoice.items.forEachIndexed { idx, item ->
                    if (idx > 0) {
                        HorizontalDivider(color = Color.Black, thickness = 1.dp)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1.3f)
                                .fillMaxHeight()
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = item.cropName,
                                color = Color.Black,
                                fontSize = (11 * textScale).sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = CairoFontFamily
                            )
                        }

                        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.Black))

                        Box(
                            modifier = Modifier
                                .weight(0.7f)
                                .fillMaxHeight()
                                .padding(vertical = 6.dp, horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val countVal = (item.weightOrCount / 20).toInt().coerceAtLeast(1)
                            Text(
                                text = "$countVal",
                                color = Color.Black,
                                fontSize = (11 * textScale).sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = CairoFontFamily
                            )
                        }

                        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.Black))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(vertical = 6.dp, horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${item.weightOrCount.toInt()} كغم",
                                color = Color.Black,
                                fontSize = (11 * textScale).sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = CairoFontFamily
                            )
                        }

                        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.Black))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${item.unitPriceIQD}",
                                color = Color.Black,
                                fontSize = (11 * textScale).sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = CairoFontFamily
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dashed Line Separator
            Text(
                text = "---------------------------------------------------------------------------------------------------",
                color = Color.Black,
                fontSize = (9 * textScale).sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Grand Total Row (RTL)
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "الإجمالي المستحق:",
                            color = Color.Black,
                            fontSize = (13 * textScale).sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = CairoFontFamily
                        )
                        Text(
                            text = "${String.format("%,d", invoice.grandTotalIQD)} د.ع",
                            color = Color.Black,
                            fontSize = (16 * textScale).sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = CairoFontFamily
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "---------------------------------------------------------------------------------------------------",
                        color = Color.Black,
                        fontSize = (9 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 5. Notes Section (RTL)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "ملاحظات:",
                            color = Color.Black,
                            fontSize = (11 * textScale).sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = CairoFontFamily
                        )
                        Text(
                            text = "تم الفحص والعد بالكامل",
                            color = Color.Black,
                            fontSize = (10.5 * textScale).sp,
                            fontFamily = CairoFontFamily
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "---------------------------------------------------------------------------------------------------",
                        color = Color.Black,
                        fontSize = (9 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // 6. System Registration & QR Code Block (QR occupying 80% width)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "رمز التحقق والسيستم (QR)",
                    color = Color.Black,
                    fontSize = (10 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                )

                // High Contrast 80% Width QR Code Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .aspectRatio(1f)
                        .background(Color.White)
                        .border(2.dp, Color.Black)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val step = size.width / 9f
                        for (r in 0..8) {
                            for (c in 0..8) {
                                val isTopLeftFinder = r in 0..2 && c in 0..2
                                val isTopRightFinder = r in 0..2 && c in 6..8
                                val isBottomLeftFinder = r in 6..8 && c in 0..2
                                val isRandomPattern = (r * 3 + c * 7) % 5 == 0 || (r + c) % 2 == 0

                                if (isTopLeftFinder || isTopRightFinder || isBottomLeftFinder || isRandomPattern) {
                                    drawRect(
                                        color = Color.Black,
                                        topLeft = Offset(c * step, r * step),
                                        size = androidx.compose.ui.geometry.Size(step * 0.92f, step * 0.92f)
                                    )
                                }
                            }
                        }
                    }
                }

                Text("Invoice Code: ${invoice.code}", color = Color.Black, fontSize = (10 * textScale).sp, fontWeight = FontWeight.Bold)
                Text("Cashier: $accountantName", color = Color.Black, fontSize = (9.5 * textScale).sp, fontFamily = CairoFontFamily)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "---------------------------------------------------------------------------------------------------",
                color = Color.Black,
                fontSize = (9 * textScale).sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 7. Barcode Section (80% Width Barcode)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "باركود الفاتورة",
                    color = Color.Black,
                    fontSize = (10 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CairoFontFamily
                )

                Box(
                    modifier = Modifier.fillMaxWidth(0.80f),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(40.dp)) {
                        val barWidth = 3f
                        var x = (size.width - (35 * barWidth * 2)) / 2f
                        val patterns = listOf(3, 1, 2, 1, 4, 1, 2, 3, 1, 2, 1, 3, 2, 1, 4, 1, 2, 3, 1, 2, 1, 4, 1, 2)
                        patterns.forEachIndexed { i, w ->
                            val color = if (i % 2 == 0) Color.Black else Color.Transparent
                            drawRect(color, Offset(x, 0f), androidx.compose.ui.geometry.Size(w * barWidth, size.height))
                            x += w * barWidth
                        }
                    }
                }

                Text("* INV-${invoice.code} *", color = Color.Black, fontSize = (10 * textScale).sp, fontWeight = FontWeight.Black)
            }

            Spacer(modifier = Modifier.height(10.dp))

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "شكراً لتعاملكم معنا - $alwaName",
                        color = Color.Black,
                        fontSize = (11 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "---------------------------------------------------------------------------------------------------",
                        color = Color.Black,
                        fontSize = (9 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "برمجة وتطوير شركة Prime™ Solutions",
                        color = Color.Black,
                        fontSize = (9.5 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Whatsapp: 07749883474",
                        color = Color.Black,
                        fontSize = (9 * textScale).sp,
                        fontFamily = CairoFontFamily,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Serrated Edge
            SerratedEdgeCanvas(modifier = Modifier.fillMaxWidth().height(10.dp))
        }
    }
}
