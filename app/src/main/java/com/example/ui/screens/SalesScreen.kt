package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.SalesInvoice
import com.example.ui.theme.*

@Composable
fun SalesScreen(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    activeFilter: String,
    onFilterChange: (String) -> Unit,
    salesInvoices: List<SalesInvoice>,
    onNewSaleClick: () -> Unit,
    onPrintInvoice: (SalesInvoice) -> Unit,
    onViewDetails: (SalesInvoice) -> Unit = onPrintInvoice,
    onDeleteInvoice: (String) -> Unit = {},
    formatIQD: (Long) -> String,
    modifier: Modifier = Modifier
) {
    val filteredInvoices = salesInvoices.filter { sale ->
        val matchesSearch = sale.customerName.contains(searchQuery, ignoreCase = true) ||
                sale.code.contains(searchQuery, ignoreCase = true) ||
                sale.items.any { it.cropName.contains(searchQuery, ignoreCase = true) }
        val matchesFilter = when (activeFilter) {
            "آجل غير مدفوع ⏳" -> sale.paymentType == "آجل"
            "كاش / مدفوع ✅" -> sale.paymentType == "كاش"
            else -> true
        }
        matchesSearch && matchesFilter
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Sales Accent Card (بطاقة المبيعات العرضية)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(DarkForestGreen, Color(0xFF0F2D21), MediumForestGreen)
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(20.dp)
                ) {
                    BoxWithConstraints {
                        if (maxWidth > 600.dp) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color.White.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.ShoppingCart,
                                            contentDescription = "Sales",
                                            tint = GoldLicense,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "المبيعات اليومية",
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontFamily = CairoFontFamily
                                        )
                                        Text(
                                            text = "أنشئ فواتير البيع للزبائن واخصم العمولات للشركة والفلاح فوراً",
                                            color = MintGreen,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = CairoFontFamily
                                        )
                                    }
                                }

                                Button(
                                    onClick = onNewSaleClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.ShoppingCart,
                                            contentDescription = "New Sale",
                                            tint = DarkForestGreen,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "فاتورة بيع جديدة",
                                            color = DarkForestGreen,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = CairoFontFamily
                                        )
                                    }
                                }
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "المبيعات اليومية",
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontFamily = CairoFontFamily
                                        )
                                        Text(
                                            text = "أنشئ فواتير البيع للزبائن واخصم العمولات للشركة والفلاح فوراً",
                                            color = MintGreen,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = CairoFontFamily,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Rounded.ShoppingCart,
                                        contentDescription = "Sales",
                                        tint = GoldLicense,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }

                                Button(
                                    onClick = onNewSaleClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.ShoppingCart,
                                            contentDescription = "New Sale",
                                            tint = DarkForestGreen,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "فاتورة بيع جديدة",
                                            color = DarkForestGreen,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = CairoFontFamily
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Search & Filter Bar
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(16.dp)),
                    placeholder = { Text("ابحث بالزبون، المحصول، أو رمز الفاتورة...", fontSize = 13.sp, fontFamily = CairoFontFamily) },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search", tint = MediumForestGreen) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = MediumForestGreen,
                        unfocusedBorderColor = GlassBorder
                    )
                )

                val filterChips = listOf("الجميع", "آجل غير مدفوع ⏳", "كاش / مدفوع ✅")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filterChips) { chip ->
                        val isSelected = activeFilter == chip
                        FilterChip(
                            selected = isSelected,
                            onClick = { onFilterChange(chip) },
                            label = {
                                Text(
                                    text = chip,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else TextPrimaryDark,
                                    fontFamily = CairoFontFamily
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DarkForestGreen,
                                containerColor = CardSurfaceWhite
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = GlassBorder
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        // 3. Sales Invoices Cards (Redesigned Exact Match to Screenshot Image 1)
        items(filteredInvoices, key = { it.id }) { sale ->
            SalesInvoiceCard(
                sale = sale,
                onPrintInvoice = onPrintInvoice,
                onViewDetails = onViewDetails,
                onDeleteInvoice = onDeleteInvoice,
                formatIQD = formatIQD
            )
        }
    }
}

@Composable
fun SalesInvoiceCard(
    sale: SalesInvoice,
    onPrintInvoice: (SalesInvoice) -> Unit,
    onViewDetails: (SalesInvoice) -> Unit,
    onDeleteInvoice: (String) -> Unit,
    formatIQD: (Long) -> String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Far Right Thick Green Stripe Accent (matches image perfectly)
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(
                        DarkForestGreen,
                        shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 16.dp, bottomEnd = 16.dp)
                    )
            )

            // Card Inner Content Container
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 1. Far Right ID Badge & Date
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.widthIn(min = 75.dp)
                ) {
                    Surface(
                        color = DarkForestGreen,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "ID: ${sale.code}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = CairoFontFamily,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    val dateOnly = sale.date.split(" ").firstOrNull() ?: sale.date
                    Text(
                        text = dateOnly,
                        fontSize = 11.sp,
                        color = TextSecondaryMuted,
                        fontFamily = CairoFontFamily
                    )
                }

                // 2. Customer Name & Location
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.widthIn(min = 130.dp, max = 170.dp)
                ) {
                    Text(
                        text = sale.customerName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        fontFamily = CairoFontFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    val locationStr = if (sale.customerAddress.isNotBlank()) sale.customerAddress else "بغداد - زيونة"
                    Text(
                        text = locationStr,
                        fontSize = 11.5.sp,
                        color = TextSecondaryMuted,
                        fontFamily = CairoFontFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // 3. Crops Stack (Pills Container)
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.widthIn(min = 210.dp, max = 260.dp)
                ) {
                    sale.items.forEach { item ->
                        Surface(
                            color = Color(0xFFF4F8F5),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFD0E4D7))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val emoji = getCropEmoji(item.cropName)
                                Text(
                                    text = "$emoji ${item.cropName}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark,
                                    fontFamily = CairoFontFamily,
                                    maxLines = 1
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Surface(
                                    color = Color(0xFFD5E8DB),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${item.weightOrCount.toInt()} كجم",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkForestGreen,
                                        fontFamily = CairoFontFamily,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                val boxCount = (item.weightOrCount / 20).toInt().coerceAtLeast(1)
                                Surface(
                                    color = Color(0xFFD5E8DB),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "$boxCount صندوق",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkForestGreen,
                                        fontFamily = CairoFontFamily,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 4. Payment Status Badge (آجل / كاش)
                val isDeferred = sale.paymentType == "آجل"
                Surface(
                    color = if (isDeferred) Color(0xFFFDF0F0) else Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, if (isDeferred) Color(0xFFF8BBD0) else Color(0xFFA5D6A7))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (isDeferred) "📋" else "💵",
                            fontSize = 12.sp
                        )
                        Text(
                            text = if (isDeferred) "دين بالأجل" else "نقداً كاش",
                            color = if (isDeferred) RedWarningDark else EmeraldSuccess,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = CairoFontFamily
                        )
                    }
                }

                // Dotted Divider Line
                DottedVerticalDivider(
                    modifier = Modifier
                        .height(42.dp)
                        .width(1.dp)
                )

                // 5. Total Amount Column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.widthIn(min = 100.dp)
                ) {
                    Text(
                        text = "المبلغ الإجمالي",
                        fontSize = 11.sp,
                        color = TextSecondaryMuted,
                        fontFamily = CairoFontFamily
                    )
                    Text(
                        text = "${formatIQD(sale.grandTotalIQD)} د.ع",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimaryDark,
                        fontFamily = CairoFontFamily
                    )
                }

                // Dotted Divider Line
                DottedVerticalDivider(
                    modifier = Modifier
                        .height(42.dp)
                        .width(1.dp)
                )

                // 6. Action Buttons Column (Far Left: حذف | تفاصيل | طباعة)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Delete Button
                    OutlinedButton(
                        onClick = { onDeleteInvoice(sale.id) },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.2.dp, RedWarningDark),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RedWarningDark)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Rounded.Delete, contentDescription = "حذف", modifier = Modifier.size(15.dp), tint = RedWarningDark)
                            Text("حذف", fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                        }
                    }

                    // Details Button
                    OutlinedButton(
                        onClick = { onViewDetails(sale) },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.2.dp, DarkForestGreen),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkForestGreen)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Rounded.Info, contentDescription = "تفاصيل", modifier = Modifier.size(15.dp), tint = DarkForestGreen)
                            Text("تفاصيل", fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                        }
                    }

                    // Print Button
                    OutlinedButton(
                        onClick = { onPrintInvoice(sale) },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.2.dp, DarkForestGreen),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkForestGreen)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Rounded.Print, contentDescription = "طباعة", modifier = Modifier.size(15.dp), tint = DarkForestGreen)
                            Text("طباعة", fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DottedVerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFD0D0D0),
    dashHeight: Float = 6f,
    gapHeight: Float = 6f
) {
    Canvas(modifier = modifier) {
        var y = 0f
        while (y < size.height) {
            drawLine(
                color = color,
                start = Offset(size.width / 2, y),
                end = Offset(size.width / 2, (y + dashHeight).coerceAtMost(size.height)),
                strokeWidth = 2f
            )
            y += dashHeight + gapHeight
        }
    }
}

fun getCropEmoji(cropName: String): String {
    return when {
        cropName.contains("بطيخ") -> "🍋"
        cropName.contains("تفاح") -> "🥦"
        cropName.contains("موز") -> "🍌"
        cropName.contains("بامية") -> "🥒"
        cropName.contains("بصل") -> "🧅"
        cropName.contains("فلفل") -> "🫑"
        cropName.contains("ثوم") -> "🧄"
        cropName.contains("بتيتة") || cropName.contains("بطاطا") -> "🥔"
        cropName.contains("زنجبيل") -> "🍠"
        cropName.contains("طماطة") || cropName.contains("طماطم") -> "🍅"
        cropName.contains("خيارات") || cropName.contains("خيار") -> "🥒"
        cropName.contains("عنب") -> "🍇"
        cropName.contains("رمان") -> "🍎"
        cropName.contains("برتقال") -> "🍊"
        else -> "🌱"
    }
}
