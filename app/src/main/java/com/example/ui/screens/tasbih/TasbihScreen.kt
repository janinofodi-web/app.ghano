package com.example.ui.screens.tasbih

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TasbihDhikr
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.viewmodel.AppViewModel

@Composable
fun TasbihScreen(viewModel: AppViewModel) {
  val selectedDhikr by viewModel.selectedTasbihDhikr.collectAsState()
  val count by viewModel.tasbihCurrentCount.collectAsState()
  val totalCount by viewModel.tasbihTotalCount.collectAsState()
  val target by viewModel.tasbihTarget.collectAsState()
  val vibrationEnabled by viewModel.prefs.vibrationEnabled.collectAsState()

  var showResetConfirmDialog by remember { mutableStateOf(false) }
  var showAddCustomDialog by remember { mutableStateOf(false) }
  var customDhikrText by remember { mutableStateOf("") }

  // Reset Confirmation Dialog
  if (showResetConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showResetConfirmDialog = false },
      title = {
        Text(
          text = "إعادة ضبط العداد",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp
        )
      },
      text = {
        Text(
          text = "هل أنت متأكد من تصفير العداد الحالي؟ لن يتم مسح إجمالي التسبيحات الكلي.",
          fontFamily = CairoFontFamily,
          fontSize = 14.sp
        )
      },
      confirmButton = {
        TextButton(
          onClick = {
            viewModel.resetTasbih()
            showResetConfirmDialog = false
          }
        ) {
          Text("نعم، صفّر العداد", fontFamily = CairoFontFamily, color = MaterialTheme.colorScheme.error)
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetConfirmDialog = false }) {
          Text("إلغاء", fontFamily = CairoFontFamily)
        }
      }
    )
  }

  // Add Custom Dhikr Dialog
  if (showAddCustomDialog) {
    AlertDialog(
      onDismissRequest = { showAddCustomDialog = false },
      title = {
        Text(
          text = "إضافة ذكر مخصص للسبحة",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp
        )
      },
      text = {
        Column {
          OutlinedTextField(
            value = customDhikrText,
            onValueChange = { customDhikrText = it },
            label = { Text("نص الذكر", fontFamily = CairoFontFamily) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        TextButton(
          onClick = {
            if (customDhikrText.isNotBlank()) {
              val newDhikr = TasbihDhikr(
                id = "custom_${System.currentTimeMillis()}",
                titleArabic = customDhikrText.trim(),
                defaultTarget = 33
              )
              viewModel.selectTasbihDhikr(newDhikr)
              customDhikrText = ""
              showAddCustomDialog = false
            }
          }
        ) {
          Text("إضافة", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddCustomDialog = false }) {
          Text("إلغاء", fontFamily = CairoFontFamily)
        }
      }
    )
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .testTag("tasbih_screen"),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // 1. Dhikr selection carousel
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "اختر صيغة الذكر:",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = MaterialTheme.colorScheme.onBackground
        )

        OutlinedButton(
          onClick = { showAddCustomDialog = true },
          shape = RoundedCornerShape(10.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("ذكر مخصص", fontFamily = CairoFontFamily, fontSize = 11.sp)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(viewModel.tasbihPresets) { dhikr ->
          val isSelected = dhikr.id == selectedDhikr.id
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant
              )
              .clickable { viewModel.selectTasbihDhikr(dhikr) }
              .padding(horizontal = 14.dp, vertical = 8.dp)
          ) {
            Text(
              text = dhikr.titleArabic,
              fontFamily = CairoFontFamily,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              fontSize = 12.sp,
              color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }

    // 2. Targets selector (33 / 100 / 1000 / حر)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center
    ) {
      val targets = listOf(
        33 to "33",
        100 to "100",
        1000 to "1000",
        0 to "مفتوح"
      )

      targets.forEach { (t, label) ->
        val isSelected = target == t
        Box(
          modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
              if (isSelected) IslamicGold else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable { viewModel.setTasbihTarget(t) }
            .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Text(
            text = label,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    // 3. Central Giant Tactile Beads & Counter Ring
    Box(
      modifier = Modifier
        .size(270.dp)
        .clip(CircleShape)
        .background(
          brush = Brush.radialGradient(
            colors = listOf(EmeraldPrimary, EmeraldDark)
          )
        )
        .border(4.dp, IslamicGold, CircleShape)
        .clickable { viewModel.incrementTasbih() }
        .testTag("tasbih_tap_button"),
      contentAlignment = Alignment.Center
    ) {
      // Circular progress indicator
      val progress = if (target > 0) (count.toFloat() / target).coerceIn(0f, 1f) else 0f
      val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

      CircularProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier.size(260.dp),
        color = IslamicGoldLight,
        trackColor = Color.White.copy(alpha = 0.1f),
        strokeWidth = 6.dp
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
      ) {
        Text(
          text = selectedDhikr.titleArabic,
          fontFamily = AmiriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp,
          color = IslamicGoldLight,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "$count",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 54.sp,
          color = Color.White
        )

        if (target > 0) {
          Text(
            text = "الهدف: $target",
            fontFamily = CairoFontFamily,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
          )
        } else {
          Text(
            text = "عد مفتوح",
            fontFamily = CairoFontFamily,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.7f)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "المس للعد",
          fontFamily = CairoFontFamily,
          fontSize = 12.sp,
          color = IslamicGold
        )
      }
    }

    // 4. Lifetime count stats & Controls Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp),
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "إجمالي التسبيحات الكلي",
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "${String.format("%,d", totalCount)} تسبيحة",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
          )
        }

        Row {
          // Vibration toggle button
          IconButton(
            onClick = { viewModel.prefs.setVibrationEnabled(!vibrationEnabled) }
          ) {
            Icon(
              imageVector = Icons.Default.Vibration,
              contentDescription = "الاهتزاز",
              tint = if (vibrationEnabled) IslamicGold else MaterialTheme.colorScheme.outline
            )
          }

          // Reset button
          IconButton(
            onClick = { showResetConfirmDialog = true },
            modifier = Modifier.testTag("reset_tasbih_button")
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "تصفير",
              tint = MaterialTheme.colorScheme.primary
            )
          }
        }
      }
    }
  }
}
