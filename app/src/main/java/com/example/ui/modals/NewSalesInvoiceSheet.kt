package com.example.ui.modals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.SaleCropItem
import com.example.ui.theme.*

data class EditableSaleCropRow(
    var id: String = java.util.UUID.randomUUID().toString(),
    var cropName: String = "طماطة النجف",
    var boxCountText: String = "10",
    var weightText: String = "250",
    var priceText: String = "1200"
) {
    val boxCount: Int get() = boxCountText.toIntOrNull() ?: 1
    val weightOrCount: Double get() = weightText.toDoubleOrNull() ?: 0.0
    val unitPrice: Long get() = priceText.toLongOrNull() ?: 0L
    val subtotal: Long get() = (weightOrCount * unitPrice).toLong()
    val commission7: Long get() = (subtotal * 0.07).toLong()
    val carryingFee: Long get() = (boxCount * 250L).coerceAtLeast(1000L)
}

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
    // 1. Customer State
    var customerName by remember { mutableStateOf("محل الخضار الحديث - أبو زهراء") }
    var customerPhone by remember { mutableStateOf("07801122334") }
    var customerAddress by remember { mutableStateOf("المنصور - الشارع الرئيسي") }
    var customerDropdownExpanded by remember { mutableStateOf(false) }

    val customerSuggestions = listOf(
        "محل الخضار الحديث - أبو زهراء",
        "مكتب الفواكه الذهبية - الحاج حسن",
        "محلات البركة للجملة - أبو علي",
        "أسواق الرافدين - أبو أحمد",
        "محل الهداية - أبو حسين",
        "محل النور - أبو جاسم"
    )

    // 2. Crop Items State
    val saleCropRows = remember {
        mutableStateListOf(
            EditableSaleCropRow(
                cropName = "طماطة النجف",
                boxCountText = "10",
                weightText = "250",
                priceText = "1200"
            )
        )
    }

    val availableCropSuggestions = listOf(
        "طماطة النجف",
        "خيارات الحلة",
        "بطاطا الموصل",
        "رقي كربلاء",
        "بصل الزبير",
        "باذنجان كربلاء",
        "تفاح أربيل",
        "رمان الديالى",
        "موز سومر",
        "فلفل بعقوبة"
    )

    // 3. General Options & Payment State
    var invoiceNotes by remember { mutableStateOf("") }
    var paymentType by remember { mutableStateOf("آجل") } // Default active: "آجل"
    var selectedMaturityOption by remember { mutableStateOf("10 أيام") } // "5 أيام", "10 أيام", "15 يوم", "مخصص"
    var customDaysText by remember { mutableStateOf("20") }

    val computedDeferredDays = when (selectedMaturityOption) {
        "5 أيام" -> 5
        "10 أيام" -> 10
        "15 يوم" -> 15
        else -> customDaysText.toIntOrNull() ?: 10
    }

    // 4. Financial Calculations
    val goodsTotal = saleCropRows.sumOf { it.subtotal }
    val commission7 = saleCropRows.sumOf { it.commission7 }
    val totalCarryingFee = saleCropRows.sumOf { it.carryingFee }.coerceAtLeast(5000L)
    val grandTotal = goodsTotal + commission7 + totalCarryingFee

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = BackgroundSoft,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            val isWideScreen = maxWidth > 600.dp

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Bar (Title & Close Button Top Left)
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
                            shape = RoundedCornerShape(12.dp),
                            color = DarkForestGreen.copy(alpha = 0.12f)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ReceiptLong,
                                contentDescription = null,
                                tint = DarkForestGreen,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "إنشاء فاتورة بيع جديدة",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimaryDark
                            )
                            Text(
                                text = "إدخال أصناف البضاعة المباعة واحتساب العمولات والدين",
                                fontSize = 11.sp,
                                color = TextSecondaryMuted
                            )
                        }
                    }

                    // Close Button (X) Top Left
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "إغلاق",
                            tint = TextPrimaryDark
                        )
                    }
                }

                HorizontalDivider(color = GlassBorder)

                // Scrollable Content Layout
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        if (isWideScreen) {
                            // Two Columns Side-by-Side for Landscape Tablet
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                // Column 1: Main Data & Invoice Crops
                                Column(
                                    modifier = Modifier.weight(1.2f),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    CustomerHeaderGridSection(
                                        customerName = customerName,
                                        onCustomerNameChange = { customerName = it },
                                        customerPhone = customerPhone,
                                        onPhoneChange = { customerPhone = it },
                                        customerAddress = customerAddress,
                                        onAddressChange = { customerAddress = it },
                                        customerSuggestions = customerSuggestions,
                                        customerDropdownExpanded = customerDropdownExpanded,
                                        onCustomerDropdownExpandedChange = { customerDropdownExpanded = it }
                                    )

                                    DynamicCropsListSection(
                                        saleCropRows = saleCropRows,
                                        availableCropSuggestions = availableCropSuggestions,
                                        formatIQD = formatIQD
                                    )

                                    GeneralOptionsAndPaymentSection(
                                        invoiceNotes = invoiceNotes,
                                        onNotesChange = { invoiceNotes = it },
                                        paymentType = paymentType,
                                        onPaymentTypeChange = { paymentType = it },
                                        selectedMaturityOption = selectedMaturityOption,
                                        onMaturityOptionChange = { selectedMaturityOption = it },
                                        customDaysText = customDaysText,
                                        onCustomDaysChange = { customDaysText = it }
                                    )
                                }

                                // Column 2: Financial Summary Premium Card
                                Column(
                                    modifier = Modifier.weight(0.8f),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    FinancialSummaryCard(
                                        goodsTotal = goodsTotal,
                                        commission7 = commission7,
                                        totalCarryingFee = totalCarryingFee,
                                        grandTotal = grandTotal,
                                        formatIQD = formatIQD,
                                        onSubmit = {
                                            if (customerName.isNotBlank() && saleCropRows.isNotEmpty()) {
                                                val finalItems = saleCropRows.map { row ->
                                                    SaleCropItem(
                                                        cropName = row.cropName,
                                                        weightOrCount = row.weightOrCount,
                                                        unitPriceIQD = row.unitPrice
                                                    )
                                                }
                                                onSubmit(
                                                    customerName,
                                                    customerPhone,
                                                    customerAddress,
                                                    paymentType,
                                                    if (paymentType == "آجل") computedDeferredDays else 0,
                                                    finalItems
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        } else {
                            // Single Column Layout for Mobile
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                CustomerHeaderGridSection(
                                    customerName = customerName,
                                    onCustomerNameChange = { customerName = it },
                                    customerPhone = customerPhone,
                                    onPhoneChange = { customerPhone = it },
                                    customerAddress = customerAddress,
                                    onAddressChange = { customerAddress = it },
                                    customerSuggestions = customerSuggestions,
                                    customerDropdownExpanded = customerDropdownExpanded,
                                    onCustomerDropdownExpandedChange = { customerDropdownExpanded = it }
                                )

                                DynamicCropsListSection(
                                    saleCropRows = saleCropRows,
                                    availableCropSuggestions = availableCropSuggestions,
                                    formatIQD = formatIQD
                                )

                                GeneralOptionsAndPaymentSection(
                                    invoiceNotes = invoiceNotes,
                                    onNotesChange = { invoiceNotes = it },
                                    paymentType = paymentType,
                                    onPaymentTypeChange = { paymentType = it },
                                    selectedMaturityOption = selectedMaturityOption,
                                    onMaturityOptionChange = { selectedMaturityOption = it },
                                    customDaysText = customDaysText,
                                    onCustomDaysChange = { customDaysText = it }
                                )

                                FinancialSummaryCard(
                                    goodsTotal = goodsTotal,
                                    commission7 = commission7,
                                    totalCarryingFee = totalCarryingFee,
                                    grandTotal = grandTotal,
                                    formatIQD = formatIQD,
                                    onSubmit = {
                                        if (customerName.isNotBlank() && saleCropRows.isNotEmpty()) {
                                            val finalItems = saleCropRows.map { row ->
                                                SaleCropItem(
                                                    cropName = row.cropName,
                                                    weightOrCount = row.weightOrCount,
                                                    unitPriceIQD = row.unitPrice
                                                )
                                            }
                                            onSubmit(
                                                customerName,
                                                customerPhone,
                                                customerAddress,
                                                paymentType,
                                                if (paymentType == "آجل") computedDeferredDays else 0,
                                                finalItems
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 1. Customer Header Grid Section
// ---------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomerHeaderGridSection(
    customerName: String,
    onCustomerNameChange: (String) -> Unit,
    customerPhone: String,
    onPhoneChange: (String) -> Unit,
    customerAddress: String,
    onAddressChange: (String) -> Unit,
    customerSuggestions: List<String>,
    customerDropdownExpanded: Boolean,
    onCustomerDropdownExpandedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Rounded.Person, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(18.dp))
                Text(
                    text = "معلومات الزبون (صاحب المحل):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForestGreen
                )
            }

            // Customer Name Autocomplete Dropdown
            ExposedDropdownMenuBox(
                expanded = customerDropdownExpanded,
                onExpandedChange = { onCustomerDropdownExpandedChange(!customerDropdownExpanded) },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = customerName,
                    onValueChange = {
                        onCustomerNameChange(it)
                        onCustomerDropdownExpandedChange(true)
                    },
                    label = { Text("اسم الزبون (صاحب المحل)") },
                    leadingIcon = {
                        Icon(Icons.Rounded.Person, contentDescription = null, tint = MediumForestGreen)
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = customerDropdownExpanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkForestGreen,
                        unfocusedBorderColor = GlassBorder
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                val filtered = customerSuggestions.filter { it.contains(customerName, ignoreCase = true) }
                if (filtered.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = customerDropdownExpanded,
                        onDismissRequest = { onCustomerDropdownExpandedChange(false) },
                        modifier = Modifier.background(CardSurfaceWhite)
                    ) {
                        filtered.forEach { suggestion ->
                            DropdownMenuItem(
                                text = { Text(suggestion, fontSize = 13.sp, fontWeight = FontWeight.Medium) },
                                onClick = {
                                    onCustomerNameChange(suggestion)
                                    onCustomerDropdownExpandedChange(false)
                                },
                                leadingIcon = {
                                    Icon(Icons.Rounded.Storefront, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(16.dp))
                                }
                            )
                        }
                    }
                }
            }

            // Phone & Address Optional Fields Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = customerPhone,
                    onValueChange = onPhoneChange,
                    label = { Text("رقم الهاتف (اختياري)") },
                    leadingIcon = {
                        Icon(Icons.Rounded.Phone, contentDescription = null, tint = MediumForestGreen)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkForestGreen,
                        unfocusedBorderColor = GlassBorder
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = customerAddress,
                    onValueChange = onAddressChange,
                    label = { Text("العنوان (اختياري)") },
                    leadingIcon = {
                        Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = MediumForestGreen)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkForestGreen,
                        unfocusedBorderColor = GlassBorder
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 2. Dynamic Crops List Section
// ---------------------------------------------------------------------------
@Composable
private fun DynamicCropsListSection(
    saleCropRows: MutableList<EditableSaleCropRow>,
    availableCropSuggestions: List<String>,
    formatIQD: (Long) -> String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Rounded.Grass, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(18.dp))
                    Text(
                        text = "أسطر الأصناف والمحاصيل المباعة:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkForestGreen
                    )
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MintGreen.copy(alpha = 0.3f)
                ) {
                    Text(
                        text = "${saleCropRows.size} أصناف",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkForestGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            // Editable Rows List
            saleCropRows.forEachIndexed { index, row ->
                EditableCropRowItem(
                    index = index + 1,
                    row = row,
                    canDelete = saleCropRows.size > 1,
                    availableCropSuggestions = availableCropSuggestions,
                    onDelete = { saleCropRows.removeAt(index) },
                    formatIQD = formatIQD
                )
            }

            // Dashed Button to Add Another Crop Line
            DashedButton(
                onClick = {
                    saleCropRows.add(
                        EditableSaleCropRow(
                            cropName = availableCropSuggestions.getOrElse(saleCropRows.size % availableCropSuggestions.size) { "صنف جديد" },
                            boxCountText = "10",
                            weightText = "200",
                            priceText = "1000"
                        )
                    )
                },
                color = DarkForestGreen,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null, tint = DarkForestGreen)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "+ إضافة صنف آخر في الفاتورة",
                    color = DarkForestGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditableCropRowItem(
    index: Int,
    row: EditableSaleCropRow,
    canDelete: Boolean,
    availableCropSuggestions: List<String>,
    onDelete: () -> Unit,
    formatIQD: (Long) -> String
) {
    var cropDropdownExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF8FAF9),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Item Number & Dropdown & Delete Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "#$index",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MediumForestGreen
                    )

                    // Crop Dropdown Menu Box
                    ExposedDropdownMenuBox(
                        expanded = cropDropdownExpanded,
                        onExpandedChange = { cropDropdownExpanded = !cropDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = row.cropName,
                            onValueChange = { row.cropName = it },
                            label = { Text("اسم المحصول / الصنف") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = cropDropdownExpanded)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DarkForestGreen,
                                unfocusedBorderColor = GlassBorder,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = cropDropdownExpanded,
                            onDismissRequest = { cropDropdownExpanded = false },
                            modifier = Modifier.background(CardSurfaceWhite)
                        ) {
                            availableCropSuggestions.forEach { suggestion ->
                                DropdownMenuItem(
                                    text = { Text(suggestion, fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                                    onClick = {
                                        row.cropName = suggestion
                                        cropDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                if (canDelete) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(start = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "حذف الصنف",
                            tint = RedWarning,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Input Fields Row: Boxes, Weight, Unit Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = row.boxCountText,
                    onValueChange = { row.boxCountText = it },
                    label = { Text("الصناديق/الأكياس") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkForestGreen,
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = row.weightText,
                    onValueChange = { row.weightText = it },
                    label = { Text("الوزن (كغم)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkForestGreen,
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = row.priceText,
                    onValueChange = { row.priceText = it },
                    label = { Text("السعر (د.ع)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkForestGreen,
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1.2f)
                )
            }

            // Subtotal Calculation Banner for Item
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkForestGreen.copy(alpha = 0.06f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "المجموع: ${formatIQD(row.subtotal)}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
                Text(
                    text = "عمولة 7%: ${formatIQD(row.commission7)} | حمالية: ${formatIQD(row.carryingFee)}",
                    fontSize = 10.sp,
                    color = TextSecondaryMuted
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 3. General Options & Payment Method Section
// ---------------------------------------------------------------------------
@Composable
private fun GeneralOptionsAndPaymentSection(
    invoiceNotes: String,
    onNotesChange: (String) -> Unit,
    paymentType: String,
    onPaymentTypeChange: (String) -> Unit,
    selectedMaturityOption: String,
    onMaturityOptionChange: (String) -> Unit,
    customDaysText: String,
    onCustomDaysChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "الخيارات العامة وطريقة الدفع:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DarkForestGreen
            )

            // Invoice Notes Field
            OutlinedTextField(
                value = invoiceNotes,
                onValueChange = onNotesChange,
                label = { Text("ملاحظات الفاتورة (اختياري)") },
                leadingIcon = {
                    Icon(Icons.Rounded.NoteAlt, contentDescription = null, tint = MediumForestGreen)
                },
                placeholder = { Text("مثال: تسليم عند المحل / دفعة جزيئة مع البضاعة", fontSize = 11.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkForestGreen,
                    unfocusedBorderColor = GlassBorder
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Payment Type Toggle Switch Group
            Text(
                text = "طريقة الدفع الفوري/الآجل:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundSoft, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Cash Button
                Button(
                    onClick = { onPaymentTypeChange("كاش") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (paymentType == "كاش") EmeraldSuccess else Color.Transparent,
                        contentColor = if (paymentType == "كاش") Color.White else TextPrimaryDark
                    ),
                    elevation = if (paymentType == "كاش") ButtonDefaults.buttonElevation(2.dp) else ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("💵 نقد (كاش)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Deferred Debt Button (Default active)
                Button(
                    onClick = { onPaymentTypeChange("آجل") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (paymentType == "آجل") RedWarning else Color.Transparent,
                        contentColor = if (paymentType == "آجل") Color.White else TextPrimaryDark
                    ),
                    elevation = if (paymentType == "آجل") ButtonDefaults.buttonElevation(2.dp) else ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("📋 دين بالأجل", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Segmented Control for Debt Maturity Options
            AnimatedVisibility(
                visible = paymentType == "آجل",
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(RedWarning.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "خيارات استحقاق الدين الآجل:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RedWarning
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val options = listOf("5 أيام", "10 أيام", "15 يوم", "مخصص")
                        options.forEach { option ->
                            FilterChip(
                                selected = selectedMaturityOption == option,
                                onClick = { onMaturityOptionChange(option) },
                                label = {
                                    Text(
                                        text = option,
                                        fontSize = 11.sp,
                                        fontWeight = if (selectedMaturityOption == option) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RedWarning,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White,
                                    labelColor = TextPrimaryDark
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = if (selectedMaturityOption == option) RedWarning else GlassBorder,
                                    enabled = true,
                                    selected = selectedMaturityOption == option
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Custom Days Input Field when "مخصص" is selected
                    if (selectedMaturityOption == "مخصص") {
                        OutlinedTextField(
                            value = customDaysText,
                            onValueChange = onCustomDaysChange,
                            label = { Text("عدد الأيام المخصصة") },
                            leadingIcon = {
                                Icon(Icons.Rounded.Schedule, contentDescription = null, tint = RedWarning)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RedWarning,
                                unfocusedBorderColor = GlassBorder,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 4. Financial Summary Card & Primary Button
// ---------------------------------------------------------------------------
@Composable
private fun FinancialSummaryCard(
    goodsTotal: Long,
    commission7: Long,
    totalCarryingFee: Long,
    grandTotal: Long,
    formatIQD: (Long) -> String,
    onSubmit: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        color = Color(0xFFF4F8F5), // Light background with soft green accent
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, Color(0xFFC8E6C9))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DarkForestGreen
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Calculate,
                        contentDescription = null,
                        tint = GoldLicense,
                        modifier = Modifier
                            .padding(6.dp)
                            .size(20.dp)
                    )
                }
                Text(
                    text = "ملخص الفاتورة والحسابات",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkForestGreen
                )
            }

            HorizontalDivider(color = Color(0xFFD0E4D7))

            // Subtotal Goods
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "مجموع أسعار البضاعة (Subtotal):",
                    fontSize = 12.sp,
                    color = TextPrimaryDark
                )
                Text(
                    text = formatIQD(goodsTotal),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            }

            // Office Commission 7%
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "عمولة المكتب (7%):",
                    fontSize = 12.sp,
                    color = TextPrimaryDark
                )
                Text(
                    text = formatIQD(commission7),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForestGreen
                )
            }

            // Carrying Fees
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "الحمالية الكلية (Carrying Fees):",
                    fontSize = 12.sp,
                    color = TextPrimaryDark
                )
                Text(
                    text = formatIQD(totalCarryingFee),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            }

            HorizontalDivider(color = Color(0xFFD0E4D7), thickness = 1.5.dp)

            // Grand Total
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkForestGreen.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "المبلغ الإجمالي الكلي:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkForestGreen
                )
                Text(
                    text = formatIQD(grandTotal),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkForestGreen
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Emerald Green Full Width Primary Button
            Button(
                onClick = onSubmit,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkForestGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = GoldLicense,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "إصدار الفاتورة وحساب الأرباح",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Helper Dashed Border Button Component
// ---------------------------------------------------------------------------
@Composable
private fun DashedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = DarkForestGreen,
    content: @Composable RowScope.() -> Unit
) {
    val stroke = Stroke(
        width = 3f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
    )
    Surface(
        onClick = onClick,
        modifier = modifier.drawWithContent {
            drawContent()
            drawRoundRect(
                color = color,
                style = stroke,
                cornerRadius = CornerRadius(14.dp.toPx())
            )
        },
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF4F8F5)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}
