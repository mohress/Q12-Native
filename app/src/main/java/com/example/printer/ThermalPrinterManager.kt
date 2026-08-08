package com.example.printer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import com.example.data.models.SalesInvoice
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.util.UUID

data class PrinterDevice(
    val name: String,
    val address: String,
    val isPaired: Boolean = false,
    val isConnected: Boolean = false,
    val paperSize: String = "58mm" // "58mm" or "80mm"
)

object ThermalPrinterManager {

    private const val TAG = "ThermalPrinter"
    private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private var currentSocket: BluetoothSocket? = null
    private var connectedDevice: PrinterDevice? = null

    // ESC/POS Commands
    val ESC_INIT = byteArrayOf(0x1B, 0x40)
    val ESC_ALIGN_LEFT = byteArrayOf(0x1B, 0x61, 0x00)
    val ESC_ALIGN_CENTER = byteArrayOf(0x1B, 0x61, 0x01)
    val ESC_ALIGN_RIGHT = byteArrayOf(0x1B, 0x61, 0x02)
    val ESC_BOLD_ON = byteArrayOf(0x1B, 0x45, 0x01)
    val ESC_BOLD_OFF = byteArrayOf(0x1B, 0x45, 0x00)
    val ESC_DOUBLE_SIZE = byteArrayOf(0x1D, 0x21, 0x11)
    val ESC_NORMAL_SIZE = byteArrayOf(0x1D, 0x21, 0x00)
    val ESC_FEED_AND_CUT = byteArrayOf(0x1D, 0x56, 0x42, 0x00)

    @SuppressLint("MissingPermission")
    fun getPairedBluetoothDevices(): List<PrinterDevice> {
        val adapter = BluetoothAdapter.getDefaultAdapter() ?: return emptyList()
        return try {
            val bonded = adapter.bondedDevices ?: emptySet()
            bonded.map { device ->
                PrinterDevice(
                    name = device.name ?: "طابعة حرارية",
                    address = device.address,
                    isPaired = true,
                    isConnected = (currentSocket?.remoteDevice?.address == device.address && currentSocket?.isConnected == true),
                    paperSize = if (device.name?.contains("80") == true) "80mm" else "58mm"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching bluetooth devices", e)
            emptyList()
        }
    }

    /**
     * Connect to a Bluetooth thermal printer by MAC address
     */
    @SuppressLint("MissingPermission")
    fun connectDevice(address: String): Boolean {
        disconnect()

        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null || !adapter.isEnabled) {
            Log.e(TAG, "Bluetooth adapter is null or disabled")
            return false
        }

        return try {
            val device = adapter.getRemoteDevice(address)
            val socket = device.createRfcommSocketToServiceRecord(SPP_UUID)
            adapter.cancelDiscovery()
            socket.connect()
            currentSocket = socket
            connectedDevice = PrinterDevice(
                name = device.name ?: "طابعة حرارية",
                address = device.address,
                isPaired = true,
                isConnected = true,
                paperSize = if (device.name?.contains("80") == true) "80mm" else "58mm"
            )
            Log.d(TAG, "Successfully connected to Bluetooth printer: ${device.name}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Real Bluetooth socket connection failed for address: $address", e)
            disconnect()
            false
        }
    }

    fun disconnect() {
        try {
            currentSocket?.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error closing bluetooth socket", e)
        } finally {
            currentSocket = null
            connectedDevice = null
        }
    }

    fun isConnected(): Boolean {
        return currentSocket?.isConnected == true
    }

    fun getConnectedDevice(): PrinterDevice? {
        return if (isConnected()) connectedDevice else null
    }

    fun getConnectedDeviceName(): String {
        return if (isConnected()) connectedDevice?.name ?: "طابعة متصلة" else "غير متصلة"
    }

    /**
     * Converts an Android Bitmap into ESC/POS Raster Bit Image (GS v 0) bytes.
     * Compatible with 100% of Chinese portable thermal printers (RPP02N, PT-210, MTP-II, etc.)
     */
    fun bitmapToEscPosRaster(bitmap: Bitmap): ByteArray {
        val width = bitmap.width
        val height = bitmap.height
        val widthBytes = (width + 7) / 8
        val stream = ByteArrayOutputStream()

        // GS v 0 m xL xH yL yH
        stream.write(byteArrayOf(0x1D, 0x76, 0x30, 0x00))
        stream.write(byteArrayOf((widthBytes and 0xFF).toByte(), ((widthBytes shr 8) and 0xFF).toByte()))
        stream.write(byteArrayOf((height and 0xFF).toByte(), ((height shr 8) and 0xFF).toByte()))

        val rasterData = ByteArray(widthBytes * height)
        var byteIndex = 0

        for (y in 0 until height) {
            for (xByte in 0 until widthBytes) {
                var b = 0
                for (bit in 0 until 8) {
                    val x = xByte * 8 + bit
                    if (x < width) {
                        val pixel = bitmap.getPixel(x, y)
                        val r = (pixel shr 16) and 0xFF
                        val g = (pixel shr 8) and 0xFF
                        val bVal = pixel and 0xFF
                        val luminance = (0.299 * r + 0.587 * g + 0.114 * bVal)
                        if (luminance < 160) { // Black pixel
                            b = b or (0x80 shr bit)
                        }
                    }
                }
                rasterData[byteIndex++] = b.toByte()
            }
        }

        stream.write(rasterData)
        return stream.toByteArray()
    }

    /**
     * Generates a high-resolution monochrome receipt bitmap rendered specifically for 58mm (384px) or 80mm (576px) thermal paper width.
     */
    fun createInvoiceBitmap(
        invoice: SalesInvoice,
        alwaName: String,
        ownerName: String,
        phone: String,
        location: String,
        accountant: String,
        paperWidthPx: Int = 384 // 384px for 58mm, 576px for 80mm
    ): Bitmap {
        val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 20f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val paintSub = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.DKGRAY
            textSize = 16f
        }
        val paintLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            strokeWidth = 2f
        }

        // Measure height
        var currentY = 30f
        val lineSpacing = 28f

        // Estimated content lines
        val lineCount = 20 + (invoice.items.size * 3)
        val bitmapHeight = (lineCount * lineSpacing + 120).toInt()

        val bitmap = Bitmap.createBitmap(paperWidthPx, bitmapHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val margin = 12f
        val printableWidth = paperWidthPx - (margin * 2)

        fun drawCenteredText(text: String, y: Float, paint: Paint) {
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(text, paperWidthPx / 2f, y, paint)
        }

        fun drawRowText(left: String, right: String, y: Float, paint: Paint) {
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText(left, margin, y, paint)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(right, paperWidthPx - margin, y, paint)
        }

        fun drawDivider(y: Float, isDashed: Boolean = false) {
            if (isDashed) {
                var x = margin
                while (x < paperWidthPx - margin) {
                    canvas.drawLine(x, y, x + 6f, y, paintLine)
                    x += 12f
                }
            } else {
                canvas.drawLine(margin, y, paperWidthPx - margin, y, paintLine)
            }
        }

        // 1. Header
        paintText.textSize = 24f
        drawCenteredText("🌴 $alwaName 🌴", currentY, paintText)
        currentY += 30f

        paintSub.textSize = 15f
        drawCenteredText("سوق الجملة للفواكه والخضروات", currentY, paintSub)
        currentY += 24f

        drawCenteredText("الإدارة: $ownerName | هاتف: $phone", currentY, paintSub)
        currentY += 24f

        drawCenteredText(location, currentY, paintSub)
        currentY += 26f

        drawDivider(currentY)
        currentY += 15f

        // 2. Invoice Meta
        paintText.textSize = 18f
        drawRowText("فاتورة مبيعات:", invoice.code, currentY, paintText)
        currentY += 24f

        paintSub.textSize = 16f
        drawRowText("التاريخ والوقت:", invoice.date, currentY, paintSub)
        currentY += 24f

        drawRowText("الزبون:", invoice.customerName, currentY, paintSub)
        currentY += 24f

        val payStr = "${invoice.paymentType} ${if (invoice.paymentType == "آجل") "(${invoice.deferredDays} يوم)" else ""}"
        drawRowText("طريقة الدفع:", payStr, currentY, paintSub)
        currentY += 26f

        drawDivider(currentY, isDashed = true)
        currentY += 18f

        // 3. Table Headers
        paintText.textSize = 17f
        drawRowText("الصنف والكمية", "الإجمالي (د.ع)", currentY, paintText)
        currentY += 22f

        drawDivider(currentY, isDashed = true)
        currentY += 16f

        // 4. Items
        invoice.items.forEach { item ->
            paintText.textSize = 17f
            drawRowText(item.cropName, "${item.totalAmountIQD}", currentY, paintText)
            currentY += 22f

            paintSub.textSize = 14f
            val details = "${item.weightOrCount} كغم × ${item.unitPriceIQD} د.ع"
            paintSub.textAlign = Paint.Align.LEFT
            canvas.drawText(details, margin + 8f, currentY, paintSub)
            currentY += 24f
        }

        drawDivider(currentY)
        currentY += 18f

        // 5. Totals Breakdown
        paintSub.textSize = 16f
        drawRowText("مجموع البضاعة:", "${invoice.goodsTotalIQD} د.ع", currentY, paintSub)
        currentY += 22f

        drawRowText("عمولة المكتب (7%):", "${invoice.officeCommission7Percent} د.ع", currentY, paintSub)
        currentY += 22f

        drawRowText("أجور الحمالية:", "${invoice.porterageFeeIQD} د.ع", currentY, paintSub)
        currentY += 26f

        drawDivider(currentY)
        currentY += 20f

        // Grand Total Box
        paintText.textSize = 21f
        drawRowText("المبلغ الإجمالي الكلي:", "${invoice.grandTotalIQD} د.ع", currentY, paintText)
        currentY += 30f

        drawDivider(currentY)
        currentY += 20f

        // 6. Footer
        paintSub.textSize = 14f
        drawCenteredText("المحاسب المسؤول: $accountant", currentY, paintSub)
        currentY += 22f

        drawCenteredText("شكرًا لتعاملكم - نظام علوة الذكي 2026", currentY, paintSub)
        currentY += 22f

        drawCenteredText("* فاتورة رسمية صادرة آلياً *", currentY, paintSub)
        currentY += 30f

        // Crop final bitmap to actual rendered height
        val finalBitmap = Bitmap.createBitmap(bitmap, 0, 0, paperWidthPx, currentY.toInt().coerceAtMost(bitmapHeight))
        return finalBitmap
    }

    /**
     * Sends formatted ESC/POS data to the connected thermal printer socket.
     */
    fun printInvoiceToBluetooth(
        invoice: SalesInvoice,
        alwaName: String,
        ownerName: String,
        phone: String,
        location: String,
        accountant: String,
        paperSize: String = "58mm"
    ): Boolean {
        val socket = currentSocket
        if (socket == null || !socket.isConnected) {
            Log.e(TAG, "Cannot print: Bluetooth thermal printer is not connected")
            return false
        }

        return try {
            val paperPx = if (paperSize == "80mm") 576 else 384
            val bitmap = createInvoiceBitmap(invoice, alwaName, ownerName, phone, location, accountant, paperPx)
            val rasterBytes = bitmapToEscPosRaster(bitmap)

            val out: OutputStream = socket.outputStream
            out.write(ESC_INIT)
            out.write(ESC_ALIGN_CENTER)
            out.write(rasterBytes)
            out.write(byteArrayOf(0x0A, 0x0A, 0x0A)) // Line feeds
            out.write(ESC_FEED_AND_CUT)
            out.flush()
            Log.d(TAG, "Print data sent successfully to real Bluetooth printer")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed sending print bytes to Bluetooth socket", e)
            disconnect()
            false
        }
    }
}
