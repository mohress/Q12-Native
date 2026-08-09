package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val paperWidth = if (is58mm) 285.dp else 350.dp
    val textScale = if (is58mm) 1.0f else 1.15f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
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
                            "ورق 58mm (محمول)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (is58mm) Color.White else TextSecondaryMuted,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }

                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                        color = if (!is58mm) DarkForestGreen else Color.Transparent,
                        onClick = { paperSize = "80mm" }
                    ) {
                        Text(
                            "ورق 80mm (قياسي)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (!is58mm) Color.White else TextSecondaryMuted,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
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

        // Professional Realistic Thermal Paper Strip Box (58mm Optimized)
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
                    .padding(horizontal = if (is58mm) 8.dp else 14.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Feed Notch
                Box(
                    modifier = Modifier
                        .size(24.dp, 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF888888))
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Store Icon Badge (Outlined Thermal Style)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color.Black, CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.Storefront,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // 1. Alwa Header (Centered Title Block)
                Text(
                    text = alwaName,
                    color = Color.Black,
                    fontSize = (18 * textScale).sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "بإدارة: $ownerName",
                    color = Color.Black,
                    fontSize = (11 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "هاتف: $phoneNumber",
                    color = Color.Black,
                    fontSize = (10 * textScale).sp,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "العنوان: $location",
                    color = Color.Black,
                    fontSize = (10 * textScale).sp,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Dashed Line Separator
                Text(
                    text = "---------------------------------------------------------------------------------------------------",
                    color = Color.Black,
                    fontSize = (8 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 2. Invoice Customer & Meta Info Block (Matching exact image RTL key-value order)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("(# ${invoice.code}) 72 #", color = Color.Black, fontSize = (9.5 * textScale).sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                        Text("رقم الفاتورة:", color = Color.Black, fontSize = (10 * textScale).sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(invoice.customerName, color = Color.Black, fontSize = (10.5 * textScale).sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily)
                        Text("الزبون:", color = Color.Black, fontSize = (10 * textScale).sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(invoice.date, color = Color.Black, fontSize = (9.5 * textScale).sp, fontFamily = CairoFontFamily)
                        Text("التاريخ:", color = Color.Black, fontSize = (10 * textScale).sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (invoice.paymentType == "آجل") "(📋 بالأجل)" else "نقداً (كاش)",
                            color = Color.Black,
                            fontSize = (10 * textScale).sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = CairoFontFamily
                        )
                        Text("طريقة الدفع:", color = Color.Black, fontSize = (10 * textScale).sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Dashed Line Separator
                Text(
                    text = "---------------------------------------------------------------------------------------------------",
                    color = Color.Black,
                    fontSize = (8 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 3. Items Grid Table (4 Columns Grid with Solid Black Border: الصنف | العدد | الوزن | السعر)
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
                        // Col 1 (Right): الصنف
                        Box(
                            modifier = Modifier
                                .weight(1.3f)
                                .fillMaxHeight()
                                .padding(vertical = 4.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("الصنف", color = Color.Black, fontSize = (10 * textScale).sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily)
                        }

                        Box(modifier = Modifier.width(1.5.dp).fillMaxHeight().background(Color.Black))

                        // Col 2: العدد
                        Box(
                            modifier = Modifier
                                .weight(0.7f)
                                .fillMaxHeight()
                                .padding(vertical = 4.dp, horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("العدد", color = Color.Black, fontSize = (10 * textScale).sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily)
                        }

                        Box(modifier = Modifier.width(1.5.dp).fillMaxHeight().background(Color.Black))

                        // Col 3: الوزن
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(vertical = 4.dp, horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("الوزن", color = Color.Black, fontSize = (10 * textScale).sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily)
                        }

                        Box(modifier = Modifier.width(1.5.dp).fillMaxHeight().background(Color.Black))

                        // Col 4 (Left): السعر
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(vertical = 4.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("السعر", color = Color.Black, fontSize = (10 * textScale).sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily)
                        }
                    }

                    Divider(color = Color.Black, thickness = 1.5.dp)

                    // Data Rows
                    invoice.items.forEachIndexed { idx, item ->
                        if (idx > 0) {
                            Divider(color = Color.Black, thickness = 1.dp)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            // Col 1 (Right): الصنف
                            Box(
                                modifier = Modifier
                                    .weight(1.3f)
                                    .fillMaxHeight()
                                    .padding(vertical = 5.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text(
                                    text = item.cropName,
                                    color = Color.Black,
                                    fontSize = (10 * textScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = CairoFontFamily
                                )
                            }

                            Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.Black))

                            // Col 2: العدد
                            Box(
                                modifier = Modifier
                                    .weight(0.7f)
                                    .fillMaxHeight()
                                    .padding(vertical = 5.dp, horizontal = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                val countVal = (item.weightOrCount / 20).toInt().coerceAtLeast(1)
                                Text(
                                    text = "$countVal",
                                    color = Color.Black,
                                    fontSize = (10 * textScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = CairoFontFamily
                                )
                            }

                            Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.Black))

                            // Col 3: الوزن
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(vertical = 5.dp, horizontal = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${item.weightOrCount.toInt()} كغم",
                                    color = Color.Black,
                                    fontSize = (10 * textScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = CairoFontFamily
                                )
                            }

                            Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.Black))

                            // Col 4 (Left): السعر
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(vertical = 5.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${item.unitPriceIQD}",
                                    color = Color.Black,
                                    fontSize = (10 * textScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = CairoFontFamily
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Dashed Line Separator
                Text(
                    text = "---------------------------------------------------------------------------------------------------",
                    color = Color.Black,
                    fontSize = (8 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 4. Grand Total Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${String.format("%,d", invoice.grandTotalIQD)} د.ع",
                        color = Color.Black,
                        fontSize = (14 * textScale).sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = CairoFontFamily
                    )
                    Text(
                        text = "الإجمالي المستحق:",
                        color = Color.Black,
                        fontSize = (11 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Dashed Line Separator
                Text(
                    text = "---------------------------------------------------------------------------------------------------",
                    color = Color.Black,
                    fontSize = (8 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 5. Notes Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "ملاحظات:",
                        color = Color.Black,
                        fontSize = (10 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily,
                        textAlign = TextAlign.Right
                    )
                    Text(
                        text = "تم الفحص والعد بالكامل",
                        color = Color.Black,
                        fontSize = (9.5 * textScale).sp,
                        fontFamily = CairoFontFamily,
                        textAlign = TextAlign.Right
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Dashed Line Separator
                Text(
                    text = "---------------------------------------------------------------------------------------------------",
                    color = Color.Black,
                    fontSize = (8 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 6. System Registration & QR Code Block
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Column
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Invoice: ${invoice.code}", color = Color.Black, fontSize = (9 * textScale).sp, fontWeight = FontWeight.Bold)
                        Text("Cashier: $accountantName", color = Color.Black, fontSize = (8.5 * textScale).sp)
                        Text("This Invoice was successfully registered in the system", color = Color.Black, fontSize = (7.5 * textScale).sp)
                    }

                    // Right QR Code
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color.White)
                            .border(1.dp, Color.Black)
                            .padding(2.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val step = size.width / 7
                            for (r in 0..6) {
                                for (c in 0..6) {
                                    if ((r in 0..2 && c in 0..2) || (r in 0..2 && c in 4..6) || (r in 4..6 && c in 0..2) || (r + c) % 3 == 0) {
                                        drawRect(Color.Black, Offset(c * step, r * step), androidx.compose.ui.geometry.Size(step, step))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Dashed Line Separator
                Text(
                    text = "---------------------------------------------------------------------------------------------------",
                    color = Color.Black,
                    fontSize = (8 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 7. Barcode Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(28.dp)) {
                        val barWidth = 2.4f
                        var x = (size.width - (35 * barWidth * 2)) / 2f
                        val patterns = listOf(3, 1, 2, 1, 4, 1, 2, 3, 1, 2, 1, 3, 2, 1, 4, 1, 2, 3, 1, 2, 1, 4, 1, 2)
                        patterns.forEachIndexed { i, w ->
                            val color = if (i % 2 == 0) Color.Black else Color.Transparent
                            drawRect(color, Offset(x, 0f), androidx.compose.ui.geometry.Size(w * barWidth, size.height))
                            x += w * barWidth
                        }
                    }
                    Text("0 895529 020666", color = Color.Black, fontSize = (8.5 * textScale).sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "شكراً لتعاملكم معنا - $alwaName",
                    color = Color.Black,
                    fontSize = (9 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "---------------------------------------------------------------------------------------------------",
                    color = Color.Black,
                    fontSize = (8 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "برمجة وتطوير شركة Prime™ Solutions",
                    color = Color.Black,
                    fontSize = (8.5 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Whatsapp: 07749883474",
                    color = Color.Black,
                    fontSize = (8 * textScale).sp,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Serrated Edge
                SerratedEdgeCanvas(modifier = Modifier.fillMaxWidth().height(8.dp))
            }
        }

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        isPrintingByBt = true
                        withContext(Dispatchers.IO) {
                            kotlinx.coroutines.delay(600)
                        }
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
                    Icon(
                        Icons.Rounded.Bluetooth,
                        contentDescription = null,
                        tint = GoldLicense,
                        modifier = Modifier.size(18.dp)
                    )
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Share, contentDescription = null, tint = TextPrimaryDark, modifier = Modifier.size(16.dp))
                    Text("مشاركة", color = TextPrimaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
