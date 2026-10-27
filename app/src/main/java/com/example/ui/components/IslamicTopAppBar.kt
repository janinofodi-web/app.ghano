package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamicTopAppBar(
  title: String,
  canNavigateBack: Boolean = false,
  isDarkMode: Boolean = false,
  onNavigateBack: () -> Unit = {},
  onToggleDarkMode: () -> Unit = {},
  onOpenSettings: () -> Unit = {}
) {
  TopAppBar(
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(IslamicGold)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = title,
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 19.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    },
    navigationIcon = {
      if (canNavigateBack) {
        IconButton(
          onClick = onNavigateBack,
          modifier = Modifier.testTag("top_bar_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "رجوع",
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }
    },
    actions = {
      IconButton(
        onClick = onToggleDarkMode,
        modifier = Modifier.testTag("toggle_dark_mode_button")
      ) {
        Icon(
          imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
          contentDescription = "تبديل المظهر",
          tint = IslamicGold
        )
      }
      IconButton(
        onClick = onOpenSettings,
        modifier = Modifier.testTag("open_settings_button")
      ) {
        Icon(
          imageVector = Icons.Default.Settings,
          contentDescription = "الإعدادات",
          tint = MaterialTheme.colorScheme.primary
        )
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surface,
      titleContentColor = MaterialTheme.colorScheme.onSurface
    )
  )
}
