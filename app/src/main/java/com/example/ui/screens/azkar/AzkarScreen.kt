package com.example.ui.screens.azkar

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DhikrItem
import com.example.data.repository.AzkarRepository
import com.example.ui.components.ShareCardDialog
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.viewmodel.AppViewModel

@Composable
fun AzkarScreen(viewModel: AppViewModel) {
  val selectedCatId by viewModel.selectedAzkarCategory.collectAsState()
  val countsMap by viewModel.azkarCounts.collectAsState()

  val items = remember(selectedCatId) {
    AzkarRepository.getAzkarForCategory(selectedCatId)
  }

  var shareData by remember { mutableStateOf<Triple<String, String, String>?>(null) }

  shareData?.let { (title, text, source) ->
    ShareCardDialog(
      title = title,
      mainText = text,
      sourceOrSubtitle = source,
      onDismiss = { shareData = null }
    )
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("azkar_screen")
  ) {
    // Categories Horizontal Scroll Row
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp),
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(AzkarRepository.CATEGORIES) { cat ->
        val isSelected = cat.id == selectedCatId
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
              if (isSelected) MaterialTheme.colorScheme.primary
              else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable { viewModel.selectAzkarCategory(cat.id) }
            .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
          Text(
            text = cat.titleArabic,
            fontFamily = CairoFontFamily,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 13.sp,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    // Azkar Items List
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 80.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(items, key = { it.id }) { item ->
        val currentCount = countsMap[item.id] ?: 0
        val isCompleted = currentCount >= item.targetCount

        DhikrCard(
          item = item,
          currentCount = currentCount,
          isCompleted = isCompleted,
          onIncrement = { viewModel.incrementDhikrCount(item) },
          onReset = { viewModel.resetDhikrCount(item) },
          onShare = {
            shareData = Triple(
              "ذكر مبارك",
              item.textArabic,
              item.referenceOrVirtue
            )
          }
        )
      }
    }
  }
}

@Composable
private fun DhikrCard(
  item: DhikrItem,
  currentCount: Int,
  isCompleted: Boolean,
  onIncrement: () -> Unit,
  onReset: () -> Unit,
  onShare: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isCompleted) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
      else MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Header with repetition badge & actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
              if (isCompleted) IslamicGold else MaterialTheme.colorScheme.primaryContainer
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "التكرار المطلوب: ${item.targetCount} ${if (item.targetCount == 1) "مرة" else "مرات"}",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = if (isCompleted) Color.Black else MaterialTheme.colorScheme.primary
          )
        }

        Row {
          IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "مشاركة",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp)
            )
          }

          if (currentCount > 0) {
            IconButton(onClick = onReset, modifier = Modifier.size(32.dp)) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "إعادة ضبط",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Arabic Dhikr Text
      Text(
        text = item.textArabic,
        fontFamily = AmiriFontFamily,
        fontSize = 19.sp,
        lineHeight = 30.sp,
        textAlign = TextAlign.Right,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Reference / Virtue note
      Text(
        text = "الفضل / المصدر: ${item.referenceOrVirtue}",
        fontFamily = CairoFontFamily,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 16.sp
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Progress bar
      val progress = if (item.targetCount > 0) currentCount.toFloat() / item.targetCount else 1f
      LinearProgressIndicator(
        progress = { progress.coerceIn(0f, 1f) },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = IslamicGold,
        trackColor = MaterialTheme.colorScheme.outlineVariant
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Big Touch Counter Button
      Button(
        onClick = onIncrement,
        enabled = !isCompleted,
        colors = ButtonDefaults.buttonColors(
          containerColor = if (isCompleted) IslamicGold else MaterialTheme.colorScheme.primary,
          disabledContainerColor = IslamicGold.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
      ) {
        if (isCompleted) {
          Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "اكتمل الذكر • بارك الله فيك",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color.Black
          )
        } else {
          Text(
            text = "سبّح ($currentCount / ${item.targetCount})",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color.White
          )
        }
      }
    }
  }
}
