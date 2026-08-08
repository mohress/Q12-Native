package com.example.ui.modals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun PorterPayoutModal(
    totalCollected: Long,
    porterCount: Int,
    onDismiss: () -> Unit,
    onSubmitPayout: () -> Unit,
    formatIQD: (Long) -> String,
    modifier: Modifier = Modifier
) {
    val sharePerPorter = if (porterCount > 0) totalCollected / porterCount else 0L

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Rounded.Engineering, contentDescription = null, tint = GoldLicense)
                Text(
                    text = "صرف مستحقات الحمالين اليومية",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("إجمالي المبالغ المجمعة اليوم: ${formatIQD(totalCollected)}", fontSize = 13.sp, color = TextPrimaryDark)
                Text("عدد الحمالين المسجلين: $porterCount عمال", fontSize = 13.sp, color = TextPrimaryDark)

                Surface(
                    color = DarkForestGreen,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("حصة الحمال الواحد اليوم:", color = MintGreen, fontSize = 11.sp)
                        Text(formatIQD(sharePerPorter), color = GoldLicense, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }

                Text(
                    text = "عند الصرف، سيتم استقطاع كامل المبلغ من الخزنة وصرفه للحمالين بالتساوي وتصفير الميزانية اليومية للحمالية.",
                    fontSize = 11.sp,
                    color = TextSecondaryMuted
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onSubmitPayout,
                colors = ButtonDefaults.buttonColors(containerColor = GoldLicense),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("تأكيد الصرف وتصفير الحمالية", color = TextPrimaryDark, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                Text("إلغاء")
            }
        },
        containerColor = BackgroundSoft,
        shape = RoundedCornerShape(20.dp)
    )
}
