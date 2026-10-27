package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = IslamicGold,
  onPrimary = EmeraldDark,
  primaryContainer = EmeraldPrimary,
  onPrimaryContainer = IslamicGoldLight,
  secondary = EmeraldAccent,
  onSecondary = Color.White,
  secondaryContainer = EmeraldCardDark,
  onSecondaryContainer = IslamicGoldLight,
  tertiary = IslamicGoldLight,
  onTertiary = EmeraldDark,
  background = EmeraldDark,
  onBackground = TextPrimaryDark,
  surface = EmeraldSurfaceDark,
  onSurface = TextPrimaryDark,
  surfaceVariant = EmeraldCardDark,
  onSurfaceVariant = TextSecondaryDark,
  outline = EmeraldBorderDark,
  outlineVariant = EmeraldCardDark
)

private val LightColorScheme = lightColorScheme(
  primary = EmeraldPrimary,
  onPrimary = Color.White,
  primaryContainer = GoldContainerLight,
  onPrimaryContainer = IslamicGoldDark,
  secondary = IslamicGoldDark,
  onSecondary = Color.White,
  secondaryContainer = CreamCard,
  onSecondaryContainer = EmeraldPrimary,
  tertiary = EmeraldAccent,
  onTertiary = Color.White,
  background = CreamBackground,
  onBackground = TextPrimaryLight,
  surface = CreamSurface,
  onSurface = TextPrimaryLight,
  surfaceVariant = CreamCard,
  onSurfaceVariant = TextSecondaryLight,
  outline = CreamBorder,
  outlineVariant = CreamBorder
)

@Composable
fun NoorAlIslamTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}
