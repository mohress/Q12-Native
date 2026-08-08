package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.ui.unit.sp
import com.example.data.models.SalesInvoice
import com.example.printer.ThermalPrinterManager
import com.example.ui.theme.*

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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        color = if (paperSize == "58mm") DarkForestGreen else Color.Transparent,
                        onClick = { paperSize = "58mm" }
                    ) {
                        Text(
                            "ورق 58mm (محمول)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (paperSize == "58mm") Color.White else TextSecondaryMuted,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                        color = if (paperSize == "80mm") DarkForestGreen else Color.Transparent,
                        onClick = { paperSize = "80mm" }
                    ) {
                        Text(
                            "ورق 80mm (قياسي)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (paperSize == "80mm") Color.White else TextSecondaryMuted,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Connection Status & Quick Setup Button
            Surface(
                color = if (printerConnected) EmeraldSuccessLight else RedWarningLight,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                onClick = onOpenPrinterSetup
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Rounded.Bluetooth,
                        contentDescription = null,
                        tint = if (printerConnected) EmeraldSuccess else RedWarning,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (printerConnected) "الطابعة متصلة" else "اقتران طابعة",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (printerConnected) EmeraldSuccess else RedWarning
                    )
                    Icon(
                        Icons.Rounded.Settings,
                        contentDescription = null,
                        tint = if (printerConnected) EmeraldSuccess else RedWarning,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // Professional Realistic Thermal Paper Strip Box
        val paperWidth = if (paperSize == "58mm") 310.dp else 360.dp

        Surface(
            modifier = Modifier
                .width(paperWidth)
                .shadow(16.dp, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 2.dp, bottomEnd = 2.dp)),
            color = Color(0xFFFFFFFC), // Ultra-clean thermal paper cream-white
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 2.dp, bottomEnd = 2.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Paper Feed Notch Icon
                Box(
                    modifier = Modifier
                        .size(32.dp, 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFCBD5E1))
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Alwa Logo Badge
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E293B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.Storefront,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Alwa Header
                Text(
                    text = alwaName,
                    color = Color(0xFF0F172A),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "سوق الجملة للفواكه والخضروات",
                    color = Color(0xFF475569),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "صاحب العلوة: $ownerName  |  هاتف: $phoneNumber",
                    color = Color(0xFF334155),
                    fontSize = 10.sp,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = location,
                    color = Color(0xFF64748B),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Dashed Separator
                ReceiptDashedLine()

                Spacer(modifier = Modifier.height(10.dp))

                // Invoice Meta Info Box
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("رقم الفاتورة:", color = Color(0xFF64748B), fontSize = 11.sp, fontFamily = CairoFontFamily)
                            Text(invoice.code, color = Color(0xFF0F172A), fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("التاريخ والوقت:", color = Color(0xFF64748B), fontSize = 11.sp, fontFamily = CairoFontFamily)
                            Text(invoice.date, color = Color(0xFF0F172A), fontSize = 11.sp, fontFamily = CairoFontFamily)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("اسم الزبون:", color = Color(0xFF64748B), fontSize = 11.sp, fontFamily = CairoFontFamily)
                            Text(invoice.customerName, color = Color(0xFF0F172A), fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("طريقة الدفع:", color = Color(0xFF64748B), fontSize = 11.sp, fontFamily = CairoFontFamily)
                            Surface(
                                color = if (invoice.paymentType == "كاش") Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (invoice.paymentType == "كاش") "كاش / نقداً ✅" else "آجل (${invoice.deferredDays} يوم) ⏳",
                                    color = if (invoice.paymentType == "كاش") Color(0xFF166534) else Color(0xFF92400E),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Items Table Header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("تفاصيل الصنف والكمية", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("المجموع (د.ع)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Item Lines
                invoice.items.forEachIndexed { idx, item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${idx + 1}. ${item.cropName}",
                                color = Color(0xFF0F172A),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${item.totalAmountIQD} د.ع",
                                color = Color(0xFF0F172A),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = CairoFontFamily
                            )
                        }

                        Text(
                            text = "الكمية/الوزن: ${item.weightOrCount} كغم  ×  السعر: ${item.unitPriceIQD} د.ع",
                            color = Color(0xFF64748B),
                            fontSize = 10.sp,
                            fontFamily = CairoFontFamily,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                    if (idx < invoice.items.size - 1) {
                        Divider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 2.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                ReceiptDashedLine()
                Spacer(modifier = Modifier.height(10.dp))

                // Calculation Breakdown
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("مجموع قيمة البضاعة:", color = Color(0xFF475569), fontSize = 11.sp, fontFamily = CairoFontFamily)
                        Text("${invoice.goodsTotalIQD} د.ع", color = Color(0xFF0F172A), fontSize = 11.sp, fontFamily = CairoFontFamily)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("عمولة المكتب (7%):", color = Color(0xFF475569), fontSize = 11.sp, fontFamily = CairoFontFamily)
                        Text("${invoice.officeCommission7Percent} د.ع", color = Color(0xFF0F172A), fontSize = 11.sp, fontFamily = CairoFontFamily)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("أجور الحمالية والتفريغ:", color = Color(0xFF475569), fontSize = 11.sp, fontFamily = CairoFontFamily)
                        Text("${invoice.porterageFeeIQD} د.ع", color = Color(0xFF0F172A), fontSize = 11.sp, fontFamily = CairoFontFamily)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Prominent Grand Total Box
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = DarkForestGreen,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("المبلغ الإجمالي الكلي:", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("${invoice.grandTotalIQD} د.ع", color = GoldLicense, fontSize = 16.sp, fontWeight = FontWeight.Black, fontFamily = CairoFontFamily)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // QR Code & Barcode Visual Representation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Barcode Canvas
                    Column(horizontalAlignment = Alignment.Start) {
                        Canvas(modifier = Modifier.size(130.dp, 28.dp)) {
                            val barWidth = 3f
                            var x = 0f
                            val patterns = listOf(3, 1, 2, 1, 4, 1, 2, 3, 1, 2, 1, 3, 2, 1, 4, 1, 2)
                            patterns.forEachIndexed { i, w ->
                                val color = if (i % 2 == 0) Color.Black else Color.Transparent
                                drawRect(color, Offset(x, 0f), androidx.compose.ui.geometry.Size(w * barWidth, size.height))
                                x += w * barWidth
                            }
                        }
                        Text(invoice.code, color = Color(0xFF64748B), fontSize = 9.sp, fontFamily = CairoFontFamily)
                    }

                    // QR Canvas
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White)
                            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                            .padding(4.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val step = size.width / 5
                            for (r in 0..4) {
                                for (c in 0..4) {
                                    if ((r + c) % 2 == 0 || (r == 0 && c == 0) || (r == 4 && c == 4) || (r == 0 && c == 4)) {
                                        drawRect(Color.Black, Offset(c * step, r * step), androidx.compose.ui.geometry.Size(step, step))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                ReceiptDashedLine()
                Spacer(modifier = Modifier.height(10.dp))

                // Accountant & Footer
                Text(
                    text = "المحاسب المسؤول: $accountantName",
                    color = Color(0xFF334155),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CairoFontFamily
                )

                Text(
                    text = "شكرًا لتعاملكم مع $alwaName 🌿",
                    color = Color(0xFF64748B),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "* الفاتورة صادرة إلكترونياً من نظام إدارة العلوة *",
                    color = Color(0xFF94A3B8),
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Serrated Tear Bottom Edge Visual Canvas
                SerratedEdgeCanvas(modifier = Modifier.fillMaxWidth().height(10.dp))
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
                        val success = withContext(Dispatchers.IO) {
                            ThermalPrinterManager.printInvoiceToBluetooth(
                                invoice = invoice,
                                alwaName = alwaName,
                                ownerName = ownerName,
                                phone = phoneNumber,
                                location = location,
                                accountant = accountantName,
                                paperSize = paperSize
                            )
                        }
                        isPrintingByBt = false
                        if (success) {
                            Toast.makeText(context, "تم إرسال الفاتورة إلى الطابعة الحرارية بنجاح! 🛈", Toast.LENGTH_SHORT).show()
                            onPrintBluetooth()
                        } else {
                            Toast.makeText(context, "فشلت الطباعة: يرجى التأكد من تشغيل الطابعة واقتران البلوتوث", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (printerConnected) DarkForestGreen else Color(0xFF475569)
                ),
                shape = RoundedCornerShape(14.dp),
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
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        if (isPrintingByBt) "جاري الطباعة..." else "طباعة حرارية (BT)",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = {
                    Toast.makeText(context, "تم توجيه أمر الطباعة لنظام Android", Toast.LENGTH_SHORT).show()
                    onPrintSystem()
                },
                colors = ButtonDefaults.buttonColors(containerColor = SkyBlueInfo),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Print, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Text("طابعة النظام", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Button(
                onClick = {
                    Toast.makeText(context, "تم حفظ وصورة الفاتورة لخاصية المشاركة", Toast.LENGTH_SHORT).show()
                    onShareImage()
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldLicense),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Share, contentDescription = null, tint = TextPrimaryDark, modifier = Modifier.size(18.dp))
                    Text("حفظ صورة", color = TextPrimaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ReceiptDashedLine(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
        drawLine(
            color = Color(0xFFCBD5E1),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect,
            strokeWidth = 2f
        )
    }
}

@Composable
fun SerratedEdgeCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val path = Path()
        val triangleWidth = 12f
        val triangleHeight = size.height
        var x = 0f

        path.moveTo(0f, 0f)
        while (x < size.width) {
            path.lineTo(x + triangleWidth / 2, triangleHeight)
            path.lineTo(x + triangleWidth, 0f)
            x += triangleWidth
        }

        drawPath(
            path = path,
            color = Color(0xFFCBD5E1),
            style = Stroke(width = 1.5f)
        )
    }
}
