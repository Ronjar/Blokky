package com.robingebert.blokky.feature_preferences.ui.composables

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.robingebert.blokky.R

@Composable
fun InstagramColoredIcon() {
    val gradientBrush = remember {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFF58529),
                Color(0xFFDD2A7B),
                Color(0xFF8134AF),
                Color(0xFF515BD4)
            )
        )
    }

    Icon(
        painter = painterResource(R.drawable.ic_instagram),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(gradientBrush, blendMode = BlendMode.SrcAtop)
                }
            }
    )
}