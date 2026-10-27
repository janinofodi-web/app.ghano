package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.viewmodel.AppDestination

@Composable
fun NoorBottomNavigationBar(
  currentDestination: AppDestination,
  onDestinationSelected: (AppDestination) -> Unit
) {
  NavigationBar(
    windowInsets = WindowInsets.navigationBars,
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp
  ) {
    val items = listOf(
      Triple(AppDestination.HOME, Icons.Default.Home, Icons.Outlined.Home),
      Triple(AppDestination.PRAYER, Icons.Default.AccessTime, Icons.Outlined.AccessTime),
      Triple(AppDestination.QURAN, Icons.Default.MenuBook, Icons.Outlined.MenuBook),
      Triple(AppDestination.AZKAR, Icons.Default.Favorite, Icons.Outlined.FavoriteBorder),
      Triple(AppDestination.TASBIH, Icons.Default.TouchApp, Icons.Outlined.TouchApp),
      Triple(AppDestination.MORE, Icons.Default.GridView, Icons.Outlined.GridView)
    )

    items.forEach { (destination, selectedIcon, unselectedIcon) ->
      val isSelected = currentDestination == destination
      NavigationBarItem(
        selected = isSelected,
        onClick = { onDestinationSelected(destination) },
        icon = {
          Icon(
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = destination.titleArabic,
            modifier = Modifier.size(22.dp)
          )
        },
        label = {
          Text(
            text = destination.titleArabic,
            fontFamily = CairoFontFamily,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = MaterialTheme.colorScheme.onPrimary,
          selectedTextColor = MaterialTheme.colorScheme.primary,
          indicatorColor = MaterialTheme.colorScheme.primary,
          unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
          unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.testTag("nav_tab_${destination.name.lowercase()}")
      )
    }
  }
}
