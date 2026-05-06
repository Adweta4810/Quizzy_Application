package com.dma.studentapplication.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// ── Theme colors ──────────────────────────────────────────────────────────────

private val PrimaryGreen = Color(0xFF27D17F)
private val LightTextMuted = Color(0xFF5B6785)
private val DarkTextMuted = Color(0xFF8FA3C8)
private val LightNavBar = Color.White
private val DarkNavBar = Color(0xFF041225)

// ── Data model ────────────────────────────────────────────────────────────────

/**
 * Represents a single item in the bottom navigation bar.
 *
 * @param key      Unique identifier used to determine which item is currently selected.
 * @param label    Display label shown below the icon.
 * @param icon     Icon shown for this nav item.
 * @param onClick  Callback triggered when the user taps this item.
 */


data class BottomNavItem(
    val key: String,
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

// ── Composable ────────────────────────────────────────────────────────────────

/**
 * Shared bottom navigation bar used across all main screens.
 *
 * Highlights the active tab using [current] to match against each item's key.
 * Automatically adapts colors based on the system dark/light theme.
 *
 * @param current        The key of the currently active screen (e.g. "home", "topics").
 * @param onHomeClick    Called when the Home tab is tapped.
 * @param onTopicsClick  Called when the Topics tab is tapped.
 * @param onHistoryClick Called when the History tab is tapped.
 * @param onProfileClick Called when the Profile tab is tapped.
 * @param isDark         Whether dark mode is active. Defaults to system setting.
 */
@Composable
fun AppBottomNavBar(
    current: String,
    onHomeClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit,
    isDark: Boolean = isSystemInDarkTheme()
) {
    // Resolve colors based on current theme
    val navBarColor = if (isDark) DarkNavBar else LightNavBar
    val mutedColor = if (isDark) DarkTextMuted else LightTextMuted

    // Define all nav items with their keys, labels, icons and click handlers
    val items = listOf(
        BottomNavItem("home", "Home", Icons.Default.Home, onHomeClick),
        BottomNavItem("topics", "Topics", Icons.Default.GridView, onTopicsClick),
        BottomNavItem("history", "History", Icons.Default.History, onHistoryClick),
        BottomNavItem("profile", "Profile", Icons.Default.Person, onProfileClick)
    )

    NavigationBar(containerColor = navBarColor, tonalElevation = 0.dp) // Flat surface, no elevation tint
    {
        items.forEach { item ->
            // Item is selected if its key matches the current active screen
            val selected = item.key == current
            NavigationBarItem(
                selected = selected,
                onClick = item.onClick,
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryGreen,    // Active icon color
                    selectedTextColor = PrimaryGreen,    // Active label color
                    unselectedIconColor = mutedColor,    // Inactive icon color
                    unselectedTextColor = mutedColor,    // Inactive label color
                    indicatorColor = Color.Transparent   // No pill/highlight behind icon
                )
            )
        }
    }
}
