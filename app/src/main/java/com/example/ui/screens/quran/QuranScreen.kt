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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SurahMeta
import com.example.data.repository.QuranRepository
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.viewmodel.AppViewModel
import com.example.ui.viewmodel.SubScreen

@Composable
fun QuranScreen(
  viewModel: AppViewModel,
  onOpenSurah: (SurahMeta) -> Unit,
  onOpenMemorizationTest: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedTab by remember { mutableStateOf(0) } // 0: السور, 1: الأجزاء, 2: الختمة

  val filteredSurahs = remember(searchQuery) {
    if (searchQuery.isBlank()) QuranRepository.SURAHS
    else QuranRepository.SURAHS.filter {
      it.nameArabic.contains(searchQuery.trim()) ||
        it.nameEnglish.contains(searchQuery.trim(), ignoreCase = true) ||
        it.number.toString() == searchQuery.trim()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("quran_screen")
  ) {
    // Top Tabs: السور / الأجزاء / الختمة
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = MaterialTheme.colorScheme.primary
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = {
          Text(
            text = "فهرس السور (١١٤)",
            fontFamily = CairoFontFamily,
            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
          )
        }
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = {
          Text(
            text = "الأجزاء (٣٠)",
            fontFamily = CairoFontFamily,
            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
          )
        }
      )
      Tab(
        selected = selectedTab == 2,
        onClick = { selectedTab = 2 },
        text = {
          Text(
            text = "متابعة الختمة",
            fontFamily = CairoFontFamily,
            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
          )
        }
      )
    }

    when (selectedTab) {
      0 -> {
        // Search & Memorization bar
        Column(modifier = Modifier.padding(16.dp)) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("quran_search_field"),
            placeholder = {
              Text("ابحث باسم السورة أو رقمها...", fontFamily = CairoFontFamily, fontSize = 13.sp)
            },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { searchQuery = "" }) {
                  Icon(Icons.Default.Clear, contentDescription = "مسح")
                }
              }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Memorization Test Banner Button
          Button(
            onClick = onOpenMemorizationTest,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("open_memorization_test_button")
          ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IslamicGoldLight)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "اختبار ومراجعة حفظ السور",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color.White
            )
          }
        }

        // Surah List
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 80.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(filteredSurahs, key = { it.number }) { surah ->
            SurahListItem(
              surah = surah,
              onClick = { onOpenSurah(surah) }
            )
          }
        }
      }

      1 -> {
        // Juz Tab List (30 Juz)
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 80.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items((1..30).toList()) { juzNumber ->
            val startSurah = QuranRepository.SURAHS.find { it.juz == juzNumber } ?: QuranRepository.SURAHS[0]
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenSurah(startSurah) },
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(42.dp)
                      .clip(CircleShape)
                      .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = "$juzNumber",
                      fontFamily = CairoFontFamily,
                      fontWeight = FontWeight.Bold,
                      fontSize = 15.sp,
                      color = MaterialTheme.colorScheme.primary
                    )
                  }
                  Spacer(modifier = Modifier.width(12.dp))
                  Column {
                    Text(
                      text = "الجزء $juzNumber",
                      fontFamily = CairoFontFamily,
                      fontWeight = FontWeight.Bold,
                      fontSize = 16.sp,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = "يبدأ من سورة ${startSurah.nameArabic} • صفحة ${startSurah.page}",
                      fontFamily = CairoFontFamily,
                      fontSize = 12.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }

                Icon(
                  imageVector = Icons.Default.MenuBook,
                  contentDescription = null,
                  tint = IslamicGold
                )
              }
            }
          }
        }
      }

      2 -> {
        // Khatma Tracking Screen
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
          ) {
            Column(modifier = Modifier.padding(20.dp)) {
              Text(
                text = "متابعة ختمة القرآن الكريم",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "«اقْرَؤُوا القُرْآنَ فإنَّه يَأْتي يَومَ القِيامَةِ شَفِيعًا لأَصْحابِهِ»",
                fontFamily = AmiriFontFamily,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(14.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "الصفحات المنجزة: 65 من 604",
                  fontFamily = CairoFontFamily,
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = "11%",
                  fontFamily = CairoFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = IslamicGold
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              LinearProgressIndicator(
                progress = { 65f / 604f },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(10.dp)
                  .clip(RoundedCornerShape(5.dp)),
                color = IslamicGold,
                trackColor = MaterialTheme.colorScheme.outlineVariant
              )

              Spacer(modifier = Modifier.height(16.dp))

              Text(
                text = "آخر موضع قراءة: سورة البقرة (صفحة 42)",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
              )

              Spacer(modifier = Modifier.height(12.dp))

              Button(
                onClick = {
                  val baqarah = QuranRepository.SURAHS[1]
                  onOpenSurah(baqarah)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = "متابعة القراءة من آخر موضع",
                  fontFamily = CairoFontFamily,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun SurahListItem(
  surah: SurahMeta,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("surah_item_${surah.number}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(1.dp, IslamicGold.copy(alpha = 0.5f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "${surah.number}",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.primary
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = "سورة ${surah.nameArabic}",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "${surah.revelationType} • ${surah.ayahCount} آية • صفحة ${surah.page}",
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Text(
        text = surah.nameArabic,
        fontFamily = AmiriFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = IslamicGold
      )
    }
  }
}
