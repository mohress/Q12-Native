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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.CustomerDebt
import com.example.data.models.FarmerReceivable
import com.example.ui.theme.*

@Composable
fun AccountsScreen(
    activeSubTab: Int, // 0: Debts, 1: Farmers, 2: Porters
    onSubTabSelect: (Int) -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    activeFilter: String,
    onFilterChange: (String) -> Unit,
    customerDebts: List<CustomerDebt>,
    farmerReceivables: List<FarmerReceivable>,
    porterFeesCollected: Long,
    porterCount: Int,
    onOpenPaymentModal: (String, Long) -> Unit,
    onOpenPorterPayoutModal: () -> Unit,
    formatIQD: (Long) -> String,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Segmented Control Subtabs
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(18.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Row(
                    modifier = Modifier.padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val tabs = listOf(
                        Triple("ديون الزبائن", Icons.Rounded.PeopleAlt, 0),
                        Triple("مستحقات الفلاحين", Icons.Rounded.Agriculture, 1),
                        Triple("مستحقات الحمالين", Icons.Rounded.Engineering, 2)
                    )

                    tabs.forEach { (label, icon, idx) ->
                        val isSelected = activeSubTab == idx
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onSubTabSelect(idx) },
                            color = if (isSelected) DarkForestGreen else Color.Transparent,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) GoldLicense else TextSecondaryMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextSecondaryMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        when (activeSubTab) {
            0 -> {
                // Customer Debts Tab
                // Ultra-Compact Search & Filter Bar (Debts)
                item {
                    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                        if (maxWidth > 600.dp) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = onSearchChange,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .shadow(1.dp, RoundedCornerShape(12.dp)),
                                    placeholder = { Text("ابحث باسم الزبون المدين...", fontSize = 11.sp, fontFamily = CairoFontFamily) },
                                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search", tint = MediumForestGreen, modifier = Modifier.size(18.dp)) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedBorderColor = MediumForestGreen,
                                        unfocusedBorderColor = GlassBorder
                                    ),
                                    textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, fontFamily = CairoFontFamily)
                                )

                                val filterChips = listOf("الجميع", "متأخرة 🚨", "قادمة ⏳")
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    filterChips.forEach { chip ->
                                        val isSelected = activeFilter == chip
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = { onFilterChange(chip) },
                                            label = { Text(chip, fontSize = 11.sp, color = if (isSelected) Color.White else TextPrimaryDark, fontFamily = CairoFontFamily) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = DarkForestGreen,
                                                containerColor = CardSurfaceWhite
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.height(36.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = onSearchChange,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(42.dp)
                                        .shadow(1.dp, RoundedCornerShape(12.dp)),
                                    placeholder = { Text("ابحث باسم الزبون المدين...", fontSize = 11.sp, fontFamily = CairoFontFamily) },
                                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search", tint = MediumForestGreen, modifier = Modifier.size(18.dp)) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedBorderColor = MediumForestGreen,
                                        unfocusedBorderColor = GlassBorder
                                    ),
                                    textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, fontFamily = CairoFontFamily)
                                )

                                val filterChips = listOf("الجميع", "متأخرة 🚨", "قادمة ⏳")
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(filterChips) { chip ->
                                        val isSelected = activeFilter == chip
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = { onFilterChange(chip) },
                                            label = { Text(chip, fontSize = 11.sp, color = if (isSelected) Color.White else TextPrimaryDark, fontFamily = CairoFontFamily) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = DarkForestGreen,
                                                containerColor = CardSurfaceWhite
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.height(34.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                val filteredDebts = customerDebts.filter { debt ->
                    val matchesSearch = debt.customerName.contains(searchQuery, ignoreCase = true)
                    val matchesFilter = when (activeFilter) {
                        "متأخرة 🚨" -> debt.status == "متأخرة"
                        "قادمة ⏳" -> debt.status == "قادمة"
                        else -> true
                    }
                    matchesSearch && matchesFilter
                }

                items(filteredDebts, key = { it.id }) { debt ->
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
                            val isTabletLandscape = maxWidth > 600.dp

                            if (isTabletLandscape) {
                                // Tablet Wide Horizontal Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Customer Info
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.weight(1.3f)
                                    ) {
                                        Surface(
                                            color = if (debt.status == "متأخرة") RedWarningLight else SkyBlueInfoLight,
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Box(modifier = Modifier.padding(8.dp)) {
                                                Icon(
                                                    Icons.Rounded.Warning,
                                                    contentDescription = null,
                                                    tint = if (debt.status == "متأخرة") RedWarning else SkyBlueInfo,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                        Column {
                                            Text(
                                                text = debt.customerName,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = TextPrimaryDark,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "الهاتف: ${debt.customerPhone} • استحقاق: ${debt.dueDate}",
                                                fontSize = 11.sp,
                                                color = TextSecondaryMuted
                                            )
                                        }
                                    }

                                    // Debt Status Badge & Amount
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Surface(
                                            color = if (debt.status == "متأخرة") RedWarningLight else SkyBlueInfoLight,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = if (debt.status == "متأخرة") "متأخرة 🚨" else "استحقاق قادم ⏳",
                                                color = if (debt.status == "متأخرة") RedWarning else SkyBlueInfo,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = formatIQD(debt.totalDebtIQD),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = RedWarning
                                        )
                                    }

                                    // Actions
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        OutlinedButton(
                                            onClick = { onOpenPaymentModal(debt.customerName, debt.totalDebtIQD / 2) },
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("تسديد جزء", fontSize = 11.sp)
                                        }

                                        Button(
                                            onClick = { onOpenPaymentModal(debt.customerName, debt.totalDebtIQD) },
                                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("تسديد كامل", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            } else {
                                // Mobile Compact Horizontal Row Layout
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = debt.customerName,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = TextPrimaryDark,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "رقم الهاتف: ${debt.customerPhone}",
                                                fontSize = 12.sp,
                                                color = TextSecondaryMuted
                                            )
                                        }

                                        Surface(
                                            color = if (debt.status == "متأخرة") RedWarningLight else SkyBlueInfoLight,
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(
                                                text = if (debt.status == "متأخرة") "متأخرة 🚨" else "قادمة ⏳",
                                                color = if (debt.status == "متأخرة") RedWarning else SkyBlueInfo,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    HorizontalDivider(color = GlassBorder)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("استحقاق: ${debt.dueDate}", fontSize = 10.sp, color = TextSecondaryMuted)
                                            Text(
                                                text = formatIQD(debt.totalDebtIQD),
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = RedWarning
                                            )
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            OutlinedButton(
                                                onClick = { onOpenPaymentModal(debt.customerName, debt.totalDebtIQD / 2) },
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Text("تسديد جزء", fontSize = 11.sp)
                                            }

                                            Button(
                                                onClick = { onOpenPaymentModal(debt.customerName, debt.totalDebtIQD) },
                                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Text("تسديد كامل", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Farmers Receivables Tab
                // Ultra-Compact Search & Filter Bar (Farmers)
                item {
                    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                        if (maxWidth > 600.dp) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = onSearchChange,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .shadow(1.dp, RoundedCornerShape(12.dp)),
                                    placeholder = { Text("ابحث باسم الفلاح...", fontSize = 11.sp, fontFamily = CairoFontFamily) },
                                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search", tint = MediumForestGreen, modifier = Modifier.size(18.dp)) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedBorderColor = MediumForestGreen,
                                        unfocusedBorderColor = GlassBorder
                                    ),
                                    textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, fontFamily = CairoFontFamily)
                                )

                                val filterChips = listOf("الجميع", "مستحقات اليوم", "مستحقات سابقة")
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    filterChips.forEach { chip ->
                                        val isSelected = activeFilter == chip
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = { onFilterChange(chip) },
                                            label = { Text(chip, fontSize = 11.sp, color = if (isSelected) Color.White else TextPrimaryDark, fontFamily = CairoFontFamily) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = DarkForestGreen,
                                                containerColor = CardSurfaceWhite
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.height(36.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = onSearchChange,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(42.dp)
                                        .shadow(1.dp, RoundedCornerShape(12.dp)),
                                    placeholder = { Text("ابحث باسم الفلاح...", fontSize = 11.sp, fontFamily = CairoFontFamily) },
                                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search", tint = MediumForestGreen, modifier = Modifier.size(18.dp)) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedBorderColor = MediumForestGreen,
                                        unfocusedBorderColor = GlassBorder
                                    ),
                                    textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, fontFamily = CairoFontFamily)
                                )

                                val filterChips = listOf("الجميع", "مستحقات اليوم", "مستحقات سابقة")
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(filterChips) { chip ->
                                        val isSelected = activeFilter == chip
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = { onFilterChange(chip) },
                                            label = { Text(chip, fontSize = 11.sp, color = if (isSelected) Color.White else TextPrimaryDark, fontFamily = CairoFontFamily) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = DarkForestGreen,
                                                containerColor = CardSurfaceWhite
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.height(34.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                val filteredFarmers = farmerReceivables.filter { farmer ->
                    val matchesSearch = farmer.farmerName.contains(searchQuery, ignoreCase = true)
                    val matchesFilter = when (activeFilter) {
                        "مستحقات اليوم" -> farmer.status == "مستحقات اليوم"
                        "مستحقات سابقة" -> farmer.status == "مستحقات سابقة"
                        else -> true
                    }
                    matchesSearch && matchesFilter
                }

                items(filteredFarmers, key = { it.id }) { farmer ->
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
                            val isTabletLandscape = maxWidth > 600.dp

                            if (isTabletLandscape) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1.3f)) {
                                        Text(
                                            text = farmer.farmerName,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = TextPrimaryDark,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "الهاتف: ${farmer.farmerPhone} • ${farmer.date}",
                                            fontSize = 11.sp,
                                            color = TextSecondaryMuted
                                        )
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1.2f)
                                    ) {
                                        Text("بيع المحاصيل: ${formatIQD(farmer.totalSalesIQD)}", fontSize = 10.sp, color = TextSecondaryMuted)
                                        Text(
                                            text = "الصافي (بعد 2%): ${formatIQD(farmer.netAmountIQD)}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkForestGreen
                                        )
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Button(
                                            onClick = { /* Pay farmer */ },
                                            colors = ButtonDefaults.buttonColors(containerColor = MediumForestGreen),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("دفع المستحقات", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                                        Column {
                                            Text(
                                                text = farmer.farmerName,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = TextPrimaryDark,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "الهاتف: ${farmer.farmerPhone} • ${farmer.date}",
                                                fontSize = 11.sp,
                                                color = TextSecondaryMuted
                                            )
                                        }

                                        Surface(
                                            color = MintGreen,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = farmer.status,
                                                color = DarkForestGreen,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    HorizontalDivider(color = GlassBorder)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("إجمالي البيع: ${formatIQD(farmer.totalSalesIQD)}", fontSize = 10.sp, color = TextSecondaryMuted)
                                            Text(
                                                text = "الصافي: ${formatIQD(farmer.netAmountIQD)}",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = DarkForestGreen
                                            )
                                        }

                                        Button(
                                            onClick = { /* Pay farmer */ },
                                            colors = ButtonDefaults.buttonColors(containerColor = MediumForestGreen),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("دفع المستحقات", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Porters Receivables Tab Wide Horizontal Card
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(22.dp)),
                        color = DarkForestGreen,
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        BoxWithConstraints(modifier = Modifier.padding(20.dp)) {
                            val isTablet = maxWidth > 600.dp

                            if (isTablet) {
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
                                        Icon(Icons.Rounded.Engineering, contentDescription = null, tint = GoldLicense, modifier = Modifier.size(36.dp))
                                        Column {
                                            Text("أجور الحمالية اليومية المجمعة", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                            Text("المبلغ الكلي: ${formatIQD(porterFeesCollected)}", color = GoldLicense, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                                        }
                                    }

                                    if (porterCount > 0 && porterFeesCollected > 0) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        ) {
                                            Text("حصة العامل (${porterCount} حمالين):", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                                            Text(formatIQD(porterFeesCollected / porterCount), color = MintGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Button(
                                        onClick = onOpenPorterPayoutModal,
                                        colors = ButtonDefaults.buttonColors(containerColor = GoldLicense),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.height(44.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Rounded.Payments, contentDescription = null, tint = TextPrimaryDark)
                                            Text("توزيع وصرف المستحقات", color = TextPrimaryDark, fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
                                        Column {
                                            Text("ملخص أجور الحمالية اليومية", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                            Text("مجموع المبالغ المستقطعة من بيوعات اليوم", color = MintGreen, fontSize = 11.sp)
                                        }
                                        Icon(Icons.Rounded.Engineering, contentDescription = null, tint = GoldLicense, modifier = Modifier.size(32.dp))
                                    }

                                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("المبالغ المجمعة بالحمالية:", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                                            Text(
                                                text = formatIQD(porterFeesCollected),
                                                color = GoldLicense,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("عدد الحمالين المسجلين:", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                                            Text(
                                                text = "$porterCount عمال",
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = onOpenPorterPayoutModal,
                                        colors = ButtonDefaults.buttonColors(containerColor = GoldLicense),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(46.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Rounded.Payments, contentDescription = null, tint = TextPrimaryDark)
                                            Text(
                                                text = "توزيع وصرف مستحقات الحمالين",
                                                color = TextPrimaryDark,
                                                fontSize = 13.sp,
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
}
