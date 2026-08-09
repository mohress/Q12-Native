package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    alwaName: String,
    isLocked: Boolean,
    enteredPin: String,
    pinError: Boolean,
    onPinDigit: (String) -> Unit,
    onClearPin: () -> Unit,
    onDismissSplash: () -> Unit,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableStateOf(0.1f) }

    // Auto-dismiss splash screen after short initialization delay if not locked
    LaunchedEffect(isLocked) {
        if (!isLocked) {
            progress = 0.3f
            delay(300)
            progress = 0.7f
            delay(400)
            progress = 1.0f
            delay(300)
            onDismissSplash()
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "SplashProgress"
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        DarkForestGreen,
                        MediumForestGreen,
                        Color(0xFF0F2D21)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Decorative background glow circles
        Box(
            modifier = Modifier
                .size(320.dp)
                .clip(CircleShape)
                .background(GoldLicense.copy(alpha = 0.08f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Surface(
                modifier = Modifier
                    .size(90.dp)
                    .shadow(16.dp, CircleShape),
                shape = CircleShape,
                color = CardSurfaceWhite
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ReceiptLong,
                        contentDescription = "Logo",
                        tint = DarkForestGreen,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Badge
            Surface(
                color = GoldLicense,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "النظام المحاسبي الذكي ⚡",
                    color = TextPrimaryDark,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Alwa Title
            Text(
                text = alwaName,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "شريكك الموثوق في إدارة المحاصيل والبيوعات",
                color = MintGreen,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isLocked) {
                // Passcode PIN Entry Screen
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .shadow(12.dp, RoundedCornerShape(24.dp)),
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Rounded.Lock, contentDescription = null, tint = GoldLicense)
                            Text(
                                text = "أدخل رمز مرور قفل التطبيق",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // PIN Indicators
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            for (i in 0 until 4) {
                                val filled = i < enteredPin.length
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(if (filled) GoldLicense else Color.White.copy(alpha = 0.3f))
                                )
                            }
                        }

                        if (pinError) {
                            Text(
                                text = "رمز المرور غير صحيح! حاول مجدداً (رمز الافتراضي: 1234)",
                                color = RedWarning,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Number Pad 3x4
                        val pinKeys = listOf(
                            listOf("1", "2", "3"),
                            listOf("4", "5", "6"),
                            listOf("7", "8", "9"),
                            listOf("C", "0", "🔓")
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            pinKeys.forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    row.forEach { key ->
                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable {
                                                    when (key) {
                                                        "C" -> onClearPin()
                                                        "🔓" -> onDismissSplash()
                                                        else -> onPinDigit(key)
                                                    }
                                                },
                                            color = Color.White.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = key,
                                                    color = Color.White,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Loading Spinner & Diagnostics Panel
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = GoldLicense,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(36.dp)
                    )

                    Text(
                        text = "جاري فتح النظام المحاسبي تلقائياً...",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    // Diagnostic Panel
                    Surface(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Shield,
                                    contentDescription = "Security",
                                    tint = EmeraldSuccess,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "جاري تجهيز بيانات المبيعات والمخزون...",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            LinearProgressIndicator(
                                progress = animatedProgress,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = GoldLicense,
                                trackColor = Color.White.copy(alpha = 0.2f)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("الحالة: جاري الدخول تلقائياً...", color = MintGreen, fontSize = 10.sp)
                                Text("${(animatedProgress * 100).toInt()}%", color = GoldLicense, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onDismissSplash,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLicense),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldLicense.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(44.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "دخول سريع ⚡",
                                color = GoldLicense,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
