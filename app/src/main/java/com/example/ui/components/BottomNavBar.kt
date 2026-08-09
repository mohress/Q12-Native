package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val index: Int
)

@Composable
fun BottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        NavItem("الاستيراد", Icons.Rounded.Receipt, 0),
        NavItem("المبيعات", Icons.Rounded.ShoppingBag, 1),
        NavItem("الحسابات", Icons.Rounded.Payments, 2),
        NavItem("الإحصائيات", Icons.Rounded.Analytics, 3),
        NavItem("الإعدادات", Icons.Rounded.Settings, 4)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 14.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.42f)
                .widthIn(min = 380.dp, max = 580.dp)
                .shadow(16.dp, RoundedCornerShape(28.dp), spotColor = Color.Black.copy(alpha = 0.25f)),
            color = CardSurfaceWhite,
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navItems.forEach { item ->
                    val isSelected = selectedTab == item.index

                    val backgroundColor by animateColorAsState(
                        targetValue = if (isSelected) DarkForestGreen else Color.Transparent,
                        label = "bgColor"
                    )

                    val itemScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.06f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "itemScale"
                    )

                    Box(
                        modifier = Modifier
                            .scale(itemScale)
                            .clip(RoundedCornerShape(20.dp))
                            .background(backgroundColor)
                            .clickable { onTabSelected(item.index) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (isSelected) GoldLicense else TextSecondaryMuted,
                                modifier = Modifier.size(20.dp)
                            )
                            AnimatedVisibility(
                                visible = isSelected,
                                enter = expandHorizontally(expandFrom = Alignment.Start),
                                exit = shrinkHorizontally(shrinkTowards = Alignment.Start)
                            ) {
                                Text(
                                    text = item.title,
                                    color = Color.White,
                                    fontSize = 12.sp,
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
}

