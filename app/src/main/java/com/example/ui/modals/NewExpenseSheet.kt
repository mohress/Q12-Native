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
fun NewExpenseSheet(
    onDismiss: () -> Unit,
    onSubmit: (category: String, title: String, amount: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var category by remember { mutableStateOf("مصاريف يومية") } // مصاريف يومية, رواتب, مصاريف شخصية
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }

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
                Text("تسجيل مصروف جديد", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close")
                }
            }

            Text("تبويب نوع المصروف:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkForestGreen)

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("مصاريف يومية", "رواتب", "مصاريف شخصية").forEach { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkForestGreen,
                            containerColor = CardSurfaceWhite
                        )
                    )
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("الموضوع / الوصف") },
                placeholder = { Text("مثال: شراء أكياس أو ضيافة مكتب") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("المبلغ المستقطع (بالدينار)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = {
                    val amt = amountText.toLongOrNull() ?: 0L
                    if (title.isNotEmpty() && amt > 0) {
                        onSubmit(category, title, amt)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RedWarning),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("حفظ المصروف وتحديث الخزنة", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
