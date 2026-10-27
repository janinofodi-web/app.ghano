package com.example.ui.screens.quran

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.data.repository.QuranRepository
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.viewmodel.AppViewModel

@Composable
fun MemorizationTestScreen(viewModel: AppViewModel) {
  val testSurahs = listOf(
    QuranRepository.SURAHS[0], // Al-Fatihah
    QuranRepository.SURAHS[111], // Al-Ikhlas
    QuranRepository.SURAHS[112], // Al-Falaq
    QuranRepository.SURAHS[113]  // An-Nas
  )

  var selectedSurah by remember { mutableStateOf(testSurahs[0]) }
  val verses = remember(selectedSurah.number) {
    QuranRepository.getVersesForSurah(selectedSurah.number)
  }

  // Set of revealed verse indices
  val revealedSet = remember(selectedSurah.number) { mutableStateMapOf<Int, Boolean>() }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("memorization_test_screen")
  ) {
    // Top Info Header
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "اختبار حفظ السور وإخفاء الآيات",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "اقرأ الآية من حفظك غيبًا أولًا، ثم اضغط على 'إظهار الآية' للتحقق من صحة حفظك وتثبيته.",
          fontFamily = CairoFontFamily,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Surah selector chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          testSurahs.forEach { s ->
            val isSelected = s.number == selectedSurah.number
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(
                  if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                )
                .clickable { selectedSurah = s }
                .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text(
                text = s.nameArabic,
                fontFamily = CairoFontFamily,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 80.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      itemsIndexed(verses) { index, ayah ->
        val isRevealed = revealedSet[index] == true

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "الآية رقم ${ayah.numberInSurah}",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
              )

              OutlinedButton(
                onClick = { revealedSet[index] = !isRevealed },
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Icon(
                  imageVector = if (isRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (isRevealed) "إخفاء" else "إظهار الآية",
                  fontFamily = CairoFontFamily,
                  fontSize = 12.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (isRevealed) {
              Text(
                text = "${ayah.textUthmani} ﴿${ayah.numberInSurah}﴾",
                fontFamily = AmiriFontFamily,
                fontSize = 20.sp,
                textAlign = TextAlign.Right,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
              )
            } else {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                  .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "•••••• الآية مخفية لاختبار حفظك ••••••",
                  fontFamily = CairoFontFamily,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }
  }
}
