package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dma.studentapplication.model.RoboBuddyState
import com.dma.studentapplication.ui.components.RoboBuddy
import com.dma.studentapplication.ui.theme.StudentApplicationTheme

@Composable
fun SplashScreen(
    onStartClick: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()

    val titleColor = if (isDark) Color(0xFFF5F7FB) else Color(0xFF081A56)
    val subtitleColor = if (isDark) Color(0xFF9DA6BE) else Color(0xFF6E778C)

    val pillBg = if (isDark) Color(0xFF072C33) else Color(0x1A2DCC6A)
    val pillBorder = if (isDark) Color(0xFF0F4D55) else Color(0xFF2DCC6A)
    val pillText = Color(0xFF27D67B)

    val buttonTop = Color(0xFF35D86E)
    val buttonBottom = Color(0xFF14C97C)
    val buttonShadow = if (isDark) Color(0xFF001A28) else Color(0xFFB7E8D2)

    val featureTextColor = if (isDark) Color(0xFFE9EEF8) else Color(0xFF16213E)
    val featureDividerColor = if (isDark) Color(0xFF233359) else Color(0xFFD4DCEB)
    val featureIconBg = if (isDark) Color(0xFF09263D) else Color(0xFFEAF7F1)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = if (isDark) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF02133A),
                            Color(0xFF031B4F)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF7FAFC),
                            Color(0xFFEAF7F1)
                        )
                    )
                }
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        SplashBackground(isDark = isDark)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                RoboBuddy(
                    state = RoboBuddyState.WAVE,
                    modifier = Modifier.size(235.dp)
                )
            }

            Spacer(modifier = Modifier.height(if (isDark) 10.dp else 16.dp))

            Surface(
                shape = RoundedCornerShape(999.dp),
                color = pillBg,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier.border(
                    width = 1.4.dp,
                    color = pillBorder,
                    shape = RoundedCornerShape(999.dp)
                )
            ) {
                Text(
                    text = "Quiz • Learn • Level Up",
                    color = pillText,
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Quizzy",
                color = titleColor,
                fontSize = 66.sp,
                lineHeight = 66.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1.8).sp,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = Shadow(
                        color = if (isDark) Color(0x66000000) else Color.Transparent,
                        offset = Offset(0f, 2f),
                        blurRadius = 2f
                    )
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .width(86.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFF2ED86F))
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Practice fun topics, answer fast, and let your robo buddy cheer you on through every quiz.",
                color = subtitleColor,
                fontSize = 17.sp,
                lineHeight = 27.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.84f)
            )

            Spacer(modifier = Modifier.height(30.dp))

            StartLearningButton(
                isDark = isDark,
                topColor = buttonTop,
                bottomColor = buttonBottom,
                shadowColor = buttonShadow,
                onClick = onStartClick
            )

            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

@Composable
private fun SplashBackground(isDark: Boolean) {
    val bigCircleColor = if (isDark) {
        Brush.radialGradient(
            colors = listOf(Color(0xFF0D7D67).copy(alpha = 0.45f), Color.Transparent)
        )
    } else {
        Brush.radialGradient(
            colors = listOf(Color(0xFFBDEFE0), Color.Transparent)
        )
    }

    val smallCircleColor = if (isDark) {
        Brush.radialGradient(
            colors = listOf(Color(0xFF138C74).copy(alpha = 0.35f), Color.Transparent)
        )
    } else {
        Brush.radialGradient(
            colors = listOf(Color(0xFFD5F5EA), Color.Transparent)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = bigCircleColor,
            radius = size.minDimension * 0.13f,
            center = Offset(size.width * 0.10f, size.height * 0.12f)
        )

        drawCircle(
            brush = smallCircleColor,
            radius = size.minDimension * 0.07f,
            center = Offset(size.width * 0.86f, size.height * 0.15f)
        )

        drawCircle(
            brush = smallCircleColor,
            radius = size.minDimension * 0.055f,
            center = Offset(size.width * 0.10f, size.height * 0.67f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = if (isDark) {
                    listOf(Color(0xFF10E39F).copy(alpha = 0.18f), Color.Transparent)
                } else {
                    listOf(Color(0xFFBEF5E4).copy(alpha = 0.90f), Color.Transparent)
                }
            ),
            radius = size.minDimension * 0.26f,
            center = Offset(size.width * 0.50f, size.height * 0.33f)
        )

        drawArc(
            color = if (isDark) Color(0xFF0A7B73).copy(alpha = 0.55f) else Color(0xFFB3EAD8),
            startAngle = 155f,
            sweepAngle = 230f,
            useCenter = false,
            topLeft = Offset(size.width * 0.19f, size.height * 0.21f),
            size = Size(size.width * 0.64f, size.height * 0.18f),
            style = Stroke(
                width = 3f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 12f), 0f)
            )
        )

        drawRoundRect(
            color = if (isDark) Color(0xFF000C22).copy(alpha = 0.9f) else Color(0xFFCFF3E3),
            topLeft = Offset(size.width * 0.37f, size.height * 0.46f),
            size = Size(size.width * 0.26f, size.height * 0.018f),
            cornerRadius = CornerRadius(100f, 100f)
        )

        fun sparkle(cx: Float, cy: Float, r: Float, color: Color) {
            drawLine(
                color = color,
                start = Offset(cx, cy - r),
                end = Offset(cx, cy + r),
                strokeWidth = 3f
            )
            drawLine(
                color = color,
                start = Offset(cx - r, cy),
                end = Offset(cx + r, cy),
                strokeWidth = 3f
            )
        }

        sparkle(size.width * 0.24f, size.height * 0.26f, 10f, Color(0xFF2D67D8))
        sparkle(size.width * 0.73f, size.height * 0.25f, 9f, Color(0xFF2D67D8))
        sparkle(size.width * 0.84f, size.height * 0.31f, 12f, Color(0xFF2DD9A5))
        sparkle(size.width * 0.15f, size.height * 0.32f, 8f, Color(0xFF2DD9A5))
        sparkle(size.width * 0.67f, size.height * 0.24f, 7f, Color(0xFF41E0B4))
        sparkle(size.width * 0.41f, size.height * 0.20f, 6f, Color(0xFF2FD19F))
        sparkle(size.width * 0.21f, size.height * 0.42f, 11f, Color(0xFF2ED86F))
        sparkle(size.width * 0.78f, size.height * 0.40f, 9f, Color(0xFF2ED86F))
    }
}

@Composable
private fun StartLearningButton(
    isDark: Boolean,
    topColor: Color,
    bottomColor: Color,
    shadowColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 10.dp, start = 4.dp, end = 4.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(shadowColor)
                .alpha(if (isDark) 0.9f else 0.75f)
        )

        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(28.dp),
            color = Color.Transparent,
            shadowElevation = 0.dp,
            tonalElevation = 0.dp,
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    drawRoundRect(
                        brush = Brush.verticalGradient(listOf(topColor, bottomColor)),
                        cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx())
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.RocketLaunch,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Start Learning",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun FeatureDivider(color: Color) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .width(1.dp)
            .height(54.dp)
            .background(color)
    )
}

@Preview(
    name = "Splash Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun SplashLightPreview() {
    StudentApplicationTheme(darkTheme = false, dynamicColor = false) {
        SplashScreen()
    }
}

@Preview(
    name = "Splash Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SplashDarkPreview() {
    StudentApplicationTheme(darkTheme = true, dynamicColor = false) {
        SplashScreen()
    }
}