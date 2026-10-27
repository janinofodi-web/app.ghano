package com.example.ui.screens.prayer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.viewmodel.AppViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QiblaCompassScreen(viewModel: AppViewModel) {
  val compassAzimuth by viewModel.compassAzimuth.collectAsState()
  val qiblaBearing = viewModel.qiblaBearing.toFloat()
  val distanceKm = viewModel.distanceToMakkahKm
  val city by viewModel.prefs.selectedCity.collectAsState()

  // Calculate rotation difference so Kaaba pointer aligns correctly
  val targetRotation = qiblaBearing - compassAzimuth
  val animatedRotation by animateFloatAsState(
    targetValue = targetRotation,
    animationSpec = spring(),
    label = "compass_rotation"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .testTag("qibla_compass_screen"),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // City & Bearing Header Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "بوصلة القبلة المشرفة",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "${city.nameArabic} • المسافة إلى الكعبة: ${String.format("%,d", distanceKm)} كم",
          fontFamily = CairoFontFamily,
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "زاوية القبلة: ${qiblaBearing.toInt()}° من اتجاه الشمال",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = IslamicGold
        )
      }
    }

    // Interactive 360° Compass Dial Canvas
    Box(
      modifier = Modifier
        .size(290.dp)
        .clip(CircleShape)
        .background(
          brush = Brush.radialGradient(
            colors = listOf(EmeraldPrimary, EmeraldDark)
          )
        )
        .border(4.dp, IslamicGold, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val radius = size.width / 2f - 16.dp.toPx()

        // Draw compass tick marks every 30 degrees
        for (i in 0 until 360 step 15) {
          val angleRad = Math.toRadians((i - 90).toDouble())
          val isMajor = i % 90 == 0
          val tickLen = if (isMajor) 14.dp.toPx() else 8.dp.toPx()
          val startX = centerX + (radius - tickLen) * cos(angleRad).toFloat()
          val startY = centerY + (radius - tickLen) * sin(angleRad).toFloat()
          val endX = centerX + radius * cos(angleRad).toFloat()
          val endY = centerY + radius * sin(angleRad).toFloat()

          drawLine(
            color = if (isMajor) IslamicGold else Color.White.copy(alpha = 0.4f),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = if (isMajor) 3.dp.toPx() else 1.5.dp.toPx()
          )
        }

        // Draw animated Qibla Pointer Needle
        rotate(animatedRotation, pivot = Offset(centerX, centerY)) {
          // North indicator (Gold arrow pointing up towards Kaaba)
          val arrowPath = Path().apply {
            moveTo(centerX, centerY - radius + 20.dp.toPx())
            lineTo(centerX - 16.dp.toPx(), centerY)
            lineTo(centerX + 16.dp.toPx(), centerY)
            close()
          }
          drawPath(path = arrowPath, color = IslamicGold)

          // South indicator (Dark subtle arrow pointing down)
          val southPath = Path().apply {
            moveTo(centerX, centerY + radius - 20.dp.toPx())
            lineTo(centerX - 12.dp.toPx(), centerY)
            lineTo(centerX + 12.dp.toPx(), centerY)
            close()
          }
          drawPath(path = southPath, color = Color.White.copy(alpha = 0.3f))

          // Center pin
          drawCircle(
            color = IslamicGoldLight,
            radius = 12.dp.toPx(),
            center = Offset(centerX, centerY)
          )
        }
      }

      // Center Kaaba Emblem
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 60.dp)
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.Black)
            .border(2.dp, IslamicGold, RoundedCornerShape(6.dp)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "🕋",
            fontSize = 18.sp
          )
        }
        Text(
          text = "الكعبة",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp,
          color = IslamicGoldLight
        )
      }
    }

    // Sensor Calibration Instruction Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
      Row(
        modifier = Modifier.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "ضع الهاتف بشكل مستوٍ وحرّكه على شكل رقم 8 في الهواء لمعايرة مستشعر البوصلة للحصول على أدق اتجاه.",
          fontFamily = CairoFontFamily,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 18.sp
        )
      }
    }
  }
}
