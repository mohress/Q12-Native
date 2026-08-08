package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text(
                                            text = "أنشئ فواتير البيع للزبائن واخصم العمولات للشركة والفلاح فوراً",
                                            color = MintGreen,
                                            style = MaterialTheme.typography.bodyMedium
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
                                            fontWeight = FontWeight.Bold
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
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text(
                                            text = "أنشئ فواتير البيع للزبائن واخصم العمولات للشركة والفلاح فوراً",
                                            color = MintGreen,
                                            style = MaterialTheme.typography.bodyMedium,
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
                                            fontWeight = FontWeight.Bold
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
                    placeholder = { Text("ابحث بالزبون، المحصول، أو رمز الفاتورة...", fontSize = 13.sp) },
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
                                    color = if (isSelected) Color.White else TextPrimaryDark
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

        // 3. Sales Invoices Cards (البطاقات السطرية والعرضية للمبيعات)
        items(filteredInvoices, key = { it.id }) { sale ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(18.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                BoxWithConstraints(
                    modifier = Modifier.padding(14.dp)
                ) {
                    val isTabletLandscape = maxWidth > 650.dp

                    if (isTabletLandscape) {
                        // Wide Screen: Single Horizontal Table-Style Row with 4 Smart Columns
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Column 1: Customer & Code & Payment Badge
                            Column(
                                modifier = Modifier.weight(1.3f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        color = DarkForestGreen.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = sale.code,
                                            color = DarkForestGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = sale.customerName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimaryDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = sale.date,
                                        fontSize = 11.sp,
                                        color = TextSecondaryMuted
                                    )
                                    Surface(
                                        color = if (sale.paymentType == "كاش") EmeraldSuccessLight else RedWarningLight,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = if (sale.paymentType == "كاش") "💵 كاش" else "📋 آجل (${sale.deferredDays}ي)",
                                            color = if (sale.paymentType == "كاش") EmeraldSuccess else RedWarningDark,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier
                                    .height(44.dp)
                                    .width(1.dp),
                                color = GlassBorder
                            )

                            // Column 2: Sold Crops List (Compact Horizontal Row)
                            Column(
                                modifier = Modifier
                                    .weight(1.6f)
                                    .padding(horizontal = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "المحصول المباع:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondaryMuted
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    sale.items.forEach { item ->
                                        Surface(
                                            color = BackgroundSoft,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "${item.cropName}: ${item.weightOrCount}كغم × ${item.unitPriceIQD}",
                                                fontSize = 11.sp,
                                                color = TextPrimaryDark,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier
                                    .height(44.dp)
                                    .width(1.dp),
                                color = GlassBorder
                            )

                            // Column 3: Amounts Summary (Goods, Commission, Porterage & Total)
                            Column(
                                modifier = Modifier
                                    .weight(1.5f)
                                    .padding(horizontal = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text("البضاعة: ${formatIQD(sale.goodsTotalIQD)} • عمولة (7%): ${formatIQD(sale.officeCommission7Percent)}", fontSize = 10.sp, color = TextSecondaryMuted)
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("الإجمالي الكلي:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                    Text(
                                        text = formatIQD(sale.grandTotalIQD),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DarkForestGreen
                                    )
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier
                                    .height(44.dp)
                                    .width(1.dp),
                                color = GlassBorder
                            )

                            // Column 4: Action Buttons
                            Row(
                                modifier = Modifier
                                    .weight(1.2f)
                                    .padding(start = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { onViewDetails(sale) },
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("التفاصيل", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = { onPrintInvoice(sale) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MediumForestGreen),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Rounded.Print, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                        Text("طباعة", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    } else {
                        // Mobile Screen: Compact Horizontal Row Card Design
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Header Row: Customer Name, Code & Payment Type Badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        color = DarkForestGreen.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = sale.code,
                                            color = DarkForestGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = sale.customerName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimaryDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Surface(
                                    color = if (sale.paymentType == "كاش") EmeraldSuccessLight else RedWarningLight,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (sale.paymentType == "كاش") "💵 كاش" else "📋 آجل (${sale.deferredDays} يوم)",
                                        color = if (sale.paymentType == "كاش") EmeraldSuccess else RedWarningDark,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            // Items Horizontal Summary Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    sale.items.forEach { item ->
                                        Surface(
                                            color = BackgroundSoft,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "• ${item.cropName} (${item.weightOrCount}كغم × ${item.unitPriceIQD})",
                                                fontSize = 11.sp,
                                                color = TextPrimaryDark,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = sale.date,
                                    fontSize = 10.sp,
                                    color = TextSecondaryMuted
                                )
                            }

                            // Amounts Compact Banner
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(BackgroundSoft, RoundedCornerShape(10.dp))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("البضاعة: ${formatIQD(sale.goodsTotalIQD)} | عمولة: ${formatIQD(sale.officeCommission7Percent)}", fontSize = 10.sp, color = TextSecondaryMuted)
                                Text("الإجمالي: ${formatIQD(sale.grandTotalIQD)}", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = DarkForestGreen)
                            }

                            // Action Buttons Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { onViewDetails(sale) },
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("التفاصيل", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = { onPrintInvoice(sale) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MediumForestGreen),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Rounded.Print, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Text("طباعة فاتورة", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
