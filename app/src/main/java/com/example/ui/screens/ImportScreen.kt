package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ImportInvoice
import com.example.ui.theme.*

@Composable
fun ImportScreen(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    activeFilter: String,
    onFilterChange: (String) -> Unit,
    importInvoices: List<ImportInvoice>,
    onNewImportClick: () -> Unit,
    onViewDetails: (ImportInvoice) -> Unit,
    onDeleteInvoice: (String) -> Unit = {},
    formatIQD: (Long) -> String,
    modifier: Modifier = Modifier
) {
    val filteredInvoices = importInvoices.filter { invoice ->
        val matchesSearch = invoice.farmerName.contains(searchQuery, ignoreCase = true) ||
                invoice.code.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (activeFilter) {
            "قيد البيع ⏳" -> invoice.status.contains("قيد البيع")
            "جاهز للتسوية ⚠️" -> invoice.status.contains("جاهز للتسوية")
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
        // 1. Import Accent Card (بطاقة الاستيراد العرضية)
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
                                listOf(DarkForestGreen, MediumForestGreen, Color(0xFF1B4332))
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
                                            imageVector = Icons.Rounded.LocalShipping,
                                            contentDescription = "Import",
                                            tint = GoldLicense,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "إدارة فواتير الاستيراد",
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontFamily = CairoFontFamily
                                        )
                                        Text(
                                            text = "سجل البضائع المستلمة من الفلاحين وتتبع تقدم بيعها دقيقة بدقيقة",
                                            color = MintGreen,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = CairoFontFamily
                                        )
                                    }
                                }

                                Button(
                                    onClick = onNewImportClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.AddCircle,
                                            contentDescription = "New Invoice",
                                            tint = DarkForestGreen,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Text(
                                            text = "فاتورة استيراد جديدة",
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
                                            text = "إدارة فواتير الاستيراد",
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontFamily = CairoFontFamily
                                        )
                                        Text(
                                            text = "سجل البضائع المستلمة وتتبع تقدم بيعها دقيقة بدقيقة",
                                            color = MintGreen,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = CairoFontFamily,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Rounded.LocalShipping,
                                        contentDescription = "Import",
                                        tint = GoldLicense,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }

                                Button(
                                    onClick = onNewImportClick,
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
                                            imageVector = Icons.Rounded.AddCircle,
                                            contentDescription = "New Invoice",
                                            tint = DarkForestGreen,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Text(
                                            text = "فاتورة استيراد جديدة",
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

        // 2. Search & Filter Chips Bar
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(16.dp)),
                    placeholder = { Text("ابحث باسم الفلاح أو رمز الفاتورة...", fontSize = 13.sp, fontFamily = CairoFontFamily) },
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

                val filterChips = listOf("الجميع", "قيد البيع ⏳", "جاهز للتسوية ⚠️")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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

        // 3. Import Invoices Cards (Redesigned Exact Match to Screenshot Image 2)
        items(filteredInvoices, key = { it.id }) { invoice ->
            ImportInvoiceCard(
                invoice = invoice,
                onViewDetails = onViewDetails,
                onDeleteInvoice = onDeleteInvoice,
                formatIQD = formatIQD
            )
        }
    }
}

@Composable
fun ImportInvoiceCard(
    invoice: ImportInvoice,
    onViewDetails: (ImportInvoice) -> Unit,
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
            // Far Right Thick Green Stripe Accent (matches Image 2)
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
                    val codeClean = if (invoice.code.contains("#")) invoice.code else "ID: #${invoice.code.takeLast(4)}"
                    Surface(
                        color = DarkForestGreen,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = codeClean,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = CairoFontFamily,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    val dateOnly = invoice.date.split(" ").firstOrNull() ?: invoice.date
                    Text(
                        text = dateOnly,
                        fontSize = 11.sp,
                        color = TextSecondaryMuted,
                        fontFamily = CairoFontFamily
                    )
                }

                // 2. Farmer/Supplier Name & Vehicle Type
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.widthIn(min = 140.dp, max = 180.dp)
                ) {
                    Text(
                        text = invoice.farmerName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        fontFamily = CairoFontFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "نوع السيارة: ${invoice.vehicleType}",
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
                    modifier = Modifier.widthIn(min = 210.dp, max = 270.dp)
                ) {
                    invoice.crops.forEach { crop ->
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
                                val emoji = getCropEmoji(crop.cropName)
                                Text(
                                    text = "$emoji ${crop.cropName}",
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
                                        text = "${String.format("%,d", crop.netWeightKg.toInt())} كجم",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkForestGreen,
                                        fontFamily = CairoFontFamily,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Surface(
                                    color = Color(0xFFD5E8DB),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${crop.boxCount} صندوق",
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

                // 4. Sales Progress & Remaining Info
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.widthIn(min = 150.dp, max = 190.dp)
                ) {
                    val percentInt = (invoice.progressPercent * 100).toInt()
                    Text(
                        text = "نسبة المبيعات: $percentInt%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        fontFamily = CairoFontFamily
                    )
                    LinearProgressIndicator(
                        progress = invoice.progressPercent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = DarkForestGreen,
                        trackColor = Color(0xFFE0E0E0)
                    )
                    val totalKg = invoice.crops.sumOf { it.netWeightKg }.toInt()
                    val totalBoxes = invoice.crops.sumOf { it.boxCount }
                    val remainingKg = (totalKg * (1 - invoice.progressPercent)).toInt()
                    val remainingBoxes = (totalBoxes * (1 - invoice.progressPercent)).toInt()
                    Text(
                        text = "(متبقي: ${String.format("%,d", remainingKg)} كجم / $remainingBoxes صندوق)",
                        fontSize = 11.sp,
                        color = TextSecondaryMuted,
                        fontFamily = CairoFontFamily
                    )
                }

                // 5. Status Badge (⏳ قيد البيع)
                Surface(
                    color = Color(0xFFFFF8E1),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFE082))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("⏳", fontSize = 12.sp)
                        Text(
                            text = invoice.status,
                            color = Color(0xFFF57F17),
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

                // 6. Action Buttons Column (Far Left: حذف | تفاصيل)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Delete Button
                    OutlinedButton(
                        onClick = { onDeleteInvoice(invoice.id) },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
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
                        onClick = { onViewDetails(invoice) },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
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
                }
            }
        }
    }
}
