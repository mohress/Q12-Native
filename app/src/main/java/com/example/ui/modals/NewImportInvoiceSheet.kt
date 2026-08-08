package com.example.ui.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ImportCrop
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewImportInvoiceSheet(
    onDismiss: () -> Unit,
    onSubmit: (farmerName: String, vehicleType: String, crops: List<ImportCrop>) -> Unit,
    modifier: Modifier = Modifier
) {
    var farmerName by remember { mutableStateOf("جاسم المحمدي") }
    var vehicleType by remember { mutableStateOf("تريلة مرسيدس") }

    var cropName by remember { mutableStateOf("طماطة النجف") }
    var boxCountText by remember { mutableStateOf("200") }
    var grossWeightText by remember { mutableStateOf("6000") }
    var tareWeightText by remember { mutableStateOf("400") }

    val cropsList = remember {
        mutableStateListOf(
            ImportCrop("طماطة النجف", 200, 6000.0, 400.0)
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = BackgroundSoft
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "إنشاء فاتورة استيراد جديدة",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close")
                    }
                }
            }

            // Farmer & Vehicle Info
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CardSurfaceWhite,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("بيانات المورد / الفلاح الشاحن:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkForestGreen)

                        OutlinedTextField(
                            value = farmerName,
                            onValueChange = { farmerName = it },
                            label = { Text("اسم الفلاح / المورد") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = vehicleType,
                            onValueChange = { vehicleType = it },
                            label = { Text("نوع السيارة (تريلة / بيك أب / لوري)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Crops Entry Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CardSurfaceWhite,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("إدراج صنف محاصيل جديدة في الشحنة:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MediumForestGreen)

                        OutlinedTextField(
                            value = cropName,
                            onValueChange = { cropName = it },
                            label = { Text("اسم الصنف / المحصول") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = boxCountText,
                                onValueChange = { boxCountText = it },
                                label = { Text("الصناديق") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )

                            OutlinedTextField(
                                value = grossWeightText,
                                onValueChange = { grossWeightText = it },
                                label = { Text("القائم (كغم)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )

                            OutlinedTextField(
                                value = tareWeightText,
                                onValueChange = { tareWeightText = it },
                                label = { Text("الفارغ (كغم)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        val calculatedNet = (grossWeightText.toDoubleOrNull() ?: 0.0) - (tareWeightText.toDoubleOrNull() ?: 0.0)
                        Surface(
                            color = MediumForestGreen.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("الوزن الصافي المحسوب تلقائياً:", fontSize = 12.sp, color = TextSecondaryMuted)
                                Text("$calculatedNet كغم", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = DarkForestGreen)
                            }
                        }

                        Button(
                            onClick = {
                                if (cropName.isNotEmpty()) {
                                    cropsList.add(
                                        ImportCrop(
                                            cropName = cropName,
                                            boxCount = boxCountText.toIntOrNull() ?: 1,
                                            grossWeightKg = grossWeightText.toDoubleOrNull() ?: 0.0,
                                            tareWeightKg = tareWeightText.toDoubleOrNull() ?: 0.0
                                        )
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkForestGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ إضافة صنف آخر بالفاتورة", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Added crops list
                        cropsList.forEachIndexed { idx, crop ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(BackgroundSoft, RoundedCornerShape(10.dp))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${crop.cropName} (${crop.boxCount} صندوق) • صافي: ${crop.netWeightKg} كغم", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                IconButton(onClick = { cropsList.removeAt(idx) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = RedWarning, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Submit Button Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(18.dp)),
                    color = DarkForestGreen,
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (farmerName.isNotEmpty() && cropsList.isNotEmpty()) {
                                    onSubmit(farmerName, vehicleType, cropsList.toList())
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldLicense),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("إصدار فاتورة الاستيراد وحفظ الشحنة", color = TextPrimaryDark, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}
