package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dma.studentapplication.model.RoboBuddyState
import com.dma.studentapplication.ui.components.RoboBuddy
import com.dma.studentapplication.ui.theme.StudentApplicationTheme

/**
 * Splash screen of the quiz application.
 *
 * This screen introduces the app with:
 * - animated background
 * - robot mascot section
 * - app branding
 * - short subtitle
 * - start learning button
 *
 * The layout supports both light and dark themes.
 *
 * @param onStartClick callback triggered when the user taps the start button
 */
@Composable
fun SplashScreen(
    onStartClick: () -> Unit = {},
) {
    val isDark = isSystemInDarkTheme()
    val scrollState = rememberScrollState()

    // Main text colors based on current theme.
    val titleColor = if (isDark) Color(0xFFF7FAFF) else Color(0xFF081A56)
    val subtitleColor = if (isDark) Color(0xFFB8C2D6) else Color(0xFF52607A)

    // Tag/pill colors shown below the robot.
    val pillBg = if (isDark) Color(0xFF031126) else Color(0xFFF1FFF7)
    val pillBorder = if (isDark) Color(0xFF126E72) else Color(0xFF20BF72)
    val pillText = if (isDark) Color(0xFF2DE08A) else Color(0xFF15995D)

    // Button gradient and shadow colors.
    val buttonTop = Color(0xFF35D86E)
    val buttonBottom = Color(0xFF14C97C)
    val buttonShadow = if (isDark) Color(0xFF001A28) else Color(0xFFC5F0DB)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) Color(0xFF000B1B) else Color(0xFFFAFBFD))
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        val contentMaxWidth = 420.dp
        val screenMinHeight = this.maxHeight - 24.dp

        // Decorative animated background layer.
        SplashBackground(isDark = isDark)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
                    .heightIn(min = screenMinHeight),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(56.dp))

                // Main animated robot section.
                RobotHeroSection(isDark = isDark)

                Spacer(modifier = Modifier.height(1.dp))

                // Small branding pill under the mascot.
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = pillBg,
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    modifier = Modifier.border(
                        width = 1.6.dp,
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
                        modifier = Modifier.padding(horizontal = 26.dp, vertical = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // App title.
                Text(
                    text = "Quizzy",
                    color = titleColor,
                    fontSize = 58.sp,
                    lineHeight = 58.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1.8).sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = if (isDark) 0.34f else 0.12f),
                            offset = Offset(0f, 4f),
                            blurRadius = 10f
                        )
                    ),
                    modifier = Modifier.semantics { heading() }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Decorative underline below the title.
                Box(
                    modifier = Modifier
                        .width(92.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF35D86E),
                                    Color(0xFF14C97C)
                                )
                            )
                        )
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Short app description.
                Text(
                    text = "Practice fun topics, answer fast, and let your robo buddy cheer you on through every quiz.",
                    color = subtitleColor,
                    fontSize = 17.sp,
                    lineHeight = 27.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.92f)
                )

                Spacer(modifier = Modifier.height(34.dp))

                // Primary call-to-action button.
                StartLearningButton(
                    isDark = isDark,
                    topColor = buttonTop,
                    bottomColor = buttonBottom,
                    shadowColor = buttonShadow,
                    onClick = onStartClick
                )

                Spacer(modifier = Modifier.height(32.dp))
                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            }
        }
    }
}

/**
 * Animated hero section that displays the robot mascot.
 *
 * Includes:
 * - floating animation for the robot
 * - ambient glow beneath the robot
 * - animated shadow for more depth
 */
@Composable
private fun RobotHeroSection(isDark: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "robot_hero")

    // Floating motion for the robot.
    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "robot_float"
    )

    // Animated ambient glow alpha.
    val ambientAlpha by infiniteTransition.animateFloat(
        initialValue = if (isDark) 0.08f else 0.12f,
        targetValue = if (isDark) 0.16f else 0.20f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambient_alpha"
    )

    // Animated floor shadow alpha.
    val shadowAlpha by infiniteTransition.animateFloat(
        initialValue = if (isDark) 0.24f else 0.18f,
        targetValue = if (isDark) 0.34f else 0.24f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shadow_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // Decorative light glow and shadow under the robot.
        Canvas(modifier = Modifier.fillMaxSize()) {
            val ambientWidth = size.width * 0.42f
            val ambientHeight = size.height * 0.08f
            val ambientX = (size.width - ambientWidth) / 2f
            val ambientY = size.height * 0.74f

            drawOval(
                brush = Brush.radialGradient(
                    colors = if (isDark) {
                        listOf(
                            Color(0xFF19D9A3).copy(alpha = ambientAlpha),
                            Color.Transparent
                        )
                    } else {
                        listOf(
                            Color(0xFF9FE8CF).copy(alpha = ambientAlpha),
                            Color.Transparent
                        )
                    },
                    center = Offset(
                        x = ambientX + ambientWidth / 2f,
                        y = ambientY + ambientHeight / 2f
                    ),
                    radius = ambientWidth * 0.52f
                ),
                topLeft = Offset(ambientX, ambientY),
                size = Size(ambientWidth, ambientHeight)
            )

            val shadowWidth = size.width * 0.42f
            val shadowHeight = size.height * 0.08f
            val shadowX = (size.width - shadowWidth) / 2f
            val shadowY = size.height * 0.74f

            drawOval(
                brush = Brush.radialGradient(
                    colors = if (isDark) {
                        listOf(
                            Color(0xFF19D9A3).copy(alpha = shadowAlpha),
                            Color(0xFF19D9A3).copy(alpha = shadowAlpha * 0.45f),
                            Color.Transparent
                        )
                    } else {
                        listOf(
                            Color(0xFF87DDBE).copy(alpha = shadowAlpha * 0.80f),
                            Color(0xFFAEEBD4).copy(alpha = shadowAlpha * 0.40f),
                            Color.Transparent
                        )
                    },
                    center = Offset(
                        x = shadowX + shadowWidth / 2f,
                        y = shadowY + shadowHeight / 2f
                    ),
                    radius = shadowWidth * 0.44f
                ),
                topLeft = Offset(shadowX, shadowY),
                size = Size(shadowWidth, shadowHeight)
            )
        }

        // Main mascot composable.
        RoboBuddy(
            state = RoboBuddyState.WAVE,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    translationY = floatY
                }
                .size(255.dp)
                .semantics {
                    contentDescription = "Friendly robot mascot waving"
                }
        )
    }
}

/**
 * Animated decorative background for the splash screen.
 *
 * Draws:
 * - soft glow circles
 * - dashed arc
 * - animated sparkles
 */
@Composable
private fun SplashBackground(isDark: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "background_animation")

    // Pulse scale for glow effects.
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_pulse"
    )

    // Phase value used to alternate sparkle brightness.
    val sparklePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle_phase"
    )

    val bigCircleColor = if (isDark) {
        Brush.radialGradient(
            colors = listOf(Color(0xFF0D7D67).copy(alpha = 0.42f), Color.Transparent)
        )
    } else {
        Brush.radialGradient(
            colors = listOf(Color(0xFFC9F5E4), Color.Transparent)
        )
    }

    val smallCircleColor = if (isDark) {
        Brush.radialGradient(
            colors = listOf(Color(0xFF138C74).copy(alpha = 0.30f), Color.Transparent)
        )
    } else {
        Brush.radialGradient(
            colors = listOf(Color(0xFFDDF9EF), Color.Transparent)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Top-left glow.
        drawCircle(
            brush = bigCircleColor,
            radius = size.minDimension * 0.13f * glowPulse,
            center = Offset(size.width * 0.10f, size.height * 0.12f)
        )

        // Top-right glow.
        drawCircle(
            brush = smallCircleColor,
            radius = size.minDimension * 0.07f * glowPulse,
            center = Offset(size.width * 0.86f, size.height * 0.15f)
        )

        // Lower-left glow.
        drawCircle(
            brush = smallCircleColor,
            radius = size.minDimension * 0.055f * glowPulse,
            center = Offset(size.width * 0.10f, size.height * 0.67f)
        )

        // Center ambient glow.
        drawCircle(
            brush = Brush.radialGradient(
                colors = if (isDark) {
                    listOf(
                        Color(0xFF19D9A3).copy(alpha = 0.08f * glowPulse),
                        Color.Transparent
                    )
                } else {
                    listOf(
                        Color(0xFF91E2C5).copy(alpha = 0.12f * glowPulse),
                        Color.Transparent
                    )
                }
            ),
            radius = size.minDimension * 0.24f * glowPulse,
            center = Offset(size.width * 0.5f, size.height * 0.54f)
        )

        // Decorative dashed arc.
        drawArc(
            color = if (isDark) Color(0xFF0A7B73).copy(alpha = 0.48f) else Color(0xFF9FDBCA),
            startAngle = 155f,
            sweepAngle = 230f,
            useCenter = false,
            topLeft = Offset(size.width * 0.19f, size.height * 0.21f),
            size = Size(size.width * 0.5f, size.height * 0.18f),
            style = Stroke(
                width = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 12f), 0f)
            )
        )

        /**
         * Draws a plus-shaped sparkle using two crossing lines.
         */
        fun sparkle(cx: Float, cy: Float, r: Float, color: Color, alpha: Float) {
            drawLine(
                color = color.copy(alpha = alpha),
                start = Offset(cx, cy - r),
                end = Offset(cx, cy + r),
                strokeWidth = 3f
            )
            drawLine(
                color = color.copy(alpha = alpha),
                start = Offset(cx - r, cy),
                end = Offset(cx + r, cy),
                strokeWidth = 3f
            )
        }

        // Animated sparkle decorations.
        sparkle(size.width * 0.24f, size.height * 0.26f, 10f, Color(0xFF2D67D8), 0.55f + 0.35f * sparklePhase)
        sparkle(size.width * 0.73f, size.height * 0.25f, 9f, Color(0xFF2D67D8), 0.50f + 0.30f * (1f - sparklePhase))
        sparkle(size.width * 0.84f, size.height * 0.31f, 12f, Color(0xFF2DD9A5), 0.60f + 0.30f * sparklePhase)
        sparkle(size.width * 0.15f, size.height * 0.32f, 8f, Color(0xFF2DD9A5), 0.45f + 0.25f * (1f - sparklePhase))
        sparkle(size.width * 0.67f, size.height * 0.24f, 7f, Color(0xFF41E0B4), 0.45f + 0.25f * sparklePhase)
        sparkle(size.width * 0.41f, size.height * 0.20f, 6f, Color(0xFF2FD19F), 0.35f + 0.25f * (1f - sparklePhase))
        sparkle(size.width * 0.21f, size.height * 0.42f, 11f, Color(0xFF2ED86F), 0.55f + 0.25f * sparklePhase)
        sparkle(size.width * 0.78f, size.height * 0.40f, 9f, Color(0xFF2ED86F), 0.45f + 0.25f * (1f - sparklePhase))
    }
}

/**
 * Main start button displayed on the splash screen.
 *
 * Includes:
 * - gradient fill
 * - glow effect
 * - moving shine animation
 * - button shadow layer
 *
 * @param isDark whether dark theme is enabled
 * @param topColor top gradient color
 * @param bottomColor bottom gradient color
 * @param shadowColor shadow/background color behind the button
 * @param onClick callback for button tap
 */
@Composable
private fun StartLearningButton(
    isDark: Boolean,
    topColor: Color,
    bottomColor: Color,
    shadowColor: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val infiniteTransition = rememberInfiniteTransition(label = "button_animation")

    // Horizontal moving shine across the button.
    val shineOffset by infiniteTransition.animateFloat(
        initialValue = -250f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "button_shine"
    )

    // Animated glow intensity.
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = if (isDark) 0.18f else 0.10f,
        targetValue = if (isDark) 0.30f else 0.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_glow"
    )

    // Scale animation value for future button press effects.
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(180),
        label = "button_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        // Bottom shadow layer.
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 10.dp, start = 4.dp, end = 4.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(shadowColor)
                .alpha(if (isDark) 0.90f else 0.75f)
        )

        // Main clickable surface.
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.Transparent,
            shadowElevation = 0.dp,
            tonalElevation = 0.dp,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(28.dp))
                .semantics {
                    role = Role.Button
                    contentDescription = "Start learning"
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
                .drawBehind {
                    // Base vertical gradient.
                    drawRoundRect(
                        brush = Brush.verticalGradient(listOf(topColor, bottomColor)),
                        cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx())
                    )

                    // Soft highlight overlay.
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.White.copy(alpha = 0.12f),
                                Color.Transparent
                            )
                        ),
                        cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx())
                    )

                    // Glow effect near top area.
                    drawRoundRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = glowAlpha),
                                Color.Transparent
                            ),
                            center = Offset(size.width / 2f, size.height * 0.15f),
                            radius = size.width * 0.7f
                        ),
                        cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx())
                    )

                    // Moving shine stripe.
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.16f),
                                Color.Transparent
                            ),
                            start = Offset(shineOffset, 0f),
                            end = Offset(shineOffset + 220f, size.height)
                        ),
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

/**
 * Light theme preview of the splash screen.
 */
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

/**
 * Dark theme preview of the splash screen.
 */
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