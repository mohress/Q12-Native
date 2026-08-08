package com.example.ui.modals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.CustomerDebt
import com.example.ui.theme.*

@Composable
fun DebtAlertsDialog(
    debts: List<CustomerDebt>,
    onDismiss: () -> Unit,
    onOpenPaymentModal: (String, Long) -> Unit,
    formatIQD: (Long) -> String,
    modifier: Modifier = Modifier
) {
    val overdueDebts = debts.filter { it.status == "متأخرة" }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Rounded.NotificationsActive, contentDescription = null, tint = RedWarning)
                Text(
                    text = "تنبيهات استحقاق الديون المتأخرة",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "يوجد عدد (${overdueDebts.size}) زبائن تجاوزوا فترة الاستحقاق المحددة للديون الآجلة:",
                    fontSize = 12.sp,
                    color = TextSecondaryMuted
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 260.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(overdueDebts, key = { it.id }) { debt ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = CardSurfaceWhite,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RedWarningLight)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(debt.customerName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                    Text("تاريخ الاستحقاق: ${debt.dueDate}", fontSize = 10.sp, color = RedWarning)
                                    Text(formatIQD(debt.totalDebtIQD), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = RedWarning)
                                }

                                Button(
                                    onClick = {
                                        onDismiss()
                                        onOpenPaymentModal(debt.customerName, debt.totalDebtIQD)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("تحصيل", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = DarkForestGreen)) {
                Text("إغلاق التنبيهات", color = Color.White)
            }
        },
        containerColor = BackgroundSoft,
        shape = RoundedCornerShape(20.dp)
    )
}
