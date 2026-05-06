package com.dma.studentapplication.ui.screens.welcome

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

// ── Main screen ───────────────────────────────────────────────────────────────

/**
 * Splash / onboarding screen shown when the app first launches.
 *
 * Displays:
 * - Animated decorative background (glows, dashes, sparkles)
 * - Floating RoboBuddy mascot hero section
 * - Branding pill ("Quiz • Learn • Level Up")
 * - App title "Quizzy" with gradient underline
 * - Short descriptive subtitle
 * - Animated "Start Learning" CTA button
 *
 * The layout is scrollable to accommodate smaller screen heights and supports
 * both light and dark themes automatically.
 *
 * @param onStartClick Called when the user taps the "Start Learning" button.
 */
@Composable
fun SplashScreen(
    onStartClick: () -> Unit = {},
) {
    val isDark      = isSystemInDarkTheme()
    val scrollState = rememberScrollState()

    // ── Theme-aware colors ────────────────────────────────────────────────────
    val titleColor    = if (isDark) Color(0xFFF7FAFF) else Color(0xFF081A56)
    val subtitleColor = if (isDark) Color(0xFFB8C2D6) else Color(0xFF52607A)

    // Branding pill colors
    val pillBg     = if (isDark) Color(0xFF031126) else Color(0xFFF1FFF7)
    val pillBorder = if (isDark) Color(0xFF126E72) else Color(0xFF20BF72)
    val pillText   = if (isDark) Color(0xFF2DE08A) else Color(0xFF15995D)

    // CTA button gradient and drop-shadow colors
    val buttonTop    = Color(0xFF35D86E)
    val buttonBottom = Color(0xFF14C97C)
    val buttonShadow = if (isDark) Color(0xFF001A28) else Color(0xFFC5F0DB)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) Color(0xFF000B1B) else Color(0xFFFAFBFD))
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        val contentMaxWidth  = 420.dp
        // Reserve space so the content always fills at least the visible height
        val screenMinHeight  = this.maxHeight - 24.dp

        // Decorative animated layer rendered beneath all content
        SplashBackground(isDark = isDark)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
                    .heightIn(min = screenMinHeight),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                // Floating mascot with ambient glow and shadow
                RobotHeroSection(isDark = isDark)

                Spacer(modifier = Modifier.height(1.dp))

                // Branding pill shown immediately below the mascot
                Surface(
                    shape          = RoundedCornerShape(999.dp),
                    color          = pillBg,
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    modifier       = Modifier.border(
                        width  = 1.6.dp,
                        color  = pillBorder,
                        shape  = RoundedCornerShape(999.dp)
                    )
                ) {
                    Text(
                        text       = "Quiz • Learn • Level Up",
                        color      = pillText,
                        fontSize   = 18.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier   = Modifier.padding(horizontal = 26.dp, vertical = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // App name — marked as a heading for accessibility
                Text(
                    text          = "Quizzy",
                    color         = titleColor,
                    fontSize      = 58.sp,
                    lineHeight    = 58.sp,
                    fontWeight    = FontWeight.ExtraBold,
                    letterSpacing = (-1.8).sp,
                    textAlign     = TextAlign.Center,
                    style         = TextStyle(
                        shadow = Shadow(
                            color      = Color.Black.copy(alpha = if (isDark) 0.34f else 0.12f),
                            offset     = Offset(0f, 4f),
                            blurRadius = 10f
                        )
                    ),
                    modifier = Modifier.semantics { heading() }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Short green gradient underline beneath the app name
                Box(
                    modifier = Modifier
                        .width(92.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF35D86E), Color(0xFF14C97C))
                            )
                        )
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Short one-line value proposition for the app
                Text(
                    text       = "Practice fun topics, answer fast, and let your robo buddy cheer you on through every quiz.",
                    color      = subtitleColor,
                    fontSize   = 17.sp,
                    lineHeight = 27.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign  = TextAlign.Center,
                    modifier   = Modifier.fillMaxWidth(0.92f)
                )

                Spacer(modifier = Modifier.height(34.dp))

                // Primary CTA — navigates the user into the app
                StartLearningButton(
                    isDark      = isDark,
                    topColor    = buttonTop,
                    bottomColor = buttonBottom,
                    shadowColor = buttonShadow,
                    onClick     = onStartClick
                )

                Spacer(modifier = Modifier.height(32.dp))
                // Extra space equal to the system navigation bar height
                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            }
        }
    }
}

// ── Robot hero section ────────────────────────────────────────────────────────

/**
 * Animated hero section displaying the RoboBuddy mascot.
 *
 * Animations running in parallel on an infinite loop:
 * - **Float** — vertical oscillation giving the robot a hovering feel
 * - **Ambient glow** — soft oval beneath the robot that pulses in opacity
 * - **Floor shadow** — elliptical shadow that breathes with the float cycle
 *
 * All three animations are driven by a single [InfiniteTransition] to keep
 * them in sync without manual coordination.
 *
 * @param isDark Whether dark mode is active (affects glow and shadow colors).
 */
@Composable
private fun RobotHeroSection(isDark: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "robot_hero")

    // Vertical float — robot moves up by 10px then back down continuously
    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue  = -10f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "robot_float"
    )

    // Ambient glow beneath the robot pulses between two opacity values
    val ambientAlpha by infiniteTransition.animateFloat(
        initialValue = if (isDark) 0.08f else 0.12f,
        targetValue  = if (isDark) 0.16f else 0.20f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambient_alpha"
    )

    // Floor shadow intensity matches the float cycle so it "compresses" as robot rises
    val shadowAlpha by infiniteTransition.animateFloat(
        initialValue = if (isDark) 0.24f else 0.18f,
        targetValue  = if (isDark) 0.34f else 0.24f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shadow_alpha"
    )

    Box(
        modifier        = Modifier.fillMaxWidth().height(280.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // Canvas draws the two oval overlays (ambient glow + floor shadow)
        Canvas(modifier = Modifier.fillMaxSize()) {
            // ── Ambient glow ──────────────────────────────────────────────────
            val ambientWidth  = size.width * 0.42f
            val ambientHeight = size.height * 0.08f
            val ambientX      = (size.width - ambientWidth) / 2f
            val ambientY      = size.height * 0.74f

            drawOval(
                brush = Brush.radialGradient(
                    colors = if (isDark) {
                        listOf(Color(0xFF19D9A3).copy(alpha = ambientAlpha), Color.Transparent)
                    } else {
                        listOf(Color(0xFF9FE8CF).copy(alpha = ambientAlpha), Color.Transparent)
                    },
                    center = Offset(ambientX + ambientWidth / 2f, ambientY + ambientHeight / 2f),
                    radius = ambientWidth * 0.52f
                ),
                topLeft = Offset(ambientX, ambientY),
                size    = Size(ambientWidth, ambientHeight)
            )

            // ── Floor shadow ──────────────────────────────────────────────────
            val shadowWidth  = size.width * 0.42f
            val shadowHeight = size.height * 0.08f
            val shadowX      = (size.width - shadowWidth) / 2f
            val shadowY      = size.height * 0.74f

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
                    center = Offset(shadowX + shadowWidth / 2f, shadowY + shadowHeight / 2f),
                    radius = shadowWidth * 0.44f
                ),
                topLeft = Offset(shadowX, shadowY),
                size    = Size(shadowWidth, shadowHeight)
            )
        }

        // RoboBuddy mascot — offset by the float animation each frame
        RoboBuddy(
            state    = RoboBuddyState.WAVE,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer { translationY = floatY }
                .size(255.dp)
                .semantics { contentDescription = "Friendly robot mascot waving" }
        )
    }
}

// ── Decorative background ─────────────────────────────────────────────────────

/**
 * Full-screen animated background rendered behind all splash content.
 *
 * Drawn entirely on a [Canvas] with no layout overhead:
 * - **Glow circles** — soft radial gradients at the corners and center that pulse in size
 * - **Dashed arc** — static decorative curved stroke across the upper portion
 * - **Sparkles** — plus-shaped (+) accent marks that alternate in brightness
 *
 * @param isDark Whether dark mode is active (changes glow and sparkle colors).
 */
@Composable
private fun SplashBackground(isDark: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "background_animation")

    // Glow circles scale between 0.92× and 1.08× for a gentle breathing effect
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue  = 1.08f,
        animationSpec = infiniteRepeatable(
            animation  = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_pulse"
    )

    // Sparkle phase goes 0→1 so odd and even sparkles can alternate (1f - phase)
    val sparklePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue  = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle_phase"
    )

    // Radial gradient brushes for the large and small background circles
    val bigCircleColor = if (isDark) {
        Brush.radialGradient(colors = listOf(Color(0xFF0D7D67).copy(alpha = 0.42f), Color.Transparent))
    } else {
        Brush.radialGradient(colors = listOf(Color(0xFFC9F5E4), Color.Transparent))
    }

    val smallCircleColor = if (isDark) {
        Brush.radialGradient(colors = listOf(Color(0xFF138C74).copy(alpha = 0.30f), Color.Transparent))
    } else {
        Brush.radialGradient(colors = listOf(Color(0xFFDDF9EF), Color.Transparent))
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        // ── Glow circles ──────────────────────────────────────────────────────

        // Top-left corner glow
        drawCircle(
            brush  = bigCircleColor,
            radius = size.minDimension * 0.13f * glowPulse,
            center = Offset(size.width * 0.10f, size.height * 0.12f)
        )

        // Top-right corner glow
        drawCircle(
            brush  = smallCircleColor,
            radius = size.minDimension * 0.07f * glowPulse,
            center = Offset(size.width * 0.86f, size.height * 0.15f)
        )

        // Lower-left accent glow
        drawCircle(
            brush  = smallCircleColor,
            radius = size.minDimension * 0.055f * glowPulse,
            center = Offset(size.width * 0.10f, size.height * 0.67f)
        )

        // Large center ambient glow behind the mascot area
        drawCircle(
            brush = Brush.radialGradient(
                colors = if (isDark) {
                    listOf(Color(0xFF19D9A3).copy(alpha = 0.08f * glowPulse), Color.Transparent)
                } else {
                    listOf(Color(0xFF91E2C5).copy(alpha = 0.12f * glowPulse), Color.Transparent)
                }
            ),
            radius = size.minDimension * 0.24f * glowPulse,
            center = Offset(size.width * 0.5f, size.height * 0.54f)
        )

        // ── Dashed arc ────────────────────────────────────────────────────────
        // Static decorative stroke running across the upper section
        drawArc(
            color      = if (isDark) Color(0xFF0A7B73).copy(alpha = 0.48f) else Color(0xFF9FDBCA),
            startAngle = 155f,
            sweepAngle = 230f,
            useCenter  = false,
            topLeft    = Offset(size.width * 0.19f, size.height * 0.21f),
            size       = Size(size.width * 0.5f, size.height * 0.18f),
            style      = Stroke(
                width      = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 12f), 0f)
            )
        )

        // ── Sparkles ──────────────────────────────────────────────────────────

        /**
         * Draws a plus-shaped (+) sparkle using two perpendicular lines.
         *
         * @param cx     Centre x coordinate.
         * @param cy     Centre y coordinate.
         * @param r      Half-length of each arm.
         * @param color  Base sparkle color (alpha applied separately).
         * @param alpha  Opacity of the sparkle at this frame.
         */
        fun sparkle(cx: Float, cy: Float, r: Float, color: Color, alpha: Float) {
            drawLine(color = color.copy(alpha = alpha), start = Offset(cx, cy - r), end = Offset(cx, cy + r), strokeWidth = 3f)
            drawLine(color = color.copy(alpha = alpha), start = Offset(cx - r, cy), end = Offset(cx + r, cy), strokeWidth = 3f)
        }

        // Odd sparkles use sparklePhase; even ones use (1f - sparklePhase) so they alternate
        sparkle(size.width * 0.24f, size.height * 0.26f, 10f, Color(0xFF2D67D8), 0.55f + 0.35f * sparklePhase)
        sparkle(size.width * 0.73f, size.height * 0.25f,  9f, Color(0xFF2D67D8), 0.50f + 0.30f * (1f - sparklePhase))
        sparkle(size.width * 0.84f, size.height * 0.31f, 12f, Color(0xFF2DD9A5), 0.60f + 0.30f * sparklePhase)
        sparkle(size.width * 0.15f, size.height * 0.32f,  8f, Color(0xFF2DD9A5), 0.45f + 0.25f * (1f - sparklePhase))
        sparkle(size.width * 0.67f, size.height * 0.24f,  7f, Color(0xFF41E0B4), 0.45f + 0.25f * sparklePhase)
        sparkle(size.width * 0.41f, size.height * 0.20f,  6f, Color(0xFF2FD19F), 0.35f + 0.25f * (1f - sparklePhase))
        sparkle(size.width * 0.21f, size.height * 0.42f, 11f, Color(0xFF2ED86F), 0.55f + 0.25f * sparklePhase)
        sparkle(size.width * 0.78f, size.height * 0.40f,  9f, Color(0xFF2ED86F), 0.45f + 0.25f * (1f - sparklePhase))
    }
}

// ── CTA button ────────────────────────────────────────────────────────────────

/**
 * Animated "Start Learning" call-to-action button.
 *
 * Visual layers rendered with [drawBehind] (back to front):
 * 1. **Drop shadow** — offset Box behind the button surface
 * 2. **Base gradient** — vertical green gradient fill
 * 3. **Highlight overlay** — semi-transparent white fade from top
 * 4. **Glow** — radial white glow near the top edge that pulses
 * 5. **Shine stripe** — diagonal white band that sweeps left-to-right continuously
 *
 * The button uses a custom [clickable] with [indication = null] so ripple is
 * suppressed in favour of the shine animation.
 *
 * @param isDark       Whether dark mode is active (affects glow and shadow intensity).
 * @param topColor     Top color of the vertical gradient fill.
 * @param bottomColor  Bottom color of the vertical gradient fill.
 * @param shadowColor  Color of the drop-shadow Box behind the button.
 * @param onClick      Called when the button is tapped.
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

    // Shine stripe sweeps from left (-250px) to right (900px) on a 2.2 s loop
    val shineOffset by infiniteTransition.animateFloat(
        initialValue = -250f,
        targetValue  = 900f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "button_shine"
    )

    // Radial glow near the top pulses between two opacity values
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = if (isDark) 0.18f else 0.10f,
        targetValue  = if (isDark) 0.30f else 0.18f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_glow"
    )

    // Scale reserved for press feedback — currently always 1f
    val scale by animateFloatAsState(
        targetValue   = 1f,
        animationSpec = tween(180),
        label         = "button_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
    ) {
        // Drop-shadow layer — offset downward and slightly inset on sides
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 10.dp, start = 4.dp, end = 4.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(shadowColor)
                .alpha(if (isDark) 0.90f else 0.75f)
        )

        // Main interactive surface — all visual layers drawn with drawBehind
        Surface(
            shape           = RoundedCornerShape(28.dp),
            color           = Color.Transparent,
            shadowElevation = 0.dp,
            tonalElevation  = 0.dp,
            modifier        = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(28.dp))
                .semantics {
                    role               = Role.Button
                    contentDescription = "Start learning"
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication        = null, // Ripple suppressed — shine animation used instead
                    onClick           = onClick
                )
                .drawBehind {
                    // Layer 1: base vertical gradient fill
                    drawRoundRect(
                        brush        = Brush.verticalGradient(listOf(topColor, bottomColor)),
                        cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx())
                    )

                    // Layer 2: soft top highlight overlay
                    drawRoundRect(
                        brush        = Brush.verticalGradient(
                            listOf(Color.White.copy(alpha = 0.12f), Color.Transparent)
                        ),
                        cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx())
                    )

                    // Layer 3: pulsing radial glow near the top center
                    drawRoundRect(
                        brush        = Brush.radialGradient(
                            colors = listOf(Color.White.copy(alpha = glowAlpha), Color.Transparent),
                            center = Offset(size.width / 2f, size.height * 0.15f),
                            radius = size.width * 0.7f
                        ),
                        cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx())
                    )

                    // Layer 4: sweeping shine stripe
                    drawRoundRect(
                        brush        = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.16f),
                                Color.Transparent
                            ),
                            start = Offset(shineOffset, 0f),
                            end   = Offset(shineOffset + 220f, size.height)
                        ),
                        cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx())
                    )
                }
        ) {
            Row(
                modifier          = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rocket icon on the left
                Box(
                    modifier         = Modifier.size(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Rounded.RocketLaunch,
                        contentDescription = null,
                        tint               = Color.White,
                        modifier           = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Button label
                Text(
                    text       = "Start Learning",
                    color      = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 24.sp,
                    modifier   = Modifier.weight(1f)
                )

                // Forward chevron on the right
                Icon(
                    imageVector        = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = null,
                    tint               = Color.White,
                    modifier           = Modifier.size(24.dp)
                )
            }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

/** Light theme preview of the splash screen. */
@Preview(name = "Splash Light", showBackground = true, showSystemUi = true)
@Composable
private fun SplashLightPreview() {
    StudentApplicationTheme(darkTheme = false, dynamicColor = false) {
        SplashScreen()
    }
}

/** Dark theme preview of the splash screen. */
@Preview(
    name            = "Splash Dark",
    showBackground  = true,
    showSystemUi    = true,
    uiMode          = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SplashDarkPreview() {
    StudentApplicationTheme(darkTheme = true, dynamicColor = false) {
        SplashScreen()
    }
}