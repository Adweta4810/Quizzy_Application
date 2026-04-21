package com.dma.studentapplication.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dma.studentapplication.model.RoboBuddyState
import kotlin.math.cos
import kotlin.math.sin

// ─────────────────────────────────────────────────────────────────────────────
// Colors
// ─────────────────────────────────────────────────────────────────────────────

private val ColorBody        = Color.White
private val ColorBodyShadow  = Color(0xFFE8E8E8)
private val ColorScreen      = Color(0xFF0D0D0D)
private val ColorTeal        = Color(0xFF3DD9C5)
private val ColorGreen       = Color(0xFF4CAF50)
private val ColorGreenBright = Color(0xFF00E676)
private val ColorRed         = Color(0xFFF44336)
private val ColorYellow      = Color(0xFFFFC107)
private val ColorGold        = Color(0xFFFFD700)
private val ColorPink        = Color(0xFFFF6B9D)
private val ColorOrange      = Color(0xFFFF7043)
private val ColorOutline     = Color(0xFF222222)

// ─────────────────────────────────────────────────────────────────────────────
// Main Composable
// ─────────────────────────────────────────────────────────────────────────────

/**
 * RoboBuddy — Jetpack Compose mascot for your quiz app.
 *
 * Example:
 *   RoboBuddy(state = RoboBuddyState. CORRECT, modifier = Modifier.size(160.dp))
 */
@Composable
fun RoboBuddy(
    state: RoboBuddyState = RoboBuddyState.HAPPY,
    modifier: Modifier = Modifier.size(160.dp)
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val u  = size.width / 10f

        drawGlowCircle(cx, cy, u)
        drawBody(cx, cy, u)
        drawHead(cx, cy, u)
        drawAntenna(cx, cy, u)
        drawEars(cx, cy, u)

        when (state) {
            RoboBuddyState.HAPPY      -> { drawFaceHappy(cx, cy, u);      drawArmIdle(cx, cy, u) }
            RoboBuddyState.EXCITED    -> { drawFaceExcited(cx, cy, u) }
            RoboBuddyState.LOVE       -> { drawFaceLove(cx, cy, u);       drawHearts(cx, cy, u) }
            RoboBuddyState.SURPRISED  -> { drawFaceSurprised(cx, cy, u) }
            RoboBuddyState.SAD        -> { drawFaceSad(cx, cy, u) }
            RoboBuddyState.ANGRY      -> { drawFaceAngry(cx, cy, u);      drawAngryLines(cx, cy, u) }
            RoboBuddyState.LAUGHING   -> { drawFaceLaughing(cx, cy, u) }
            RoboBuddyState.WINK       -> { drawFaceWink(cx, cy, u) }
            RoboBuddyState.WAVE       -> { drawFaceHappy(cx, cy, u);      drawArmWave(cx, cy, u) }
            RoboBuddyState.CELEBRATE  -> { drawFaceExcited(cx, cy, u);    drawArmsUp(cx, cy, u);   drawSparkles(cx, cy, u) }
            RoboBuddyState.THUMBS_UP  -> { drawFaceHappy(cx, cy, u);      drawThumbsUp(cx, cy, u) }
            RoboBuddyState.READING    -> { drawFaceReading(cx, cy, u);    drawBook(cx, cy, u) }
            RoboBuddyState.THINKING   -> { drawFaceThinking(cx, cy, u);   drawQuestionMark(cx, cy, u) }
            RoboBuddyState.CORRECT    -> { drawFaceHappy(cx, cy, u);      drawCheckBadge(cx, cy, u) }
            RoboBuddyState.INCORRECT  -> { drawFaceSad(cx, cy, u);        drawCrossBadge(cx, cy, u) }
            RoboBuddyState.STUDYING   -> { drawFaceNeutral(cx, cy, u);    drawLaptop(cx, cy, u) }
            RoboBuddyState.WRITING    -> { drawFaceNeutral(cx, cy, u);    drawPencil(cx, cy, u) }
            RoboBuddyState.TROPHY     -> { drawFaceHappy(cx, cy, u);      drawTrophy(cx, cy, u) }
            RoboBuddyState.IDEA       -> { drawFaceHappy(cx, cy, u);      drawLightbulb(cx, cy, u) }
            RoboBuddyState.PROGRESS   -> { drawFaceHappy(cx, cy, u);      drawBarChart(cx, cy, u) }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Labelled wrapper used in grid previews
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun RoboBuddyCard(state: RoboBuddyState, size: Dp = 90.dp) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RoboBuddy(state = state, modifier = Modifier.size(size))
        Text(
            text  = state.name,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ── @Preview — individual states ─────────────────────────────────────────────
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Happy",      showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewHappy()     { RoboBuddyCard(RoboBuddyState.HAPPY,     size = 140.dp) }
@Preview(name = "Excited",    showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewExcited()   { RoboBuddyCard(RoboBuddyState.EXCITED,   size = 140.dp) }
@Preview(name = "Love",       showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewLove()      { RoboBuddyCard(RoboBuddyState.LOVE,      size = 140.dp) }
@Preview(name = "Surprised",  showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewSurprised() { RoboBuddyCard(RoboBuddyState.SURPRISED, size = 140.dp) }
@Preview(name = "Sad",        showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewSad()       { RoboBuddyCard(RoboBuddyState.SAD,       size = 140.dp) }
@Preview(name = "Angry",      showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewAngry()     { RoboBuddyCard(RoboBuddyState.ANGRY,     size = 140.dp) }
@Preview(name = "Laughing",   showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewLaughing()  { RoboBuddyCard(RoboBuddyState.LAUGHING,  size = 140.dp) }
@Preview(name = "Wink",       showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewWink()      { RoboBuddyCard(RoboBuddyState.WINK,      size = 140.dp) }
@Preview(name = "Wave",       showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewWave()      { RoboBuddyCard(RoboBuddyState.WAVE,      size = 140.dp) }
@Preview(name = "Celebrate",  showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewCelebrate() { RoboBuddyCard(RoboBuddyState.CELEBRATE, size = 140.dp) }
@Preview(name = "Thumbs Up",  showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewThumbsUp()  { RoboBuddyCard(RoboBuddyState.THUMBS_UP, size = 140.dp) }
@Preview(name = "Reading",    showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewReading()   { RoboBuddyCard(RoboBuddyState.READING,   size = 140.dp) }
@Preview(name = "Thinking",   showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewThinking()  { RoboBuddyCard(RoboBuddyState.THINKING,  size = 140.dp) }
@Preview(name = "Correct",    showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewCorrect()   { RoboBuddyCard(RoboBuddyState.CORRECT,   size = 140.dp) }
@Preview(name = "Incorrect",  showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewIncorrect() { RoboBuddyCard(RoboBuddyState.INCORRECT, size = 140.dp) }
@Preview(name = "Studying",   showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewStudying()  { RoboBuddyCard(RoboBuddyState.STUDYING,  size = 140.dp) }
@Preview(name = "Writing",    showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewWriting()   { RoboBuddyCard(RoboBuddyState.WRITING,   size = 140.dp) }
@Preview(name = "Trophy",     showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewTrophy()    { RoboBuddyCard(RoboBuddyState.TROPHY,    size = 140.dp) }
@Preview(name = "Idea",       showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewIdea()      { RoboBuddyCard(RoboBuddyState.IDEA,      size = 140.dp) }
@Preview(name = "Progress",   showBackground = true, widthDp = 160, heightDp = 180) @Composable fun PreviewProgress()  { RoboBuddyCard(RoboBuddyState.PROGRESS,  size = 140.dp) }

// ─────────────────────────────────────────────────────────────────────────────
// ── @Preview — all states grid ───────────────────────────────────────────────
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "All States Grid", showBackground = true, widthDp = 400, heightDp = 700)
@Composable
fun PreviewAllStates() {
    Surface {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement   = Arrangement.spacedBy(8.dp)
        ) {
            items(RoboBuddyState.values().toList()) { state ->
                RoboBuddyCard(state = state, size = 80.dp)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ── @Preview — quiz states only ──────────────────────────────────────────────
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Quiz States", showBackground = true, widthDp = 400, heightDp = 200)
@Composable
fun PreviewQuizStates() {
    Surface {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            listOf(
                RoboBuddyState.THINKING,
                RoboBuddyState.CORRECT,
                RoboBuddyState.INCORRECT,
                RoboBuddyState.TROPHY
            ).forEach { RoboBuddyCard(it, size = 80.dp) }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ── Drawing helpers (DrawScope extensions) ───────────────────────────────────
// ─────────────────────────────────────────────────────────────────────────────

// ── Glow ──────────────────────────────────────────────────────────────────────
private fun DrawScope.drawGlowCircle(cx: Float, cy: Float, u: Float) {
    drawCircle(
        brush  = Brush.radialGradient(
            colors  = listOf(Color(0x20C8F5E8), Color.Transparent),
            center  = Offset(cx, cy + u * 0.5f),
            radius  = u * 4.2f
        ),
        radius = u * 4.2f,
        center = Offset(cx, cy + u * 0.5f)
    )
}

// ── Body ──────────────────────────────────────────────────────────────────────
private fun DrawScope.drawBody(cx: Float, cy: Float, u: Float) {
    val bodyTL = Offset(cx - u * 2.2f, cy + u * 0.8f)
    val bodySize = Size(u * 4.4f, u * 2.7f)
    val r = u * 0.8f
    // shadow
    drawRoundRect(ColorBodyShadow, topLeft = bodyTL + Offset(u * 0.1f, u * 0.15f), size = bodySize, cornerRadius = CornerRadius(r))
    drawRoundRect(ColorBody,       topLeft = bodyTL, size = bodySize, cornerRadius = CornerRadius(r))
    drawRoundRect(ColorOutline,    topLeft = bodyTL, size = bodySize, cornerRadius = CornerRadius(r), style = Stroke(width = u * 0.12f))
    // chest panel
    drawRoundRect(ColorScreen, topLeft = Offset(cx - u * 1.1f, cy + u * 1.3f), size = Size(u * 2.2f, u * 1.2f), cornerRadius = CornerRadius(u * 0.3f))
    // indicator
    drawCircle(ColorGreen, radius = u * 0.25f, center = Offset(cx, cy + u * 1.9f))
}

// ── Head ──────────────────────────────────────────────────────────────────────
private fun DrawScope.drawHead(cx: Float, cy: Float, u: Float) {
    val tl   = Offset(cx - u * 2.5f, cy - u * 2.8f)
    val sz   = Size(u * 5f, u * 3.8f)
    val r    = CornerRadius(u * 1.2f)
    drawRoundRect(ColorBodyShadow, topLeft = tl + Offset(u * 0.12f, u * 0.15f), size = sz, cornerRadius = r)
    drawRoundRect(ColorBody,       topLeft = tl, size = sz, cornerRadius = r)
    drawRoundRect(ColorOutline,    topLeft = tl, size = sz, cornerRadius = r, style = Stroke(width = u * 0.13f))
    // face screen
    drawRoundRect(ColorScreen, topLeft = Offset(cx - u * 1.9f, cy - u * 2.2f), size = Size(u * 3.8f, u * 2.7f), cornerRadius = CornerRadius(u * 0.8f))
}

// ── Ears ──────────────────────────────────────────────────────────────────────
private fun DrawScope.drawEars(cx: Float, cy: Float, u: Float) {
    val cr = CornerRadius(u * 0.3f)
    val sw = Stroke(u * 0.1f)
    drawRoundRect(ColorTeal,    topLeft = Offset(cx - u * 3.2f, cy - u * 1.5f), size = Size(u * 0.9f, u * 1.2f), cornerRadius = cr)
    drawRoundRect(ColorOutline, topLeft = Offset(cx - u * 3.2f, cy - u * 1.5f), size = Size(u * 0.9f, u * 1.2f), cornerRadius = cr, style = sw)
    drawRoundRect(ColorTeal,    topLeft = Offset(cx + u * 2.3f, cy - u * 1.5f), size = Size(u * 0.9f, u * 1.2f), cornerRadius = cr)
    drawRoundRect(ColorOutline, topLeft = Offset(cx + u * 2.3f, cy - u * 1.5f), size = Size(u * 0.9f, u * 1.2f), cornerRadius = cr, style = sw)
}

// ── Antenna ───────────────────────────────────────────────────────────────────
private fun DrawScope.drawAntenna(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, start = Offset(cx, cy - u * 2.8f), end = Offset(cx, cy - u * 4.0f), strokeWidth = u * 0.18f, cap = StrokeCap.Round)
    drawCircle(ColorGreen,   radius = u * 0.38f, center = Offset(cx, cy - u * 4.2f))
    drawCircle(ColorOutline, radius = u * 0.38f, center = Offset(cx, cy - u * 4.2f), style = Stroke(u * 0.1f))
}

// ── Stroke helper ─────────────────────────────────────────────────────────────
private fun arcPath(cx: Float, cy: Float, r: Float, startDeg: Float, sweepDeg: Float): Path {
    return Path().apply {
        addArc(
            oval      = Rect(cx - r, cy - r, cx + r, cy + r),
            startAngleDegrees = startDeg,
            sweepAngleDegrees = sweepDeg
        )
    }
}

// ── Face expressions ──────────────────────────────────────────────────────────
private fun DrawScope.drawFaceHappy(cx: Float, cy: Float, u: Float) {
    val eyeStroke = Stroke(width = u * 0.25f, cap = StrokeCap.Round)
    val ey = cy - u * 1.3f
    drawPath(arcPath(cx - u * 0.75f, ey, u * 0.35f, 180f, 180f), ColorTeal, style = eyeStroke)
    drawPath(arcPath(cx + u * 0.75f, ey, u * 0.35f, 180f, 180f), ColorTeal, style = eyeStroke)
    drawPath(arcPath(cx, cy + u * 0.0f, u * 0.8f, 0f, 180f), ColorTeal, style = Stroke(width = u * 0.22f, cap = StrokeCap.Round))
}

private fun DrawScope.drawFaceExcited(cx: Float, cy: Float, u: Float) {
    val ey = cy - u * 1.3f
    drawStar(cx - u * 0.75f, ey, u * 0.4f, ColorYellow)
    drawStar(cx + u * 0.75f, ey, u * 0.4f, ColorYellow)
    drawPath(arcPath(cx, cy + u * 0.1f, u * 0.9f, 0f, 180f), ColorTeal, style = Stroke(width = u * 0.22f, cap = StrokeCap.Round))
}

private fun DrawScope.drawFaceLove(cx: Float, cy: Float, u: Float) {
    val ey = cy - u * 1.3f
    drawHeart(cx - u * 0.75f, ey, u * 0.35f, ColorPink)
    drawHeart(cx + u * 0.75f, ey, u * 0.35f, ColorPink)
    drawPath(arcPath(cx, cy + u * 0.0f, u * 0.75f, 0f, 180f), ColorPink, style = Stroke(width = u * 0.22f, cap = StrokeCap.Round))
}

private fun DrawScope.drawFaceSurprised(cx: Float, cy: Float, u: Float) {
    val ey = cy - u * 1.3f
    drawCircle(ColorTeal, radius = u * 0.38f, center = Offset(cx - u * 0.75f, ey))
    drawCircle(ColorTeal, radius = u * 0.38f, center = Offset(cx + u * 0.75f, ey))
    drawOval(ColorTeal, topLeft = Offset(cx - u * 0.32f, cy - u * 0.2f), size = Size(u * 0.64f, u * 0.75f))
}

private fun DrawScope.drawFaceSad(cx: Float, cy: Float, u: Float) {
    val sw = Stroke(width = u * 0.25f, cap = StrokeCap.Round)
    val ey = cy - u * 1.3f
    drawPath(arcPath(cx - u * 0.75f, ey, u * 0.35f, 0f, 180f), ColorTeal, style = sw)
    drawPath(arcPath(cx + u * 0.75f, ey, u * 0.35f, 0f, 180f), ColorTeal, style = sw)
    drawPath(arcPath(cx, cy + u * 0.5f, u * 0.8f, 180f, 180f), ColorTeal, style = Stroke(width = u * 0.22f, cap = StrokeCap.Round))
}

private fun DrawScope.drawFaceAngry(cx: Float, cy: Float, u: Float) {
    val ey = cy - u * 1.3f
    drawRect(ColorTeal, topLeft = Offset(cx - u * 1.2f, ey - u * 0.18f), size = Size(u * 0.9f, u * 0.36f))
    drawRect(ColorTeal, topLeft = Offset(cx + u * 0.3f,  ey - u * 0.18f), size = Size(u * 0.9f, u * 0.36f))
    val brow = Stroke(width = u * 0.22f, cap = StrokeCap.Round)
    drawLine(ColorTeal, Offset(cx - u * 1.2f, ey - u * 0.5f), Offset(cx - u * 0.3f, ey - u * 0.2f), strokeWidth = u * 0.22f, cap = StrokeCap.Round)
    drawLine(ColorTeal, Offset(cx + u * 0.3f,  ey - u * 0.2f), Offset(cx + u * 1.2f, ey - u * 0.5f), strokeWidth = u * 0.22f, cap = StrokeCap.Round)
    drawPath(arcPath(cx, cy + u * 0.4f, u * 0.7f, 180f, 180f), ColorTeal, style = Stroke(width = u * 0.22f, cap = StrokeCap.Round))
}

private fun DrawScope.drawFaceLaughing(cx: Float, cy: Float, u: Float) {
    val ey = cy - u * 1.3f
    val sw = Stroke(width = u * 0.25f, cap = StrokeCap.Round)
    drawPath(arcPath(cx - u * 0.75f, ey, u * 0.35f, 180f, 180f), ColorTeal, style = sw)
    drawPath(arcPath(cx + u * 0.75f, ey, u * 0.35f, 180f, 180f), ColorTeal, style = sw)
    // filled open mouth
    val mouthPath = Path().apply { addArc(Rect(cx - u * 1.0f, cy - u * 0.5f, cx + u * 1.0f, cy + u * 0.7f), 0f, 180f) }
    drawPath(mouthPath, ColorTeal)
    drawPath(mouthPath, ColorOutline, style = Stroke(u * 0.1f))
}

private fun DrawScope.drawFaceWink(cx: Float, cy: Float, u: Float) {
    val ey = cy - u * 1.3f
    val sw = Stroke(width = u * 0.25f, cap = StrokeCap.Round)
    drawPath(arcPath(cx - u * 0.75f, ey, u * 0.35f, 180f, 180f), ColorTeal, style = sw)
    // X eye
    drawLine(ColorTeal, Offset(cx + u * 0.35f, ey - u * 0.3f), Offset(cx + u * 1.1f, ey + u * 0.3f), u * 0.25f, cap = StrokeCap.Round)
    drawLine(ColorTeal, Offset(cx + u * 1.1f,  ey - u * 0.3f), Offset(cx + u * 0.35f, ey + u * 0.3f), u * 0.25f, cap = StrokeCap.Round)
    drawPath(arcPath(cx, cy + u * 0.0f, u * 0.8f, 0f, 180f), ColorTeal, style = Stroke(width = u * 0.22f, cap = StrokeCap.Round))
}

private fun DrawScope.drawFaceNeutral(cx: Float, cy: Float, u: Float) {
    val ey = cy - u * 1.3f
    drawCircle(ColorTeal, u * 0.28f, Offset(cx - u * 0.75f, ey))
    drawCircle(ColorTeal, u * 0.28f, Offset(cx + u * 0.75f, ey))
    drawLine(ColorTeal, Offset(cx - u * 0.6f, cy + u * 0.1f), Offset(cx + u * 0.6f, cy + u * 0.1f), u * 0.2f, cap = StrokeCap.Round)
}

private fun DrawScope.drawFaceReading(cx: Float, cy: Float, u: Float) {
    val ey = cy - u * 1.3f
    drawCircle(ColorTeal, u * 0.28f, Offset(cx - u * 0.75f, ey))
    drawCircle(ColorTeal, u * 0.28f, Offset(cx + u * 0.75f, ey))
    drawPath(arcPath(cx, cy + u * 0.05f, u * 0.6f, 0f, 180f), ColorTeal, style = Stroke(width = u * 0.2f, cap = StrokeCap.Round))
}

private fun DrawScope.drawFaceThinking(cx: Float, cy: Float, u: Float) {
    val ey = cy - u * 1.3f
    drawCircle(ColorTeal, u * 0.28f, Offset(cx - u * 0.75f, ey))
    drawCircle(ColorTeal, u * 0.28f, Offset(cx + u * 0.75f, ey))
    val p = Path().apply {
        moveTo(cx - u * 0.7f, cy + u * 0.1f)
        quadraticBezierTo(cx - u * 0.35f, cy - u * 0.25f, cx, cy + u * 0.1f)
        quadraticBezierTo(cx + u * 0.35f, cy + u * 0.45f, cx + u * 0.7f, cy + u * 0.1f)
    }
    drawPath(p, ColorTeal, style = Stroke(width = u * 0.2f, cap = StrokeCap.Round))
}

// ── Accessories ───────────────────────────────────────────────────────────────
private fun DrawScope.drawArmIdle(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, Offset(cx + u * 2.2f, cy + u * 1.2f), Offset(cx + u * 3.2f, cy + u * 2.0f), u * 0.35f, cap = StrokeCap.Round)
    drawGlove(cx + u * 3.2f, cy + u * 2.0f, u)
}

private fun DrawScope.drawArmWave(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, Offset(cx - u * 2.2f, cy + u * 1.2f), Offset(cx - u * 3.5f, cy - u * 0.5f), u * 0.35f, cap = StrokeCap.Round)
    drawGlove(cx - u * 3.5f, cy - u * 0.5f, u)
}

private fun DrawScope.drawArmsUp(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, Offset(cx - u * 2.2f, cy + u * 1.2f), Offset(cx - u * 3.5f, cy - u * 1.5f), u * 0.35f, cap = StrokeCap.Round)
    drawLine(ColorOutline, Offset(cx + u * 2.2f, cy + u * 1.2f), Offset(cx + u * 3.5f, cy - u * 1.5f), u * 0.35f, cap = StrokeCap.Round)
    drawGlove(cx - u * 3.5f, cy - u * 1.5f, u)
    drawGlove(cx + u * 3.5f, cy - u * 1.5f, u)
}

private fun DrawScope.drawThumbsUp(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, Offset(cx + u * 2.2f, cy + u * 1.5f), Offset(cx + u * 3.4f, cy + u * 0.8f), u * 0.35f, cap = StrokeCap.Round)
    drawCircle(ColorBody,    u * 0.5f, Offset(cx + u * 3.6f, cy + u * 0.5f))
    drawCircle(ColorOutline, u * 0.5f, Offset(cx + u * 3.6f, cy + u * 0.5f), style = Stroke(u * 0.1f))
    drawLine(ColorOutline, Offset(cx + u * 3.6f, cy + u * 0.5f), Offset(cx + u * 3.6f, cy - u * 0.45f), u * 0.22f, cap = StrokeCap.Round)
}

private fun DrawScope.drawBook(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, Offset(cx - u * 2.2f, cy + u * 1.5f), Offset(cx - u * 3.0f, cy + u * 2.5f), u * 0.35f, cap = StrokeCap.Round)
    val cr = CornerRadius(u * 0.2f)
    drawRoundRect(ColorGreen,   topLeft = Offset(cx - u * 3.8f, cy + u * 2.0f), size = Size(u * 2.3f, u * 1.6f), cornerRadius = cr)
    drawRoundRect(ColorOutline, topLeft = Offset(cx - u * 3.8f, cy + u * 2.0f), size = Size(u * 2.3f, u * 1.6f), cornerRadius = cr, style = Stroke(u * 0.1f))
    val midX = cx - u * 2.65f
    drawLine(Color.White, Offset(midX, cy + u * 2.2f), Offset(midX, cy + u * 3.45f), u * 0.1f)
}

private fun DrawScope.drawQuestionMark(cx: Float, cy: Float, u: Float) {
    // drawn as text-like path using two arcs + dot
    drawPath(arcPath(cx + u * 2.5f, cy - u * 2.5f, u * 0.55f, 200f, 250f), ColorGreen, style = Stroke(u * 0.28f, cap = StrokeCap.Round))
    drawPath(arcPath(cx + u * 2.85f, cy - u * 1.8f, u * 0.3f, 270f, 160f), ColorGreen, style = Stroke(u * 0.28f, cap = StrokeCap.Round))
    drawCircle(ColorGreen, u * 0.16f, Offset(cx + u * 2.85f, cy - u * 1.2f))
}

private fun DrawScope.drawCheckBadge(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, Offset(cx + u * 2.2f, cy + u * 1.5f), Offset(cx + u * 3.2f, cy + u * 2.5f), u * 0.3f, cap = StrokeCap.Round)
    drawCircle(ColorGreen,   u * 0.9f, Offset(cx + u * 3.5f, cy + u * 2.8f))
    drawCircle(ColorOutline, u * 0.9f, Offset(cx + u * 3.5f, cy + u * 2.8f), style = Stroke(u * 0.08f))
    val tick = Path().apply {
        moveTo(cx + u * 3.1f, cy + u * 2.8f)
        lineTo(cx + u * 3.4f, cy + u * 3.15f)
        lineTo(cx + u * 3.95f, cy + u * 2.5f)
    }
    drawPath(tick, Color.White, style = Stroke(u * 0.2f, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.drawCrossBadge(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, Offset(cx + u * 2.2f, cy + u * 1.5f), Offset(cx + u * 3.2f, cy + u * 2.5f), u * 0.3f, cap = StrokeCap.Round)
    drawCircle(ColorRed,     u * 0.9f, Offset(cx + u * 3.5f, cy + u * 2.8f))
    drawCircle(ColorOutline, u * 0.9f, Offset(cx + u * 3.5f, cy + u * 2.8f), style = Stroke(u * 0.08f))
    drawLine(Color.White, Offset(cx + u * 3.1f, cy + u * 2.45f), Offset(cx + u * 3.9f, cy + u * 3.15f), u * 0.2f, cap = StrokeCap.Round)
    drawLine(Color.White, Offset(cx + u * 3.9f, cy + u * 2.45f), Offset(cx + u * 3.1f, cy + u * 3.15f), u * 0.2f, cap = StrokeCap.Round)
}

private fun DrawScope.drawLaptop(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, Offset(cx - u * 2.2f, cy + u * 1.5f), Offset(cx - u * 2.8f, cy + u * 2.8f), u * 0.3f, cap = StrokeCap.Round)
    val cr = CornerRadius(u * 0.25f)
    drawRoundRect(ColorTeal,    topLeft = Offset(cx - u * 3.8f, cy + u * 1.5f), size = Size(u * 3.0f, u * 1.5f), cornerRadius = cr)
    drawRoundRect(ColorOutline, topLeft = Offset(cx - u * 3.8f, cy + u * 1.5f), size = Size(u * 3.0f, u * 1.5f), cornerRadius = cr, style = Stroke(u * 0.1f))
    val cr2 = CornerRadius(u * 0.1f)
    drawRoundRect(ColorTeal,    topLeft = Offset(cx - u * 4.0f, cy + u * 2.95f), size = Size(u * 3.5f, u * 0.35f), cornerRadius = cr2)
    drawRoundRect(ColorOutline, topLeft = Offset(cx - u * 4.0f, cy + u * 2.95f), size = Size(u * 3.5f, u * 0.35f), cornerRadius = cr2, style = Stroke(u * 0.1f))
}

private fun DrawScope.drawPencil(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, Offset(cx + u * 2.2f, cy + u * 1.5f), Offset(cx + u * 3.0f, cy + u * 2.5f), u * 0.3f, cap = StrokeCap.Round)
    withTransform({
        rotate(-45f, pivot = Offset(cx + u * 3.5f, cy + u * 3.0f))
    }) {
        drawRect(ColorYellow, topLeft = Offset(cx + u * 3.0f, cy + u * 1.5f), size = Size(u * 1.0f, u * 2.0f))
        drawRect(ColorOutline, topLeft = Offset(cx + u * 3.0f, cy + u * 1.5f), size = Size(u * 1.0f, u * 2.0f), style = Stroke(u * 0.1f))
        val tip = Path().apply {
            moveTo(cx + u * 3.0f, cy + u * 3.5f)
            lineTo(cx + u * 4.0f, cy + u * 3.5f)
            lineTo(cx + u * 3.5f, cy + u * 4.1f)
            close()
        }
        drawPath(tip, ColorOrange)
    }
}

private fun DrawScope.drawTrophy(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, Offset(cx - u * 2.2f, cy + u * 1.5f), Offset(cx - u * 2.5f, cy + u * 2.5f), u * 0.3f, cap = StrokeCap.Round)
    val cup = Path().apply {
        moveTo(cx - u * 3.8f, cy + u * 1.5f)
        lineTo(cx - u * 1.8f, cy + u * 1.5f)
        quadraticBezierTo(cx - u * 1.8f, cy + u * 3.0f, cx - u * 2.8f, cy + u * 3.0f)
        quadraticBezierTo(cx - u * 3.8f, cy + u * 3.0f, cx - u * 3.8f, cy + u * 1.5f)
        close()
    }
    drawPath(cup, ColorGold)
    drawPath(cup, ColorOutline, style = Stroke(u * 0.1f))
    drawPath(arcPath(cx - u * 3.8f, cy + u * 2.2f, u * 0.4f, 90f, 180f),  ColorOutline, style = Stroke(u * 0.1f, cap = StrokeCap.Round))
    drawPath(arcPath(cx - u * 1.8f, cy + u * 2.2f, u * 0.4f, 270f, 180f), ColorOutline, style = Stroke(u * 0.1f, cap = StrokeCap.Round))
}

private fun DrawScope.drawLightbulb(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, Offset(cx + u * 2.2f, cy + u * 1.5f), Offset(cx + u * 2.8f, cy + u * 0.5f), u * 0.3f, cap = StrokeCap.Round)
    drawCircle(ColorYellow,  u * 0.8f, Offset(cx + u * 3.2f, cy - u * 0.2f))
    drawCircle(ColorOutline, u * 0.8f, Offset(cx + u * 3.2f, cy - u * 0.2f), style = Stroke(u * 0.1f))
    drawRect(ColorYellow, topLeft = Offset(cx + u * 2.85f, cy + u * 0.55f), size = Size(u * 0.7f, u * 0.4f))
    drawRect(ColorOutline, topLeft = Offset(cx + u * 2.85f, cy + u * 0.55f), size = Size(u * 0.7f, u * 0.4f), style = Stroke(u * 0.08f))
    for (i in 0 until 6) {
        val a  = Math.toRadians((i * 60).toDouble())
        drawLine(ColorYellow,
            start = Offset(cx + u * 3.2f + cos(a).toFloat() * u * 1.0f, cy - u * 0.2f + sin(a).toFloat() * u * 1.0f),
            end   = Offset(cx + u * 3.2f + cos(a).toFloat() * u * 1.35f, cy - u * 0.2f + sin(a).toFloat() * u * 1.35f),
            strokeWidth = u * 0.15f, cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawBarChart(cx: Float, cy: Float, u: Float) {
    drawLine(ColorOutline, Offset(cx + u * 2.2f, cy + u * 1.5f), Offset(cx + u * 3.0f, cy + u * 2.2f), u * 0.3f, cap = StrokeCap.Round)
    val barColors = listOf(ColorGreen, ColorTeal, ColorGreenBright)
    val heights   = listOf(u * 0.8f, u * 1.3f, u * 1.8f)
    val baseY     = cy + u * 3.8f
    for (i in 0..2) {
        val l = cx + u * (2.8f + i * 0.8f)
        drawRect(barColors[i], topLeft = Offset(l, baseY - heights[i]), size = Size(u * 0.6f, heights[i]))
        drawRect(ColorOutline,  topLeft = Offset(l, baseY - heights[i]), size = Size(u * 0.6f, heights[i]), style = Stroke(u * 0.08f))
    }
    val arrow = Path().apply {
        moveTo(cx + u * 5.0f, cy + u * 1.5f); lineTo(cx + u * 4.5f, cy + u * 2.3f)
        lineTo(cx + u * 4.8f, cy + u * 2.3f); lineTo(cx + u * 4.8f, cy + u * 3.3f)
        lineTo(cx + u * 5.2f, cy + u * 3.3f); lineTo(cx + u * 5.2f, cy + u * 2.3f)
        lineTo(cx + u * 5.5f, cy + u * 2.3f); close()
    }
    drawPath(arrow, ColorGreen)
}

private fun DrawScope.drawHearts(cx: Float, cy: Float, u: Float) {
    drawHeart(cx + u * 2.5f, cy - u * 2.5f, u * 0.5f, ColorPink)
    drawHeart(cx + u * 1.5f, cy - u * 3.5f, u * 0.32f, ColorPink)
}

private fun DrawScope.drawSparkles(cx: Float, cy: Float, u: Float) {
    drawStar(cx - u * 3.2f, cy - u * 1.5f, u * 0.4f, ColorYellow)
    drawStar(cx + u * 3.2f, cy - u * 3.0f, u * 0.3f, ColorYellow)
    drawCircle(ColorTeal, u * 0.15f, Offset(cx + u * 2.8f, cy - u * 1.2f))
    drawCircle(ColorTeal, u * 0.12f, Offset(cx - u * 2.5f, cy - u * 3.2f))
}

private fun DrawScope.drawAngryLines(cx: Float, cy: Float, u: Float) {
    drawLine(ColorRed, Offset(cx + u * 2.0f, cy - u * 2.5f), Offset(cx + u * 3.0f, cy - u * 1.5f), u * 0.2f, cap = StrokeCap.Round)
    drawLine(ColorRed, Offset(cx + u * 3.0f, cy - u * 2.5f), Offset(cx + u * 2.0f, cy - u * 1.5f), u * 0.2f, cap = StrokeCap.Round)
}

private fun DrawScope.drawGlove(x: Float, y: Float, u: Float) {
    drawCircle(ColorBody,    u * 0.5f, Offset(x, y))
    drawCircle(ColorOutline, u * 0.5f, Offset(x, y), style = Stroke(u * 0.12f))
}

// ── Shape helpers ─────────────────────────────────────────────────────────────
private fun DrawScope.drawStar(x: Float, y: Float, r: Float, color: Color) {
    val innerR = r * 0.45f
    val path = Path()
    for (i in 0 until 10) {
        val angle  = Math.toRadians((i * 36 - 90).toDouble())
        val radius = if (i % 2 == 0) r else innerR
        val px = x + cos(angle).toFloat() * radius
        val py = y + sin(angle).toFloat() * radius
        if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
    }
    path.close()
    drawPath(path, color)
}

private fun DrawScope.drawHeart(x: Float, y: Float, r: Float, color: Color) {
    val path = Path().apply {
        moveTo(x, y + r * 0.4f)
        cubicTo(x, y - r * 0.5f, x - r * 1.3f, y - r * 0.5f, x - r, y + r * 0.3f)
        cubicTo(x - r * 0.5f, y + r * 1.0f, x, y + r * 1.3f, x, y + r * 1.5f)
        cubicTo(x, y + r * 1.3f, x + r * 0.5f, y + r * 1.0f, x + r, y + r * 0.3f)
        cubicTo(x + r * 1.3f, y - r * 0.5f, x, y - r * 0.5f, x, y + r * 0.4f)
        close()
    }
    drawPath(path, color)
}
