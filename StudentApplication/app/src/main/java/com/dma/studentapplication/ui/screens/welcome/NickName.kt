package com.dma.studentapplication.ui.screens.welcome

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dma.studentapplication.ui.theme.StudentApplicationTheme

/**
 * Screen used to collect the user's nickname before entering the app.
 *
 * Supports:
 * - Light and dark theme
 * - Vertical scrolling for small screens
 * - Safe drawing padding
 * - Better centered card UI
 */
@Composable
fun NickName(
    onContinueClick: (String) -> Unit
) {
    val isDark = isSystemInDarkTheme()

    var nickname by rememberSaveable {
        mutableStateOf("")
    }

    val finalName = nickname.trim()

    val screenBgTop = if (isDark) Color(0xFF000B1B) else Color(0xFFF3F7FC)
    val screenBgBottom = if (isDark) Color(0xFF05162D) else Color(0xFFEAF2FB)

    val cardColor = if (isDark) Color(0xFF071833) else Color.White
    val titleColor = if (isDark) Color.White else Color(0xFF0B1B4A)
    val textMuted = if (isDark) Color(0xFFB8C4E0) else Color(0xFF5B6785)
    val borderColor = if (isDark) Color(0xFF243B5A) else Color(0xFFE0E7EF)

    val primaryGreen = Color(0xFF27D17F)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(screenBgTop, screenBgBottom)
                )
            )
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            top = 48.dp,
            bottom = 48.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = borderColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(78.dp)
                            .clip(CircleShape)
                            .background(primaryGreen.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = primaryGreen,
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = "What should we call you?",
                        color = titleColor,
                        fontSize = 28.sp,
                        lineHeight = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Enter your nickname to personalise your quiz experience.",
                        color = textMuted,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nickname") },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryGreen,
                            focusedLabelColor = primaryGreen,
                            cursorColor = primaryGreen
                        )
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = {
                            if (finalName.isNotEmpty()) {
                                onContinueClick(finalName)
                            }
                        },
                        enabled = finalName.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryGreen,
                            disabledContainerColor = if (isDark) {
                                Color(0xFF1A2A40)
                            } else {
                                Color(0xFFD5DBE5)
                            },
                            disabledContentColor = if (isDark) {
                                Color(0xFF7F8FA8)
                            } else {
                                Color(0xFF9CA3AF)
                            }
                        )
                    ) {
                        Text(
                            text = "Continue",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Nickname Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun NicknameScreenLightPreview() {
    StudentApplicationTheme {
        NickName(
            onContinueClick = {}
        )
    }
}

@Preview(
    name = "Nickname Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NicknameScreenDarkPreview() {
    StudentApplicationTheme {
        NickName(
            onContinueClick = {}
        )
    }
}