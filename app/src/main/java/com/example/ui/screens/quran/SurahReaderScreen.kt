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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.model.Ayah
import com.example.data.repository.QuranRepository
import com.example.ui.components.ShareCardDialog
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahReaderScreen(viewModel: AppViewModel) {
  val surah by viewModel.selectedSurah.collectAsState()
  val isAudioPlaying by viewModel.isAudioPlaying.collectAsState()
  val savedFontSize by viewModel.prefs.quranFontSizeSp.collectAsState()

  var fontSize by remember { mutableFloatStateOf(savedFontSize.toFloat()) }
  var showFontDialog by remember { mutableStateOf(false) }
  var selectedAyahForTafseer by remember { mutableStateOf<Ayah?>(null) }
  var shareDialogData by remember { mutableStateOf<Triple<String, String, String>?>(null) }

  val verses = remember(surah.number) {
    QuranRepository.getVersesForSurah(surah.number)
  }

  shareDialogData?.let { (title, text, source) ->
    ShareCardDialog(
      title = title,
      mainText = text,
      sourceOrSubtitle = source,
      onDismiss = { shareDialogData = null }
    )
  }

  // Tafseer Bottom Sheet
  selectedAyahForTafseer?.let { ayah ->
    ModalBottomSheet(
      onDismissRequest = { selectedAyahForTafseer = null },
      sheetState = rememberModalBottomSheetState()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "تفسير الآية رقم ${ayah.numberInSurah} من سورة ${surah.nameArabic}",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = ayah.textUthmani,
          fontFamily = AmiriFontFamily,
          fontSize = 20.sp,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = "التفسير الميسر:",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 14.sp,
          color = IslamicGold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = ayah.tafseer,
          fontFamily = CairoFontFamily,
          fontSize = 14.sp,
          lineHeight = 22.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }

  // Font Size Setting Dialog
  if (showFontDialog) {
    AlertDialog(
      onDismissRequest = { showFontDialog = false },
      title = {
        Text(
          text = "حجم خط المصحف",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp
        )
      },
      text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            fontFamily = AmiriFontFamily,
            fontSize = fontSize.sp,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(14.dp))
          Slider(
            value = fontSize,
            onValueChange = {
              fontSize = it
              viewModel.prefs.setQuranFontSize(it.toInt())
            },
            valueRange = 18f..38f
          )
          Text(
            text = "${fontSize.toInt()} نقطة",
            fontFamily = CairoFontFamily,
            fontSize = 13.sp
          )
        }
      },
      confirmButton = {
        TextButton(onClick = { showFontDialog = false }) {
          Text("حفظ وإغلاق", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold)
        }
      }
    )
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("surah_reader_screen")
  ) {
    // Reading & Audio Controls Top Strip
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = {
              if (isAudioPlaying) {
                viewModel.stopAudio()
              } else {
                viewModel.playSurahAudio(surah.number)
              }
            },
            modifier = Modifier.testTag("play_surah_audio_button")
          ) {
            Icon(
              imageVector = if (isAudioPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
              contentDescription = "استماع للسورة",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(28.dp)
            )
          }

          Text(
            text = if (isAudioPlaying) "جارٍ الاستماع..." else "استماع للتلاوة",
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
          )
        }

        IconButton(onClick = { showFontDialog = true }) {
          Icon(
            imageVector = Icons.Default.TextFields,
            contentDescription = "تغيير حجم الخط",
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 80.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Surah Header Banner
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "سُورَةُ ${surah.nameArabic}",
              fontFamily = AmiriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 28.sp,
              color = IslamicGoldLight
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "${surah.revelationType} • آياتها ${surah.ayahCount} • صفحة ${surah.page} • الجزء ${surah.juz}",
              fontFamily = CairoFontFamily,
              fontSize = 12.sp,
              color = Color.White.copy(alpha = 0.85f)
            )

            if (surah.number != 9 && surah.number != 1) {
              Spacer(modifier = Modifier.height(14.dp))
              Text(
                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                fontFamily = AmiriFontFamily,
                fontSize = 22.sp,
                color = Color.White
              )
            }
          }
        }
      }

      // Verses List
      items(verses, key = { it.numberInSurah }) { ayah ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedAyahForTafseer = ayah },
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(30.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "${ayah.numberInSurah}",
                  fontFamily = CairoFontFamily,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
              }

              Row {
                IconButton(
                  onClick = {
                    shareDialogData = Triple(
                      "سورة ${surah.nameArabic} - آية ${ayah.numberInSurah}",
                      ayah.textUthmani,
                      "تطبيق نور الإسلام"
                    )
                  },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "مشاركة",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                  )
                }

                IconButton(
                  onClick = { viewModel.addBookmark(ayah, surah) },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = "إضافة علامة مرجعية",
                    tint = IslamicGold,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "${ayah.textUthmani} ﴿${ayah.numberInSurah}﴾",
              fontFamily = AmiriFontFamily,
              fontSize = fontSize.sp,
              lineHeight = (fontSize * 1.6f).sp,
              textAlign = TextAlign.Right,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "اضغط لعرض التفسير الميسر",
              fontFamily = CairoFontFamily,
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.primary
            )
          }
        }
      }
    }
  }
}
