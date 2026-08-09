package com.example.ui.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ImportInvoice
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportInvoiceDetailsSheet(
    invoice: ImportInvoice,
    onDismiss: () -> Unit,
    onSettleAccount: (ImportInvoice) -> Unit,
    formatIQD: (Long) -> String,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = BackgroundSoft
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "تفاصيل فاتورة الاستيراد",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Surface(
                            color = DarkForestGreen.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = invoice.code,
                                color = DarkForestGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Text(
                        text = "التاريخ: ${invoice.date}",
                        fontSize = 12.sp,
                        color = TextSecondaryMuted
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardSurfaceWhite)
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = "إغلاق", tint = TextPrimaryDark)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Farmer & Vehicle Info Card
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = CardSurfaceWhite,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(DarkForestGreen.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Rounded.Person, contentDescription = null, tint = DarkForestGreen)
                                    }
                                    Column {
                                        Text("اسم الفلاح / المورد", fontSize = 11.sp, color = TextSecondaryMuted)
                                        Text(invoice.farmerName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                        if (invoice.farmerPhone.isNotEmpty()) {
                                            Text(invoice.farmerPhone, fontSize = 12.sp, color = MediumForestGreen)
                                        }
                                    }
                                }

                                Surface(
                                    color = if (invoice.progressPercent >= 1.0f) EmeraldSuccess.copy(alpha = 0.15f) else GoldLicenseDark.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = invoice.status,
                                        color = if (invoice.progressPercent >= 1.0f) EmeraldSuccess else GoldLicenseDark,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            HorizontalDivider(color = GlassBorder)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Rounded.DirectionsCar, contentDescription = null, tint = SkyBlueInfo, modifier = Modifier.size(18.dp))
                                    Text("واسطة النقل:", fontSize = 12.sp, color = TextSecondaryMuted)
                                    Text(invoice.vehicleType, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Rounded.PieChart, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(18.dp))
                                    Text("نسبة المباع:", fontSize = 12.sp, color = TextSecondaryMuted)
                                    Text("${(invoice.progressPercent * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkForestGreen)
                                }
                            }

                            // Progress indicator
                            LinearProgressIndicator(
                                progress = invoice.progressPercent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = if (invoice.progressPercent >= 1.0f) EmeraldSuccess else SkyBlueInfo,
                                trackColor = Color.LightGray.copy(alpha = 0.3f)
                            )
                        }
                    }
                }

                // Crops Breakdown Table Header
                item {
                    Text(
                        text = "تفاصيل المحاصيل الواردة في الشحنة",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                // Table Header
                item {
                    Surface(
                        color = DarkForestGreen,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("المحصول", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
                            Text("الصناديق", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            Text("القائم (كغم)", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            Text("الفارغ (كغم)", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            Text("الصافي (كغم)", fontSize = 11.sp, color = GoldLicense, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                        }
                    }
                }

                // Crops Items
                items(invoice.crops) { crop ->
                    Surface(
                        color = CardSurfaceWhite,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(crop.cropName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark, modifier = Modifier.weight(1.5f))
                            Text("${crop.boxCount} خشب", fontSize = 12.sp, color = TextPrimaryDark, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            Text("${crop.grossWeightKg.toInt()}", fontSize = 12.sp, color = TextPrimaryDark, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            Text("${crop.tareWeightKg.toInt()}", fontSize = 12.sp, color = RedWarning, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            Text("${crop.netWeightKg.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = DarkForestGreen, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                        }
                    }
                }

                // Financial Summary
                item {
                    Surface(
                        color = MediumForestGreen.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MediumForestGreen.copy(alpha = 0.2f))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("إجمالي المبيعات التقديرية:", fontSize = 13.sp, color = TextSecondaryMuted)
                                Text(
                                    formatIQD(invoice.totalEstimatedSalesIQD),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DarkForestGreen
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("عمولة العلوة المستقطعة (2%):", fontSize = 12.sp, color = TextSecondaryMuted)
                                Text(
                                    formatIQD((invoice.totalEstimatedSalesIQD * 0.02).toLong()),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RedWarning
                                )
                            }
                            HorizontalDivider(color = MediumForestGreen.copy(alpha = 0.2f))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("الصافي المستحق للفلاح:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                Text(
                                    formatIQD((invoice.totalEstimatedSalesIQD * 0.98).toLong()),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldSuccess
                                )
                            }
                        }
                    }
                }

                // Buttons
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("إغلاق", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                onSettleAccount(invoice)
                                onDismiss()
                            },
                            modifier = Modifier.weight(1.5f),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkForestGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Rounded.AccountBalanceWallet, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Text("تسوية حساب الفلاح", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
