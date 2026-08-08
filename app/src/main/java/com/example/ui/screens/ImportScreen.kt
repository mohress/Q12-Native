package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
        // 1. New Year Rollover Banner (بنر تدوير السنة المالية)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp)),
                color = MintGreen,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, LightForestGreen.copy(alpha = 0.5f))
            ) {
                BoxWithConstraints(modifier = Modifier.padding(16.dp)) {
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
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MediumForestGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Celebration,
                                        contentDescription = "New Year",
                                        tint = GoldLicense,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "تجهيز حسابات السنة الجديدة",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = DarkForestGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "نظام الأمان والتدوير التلقائي جاهز. تم تأمين السجلات وأرشفة ديون الفلاحين والزبائن بأعلى درجات الموثوقية.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondaryMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                    Text("أرشفة تلقائية", fontSize = 11.sp, color = DarkForestGreen, fontWeight = FontWeight.Bold)
                                }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.Transparent
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                Brush.horizontalGradient(listOf(GoldLicense, Color(0xFFE65100))),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "متبقي 5 أيام على نهاية السنة ⏳",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(MediumForestGreen),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Celebration,
                                            contentDescription = "New Year",
                                            tint = GoldLicense,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Text(
                                        text = "تجهيز حسابات السنة الجديدة",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = DarkForestGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.Transparent
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                Brush.horizontalGradient(listOf(GoldLicense, Color(0xFFE65100))),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "متبقي 5 أيام ⏳",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "نظام الأمان والتدوير التلقائي جاهز. تم تأمين السجلات وأرشفة ديون الفلاحين والزبائن بأعلى درجات الموثوقية.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondaryMuted
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                    Text("أرشفة تلقائية", fontSize = 11.sp, color = DarkForestGreen, fontWeight = FontWeight.Bold)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                    Text("ترحيل الديون بضغطة واحدة", fontSize = 11.sp, color = DarkForestGreen, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Import Accent Card (بطاقة الاستيراد العرضية)
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
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text(
                                            text = "سجل البضائع المستلمة من الفلاحين وتتبع تقدم بيعها دقيقة بدقيقة",
                                            color = MintGreen,
                                            style = MaterialTheme.typography.bodyMedium
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
                                            text = "إدارة فواتير الاستيراد",
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text(
                                            text = "سجل البضائع المستلمة وتتبع تقدم بيعها دقيقة بدقيقة",
                                            color = MintGreen,
                                            style = MaterialTheme.typography.bodyMedium,
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

        // 3. Search & Filter Chips Bar
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(16.dp)),
                    placeholder = { Text("ابحث باسم الفلاح أو رمز الفاتورة...", fontSize = 13.sp) },
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

        // 4. Import Invoices Horizontal Row Cards (البطاقات السطرية والعرضية المحدثة)
        items(filteredInvoices, key = { it.id }) { invoice ->
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
                            // Column 1: Code & Farmer Info
                            Column(
                                modifier = Modifier.weight(1.2f),
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
                                            text = invoice.code,
                                            color = DarkForestGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = invoice.farmerName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimaryDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "${invoice.vehicleType} • ${invoice.date}",
                                    fontSize = 11.sp,
                                    color = TextSecondaryMuted
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier
                                    .height(44.dp)
                                    .width(1.dp),
                                color = GlassBorder
                            )

                            // Column 2: Received Crops Chips
                            Column(
                                modifier = Modifier
                                    .weight(1.5f)
                                    .padding(horizontal = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "الأصناف المستلمة:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondaryMuted
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    invoice.crops.take(2).forEach { crop ->
                                        Surface(
                                            color = BackgroundSoft,
                                            shape = RoundedCornerShape(8.dp),
                                            border = androidx.compose.foundation.BorderStroke(0.5.dp, GlassBorder)
                                        ) {
                                            Text(
                                                text = "${crop.cropName} (${crop.boxCount}ص) • ${crop.netWeightKg}كغم",
                                                fontSize = 11.sp,
                                                color = TextPrimaryDark,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                    if (invoice.crops.size > 2) {
                                        Text("+${invoice.crops.size - 2}", fontSize = 10.sp, color = MediumForestGreen, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier
                                    .height(44.dp)
                                    .width(1.dp),
                                color = GlassBorder
                            )

                            // Column 3: Selling Progress & Status
                            Column(
                                modifier = Modifier
                                    .weight(1.2f)
                                    .padding(horizontal = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = if (invoice.status.contains("تسوية")) RedWarningLight else MintGreen,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = invoice.status,
                                            color = if (invoice.status.contains("تسوية")) RedWarningDark else DarkForestGreen,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "${(invoice.progressPercent * 100).toInt()}% مباع",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkForestGreen
                                    )
                                }
                                LinearProgressIndicator(
                                    progress = { invoice.progressPercent },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = if (invoice.progressPercent >= 1.0f) EmeraldSuccess else MediumForestGreen,
                                    trackColor = BackgroundSoft
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier
                                    .height(44.dp)
                                    .width(1.dp),
                                color = GlassBorder
                            )

                            // Column 4: Total IQD & Action Buttons
                            Row(
                                modifier = Modifier
                                    .weight(1.4f)
                                    .padding(start = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("التقديري:", fontSize = 10.sp, color = TextSecondaryMuted)
                                    Text(
                                        text = formatIQD(invoice.totalEstimatedSalesIQD),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DarkForestGreen
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    OutlinedButton(
                                        onClick = { onViewDetails(invoice) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("التفاصيل", fontSize = 11.sp)
                                    }

                                    Button(
                                        onClick = { onViewDetails(invoice) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (invoice.progressPercent >= 1.0f) EmeraldSuccess else SkyBlueInfo
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = if (invoice.progressPercent >= 1.0f) "تسوية" else "متابعة",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Compact / Standard Screen: Sleek Row-Based Card Layout
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Top Row: Code, Name, Vehicle & Status Badge
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
                                            text = invoice.code,
                                            color = DarkForestGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = invoice.farmerName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimaryDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Surface(
                                    color = if (invoice.status.contains("تسوية")) RedWarningLight else MintGreen,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = invoice.status,
                                        color = if (invoice.status.contains("تسوية")) RedWarningDark else DarkForestGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            // Middle Row: Horizontal Summary Chips of Crops & Progress Bar side by side
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
                                    invoice.crops.forEach { crop ->
                                        Surface(
                                            color = BackgroundSoft,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "${crop.cropName}: ${crop.boxCount}ص (${crop.netWeightKg}كغم)",
                                                fontSize = 11.sp,
                                                color = TextPrimaryDark,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.width(120.dp)
                                ) {
                                    LinearProgressIndicator(
                                        progress = { invoice.progressPercent },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = if (invoice.progressPercent >= 1.0f) EmeraldSuccess else MediumForestGreen,
                                        trackColor = BackgroundSoft
                                    )
                                    Text(
                                        text = "${(invoice.progressPercent * 100).toInt()}%",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkForestGreen
                                    )
                                }
                            }

                            HorizontalDivider(color = GlassBorder)

                            // Bottom Row: Total IQD & Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("إجمالي التقديري:", fontSize = 11.sp, color = TextSecondaryMuted)
                                    Text(
                                        text = formatIQD(invoice.totalEstimatedSalesIQD),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DarkForestGreen
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(
                                        onClick = { onViewDetails(invoice) },
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("التفاصيل", fontSize = 11.sp)
                                    }

                                    Button(
                                        onClick = { onViewDetails(invoice) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (invoice.progressPercent >= 1.0f) EmeraldSuccess else SkyBlueInfo
                                        ),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = if (invoice.progressPercent >= 1.0f) "تسوية وشطب" else "متابعة البيع",
                                            color = Color.White,
                                            fontSize = 11.sp,
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
    }
}
