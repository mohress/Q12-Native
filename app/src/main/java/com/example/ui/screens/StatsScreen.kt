package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.models.AppLog
import com.example.data.models.ExpenseItem
import com.example.data.models.LossItem
import com.example.ui.theme.*

@Composable
fun StatsScreen(
    cashBoxBalance: Long,
    netProfit: Long,
    expenses: List<ExpenseItem>,
    losses: List<LossItem>,
    logs: List<AppLog>,
    onOpenNewExpense: () -> Unit,
    onOpenNewLoss: () -> Unit,
    onDepositCash: () -> Unit,
    onOpenProfitReportPreview: () -> Unit = {},
    onOpenSalesAuditPreview: () -> Unit = {},
    onOpenInventoryAuditPreview: () -> Unit = {},
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
        // 1. Period Selector & Print Action Buttons Bar (شريط تصفية الفترة والطباعة)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(18.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                BoxWithConstraints(modifier = Modifier.padding(14.dp)) {
                    val isTablet = maxWidth > 600.dp

                    if (isTablet) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Rounded.DateRange, contentDescription = null, tint = MediumForestGreen)
                                Text("فترة الإحصائيات:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                Surface(
                                    color = BackgroundSoft,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                                ) {
                                    Text("شهر أغسطس 2026 ▾", fontSize = 12.sp, color = DarkForestGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = onOpenProfitReportPreview,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Rounded.Analytics, contentDescription = null, tint = SkyBlueInfo, modifier = Modifier.size(16.dp))
                                        Text("تقرير الأرباح", fontSize = 11.sp)
                                    }
                                }

                                OutlinedButton(
                                    onClick = onOpenSalesAuditPreview,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Rounded.Print, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(16.dp))
                                        Text("جرد مبيعات", fontSize = 11.sp)
                                    }
                                }

                                Button(
                                    onClick = onOpenInventoryAuditPreview,
                                    colors = ButtonDefaults.buttonColors(containerColor = MediumForestGreen),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Rounded.Print, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Text("جرد المخزون", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Rounded.DateRange, contentDescription = null, tint = MediumForestGreen)
                                    Text("فترة الإحصائيات:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                }

                                Surface(
                                    color = BackgroundSoft,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                                ) {
                                    Text("شهر أغسطس 2026 ▾", fontSize = 12.sp, color = DarkForestGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onOpenProfitReportPreview,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("تقرير الأرباح", fontSize = 11.sp)
                                }

                                OutlinedButton(
                                    onClick = onOpenSalesAuditPreview,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("جرد مبيعات", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = onOpenInventoryAuditPreview,
                                    colors = ButtonDefaults.buttonColors(containerColor = MediumForestGreen),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1.2f)
                                ) {
                                    Text("جرد المخزون", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Smart Cash Box & Net Profit Cards (بطاقتي الخزنة والأرباح بتنسيق سطري وعرضي)
        item {
            BoxWithConstraints {
                val isTablet = maxWidth > 600.dp

                if (isTablet) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Smart Cash Box Wide Horizontal Row Card
                        Surface(
                            modifier = Modifier
                                .weight(1.5f)
                                .shadow(6.dp, RoundedCornerShape(22.dp)),
                            color = DarkForestGreen,
                            shape = RoundedCornerShape(22.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(18.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Rounded.AccountBalanceWallet, contentDescription = null, tint = GoldLicense, modifier = Modifier.size(20.dp))
                                        Text("رصيد الخزنة الحالي", color = MintGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Text(
                                        text = formatIQD(cashBoxBalance),
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }

                                // Horizontal Action Buttons
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    // [مصاريف]
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Surface(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable { onOpenNewExpense() },
                                            color = Color.White.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(Icons.Rounded.Receipt, contentDescription = "Expenses", tint = GoldLicense, modifier = Modifier.size(22.dp))
                                            }
                                        }
                                        Text("مصاريف", color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))
                                    }

                                    // [خسائر]
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Surface(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable { onOpenNewLoss() },
                                            color = Color.White.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(Icons.Rounded.TrendingDown, contentDescription = "Losses", tint = RedWarning, modifier = Modifier.size(22.dp))
                                            }
                                        }
                                        Text("خسائر", color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))
                                    }

                                    // [إيداع سيولة]
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Surface(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable { onDepositCash() },
                                            color = Color.White.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(Icons.Rounded.Add, contentDescription = "Deposit", tint = EmeraldSuccess, modifier = Modifier.size(22.dp))
                                            }
                                        }
                                        Text("إيداع", color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))
                                    }
                                }
                            }
                        }

                        // Net Office Profit Card
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .shadow(6.dp, RoundedCornerShape(22.dp)),
                            color = MediumForestGreen,
                            shape = RoundedCornerShape(22.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(18.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("صافي أرباح المكتب", color = MintGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Icon(Icons.Rounded.TrendingUp, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                                }

                                Text(
                                    text = formatIQD(netProfit),
                                    color = GoldLicense,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Text("صافي العمولة (7%)", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Smart Cash Box Card
                        Surface(
                            modifier = Modifier
                                .weight(1.3f)
                                .shadow(6.dp, RoundedCornerShape(20.dp)),
                            color = DarkForestGreen,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("رصيد الخزنة", color = MintGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Icon(Icons.Rounded.AccountBalanceWallet, contentDescription = null, tint = GoldLicense, modifier = Modifier.size(18.dp))
                                }

                                Text(
                                    text = formatIQD(cashBoxBalance),
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Surface(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { onOpenNewExpense() },
                                        color = Color.White.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Rounded.Receipt, contentDescription = "Expenses", tint = GoldLicense, modifier = Modifier.size(18.dp))
                                        }
                                    }

                                    Surface(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { onOpenNewLoss() },
                                        color = Color.White.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Rounded.TrendingDown, contentDescription = "Losses", tint = RedWarning, modifier = Modifier.size(18.dp))
                                        }
                                    }

                                    Surface(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { onDepositCash() },
                                        color = Color.White.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Rounded.Add, contentDescription = "Deposit", tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }

                        // Net Office Profit Card
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .shadow(6.dp, RoundedCornerShape(20.dp)),
                            color = MediumForestGreen,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("أرباح المكتب", color = MintGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Icon(Icons.Rounded.TrendingUp, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = formatIQD(netProfit),
                                    color = GoldLicense,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Text("صافي 7%", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // 3. Financial Flows Breakdown (جدول التدفقات المالية العرضي)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(18.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("جدول التدفقات المالية للشركة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Inflows
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = EmeraldSuccessLight.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("المقبوضات (Inflows) 📥", color = EmeraldSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("• مبيعات نقدية: 8,450,000 د.ع", fontSize = 11.sp, color = TextPrimaryDark)
                                Text("• مبيعات آجلة: 5,200,000 د.ع", fontSize = 11.sp, color = TextPrimaryDark)
                                Text("• عمولة المكتب 7%: 1,120,000 د.ع", fontSize = 11.sp, color = TextPrimaryDark)
                                Text("• إضافات سيولة: 3,000,000 د.ع", fontSize = 11.sp, color = TextPrimaryDark)
                            }
                        }

                        // Outflows
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = RedWarningLight.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("المدفوعات (Outflows) 📤", color = RedWarning, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("• مصاريف يومية: 185,000 د.ع", fontSize = 11.sp, color = TextPrimaryDark)
                                Text("• رواتب الموظفين: 600,000 د.ع", fontSize = 11.sp, color = TextPrimaryDark)
                                Text("• مصاريف شخصية: 250,000 د.ع", fontSize = 11.sp, color = TextPrimaryDark)
                                Text("• خسائر وتلفيات: 120,000 د.ع", fontSize = 11.sp, color = TextPrimaryDark)
                            }
                        }
                    }
                }
            }
        }

        // 4. Recent Expenses Log Horizontal Row Cards
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(18.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("سجل المصروفات والخسائر الأخيرة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)

                    expenses.take(3).forEach { exp ->
                        Surface(
                            color = BackgroundSoft,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Rounded.Receipt, contentDescription = null, tint = RedWarning, modifier = Modifier.size(18.dp))
                                    Column {
                                        Text(exp.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                        Text("${exp.category} • ${exp.date}", fontSize = 10.sp, color = TextSecondaryMuted)
                                    }
                                }
                                Text("-${formatIQD(exp.amountIQD)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RedWarning)
                            }
                        }
                    }
                }
            }
        }

        // 5. Daily Application Log Row Cards
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(18.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Rounded.HistoryEdu, contentDescription = null, tint = DarkForestGreen)
                        Text("سجل عمليات التطبيق اليومي", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                    }

                    logs.take(4).forEach { log ->
                        Surface(
                            color = BackgroundSoft,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(log.action, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkForestGreen)
                                    Text(log.details, fontSize = 11.sp, color = TextSecondaryMuted)
                                }
                                Text(log.timestamp, fontSize = 10.sp, color = TextSecondaryMuted)
                            }
                        }
                    }
                }
            }
        }
    }
}
