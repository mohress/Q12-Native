package com.example.ui.modals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LicenseLockDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Rounded.Verified, contentDescription = null, tint = GoldLicense)
                Text(
                    text = "توثيق الاشتراك والترخيص الذهبي",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(
                    color = DarkForestGreen,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("الترخيص: اشتراك أوفلاين ذهبي مدى الحياة 💎", color = GoldLicense, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                        Text("معرف الجهاز (HWID): ALWA-HWID-84091-IQ99", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("النسخة والمستوى: v3.4.0 (إصدار سوق العلوة المستقر)", color = MintGreen, fontSize = 11.sp)
                    }
                }

                Text(
                    text = "التطبيق مرخص بالكامل ويعمل بدون الحاجة لاتصال بالإنترنت بشكل آمن وسريع على الأجهزة اللوحية والمحمولة.",
                    fontSize = 11.sp,
                    color = TextSecondaryMuted
                )

                Text(
                    text = "مطور النظام: شركة برايم™ للحلول البرمجية والمحاسبية 2026",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkForestGreen
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = DarkForestGreen)) {
                Text("تم، حسناً", color = Color.White)
            }
        },
        containerColor = BackgroundSoft,
        shape = RoundedCornerShape(20.dp)
    )
}
