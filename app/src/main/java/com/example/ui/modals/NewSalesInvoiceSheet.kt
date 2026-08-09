package com.example.ui.modals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.SaleCropItem
import com.example.ui.theme.*

private data class CustomerSuggestion(
    val name: String,
    val phone: String,
    val address: String
)

private enum class KeypadTarget {
    WEIGHT, PRICE, DEFERRED_DAYS
}

private val sampleCustomerSuggestions = listOf(
    CustomerSuggestion("محل الخضار الحديث - أبو زهراء", "07801122334", "المنصور - الشارع الرئيسي"),
    CustomerSuggestion("محل البهادلي - أبو محمد", "07709988776", "علوة الرشيد - خان 12"),
    CustomerSuggestion("سوق الفواكه - الكرادة", "07812345678", "الكرادة - داخل"),
    CustomerSuggestion("مخزن الحمداني - أبو علي", "07901112233", "علوة بغداد المركزية"),
    CustomerSuggestion("معرض البركة - أبو سيف", "07714455667", "سوق الشورجة - خان 5"),
    CustomerSuggestion("أسواق الزهراء - أبو فاطمة", "07823344556", "زيونة - قرب النافورة")
)

private val sampleCrops = listOf(
    "طماطة النجف", "بتيتة الموصل", "خيارات كربلاء", "تفاح أربيل",
    "رمان ديالى", "بصل الزبير", "فلفل حلة", "بامية الكوت", "بطيخ سامراء", "موز سومر"
)

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
    var customDaysText by remember { mutableStateOf("10") }
    var isCustomDaysSelected by remember { mutableStateOf(false) }

    var cropName by remember { mutableStateOf("طماطة النجف") }
    var weightText by remember { mutableStateOf("250") }
    var priceText by remember { mutableStateOf("1200") }

    var activeKeypadTarget by remember { mutableStateOf(KeypadTarget.WEIGHT) }

    var showCustomerDropdown by remember { mutableStateOf(false) }
    var showCropDropdown by remember { mutableStateOf(false) }

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
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = BackgroundSoft,
        scrimColor = Color.Black.copy(alpha = 0.35f),
        dragHandle = null,
        modifier = modifier
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f) // 60vh max height as requested
                .border(
                    BorderStroke(1.5.dp, Color(0x331B4332)),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
                .background(BackgroundSoft, shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
        ) {
            val isWideScreen = maxWidth >= 640.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                // Header (Mawjoom & Drag Handle & Title Bar)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Drag Handle
                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(5.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD1D5DB))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkForestGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.ReceiptLong,
                                    contentDescription = null,
                                    tint = GoldLicense,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Text(
                                text = "إنشاء فاتورة بيع جديدة",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkForestGreen,
                                fontFamily = CairoFontFamily
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2E8F0))
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close",
                                tint = TextPrimaryDark,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(top = 10.dp),
                        color = Color(0xFFE2E8F0)
                    )
                }

                // Inner Content (Scrollable Split Grid or Single Column)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(top = 10.dp, bottom = 16.dp)
                ) {
                    if (isWideScreen) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Column 1: Main Form (Customer + Crops + Payment Options)
                            Column(
                                modifier = Modifier.weight(1.2f),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CustomerDataSection(
                                    customerName = customerName,
                                    onCustomerNameChange = {
                                        customerName = it
                                        showCustomerDropdown = true
                                    },
                                    phone = phone,
                                    onPhoneChange = { phone = it },
                                    address = address,
                                    onAddressChange = { address = it },
                                    showDropdown = showCustomerDropdown,
                                    onDismissDropdown = { showCustomerDropdown = false },
                                    onSelectSuggestion = { sugg ->
                                        customerName = sugg.name
                                        phone = sugg.phone
                                        address = sugg.address
                                        showCustomerDropdown = false
                                    }
                                )

                                CropEntrySection(
                                    cropName = cropName,
                                    onCropNameChange = {
                                        cropName = it
                                        showCropDropdown = true
                                    },
                                    showCropDropdown = showCropDropdown,
                                    onDismissCropDropdown = { showCropDropdown = false },
                                    weightText = weightText,
                                    onWeightFocus = { activeKeypadTarget = KeypadTarget.WEIGHT },
                                    onWeightChange = { weightText = it },
                                    priceText = priceText,
                                    onPriceFocus = { activeKeypadTarget = KeypadTarget.PRICE },
                                    onPriceChange = { priceText = it },
                                    activeTarget = activeKeypadTarget,
                                    onAddCrop = {
                                        if (cropName.isNotBlank()) {
                                            saleItems.add(
                                                SaleCropItem(
                                                    cropName = cropName,
                                                    weightOrCount = weightText.toDoubleOrNull() ?: 1.0,
                                                    unitPriceIQD = priceText.toLongOrNull() ?: 1000L
                                                )
                                            )
                                            weightText = ""
                                            priceText = ""
                                        }
                                    },
                                    saleItems = saleItems,
                                    onRemoveCrop = { idx -> saleItems.removeAt(idx) },
                                    formatIQD = formatIQD
                                )

                                PaymentAndTenureSection(
                                    paymentType = paymentType,
                                    onPaymentTypeChange = { paymentType = it },
                                    deferredDays = deferredDays,
                                    onDeferredDaysChange = { days ->
                                        deferredDays = days
                                        isCustomDaysSelected = false
                                    },
                                    isCustomDaysSelected = isCustomDaysSelected,
                                    onSelectCustomDays = {
                                        isCustomDaysSelected = true
                                        activeKeypadTarget = KeypadTarget.DEFERRED_DAYS
                                    },
                                    customDaysText = customDaysText,
                                    onCustomDaysChange = { text ->
                                        customDaysText = text
                                        text.toIntOrNull()?.let { deferredDays = it }
                                    },
                                    onCustomDaysFocus = { activeKeypadTarget = KeypadTarget.DEFERRED_DAYS }
                                )
                            }

                            // Column 2: Keypad + Financial Summary
                            Column(
                                modifier = Modifier.weight(0.9f),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                SidebarNumericKeypad(
                                    activeTarget = activeKeypadTarget,
                                    onTargetChange = { activeKeypadTarget = it },
                                    weightText = weightText,
                                    priceText = priceText,
                                    customDaysText = customDaysText,
                                    onKeyPress = { key ->
                                        handleKeypadInput(
                                            key = key,
                                            activeTarget = activeKeypadTarget,
                                            weightText = weightText,
                                            onWeightChange = { weightText = it },
                                            priceText = priceText,
                                            onPriceChange = { priceText = it },
                                            customDaysText = customDaysText,
                                            onCustomDaysChange = { text ->
                                                customDaysText = text
                                                text.toIntOrNull()?.let { deferredDays = it }
                                            },
                                            onNextTarget = { target -> activeKeypadTarget = target },
                                            onAddCropTrigger = {
                                                if (cropName.isNotBlank()) {
                                                    saleItems.add(
                                                        SaleCropItem(
                                                            cropName = cropName,
                                                            weightOrCount = weightText.toDoubleOrNull() ?: 1.0,
                                                            unitPriceIQD = priceText.toLongOrNull() ?: 1000L
                                                        )
                                                    )
                                                    weightText = ""
                                                    priceText = ""
                                                    activeKeypadTarget = KeypadTarget.WEIGHT
                                                }
                                            }
                                        )
                                    }
                                )

                                FinancialSummaryCard(
                                    goodsTotal = goodsTotal,
                                    commission7 = commission7,
                                    porterageFee = porterageFee,
                                    grandTotal = grandTotal,
                                    formatIQD = formatIQD,
                                    onSubmit = {
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
                                    }
                                )
                            }
                        }
                    } else {
                        // Narrow screen vertical stack
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            CustomerDataSection(
                                customerName = customerName,
                                onCustomerNameChange = {
                                    customerName = it
                                    showCustomerDropdown = true
                                },
                                phone = phone,
                                onPhoneChange = { phone = it },
                                address = address,
                                onAddressChange = { address = it },
                                showDropdown = showCustomerDropdown,
                                onDismissDropdown = { showCustomerDropdown = false },
                                onSelectSuggestion = { sugg ->
                                    customerName = sugg.name
                                    phone = sugg.phone
                                    address = sugg.address
                                    showCustomerDropdown = false
                                }
                            )

                            CropEntrySection(
                                cropName = cropName,
                                onCropNameChange = {
                                    cropName = it
                                    showCropDropdown = true
                                },
                                showCropDropdown = showCropDropdown,
                                onDismissCropDropdown = { showCropDropdown = false },
                                weightText = weightText,
                                onWeightFocus = { activeKeypadTarget = KeypadTarget.WEIGHT },
                                onWeightChange = { weightText = it },
                                priceText = priceText,
                                onPriceFocus = { activeKeypadTarget = KeypadTarget.PRICE },
                                onPriceChange = { priceText = it },
                                activeTarget = activeKeypadTarget,
                                onAddCrop = {
                                    if (cropName.isNotBlank()) {
                                        saleItems.add(
                                            SaleCropItem(
                                                cropName = cropName,
                                                weightOrCount = weightText.toDoubleOrNull() ?: 1.0,
                                                unitPriceIQD = priceText.toLongOrNull() ?: 1000L
                                            )
                                        )
                                        weightText = ""
                                        priceText = ""
                                    }
                                },
                                saleItems = saleItems,
                                onRemoveCrop = { idx -> saleItems.removeAt(idx) },
                                formatIQD = formatIQD
                            )

                            PaymentAndTenureSection(
                                paymentType = paymentType,
                                onPaymentTypeChange = { paymentType = it },
                                deferredDays = deferredDays,
                                onDeferredDaysChange = { days ->
                                    deferredDays = days
                                    isCustomDaysSelected = false
                                },
                                isCustomDaysSelected = isCustomDaysSelected,
                                onSelectCustomDays = {
                                    isCustomDaysSelected = true
                                    activeKeypadTarget = KeypadTarget.DEFERRED_DAYS
                                },
                                customDaysText = customDaysText,
                                onCustomDaysChange = { text ->
                                    customDaysText = text
                                    text.toIntOrNull()?.let { deferredDays = it }
                                },
                                onCustomDaysFocus = { activeKeypadTarget = KeypadTarget.DEFERRED_DAYS }
                            )

                            SidebarNumericKeypad(
                                activeTarget = activeKeypadTarget,
                                onTargetChange = { activeKeypadTarget = it },
                                weightText = weightText,
                                priceText = priceText,
                                customDaysText = customDaysText,
                                onKeyPress = { key ->
                                    handleKeypadInput(
                                        key = key,
                                        activeTarget = activeKeypadTarget,
                                        weightText = weightText,
                                        onWeightChange = { weightText = it },
                                        priceText = priceText,
                                        onPriceChange = { priceText = it },
                                        customDaysText = customDaysText,
                                        onCustomDaysChange = { text ->
                                            customDaysText = text
                                            text.toIntOrNull()?.let { deferredDays = it }
                                        },
                                        onNextTarget = { target -> activeKeypadTarget = target },
                                        onAddCropTrigger = {
                                            if (cropName.isNotBlank()) {
                                                saleItems.add(
                                                    SaleCropItem(
                                                        cropName = cropName,
                                                        weightOrCount = weightText.toDoubleOrNull() ?: 1.0,
                                                        unitPriceIQD = priceText.toLongOrNull() ?: 1000L
                                                    )
                                                )
                                                weightText = ""
                                                priceText = ""
                                                activeKeypadTarget = KeypadTarget.WEIGHT
                                            }
                                        }
                                    )
                                }
                            )

                            FinancialSummaryCard(
                                goodsTotal = goodsTotal,
                                commission7 = commission7,
                                porterageFee = porterageFee,
                                grandTotal = grandTotal,
                                formatIQD = formatIQD,
                                onSubmit = {
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
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// 1. Customer Data Section with Autocomplete
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomerDataSection(
    customerName: String,
    onCustomerNameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    showDropdown: Boolean,
    onDismissDropdown: () -> Unit,
    onSelectSuggestion: (CustomerSuggestion) -> Unit
) {
    val filteredSuggestions = remember(customerName) {
        sampleCustomerSuggestions.filter {
            it.name.contains(customerName, ignoreCase = true) || it.phone.contains(customerName)
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Rounded.Person, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(18.dp))
                Text(
                    text = "بيانات الزبون (صاحب المحل / المشتري):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForestGreen,
                    fontFamily = CairoFontFamily
                )
            }

            // Customer Name Autocomplete Box
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = customerName,
                    onValueChange = onCustomerNameChange,
                    label = { Text("اسم الزبون (ابحث بالإكمال التلقائي...)", fontFamily = CairoFontFamily) },
                    trailingIcon = {
                        Icon(Icons.Rounded.ArrowDropDown, contentDescription = "Dropdown", tint = MediumForestGreen)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MediumForestGreen,
                        unfocusedBorderColor = GlassBorder
                    )
                )

                DropdownMenu(
                    expanded = showDropdown && filteredSuggestions.isNotEmpty(),
                    onDismissRequest = onDismissDropdown,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(CardSurfaceWhite)
                ) {
                    filteredSuggestions.forEach { sugg ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(sugg.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = CairoFontFamily)
                                    Text("${sugg.phone} • ${sugg.address}", fontSize = 11.sp, color = TextSecondaryMuted, fontFamily = CairoFontFamily)
                                }
                            },
                            onClick = { onSelectSuggestion(sugg) },
                            leadingIcon = {
                                Icon(Icons.Rounded.Storefront, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(18.dp))
                            }
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = phone,
                    onValueChange = onPhoneChange,
                    label = { Text("رقم الهاتف", fontFamily = CairoFontFamily) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MediumForestGreen,
                        unfocusedBorderColor = GlassBorder
                    )
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = onAddressChange,
                    label = { Text("العنوان / الفرع", fontFamily = CairoFontFamily) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MediumForestGreen,
                        unfocusedBorderColor = GlassBorder
                    )
                )
            }
        }
    }
}

// 2. Crop Entry Section (Dynamic List & Dashed Add Button)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CropEntrySection(
    cropName: String,
    onCropNameChange: (String) -> Unit,
    showCropDropdown: Boolean,
    onDismissCropDropdown: () -> Unit,
    weightText: String,
    onWeightFocus: () -> Unit,
    onWeightChange: (String) -> Unit,
    priceText: String,
    onPriceFocus: () -> Unit,
    onPriceChange: (String) -> Unit,
    activeTarget: KeypadTarget,
    onAddCrop: () -> Unit,
    saleItems: List<SaleCropItem>,
    onRemoveCrop: (Int) -> Unit,
    formatIQD: (Long) -> String
) {
    val filteredCrops = remember(cropName) {
        sampleCrops.filter { it.contains(cropName, ignoreCase = true) }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Rounded.Eco, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(18.dp))
                Text(
                    text = "قائمة المحاصيل والأصناف المباعة:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForestGreen,
                    fontFamily = CairoFontFamily
                )
            }

            // Crop Name Autocomplete
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = cropName,
                    onValueChange = onCropNameChange,
                    label = { Text("اختر أو اكتب اسم المحصول...", fontFamily = CairoFontFamily) },
                    trailingIcon = {
                        Icon(Icons.Rounded.ArrowDropDown, contentDescription = "Dropdown", tint = MediumForestGreen)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MediumForestGreen,
                        unfocusedBorderColor = GlassBorder
                    )
                )

                DropdownMenu(
                    expanded = showCropDropdown && filteredCrops.isNotEmpty(),
                    onDismissRequest = onDismissCropDropdown,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(CardSurfaceWhite)
                ) {
                    filteredCrops.forEach { crop ->
                        DropdownMenuItem(
                            text = { Text(crop, fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = CairoFontFamily) },
                            onClick = {
                                onCropNameChange(crop)
                                onDismissCropDropdown()
                            }
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Weight/Count Field
                val isWeightActive = activeTarget == KeypadTarget.WEIGHT
                OutlinedTextField(
                    value = weightText,
                    onValueChange = onWeightChange,
                    label = { Text("الوزن (كجم / صندوق)", fontFamily = CairoFontFamily) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onWeightFocus() },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isWeightActive) GoldLicenseDark else MediumForestGreen,
                        unfocusedBorderColor = if (isWeightActive) GoldLicense else GlassBorder,
                        focusedContainerColor = if (isWeightActive) GoldLicenseLight else Color.Unspecified
                    )
                )

                // Price Field
                val isPriceActive = activeTarget == KeypadTarget.PRICE
                OutlinedTextField(
                    value = priceText,
                    onValueChange = onPriceChange,
                    label = { Text("سعر الكيلو (د.ع)", fontFamily = CairoFontFamily) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onPriceFocus() },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isPriceActive) GoldLicenseDark else MediumForestGreen,
                        unfocusedBorderColor = if (isPriceActive) GoldLicense else GlassBorder,
                        focusedContainerColor = if (isPriceActive) GoldLicenseLight else Color.Unspecified
                    )
                )
            }

            // Dashed Border Button as requested
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .dashedBorder(
                        strokeWidth = 1.8.dp,
                        color = DarkForestGreen,
                        cornerRadius = 12.dp,
                        dashLength = 8.dp,
                        gapLength = 6.dp
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0FDF4))
                    .clickable { onAddCrop() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddCircle,
                        contentDescription = null,
                        tint = DarkForestGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "إضافة صنف آخر في الفاتورة",
                        color = DarkForestGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily
                    )
                }
            }

            // Added Items List Pills
            if (saleItems.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    saleItems.forEachIndexed { idx, item ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = BackgroundSoft,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.cropName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryDark,
                                        fontFamily = CairoFontFamily
                                    )
                                    Text(
                                        text = "${item.weightOrCount.toInt()} كغم × ${formatIQD(item.unitPriceIQD)} د.ع/كغم = ${formatIQD(item.totalAmountIQD)} د.ع",
                                        fontSize = 11.5.sp,
                                        color = TextSecondaryMuted,
                                        fontFamily = CairoFontFamily
                                    )
                                }

                                IconButton(
                                    onClick = { onRemoveCrop(idx) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Delete,
                                        contentDescription = "Remove",
                                        tint = RedWarningDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 3. Payment Options & Tenure Section
@Composable
private fun PaymentAndTenureSection(
    paymentType: String,
    onPaymentTypeChange: (String) -> Unit,
    deferredDays: Int,
    onDeferredDaysChange: (Int) -> Unit,
    isCustomDaysSelected: Boolean,
    onSelectCustomDays: () -> Unit,
    customDaysText: String,
    onCustomDaysChange: (String) -> Unit,
    onCustomDaysFocus: () -> Unit
) {
    val isDeferred = paymentType == "آجل"

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Rounded.Payments, contentDescription = null, tint = DarkForestGreen, modifier = Modifier.size(18.dp))
                Text(
                    text = "خيارات الدفع وطريقة الاستحقاق:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForestGreen,
                    fontFamily = CairoFontFamily
                )
            }

            // Single Toggle Row (💵 نقد / 📋 دين بالأجل)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (!isDeferred) EmeraldSuccess else Color.Transparent)
                        .clickable { onPaymentTypeChange("كاش") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💵 نقد / كاش",
                        color = if (!isDeferred) Color.White else TextPrimaryDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDeferred) RedWarningDark else Color.Transparent)
                        .clickable { onPaymentTypeChange("آجل") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📋 دين بالأجل",
                        color = if (isDeferred) Color.White else TextPrimaryDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily
                    )
                }
            }

            // Tenure Options if Deferred
            AnimatedVisibility(visible = isDeferred) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "اختر مهلة الاستحقاق للدين الآجل:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RedWarningDark,
                        fontFamily = CairoFontFamily
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(5, 10, 15).forEach { days ->
                            val isSelected = !isCustomDaysSelected && deferredDays == days
                            FilterChip(
                                selected = isSelected,
                                onClick = { onDeferredDaysChange(days) },
                                label = {
                                    Text(
                                        text = "$days أيام",
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else TextPrimaryDark,
                                        fontFamily = CairoFontFamily
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RedWarningDark,
                                    containerColor = CardSurfaceWhite
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = GlassBorder
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Custom Option Button
                        FilterChip(
                            selected = isCustomDaysSelected,
                            onClick = onSelectCustomDays,
                            label = {
                                Text(
                                    text = "مخصص",
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isCustomDaysSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCustomDaysSelected) Color.White else TextPrimaryDark,
                                    fontFamily = CairoFontFamily
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RedWarningDark,
                                containerColor = CardSurfaceWhite
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isCustomDaysSelected,
                                borderColor = GlassBorder
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (isCustomDaysSelected) {
                        OutlinedTextField(
                            value = customDaysText,
                            onValueChange = onCustomDaysChange,
                            label = { Text("أدخل مهلة الاستحقاق بالأيام (مثلاً: 20)", fontFamily = CairoFontFamily) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onCustomDaysFocus() },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RedWarningDark,
                                unfocusedBorderColor = GlassBorder
                            )
                        )
                    }
                }
            }
        }
    }
}

// 4. Sidebar Interactive Numeric Keypad with Digital Readout Screen
@Composable
private fun SidebarNumericKeypad(
    activeTarget: KeypadTarget,
    onTargetChange: (KeypadTarget) -> Unit,
    weightText: String,
    priceText: String,
    customDaysText: String,
    onKeyPress: (String) -> Unit
) {
    val currentReadout = when (activeTarget) {
        KeypadTarget.WEIGHT -> if (weightText.isBlank()) "0 كجم" else "$weightText كجم"
        KeypadTarget.PRICE -> if (priceText.isBlank()) "0 د.ع" else "$priceText د.ع"
        KeypadTarget.DEFERRED_DAYS -> if (customDaysText.isBlank()) "0 يوم" else "$customDaysText يوم"
    }

    val activeLabel = when (activeTarget) {
        KeypadTarget.WEIGHT -> "الوزن / العدد"
        KeypadTarget.PRICE -> "سعر الكيلو"
        KeypadTarget.DEFERRED_DAYS -> "مهلة الدين"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        color = CardSurfaceWhite,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Target Selector Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE2E8F0))
                    .padding(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                listOf(
                    KeypadTarget.WEIGHT to "الوزن",
                    KeypadTarget.PRICE to "السعر",
                    KeypadTarget.DEFERRED_DAYS to "المهلة"
                ).forEach { (target, label) ->
                    val isSelected = activeTarget == target
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) DarkForestGreen else Color.Transparent)
                            .clickable { onTargetChange(target) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else TextPrimaryDark,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = CairoFontFamily
                        )
                    }
                }
            }

            // Digital Display Readout Screen
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                color = DarkForestGreen,
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "المدخل [$activeLabel]:",
                        color = MintGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily
                    )
                    Text(
                        text = currentReadout,
                        color = GoldLicense,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = CairoFontFamily
                    )
                }
            }

            // Numeric Grid (4x4)
            val keys = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("C", "0", "⌫"),
                listOf("00", "OK")
            )

            keys.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    row.forEach { key ->
                        val isSpecial = key in listOf("C", "⌫", "OK")
                        val btnBg = when (key) {
                            "OK" -> DarkForestGreen
                            "C", "⌫" -> Color(0xFFFEE2E2)
                            else -> Color(0xFFF1F5F9)
                        }
                        val btnFg = when (key) {
                            "OK" -> GoldLicense
                            "C", "⌫" -> RedWarningDark
                            else -> TextPrimaryDark
                        }

                        Box(
                            modifier = Modifier
                                .weight(if (key == "OK") 2f else 1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(btnBg)
                                .clickable { onKeyPress(key) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = key,
                                color = btnFg,
                                fontSize = if (isSpecial) 13.sp else 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = CairoFontFamily
                            )
                        }
                    }
                }
            }
        }
    }
}

// 5. Financial Summary Card & Primary Action
@Composable
private fun FinancialSummaryCard(
    goodsTotal: Long,
    commission7: Long,
    porterageFee: Long,
    grandTotal: Long,
    formatIQD: (Long) -> String,
    onSubmit: () -> Unit
) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Calculate,
                    contentDescription = null,
                    tint = GoldLicense,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "ملخص الحسابات والعمولات:",
                    color = MintGreen,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CairoFontFamily
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("مجموع أسعار البضاعة المباعة:", color = Color.White, fontSize = 12.sp, fontFamily = CairoFontFamily)
                Text(formatIQD(goodsTotal), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("عمولة المكتب (7%):", color = Color.White, fontSize = 12.sp, fontFamily = CairoFontFamily)
                Text(formatIQD(commission7), color = GoldLicense, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("أجور الحمالية الكلية:", color = Color.White, fontSize = 12.sp, fontFamily = CairoFontFamily)
                Text(formatIQD(porterageFee), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = CairoFontFamily)
            }

            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("المبلغ الإجمالي المطلـوب:", color = Color.White, fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, fontFamily = CairoFontFamily)
                Text(formatIQD(grandTotal), color = GoldLicense, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, fontFamily = CairoFontFamily)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = GoldLicense),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = TextPrimaryDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "إصدار الفاتورة وحساب الأرباح",
                        color = TextPrimaryDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = CairoFontFamily
                    )
                }
            }
        }
    }
}

// Helper Keypad Handler Logic
private fun handleKeypadInput(
    key: String,
    activeTarget: KeypadTarget,
    weightText: String,
    onWeightChange: (String) -> Unit,
    priceText: String,
    onPriceChange: (String) -> Unit,
    customDaysText: String,
    onCustomDaysChange: (String) -> Unit,
    onNextTarget: (KeypadTarget) -> Unit,
    onAddCropTrigger: () -> Unit
) {
    when (key) {
        "C" -> {
            when (activeTarget) {
                KeypadTarget.WEIGHT -> onWeightChange("")
                KeypadTarget.PRICE -> onPriceChange("")
                KeypadTarget.DEFERRED_DAYS -> onCustomDaysChange("")
            }
        }
        "⌫" -> {
            when (activeTarget) {
                KeypadTarget.WEIGHT -> if (weightText.isNotEmpty()) onWeightChange(weightText.dropLast(1))
                KeypadTarget.PRICE -> if (priceText.isNotEmpty()) onPriceChange(priceText.dropLast(1))
                KeypadTarget.DEFERRED_DAYS -> if (customDaysText.isNotEmpty()) onCustomDaysChange(customDaysText.dropLast(1))
            }
        }
        "OK" -> {
            if (activeTarget == KeypadTarget.WEIGHT) {
                onNextTarget(KeypadTarget.PRICE)
            } else if (activeTarget == KeypadTarget.PRICE) {
                onAddCropTrigger()
            }
        }
        else -> {
            when (activeTarget) {
                KeypadTarget.WEIGHT -> onWeightChange(if (weightText == "0") key else weightText + key)
                KeypadTarget.PRICE -> onPriceChange(if (priceText == "0") key else priceText + key)
                KeypadTarget.DEFERRED_DAYS -> onCustomDaysChange(if (customDaysText == "0") key else customDaysText + key)
            }
        }
    }
}

// Custom Dashed Border Extension
private fun Modifier.dashedBorder(
    strokeWidth: Dp = 1.5.dp,
    color: Color = DarkForestGreen,
    cornerRadius: Dp = 12.dp,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 6.dp
) = drawWithContent {
    drawContent()
    val strokeWidthPx = strokeWidth.toPx()
    val dashLengthPx = dashLength.toPx()
    val gapLengthPx = gapLength.toPx()
    val radiusPx = cornerRadius.toPx()

    val pathEffect = PathEffect.dashPathEffect(
        floatArrayOf(dashLengthPx, gapLengthPx),
        0f
    )
    val stroke = Stroke(
        width = strokeWidthPx,
        pathEffect = pathEffect
    )

    drawRoundRect(
        color = color,
        style = stroke,
        cornerRadius = CornerRadius(radiusPx, radiusPx)
    )
}
