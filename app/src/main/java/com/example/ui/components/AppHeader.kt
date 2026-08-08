package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.ui.theme.*

@Composable
fun AppHeader(
    alwaName: String,
    brightness: Int,
    onAdjustBrightness: (Int) -> Unit,
    deviceBattery: Int,
    printerConnected: Boolean,
    onTogglePrinter: () -> Unit,
    debtAlertsCount: Int,
    onOpenDebtAlerts: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
        color = DarkForestGreen,
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            val isWideScreen = maxWidth >= 600.dp

            if (isWideScreen) {
                // Single Row Layout for Wide / Tablet Screens
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. App Title Card
                    AppTitleCard(alwaName = alwaName)

                    // 2. Date Card
                    DateCard()

                    // 3. Debt Alerts Card
                    DebtAlertsCard(
                        count = debtAlertsCount,
                        onClick = onOpenDebtAlerts
                    )

                    // 4. Hardware Controls Group (Brightness, Battery, Printer)
                    Surface(
                        color = Color.Black.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BrightnessControl(
                                brightness = brightness,
                                onAdjustBrightness = onAdjustBrightness
                            )

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(16.dp)
                                    .background(Color.White.copy(alpha = 0.2f))
                            )

                            DeviceBatteryPill(deviceBattery = deviceBattery)

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(16.dp)
                                    .background(Color.White.copy(alpha = 0.2f))
                            )

                            PrinterPill(
                                printerConnected = printerConnected,
                                onTogglePrinter = onTogglePrinter
                            )
                        }
                    }
                }
            } else {
                // Compact 2-Row Layout for Narrow Phone Screens
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Row 1: App Title & Right Items (Date + Debt Alerts)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTitleCard(alwaName = alwaName)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DebtAlertsCard(
                                count = debtAlertsCount,
                                onClick = onOpenDebtAlerts
                            )
                            DateCard()
                        }
                    }

                    // Row 2: Hardware Status Panel
                    Surface(
                        color = Color.Black.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BrightnessControl(
                                brightness = brightness,
                                onAdjustBrightness = onAdjustBrightness
                            )
                            DeviceBatteryPill(deviceBattery = deviceBattery)
                            PrinterPill(
                                printerConnected = printerConnected,
                                onTogglePrinter = onTogglePrinter
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppTitleCard(alwaName: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(DarkForestGreen, MediumForestGreen)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ReceiptLong,
                    contentDescription = "Logo",
                    tint = GoldLicense,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = "محاسب العلوة",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp
                )
                Text(
                    text = alwaName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MediumForestGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun DateCard() {
    Surface(
        color = Color.White.copy(alpha = 0.12f),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Today,
                contentDescription = "Date",
                tint = GoldLicense,
                modifier = Modifier.size(15.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "الأحد",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "2 أغسطس 2026",
                    color = MintGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun DebtAlertsCard(count: Int, onClick: () -> Unit) {
    Surface(
        color = if (count > 0) RedWarning.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.12f),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (count > 0) RedWarning.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.2f)
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box {
                Icon(
                    imageVector = Icons.Rounded.Notifications,
                    contentDescription = "Alerts",
                    tint = if (count > 0) GoldLicense else Color.White,
                    modifier = Modifier.size(15.dp)
                )
                if (count > 0) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.TopEnd)
                            .clip(CircleShape)
                            .background(RedWarning),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = count.toString(),
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Text(
                text = "التنبيهات${if (count > 0) " ($count)" else ""}",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BrightnessControl(brightness: Int, onAdjustBrightness: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.LightMode,
            contentDescription = "Brightness",
            tint = GoldLicense,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = "الإضاءة:",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 10.sp
        )
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
                .clickable { onAdjustBrightness(-10) },
            contentAlignment = Alignment.Center
        ) {
            Text("-", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            text = "$brightness%",
            color = GoldLicense,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
                .clickable { onAdjustBrightness(10) },
            contentAlignment = Alignment.Center
        ) {
            Text("+", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun DeviceBatteryPill(deviceBattery: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.BatteryStd,
            contentDescription = "Battery",
            tint = MintGreen,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = "البطارية: $deviceBattery%",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PrinterPill(printerConnected: Boolean, onTogglePrinter: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (printerConnected) EmeraldSuccess.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.1f))
            .clickable { onTogglePrinter() }
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = if (printerConnected) Icons.Rounded.Print else Icons.Rounded.RadioButtonUnchecked,
            contentDescription = "Printer",
            tint = if (printerConnected) EmeraldSuccess else Color.LightGray,
            modifier = Modifier.size(13.dp)
        )
        Text(
            text = if (printerConnected) "طابعة: متصلة (95%)" else "طابعة: غير متصلة",
            color = if (printerConnected) EmeraldSuccess else Color.LightGray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

