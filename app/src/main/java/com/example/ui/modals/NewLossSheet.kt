package com.example.ui.modals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewLossSheet(
    onDismiss: () -> Unit,
    onSubmit: (type: String, cropName: String, damagedWeightKg: Double, reason: String, amountIQD: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var lossType by remember { mutableStateOf("تلف محصول") } // تلف محصول or خسائر أخرى
    var cropName by remember { mutableStateOf("طماطة النجف") }
    var damagedWeightText by remember { mutableStateOf("15") }
    var reason by remember { mutableStateOf("تلف بسبب النقل والحرارة") }
    var amountText by remember { mutableStateOf("25000") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = BackgroundSoft
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("تسجيل خسارة أو تلفيات", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("تلف محصول", "خسائر أخرى").forEach { type ->
                    FilterChip(
                        selected = lossType == type,
                        onClick = { lossType = type },
                        label = { Text(type, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RedWarning,
                            containerColor = CardSurfaceWhite
                        )
                    )
                }
            }

            if (lossType == "تلف محصول") {
                OutlinedTextField(
                    value = cropName,
                    onValueChange = { cropName = it },
                    label = { Text("اسم الصنف التالف") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = damagedWeightText,
                    onValueChange = { damagedWeightText = it },
                    label = { Text("الوزن التالف (كغم)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("سبب الخسارة") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("مبلغ الخسارة التقديري (د.ع)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = {
                    val amt = amountText.toLongOrNull() ?: 0L
                    if (amt > 0) {
                        onSubmit(
                            lossType,
                            cropName,
                            damagedWeightText.toDoubleOrNull() ?: 0.0,
                            reason,
                            amt
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RedWarningDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("تسجيل الخسارة وتحديث الحسابات", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
