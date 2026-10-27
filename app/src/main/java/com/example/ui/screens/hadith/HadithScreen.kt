package com.example.ui.screens.hadith

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Hadith
import com.example.data.model.HadithCategory
import com.example.data.repository.HadithRepository
import com.example.ui.components.ShareCardDialog
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.viewmodel.AppViewModel

@Composable
fun HadithScreen(viewModel: AppViewModel) {
  var selectedCategory by remember { mutableStateOf(HadithCategory.ALL) }
  var searchQuery by remember { mutableStateOf("") }
  var shareData by remember { mutableStateOf<Triple<String, String, String>?>(null) }

  val filteredHadiths = remember(selectedCategory, searchQuery) {
    HadithRepository.ALL_HADITHS.filter { h ->
      val matchesCat = selectedCategory == HadithCategory.ALL || h.category == selectedCategory.titleArabic
      val matchesQuery = searchQuery.isBlank() ||
        h.matnArabic.contains(searchQuery.trim()) ||
        h.narrator.contains(searchQuery.trim()) ||
        h.explanation.contains(searchQuery.trim())
      matchesCat && matchesQuery
    }
  }

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
      .testTag("hadith_screen")
  ) {
    // Search Bar
    Column(modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 0.dp)) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("ابحث في متون الأحاديث والرواة...", fontFamily = CairoFontFamily, fontSize = 13.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Clear, contentDescription = "مسح")
            }
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("hadith_search_field"),
        singleLine = true,
        shape = RoundedCornerShape(16.dp)
      )
    }

    // Categories Scroll Row
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp),
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(HadithCategory.values()) { cat ->
        val isSelected = cat == selectedCategory
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
              if (isSelected) MaterialTheme.colorScheme.primary
              else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable { selectedCategory = cat }
            .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
          Text(
            text = cat.titleArabic,
            fontFamily = CairoFontFamily,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 12.sp,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    // Hadiths List
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 80.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      items(filteredHadiths, key = { it.id }) { hadith ->
        HadithCard(
          hadith = hadith,
          onShare = {
            shareData = Triple(
              hadith.bookName,
              hadith.matnArabic,
              "${hadith.narrator} • ${hadith.authenticityGrade}"
            )
          }
        )
      }
    }
  }
}

@Composable
private fun HadithCard(
  hadith: Hadith,
  onShare: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Header: Book + Authenticity Tag
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Book,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${hadith.bookName} • ${hadith.chapterName}",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(IslamicGold.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = hadith.authenticityGrade,
            fontFamily = CairoFontFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Narrator
      Text(
        text = hadith.narrator,
        fontFamily = CairoFontFamily,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Matn in beautiful Amiri script
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            RoundedCornerShape(12.dp)
          )
          .padding(14.dp)
      ) {
        Text(
          text = "«${hadith.matnArabic}»",
          fontFamily = AmiriFontFamily,
          fontSize = 19.sp,
          lineHeight = 29.sp,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Right,
          modifier = Modifier.fillMaxWidth()
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Explanation
      Text(
        text = "الشرح الميسر: ${hadith.explanation}",
        fontFamily = CairoFontFamily,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Practical benefits bullets
      Text(
        text = "الدروس والفوائد العملية:",
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        color = IslamicGold
      )
      hadith.practicalBenefits.forEach { benefit ->
        Text(
          text = "• $benefit",
          fontFamily = CairoFontFamily,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(start = 6.dp, top = 2.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Bottom bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "رقم الحديث: ${hadith.hadithNumber}",
          fontFamily = CairoFontFamily,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "مشاركة الحديث",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
