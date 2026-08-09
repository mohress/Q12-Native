package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    var selectedTimePeriod by remember { mutableStateOf("شهر أغسطس 2026") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Bento Header: Time Filter & Audit Buttons Bar
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                BoxWithConstraints(modifier = Modifier.padding(14.dp)) {
                    val isTablet = maxWidth >= 600.dp

                    if (isTablet) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    color = DarkForestGreen.copy(alpha = 0.1f),
                                    shape = CircleShape,
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Rounded.DateRange, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(20.dp))
                                    }
                                }
                                Column {
                                    Text("فترة الإحصائيات:", fontSize = 11.sp, color = TextSecondaryMuted, fontFamily = CairoFontFamily)
                                    Text(selectedTimePeriod, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedButton(
                                    onClick = onOpenProfitReportPreview,
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, SkyBlueInfo)
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Rounded.Analytics, contentDescription = null, tint = SkyBlueInfo, modifier = Modifier.size(16.dp))
                                        Text("تقرير الأرباح الشامل", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily, color = SkyBlueInfo)
                                    }
                                }

                                OutlinedButton(
                                    onClick = onOpenSalesAuditPreview,
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkForestGreen)
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Rounded.Print, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(16.dp))
                                        Text("جرد المبيعات", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily, color = DarkForestGreen)
                                    }
                                }

                                Button(
                                    onClick = onOpenInventoryAuditPreview,
                                    colors = ButtonDefaults.buttonColors(containerColor = MediumForestGreen),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Rounded.Inventory, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Text("جرد المخزون والوارد", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
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
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Rounded.DateRange, contentDescription = null, tint = DarkForestGreen)
                                    Text("فترة الإحصائيات:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark, fontFamily = CairoFontFamily)
                                }

                                Surface(
                                    color = BackgroundSoft,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                                ) {
                                    Text("شهر أغسطس 2026 ▾", fontSize = 11.sp, color = DarkForestGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), fontFamily = CairoFontFamily)
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
                                    Text("الأرباح", fontSize = 11.sp, fontFamily = CairoFontFamily)
                                }

                                OutlinedButton(
                                    onClick = onOpenSalesAuditPreview,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("المبيعات", fontSize = 11.sp, fontFamily = CairoFontFamily)
                                }

                                Button(
                                    onClick = onOpenInventoryAuditPreview,
                                    colors = ButtonDefaults.buttonColors(containerColor = MediumForestGreen),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1.2f)
                                ) {
                                    Text("جرد المخزون", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Bento Grid Section 1: Cash Box & Office Net Profit
        item {
            BoxWithConstraints {
                val isTablet = maxWidth >= 600.dp

                if (isTablet) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Bento Card 1: Smart Cash Box (Wide 1.6f)
                        Surface(
                            modifier = Modifier
                                .weight(1.6f)
                                .shadow(6.dp, RoundedCornerShape(24.dp)),
                            color = DarkForestGreen,
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(DarkForestGreen, MediumForestGreen)
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Surface(
                                                color = Color.White.copy(alpha = 0.15f),
                                                shape = CircleShape,
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(Icons.Rounded.AccountBalanceWallet, contentDescription = null, tint = GoldLicense, modifier = Modifier.size(18.dp))
                                                }
                                            }
                                            Text("رصيد الخزنة الحالي (السيولة المتاحة)", color = MintGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                        }

                                        Text(
                                            text = formatIQD(cashBoxBalance),
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontFamily = CairoFontFamily
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(Icons.Rounded.TrendingUp, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                            Text("+12.4% زيادة السيولة مقارنة بالأسبوع الماضي", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                        }
                                    }

                                    // Quick Cash Operations
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Surface(
                                                modifier = Modifier
                                                    .size(46.dp)
                                                    .clip(RoundedCornerShape(14.dp))
                                                    .clickable { onDepositCash() },
                                                color = EmeraldSuccess.copy(alpha = 0.2f),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess),
                                                shape = RoundedCornerShape(14.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(Icons.Rounded.AddCircle, contentDescription = "Deposit", tint = EmeraldSuccess, modifier = Modifier.size(22.dp))
                                                }
                                            }
                                            Text("إيداع سيولة", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp), fontFamily = CairoFontFamily)
                                        }

                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Surface(
                                                modifier = Modifier
                                                    .size(46.dp)
                                                    .clip(RoundedCornerShape(14.dp))
                                                    .clickable { onOpenNewExpense() },
                                                color = GoldLicense.copy(alpha = 0.2f),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldLicense),
                                                shape = RoundedCornerShape(14.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(Icons.Rounded.Receipt, contentDescription = "Expenses", tint = GoldLicense, modifier = Modifier.size(22.dp))
                                                }
                                            }
                                            Text("مصاريف", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp), fontFamily = CairoFontFamily)
                                        }

                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Surface(
                                                modifier = Modifier
                                                    .size(46.dp)
                                                    .clip(RoundedCornerShape(14.dp))
                                                    .clickable { onOpenNewLoss() },
                                                color = RedWarning.copy(alpha = 0.2f),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, RedWarning),
                                                shape = RoundedCornerShape(14.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(Icons.Rounded.TrendingDown, contentDescription = "Losses", tint = RedWarning, modifier = Modifier.size(22.dp))
                                                }
                                            }
                                            Text("تسجيل خسارة", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp), fontFamily = CairoFontFamily)
                                        }
                                    }
                                }
                            }
                        }

                        // Bento Card 2: Net Profit & Office Commission (1.0f)
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .shadow(6.dp, RoundedCornerShape(24.dp)),
                            color = MediumForestGreen,
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Rounded.MonetizationOn, contentDescription = null, tint = GoldLicense, modifier = Modifier.size(18.dp))
                                        Text("صافي أرباح المكتب", color = MintGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                    }
                                    Surface(color = GoldLicense.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) {
                                        Text("عمولة 7%", color = GoldLicense, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontFamily = CairoFontFamily)
                                    }
                                }

                                Text(
                                    text = formatIQD(netProfit),
                                    color = GoldLicense,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = CairoFontFamily,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("إنجاز هدف الشهر", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontFamily = CairoFontFamily)
                                        Text("85%", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                    }
                                    LinearProgressIndicator(
                                        progress = { 0.85f },
                                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                        color = GoldLicense,
                                        trackColor = Color.White.copy(alpha = 0.2f)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Mobile Portrait Cards Stack
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp, RoundedCornerShape(20.dp)),
                            color = DarkForestGreen,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("رصيد الخزنة الحالي", color = MintGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                    Icon(Icons.Rounded.AccountBalanceWallet, contentDescription = null, tint = GoldLicense, modifier = Modifier.size(20.dp))
                                }

                                Text(
                                    text = formatIQD(cashBoxBalance),
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = CairoFontFamily
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Button(
                                        onClick = onDepositCash,
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("+ إيداع", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = onOpenNewExpense,
                                        colors = ButtonDefaults.buttonColors(containerColor = GoldLicense),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("مصاريف", color = TextPrimaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = onOpenNewLoss,
                                        colors = ButtonDefaults.buttonColors(containerColor = RedWarning),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("خسارة", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                    }
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp, RoundedCornerShape(20.dp)),
                            color = MediumForestGreen,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("أرباح المكتب (عمولة 7%)", color = MintGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                    Icon(Icons.Rounded.TrendingUp, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                                }

                                Text(
                                    text = formatIQD(netProfit),
                                    color = GoldLicense,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = CairoFontFamily
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. NEW Bento Smart AI Section: مراقبة وتحليل أداء العلوة الذكي
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header title for AI Monitoring
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = SkyBlueInfoLight,
                                shape = CircleShape,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = SkyBlueInfo, modifier = Modifier.size(22.dp))
                                }
                            }
                            Column {
                                Text(
                                    "قسم مراقبة وتحليل أداء العلوة الذكي (AI)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = CairoFontFamily,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    "مؤشرات وكفاءة التشغيل والتوصيات الفورية",
                                    fontSize = 11.sp,
                                    fontFamily = CairoFontFamily,
                                    color = TextSecondaryMuted
                                )
                            }
                        }

                        Surface(
                            color = EmeraldSuccessLight,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                Text("الأداء ممتازة (94%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily, color = EmeraldSuccess)
                            }
                        }
                    }

                    Divider(color = GlassBorder)

                    // Smart Grid Cards inside Bento AI
                    BoxWithConstraints {
                        val isTablet = maxWidth >= 600.dp

                        if (isTablet) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Smart AI Insights Column (1.4f)
                                Column(
                                    modifier = Modifier.weight(1.4f),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Insight 1
                                    SmartInsightCard(
                                        icon = Icons.Rounded.Schedule,
                                        iconTint = SkyBlueInfo,
                                        bgLight = SkyBlueInfoLight,
                                        title = "ساعات الذروة في التداول",
                                        description = "تتركز 68% من المبيعات اليومية بين 05:00 ص و 08:30 ص. يُوصى بتركيز كادر الجباية والقطع في هذه الفترة للتسريع."
                                    )

                                    // Insight 2
                                    SmartInsightCard(
                                        icon = Icons.Rounded.PieChart,
                                        iconTint = GoldLicense,
                                        bgLight = GoldLicense.copy(alpha = 0.15f),
                                        title = "المحصول الأكثر طلباً وترجيحاً",
                                        description = "طماطة الزبير والخيار الحلي يشكلان 61% من إجمالي حجم المبيعات الإجمالي هذا الأسبوع بقيمة 10.3 مليون د.ع."
                                    )

                                    // Insight 3
                                    SmartInsightCard(
                                        icon = Icons.Rounded.FlashOn,
                                        iconTint = EmeraldSuccess,
                                        bgLight = EmeraldSuccessLight,
                                        title = "كفاءة التسوية الفورية للمزارعين",
                                        description = "نسبة تسديد المزارعين تبلغ 88% عند استلام الشحنة، مما يحافظ على سيولة نقدية عالية في الخزنة."
                                    )
                                }

                                // Key Performance Indicators (1.0f)
                                Column(
                                    modifier = Modifier.weight(1.0f),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // KPI 1
                                    KpiStatItem(
                                        title = "نسبة الجباية لنقدية",
                                        value = "98.2%",
                                        status = "ممتاز جداً",
                                        progress = 0.98f,
                                        progressColor = EmeraldSuccess
                                    )

                                    // KPI 2
                                    KpiStatItem(
                                        title = "معدل سرعة دوران المخزون",
                                        value = "3.2 ساعة",
                                        status = "سريع جداً",
                                        progress = 0.88f,
                                        progressColor = SkyBlueInfo
                                    )

                                    // KPI 3
                                    KpiStatItem(
                                        title = "مؤشر أجور الحمالة للعمال",
                                        value = "100%",
                                        status = "مستوفاة بالكامل",
                                        progress = 1.0f,
                                        progressColor = GoldLicense
                                    )
                                }
                            }
                        } else {
                            // Mobile Vertical Stack
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                SmartInsightCard(
                                    icon = Icons.Rounded.Schedule,
                                    iconTint = SkyBlueInfo,
                                    bgLight = SkyBlueInfoLight,
                                    title = "ساعات الذروة في التداول",
                                    description = "تتركز 68% من المبيعات اليومية بين 05:00 ص و 08:30 ص."
                                )

                                SmartInsightCard(
                                    icon = Icons.Rounded.PieChart,
                                    iconTint = GoldLicense,
                                    bgLight = GoldLicense.copy(alpha = 0.15f),
                                    title = "المحصول الأكثر طلباً",
                                    description = "طماطة الزبير والخيار يشكلان 61% من إجمالي تداول الأسبوع."
                                )

                                KpiStatItem(
                                    title = "نسبة الجباية النقدية",
                                    value = "98.2%",
                                    status = "ممتاز جداً",
                                    progress = 0.98f,
                                    progressColor = EmeraldSuccess
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Bento Section: Top Crops Sales Volume & Cash vs Credit Split
        item {
            BoxWithConstraints {
                val isTablet = maxWidth >= 600.dp

                if (isTablet) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Bento Card: Top Crops Volume Analysis (1.3f)
                        Surface(
                            modifier = Modifier
                                .weight(1.3f)
                                .shadow(4.dp, RoundedCornerShape(22.dp)),
                            color = CardSurfaceWhite,
                            shape = RoundedCornerShape(22.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Rounded.ShoppingBag, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(20.dp))
                                        Text("أعلى المحاصيل حركية وتداول", fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily, color = TextPrimaryDark)
                                    }
                                    Text("الوزن الإجمالي", fontSize = 11.sp, color = TextSecondaryMuted, fontFamily = CairoFontFamily)
                                }

                                CropVolumeRow("🍅 طماطة زبيرية", "14.5 طن", "6,250,000 د.ع", 0.88f, DarkForestGreen)
                                CropVolumeRow("🥒 خيار حلي (#1 السريع)", "9.2 طن", "4,100,000 د.ع", 0.72f, MediumForestGreen)
                                CropVolumeRow("🥔 بطاطا موصلية", "11.0 طن", "3,500,000 د.ع", 0.64f, SkyBlueInfo)
                                CropVolumeRow("🍆 باذنجان بصرة", "6.8 طن", "2,800,000 د.ع", 0.52f, GoldLicense)
                            }
                        }

                        // Bento Card: Cash vs Deferred Sales Split (1.0f)
                        Surface(
                            modifier = Modifier
                                .weight(1.0f)
                                .shadow(4.dp, RoundedCornerShape(22.dp)),
                            color = CardSurfaceWhite,
                            shape = RoundedCornerShape(22.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Rounded.Payments, contentDescription = null, tint = SkyBlueInfo, modifier = Modifier.size(20.dp))
                                    Text("توزيع نوع الدفع (نقد/آجل)", fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily, color = TextPrimaryDark)
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("مبيعات نقدية (65%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkForestGreen, fontFamily = CairoFontFamily)
                                        Text("8,450,000 د.ع", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkForestGreen, fontFamily = CairoFontFamily)
                                    }
                                    LinearProgressIndicator(
                                        progress = { 0.65f },
                                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                        color = DarkForestGreen,
                                        trackColor = BackgroundSoft
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("مبيعات آجلة/ديون (35%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RedWarning, fontFamily = CairoFontFamily)
                                        Text("5,200,000 د.ع", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RedWarning, fontFamily = CairoFontFamily)
                                    }
                                    LinearProgressIndicator(
                                        progress = { 0.35f },
                                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                        color = RedWarning,
                                        trackColor = BackgroundSoft
                                    )
                                }

                                Surface(
                                    color = BackgroundSoft,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "💡 مؤشر السيولة النقدية صحي وصاعد نتيجة الالتزام بالسقوف الائتمانية.",
                                        fontSize = 10.sp,
                                        fontFamily = CairoFontFamily,
                                        color = TextSecondaryMuted,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Mobile Stack
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(20.dp)),
                            color = CardSurfaceWhite,
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("أعلى المحاصيل تداولاً", fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily, color = TextPrimaryDark)
                                CropVolumeRow("🍅 طماطة زبيرية", "14.5 طن", "6.25 مليون د.ع", 0.88f, DarkForestGreen)
                                CropVolumeRow("🥒 خيار حلي", "9.2 طن", "4.10 مليون د.ع", 0.72f, MediumForestGreen)
                            }
                        }
                    }
                }
            }
        }

        // 5. Bento Section: Financial Cash Flow Matrix (مصفوفة التدفقات المالية)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp)),
                color = CardSurfaceWhite,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.AccountBalance, contentDescription = null, tint = MediumForestGreen, modifier = Modifier.size(20.dp))
                            Text("جدول ومصفوفة التدفقات المالية للشركة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily, color = TextPrimaryDark)
                        }
                        Text("محدث فورياً", fontSize = 11.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Inflows Column
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = EmeraldSuccessLight.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Rounded.ArrowDownward, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                    Text("إجمالي المقبوضات (Inflows)", color = EmeraldSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                }
                                Divider(color = EmeraldSuccess.copy(alpha = 0.2f))
                                FlowItemRow("مبيعات نقدية مستلمة", "8,450,000 د.ع")
                                FlowItemRow("تسديد ديون الزبائن", "5,200,000 د.ع")
                                FlowItemRow("عمولات مكاتب العلوة 7%", "1,120,000 د.ع")
                                FlowItemRow("إضافات سيولة وإيداعات", "3,000,000 د.ع")
                            }
                        }

                        // Outflows Column
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = RedWarningLight.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RedWarning.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Rounded.ArrowUpward, contentDescription = null, tint = RedWarning, modifier = Modifier.size(16.dp))
                                    Text("إجمالي المدفوعات (Outflows)", color = RedWarning, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
                                }
                                Divider(color = RedWarning.copy(alpha = 0.2f))
                                FlowItemRow("تسديد مستحقات المزارعين", "11,200,000 د.ع")
                                FlowItemRow("أجور وحسنيات الحمالية", "850,000 د.ع")
                                FlowItemRow("مصاريف التشغيل والكهرباء", "185,000 د.ع")
                                FlowItemRow("مصاريف شخصية وتلفيات", "250,000 د.ع")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SmartInsightCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    bgLight: Color,
    title: String,
    description: String
) {
    Surface(
        color = bgLight,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                color = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily, color = TextPrimaryDark)
                Text(description, fontSize = 11.sp, fontFamily = CairoFontFamily, color = TextSecondaryMuted)
            }
        }
    }
}

@Composable
private fun KpiStatItem(
    title: String,
    value: String,
    status: String,
    progress: Float,
    progressColor: Color
) {
    Surface(
        color = BackgroundSoft,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 11.sp, fontFamily = CairoFontFamily, color = TextSecondaryMuted)
                Text(status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = progressColor, fontFamily = CairoFontFamily)
            }

            Text(value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, fontFamily = CairoFontFamily, color = TextPrimaryDark)

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)),
                color = progressColor,
                trackColor = GlassBorder
            )
        }
    }
}

@Composable
private fun CropVolumeRow(
    cropName: String,
    weight: String,
    amountStr: String,
    progress: Float,
    barColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(cropName, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily, color = TextPrimaryDark)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(weight, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = barColor, fontFamily = CairoFontFamily)
                Text(amountStr, fontSize = 11.sp, color = TextSecondaryMuted, fontFamily = CairoFontFamily)
            }
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = barColor,
            trackColor = BackgroundSoft
        )
    }
}

@Composable
private fun FlowItemRow(label: String, valueStr: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("• $label", fontSize = 11.sp, fontFamily = CairoFontFamily, color = TextPrimaryDark)
        Text(valueStr, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily, color = TextPrimaryDark)
    }
}
