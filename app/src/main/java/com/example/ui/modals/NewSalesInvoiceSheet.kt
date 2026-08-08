package com.example.ui.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.data.models.SaleCropItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSalesInvoiceSheet(
    onDismiss: () -> Unit,
    onSubmit: (
        customerName: String,
        phone: String,
        address: String,
        paymentType: String,
        deferredDays: Int,
        items: List<SaleCropItem>
    ) -> Unit,
    formatIQD: (Long) -> String,
    modifier: Modifier = Modifier
) {
    var customerName by remember { mutableStateOf("محل الخضار الحديث - أبو زهراء") }
    var phone by remember { mutableStateOf("07801122334") }
    var address by remember { mutableStateOf("المنصور - الشارع الرئيسي") }
    var paymentType by remember { mutableStateOf("كاش") } // "كاش" or "آجل"
    var deferredDays by remember { mutableStateOf(10) }

    var cropName by remember { mutableStateOf("طماطة النجف") }
    var weightText by remember { mutableStateOf("250") }
    var priceText by remember { mutableStateOf("1200") }

    val saleItems = remember {
        mutableStateListOf(
            SaleCropItem("طماطة النجف", 250.0, 1200L)
        )
    }

    val goodsTotal = saleItems.sumOf { it.totalAmountIQD }
    val commission7 = (goodsTotal * 0.07).toLong()
    val porterageFee = (saleItems.sumOf { it.weightOrCount } * 20).toLong().coerceAtLeast(5000L)
    val grandTotal = goodsTotal + commission7 + porterageFee

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = BackgroundSoft
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Title Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "إنشاء فاتورة بيع جديدة",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close")
                    }
                }
            }

            // Customer Info Fields
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
                        Text("بيانات الزبون (صاحب المحل):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkForestGreen)

                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text("اسم الزبون (صاحب المحل)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("رقم الهاتف") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = { Text("العنوان") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Crop Items Section
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
                        Text("إدراج أصناف البضاعة المباعة:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkForestGreen)

                        OutlinedTextField(
                            value = cropName,
                            onValueChange = { cropName = it },
                            label = { Text("اسم المحصول / الصنف") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = weightText,
                                onValueChange = { weightText = it },
                                label = { Text("الوزن / العدد (كغم/صندوق)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = priceText,
                                onValueChange = { priceText = it },
                                label = { Text("السعر للكيلو/الصندوق (د.ع)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Button(
                            onClick = {
                                if (cropName.isNotEmpty()) {
                                    saleItems.add(
                                        SaleCropItem(
                                            cropName = cropName,
                                            weightOrCount = weightText.toDoubleOrNull() ?: 1.0,
                                            unitPriceIQD = priceText.toLongOrNull() ?: 1000L
                                        )
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MediumForestGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ إضافة صنف آخر في الفاتورة", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Added items
                        saleItems.forEachIndexed { idx, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(BackgroundSoft, RoundedCornerShape(10.dp))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${item.cropName} (${item.weightOrCount} كغم/صندوق × ${item.unitPriceIQD} د.ع) = ${formatIQD(item.totalAmountIQD)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                IconButton(onClick = { saleItems.removeAt(idx) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = RedWarning, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Payment Type & Deferred Debt Options
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
                        Text("طريقة الدفع والاستحقاق:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkForestGreen)

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { paymentType = "كاش" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (paymentType == "كاش") EmeraldSuccess else Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("💵 نقد / كاش", color = if (paymentType == "كاش") Color.White else TextPrimaryDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { paymentType = "آجل" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (paymentType == "آجل") RedWarning else Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("📋 دين بالأجل", color = if (paymentType == "آجل") Color.White else TextPrimaryDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (paymentType == "آجل") {
                            Text("خطة استحقاق الدين الآجل:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RedWarning)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(5, 10, 15, 30).forEach { days ->
                                    FilterChip(
                                        selected = deferredDays == days,
                                        onClick = { deferredDays = days },
                                        label = { Text("$days أيام", fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = RedWarning,
                                            containerColor = CardSurfaceWhite
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Dynamic Calculations Card
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
                        Text("بطاقة الحسابات الديناميكية للفاتورة", color = MintGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("مجموع أسعار البضاعة المباعة:", color = Color.White, fontSize = 12.sp)
                            Text(formatIQD(goodsTotal), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("عمولة المكتب (7%):", color = Color.White, fontSize = 12.sp)
                            Text(formatIQD(commission7), color = GoldLicense, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("أجور الحمالية الكلية:", color = Color.White, fontSize = 12.sp)
                            Text(formatIQD(porterageFee), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Divider(color = Color.White.copy(alpha = 0.2f))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("المبلغ الإجمالي المطلـوب:", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                            Text(formatIQD(grandTotal), color = GoldLicense, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = {
                                if (customerName.isNotEmpty() && saleItems.isNotEmpty()) {
                                    onSubmit(
                                        customerName,
                                        phone,
                                        address,
                                        paymentType,
                                        deferredDays,
                                        saleItems.toList()
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldLicense),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("إصدار الفاتورة وحساب الأرباح", color = TextPrimaryDark, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}
