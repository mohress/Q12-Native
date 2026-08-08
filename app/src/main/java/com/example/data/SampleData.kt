package com.example.data

import com.example.data.models.*
import java.util.Locale

object SampleDataGenerator {

    private val farmerNames = listOf(
        "جاسم المحمدي", "أبو علي الكربلائي", "سعدون البصري", "أبو فهد الدليمي",
        "حيدر الخفاجي", "كاظم العبيدي", "حسين الشمري", "فاضل الساعدي",
        "عبد الله التميمي", "ماجد الزبيدي", "خليل المسعودي", "عماد الجبوري",
        "ستار البغدادي", "عباس الناصري", "صلاح الحلي", "رشيد الديواني",
        "سلام العمارة", "عدنان النجفي", "مرتضى الموصلي", "فؤاد الكركوكي",
        "صبحي العكيدي", "علاء الدفاعي", "هشام الدليمي", "هادي الأنباري", "ضياء الواسطي"
    )

    private val vehicleTypes = listOf(
        "تريلة مرسيدس", "كيا بونقو", "جامبو دايو", "سوزوكي بيك أب",
        "هينو 8 طن", "إيسوزو دبل كابينة", "مان 18 طن", "فوتون بيك أب",
        "بيك أب تويوتا", "مرسيدس اكتروس"
    )

    // Crop Catalog: Name, Min Price IQD, Max Price IQD
    private val cropCatalog = listOf(
        Triple("طماطة النجف", 1200L, 1500L),
        Triple("خيار حلي وادي", 800L, 1100L),
        Triple("باذنجان ديالي", 900L, 1300L),
        Triple("بصل أبيض زبيري", 600L, 900L),
        Triple("بطاطا موصلية", 750L, 1000L),
        Triple("رمان شهربان", 1800L, 2500L),
        Triple("رقي زبيري", 400L, 650L),
        Triple("بطيخ سامراء", 500L, 800L),
        Triple("فلفل بارد حلي", 1100L, 1600L),
        Triple("تفاح أربيل", 1500L, 2200L),
        Triple("برتقال كوت", 1000L, 1400L),
        Triple("ليمون بصراوي", 2000L, 3000L),
        Triple("شجر (كوسة) حلي", 850L, 1200L),
        Triple("تين صلاح الدين", 2500L, 3800L),
        Triple("عنب ديالي", 1600L, 2400L),
        Triple("خوخ أربيل", 1800L, 2600L),
        Triple("موز مستورد", 1400L, 1900L),
        Triple("بامية كربلائية", 3000L, 4500L),
        Triple("ثوم زبيري", 2200L, 3200L),
        Triple("جزر موصلي", 700L, 1000L)
    )

    private val customerList = listOf(
        Pair("محل بركة الرحمن - أربيل", "علوة أربيل الكبرى"),
        Pair("أبو حسين للعمولة - الكرخ", "سوق جميلة"),
        Pair("أسواق الفردوس - المنصور", "بغداد - المنصور"),
        Pair("محل النور للفضليات - الأعظمية", "بغداد - الأعظمية"),
        Pair("أسواق البركة - الشعب", "بغداد - الشعب"),
        Pair("خضروات الرشيد - الزيونة", "بغداد - الزيونة"),
        Pair("أبو مصطفى للجملة - سوق جميلة", "بغداد - سوق جميلة"),
        Pair("متجر الصفا - النجف", "علوة النجف الأشرف"),
        Pair("علوة السليمانية - كاسب", "السليمانية - كاسب"),
        Pair("أسواق الهداية - الكاظمية", "بغداد - الكاظمية"),
        Pair("أبو أحمد للعمولة - الحارثية", "بغداد - الحارثية"),
        Pair("خضروات دجلة - الدورة", "بغداد - الدورة"),
        Pair("مكتب البصائر - كركوك", "علوة كركوك المركزية"),
        Pair("أسواق البصري - العشار", "البصرة - العشار"),
        Pair("أبو سارة للفواكه - شارع فلسطين", "بغداد - شارع فلسطين"),
        Pair("محل الأخوين - ديالى", "بعقوبة - السوق الكبير"),
        Pair("أسواق الرافدين - الحلة", "بابل - الحلة"),
        Pair("خضروات الكاظم - الناصرية", "ذي قار - الناصرية"),
        Pair("أسواق الأمل - السماوة", "المثنى - السماوة"),
        Pair("أبو سيف للجملة - الكوت", "واسط - الكوت"),
        Pair("معمل الشرق - الموصل", "نينوى - الموصل"),
        Pair("خضروات دجلة والفرات - الرمادي", "الأنبار - الرمادي"),
        Pair("مخضر الفرات - الديوانية", "القادسية - الديوانية"),
        Pair("أسواق الكرم - صلاح الدين", "تكريت - السوق"),
        Pair("أسواق الفرقان - دهوك", "دهوك - المركز")
    )

    fun generate50ImportInvoices(): List<ImportInvoice> {
        val imports = mutableListOf<ImportInvoice>()

        for (i in 1..50) {
            val code = "IMP-${8400 + i}"
            val farmer = farmerNames[(i - 1) % farmerNames.size]
            val phone = "078" + String.format(Locale.ENGLISH, "%07d", (1000000 + i * 12345) % 8999999)
            val vehicle = vehicleTypes[(i - 1) % vehicleTypes.size]

            val cropCount = (i % 3) + 1
            val crops = mutableListOf<ImportCrop>()
            var estSales = 0L

            for (c in 0 until cropCount) {
                val cropInfo = cropCatalog[(i + c * 3) % cropCatalog.size]
                val boxCount = 50 + ((i * 17 + c * 23) % 350)
                val grossWeight = boxCount * (15.0 + (i % 10))
                val tareWeight = boxCount * 1.5
                crops.add(
                    ImportCrop(
                        cropName = cropInfo.first,
                        boxCount = boxCount,
                        grossWeightKg = grossWeight,
                        tareWeightKg = tareWeight
                    )
                )
                estSales += ((grossWeight - tareWeight) * cropInfo.third).toLong()
            }

            val status = when {
                i % 4 == 0 -> "جاهز للتسوية ⚠️"
                i % 3 == 0 -> "قيد البيع ⏳"
                i % 5 == 0 -> "جديد 🆕"
                else -> "مكتمل ✅"
            }

            val progress = when (status) {
                "مكتمل ✅", "جاهز للتسوية ⚠️" -> 1.0f
                "جديد 🆕" -> 0.0f
                else -> 0.35f + (i % 5) * 0.12f
            }

            val dayOffset = (i - 1) / 2
            val hour = 6 + (i * 3 % 12)
            val minute = (i * 7 % 60)
            val amPm = if (hour >= 12) "م" else "ص"
            val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
            val timeStr = String.format(Locale.ENGLISH, "%02d:%02d $amPm", displayHour, minute)

            val dateStr = if (dayOffset == 0) {
                "اليوم $timeStr"
            } else if (dayOffset == 1) {
                "الأمس $timeStr"
            } else {
                val day = 7 - (dayOffset % 28)
                val adjustedDay = if (day <= 0) day + 30 else day
                val month = if (dayOffset > 7) "07" else "08"
                String.format(Locale.ENGLISH, "2026-$month-%02d $timeStr", adjustedDay)
            }

            imports.add(
                ImportInvoice(
                    id = "imp_$i",
                    code = code,
                    farmerName = farmer,
                    farmerPhone = phone,
                    vehicleType = vehicle,
                    crops = crops,
                    progressPercent = progress,
                    status = status,
                    totalEstimatedSalesIQD = estSales,
                    date = dateStr
                )
            )
        }
        return imports
    }

    fun generate50SalesInvoices(): List<SalesInvoice> {
        val sales = mutableListOf<SalesInvoice>()

        for (i in 1..50) {
            val code = "INV-${9000 + i}"
            val customerInfo = customerList[(i - 1) % customerList.size]
            val phone = "077" + String.format(Locale.ENGLISH, "%07d", (1000000 + i * 23456) % 8999999)
            val isCash = i % 3 != 0
            val paymentType = if (isCash) "كاش" else "آجل"
            val deferredDays = if (isCash) 0 else listOf(3, 7, 10, 14, 30)[i % 5]

            val itemCount = (i % 3) + 1
            val items = mutableListOf<SaleCropItem>()

            for (c in 0 until itemCount) {
                val cropInfo = cropCatalog[(i * 2 + c * 5) % cropCatalog.size]
                val weight = 100.0 + ((i * 31 + c * 19) % 800)
                val unitPrice = cropInfo.second + ((i * 50) % (cropInfo.third - cropInfo.second + 1))
                items.add(
                    SaleCropItem(
                        cropName = cropInfo.first,
                        weightOrCount = weight,
                        unitPriceIQD = unitPrice
                    )
                )
            }

            val goodsTotal = items.sumOf { it.totalAmountIQD }
            val commission = (goodsTotal * 0.07).toLong()
            val porterage = 15000L + (i % 8) * 5000L
            val grandTotal = goodsTotal + commission + porterage

            val dayOffset = (i - 1) / 2
            val hour = 7 + (i * 2 % 11)
            val minute = (i * 11 % 60)
            val amPm = if (hour >= 12) "م" else "ص"
            val displayHour = if (hour > 12) hour - 12 else hour
            val timeStr = String.format(Locale.ENGLISH, "%02d:%02d $amPm", displayHour, minute)

            val day = 7 - (dayOffset % 28)
            val adjustedDay = if (day <= 0) day + 30 else day
            val month = if (dayOffset > 7) "07" else "08"
            val dateStr = String.format(Locale.ENGLISH, "%02d/$month/2026 $timeStr", adjustedDay)

            sales.add(
                SalesInvoice(
                    id = "sale_${100 + i}",
                    code = code,
                    customerName = customerInfo.first,
                    customerPhone = phone,
                    customerAddress = customerInfo.second,
                    paymentType = paymentType,
                    deferredDays = deferredDays,
                    items = items,
                    goodsTotalIQD = goodsTotal,
                    officeCommission7Percent = commission,
                    porterageFeeIQD = porterage,
                    grandTotalIQD = grandTotal,
                    date = dateStr,
                    isPrinted = i % 2 == 0
                )
            )
        }
        return sales
    }
}
