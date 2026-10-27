package com.example.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.repository.HadithRepository
import com.example.data.repository.QuranRepository
import com.example.ui.components.ShareCardDialog
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.EmeraldBorderDark
import com.example.ui.theme.EmeraldCardDark
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.AppViewModel
import com.example.ui.viewmodel.SubScreen

@Composable
fun HomeScreen(
  viewModel: AppViewModel,
  onNavigateTo: (AppDestination) -> Unit,
  onNavigateToSub: (SubScreen) -> Unit
) {
  val prayerSchedule by viewModel.prayerSchedule.collectAsState()
  val userName by viewModel.prefs.userName.collectAsState()
  val completedPrayers by viewModel.completedPrayersToday.collectAsState()
  val homeSalawatCount by viewModel.homeSalawatCount.collectAsState()
  val isAudioPlaying by viewModel.isAudioPlaying.collectAsState()

  val dailyVerse = remember { QuranRepository.getDailyVerse() }
  val dailyHadith = remember { HadithRepository.getDailyHadith() }

  var shareDialogContent by remember { mutableStateOf<Triple<String, String, String>?>(null) }
  var showTafseerDialog by remember { mutableStateOf(false) }

  // Overall worship percentage calculation
  val prayersScore = (completedPrayers.size.coerceAtMost(5) * 12)
  val overallWorshipPercentage = (prayersScore + 25).coerceIn(0, 100)

  shareDialogContent?.let { (title, text, source) ->
    ShareCardDialog(
      title = title,
      mainText = text,
      sourceOrSubtitle = source,
      onDismiss = { shareDialogContent = null }
    )
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag("home_screen_lazy_column"),
    contentPadding = PaddingValues(bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Hero Header with Mosque Arch Image & Welcome Greeting
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(230.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.islamic_hero_banner),
          contentDescription = "بانر إسلامي فاخر",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Gradient overlay for contrast
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Black.copy(alpha = 0.25f),
                  EmeraldDark.copy(alpha = 0.85f)
                )
              )
            )
        )

        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
          verticalArrangement = Arrangement.Bottom
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
          ) {
            Column {
              Text(
                text = "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللهِ",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = IslamicGoldLight
              )
              Text(
                text = "مرحبًا بك، $userName",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.15f))
                .border(1.dp, IslamicGold.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text(
                text = prayerSchedule?.hijriDate ?: "١٥ ربيع الثاني ١٤٤٨ هـ",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = IslamicGoldLight
              )
            }
          }
        }
      }
    }

    // 2. Next Prayer Countdown Card
    item {
      prayerSchedule?.let { schedule ->
        ElevatedCard(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("prayer_countdown_card"),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  colors = listOf(
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                    MaterialTheme.colorScheme.surface
                  )
                )
              )
              .padding(18.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = IslamicGold,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "الصلاة القادمة: صلاة ${schedule.nextPrayerName}",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                  )
                }

                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.clickable { onNavigateTo(AppDestination.PRAYER) }
                ) {
                  Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = schedule.locationName,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "الوقت المتبقي للأذان",
                    fontFamily = CairoFontFamily,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Text(
                    text = schedule.remainingTimeFormatted,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = IslamicGold
                  )
                }

                Button(
                  onClick = { onNavigateTo(AppDestination.PRAYER) },
                  colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                  shape = RoundedCornerShape(12.dp),
                  contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = "جدول الصلوات",
                    fontFamily = CairoFontFamily,
                    fontSize = 12.sp,
                    color = Color.White
                  )
                }
              }
            }
          }
        }
      }
    }

    // 3. Quick Navigation Grid (6 essential Islamic modules)
    item {
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
          text = "أقسام نور الإسلام",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          HomeShortcutButton(
            title = "القرآن الكريم",
            subtitle = "المصحف كاملاً",
            icon = Icons.Default.MenuBook,
            badgeColor = IslamicGold,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateTo(AppDestination.QURAN) }
          )
          HomeShortcutButton(
            title = "مواقيت الصلاة",
            subtitle = "والأذان والقبلة",
            icon = Icons.Default.AccessTime,
            badgeColor = EmeraldPrimary,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateTo(AppDestination.PRAYER) }
          )
          HomeShortcutButton(
            title = "الأذكار اليومية",
            subtitle = "الصباح والمساء",
            icon = Icons.Default.Favorite,
            badgeColor = Color(0xFFC76D2E),
            modifier = Modifier.weight(1f),
            onClick = { onNavigateTo(AppDestination.AZKAR) }
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          HomeShortcutButton(
            title = "السبحة الذكية",
            subtitle = "عداد التسبيح",
            icon = Icons.Default.TouchApp,
            badgeColor = Color(0xFF2E9A68),
            modifier = Modifier.weight(1f),
            onClick = { onNavigateTo(AppDestination.TASBIH) }
          )
          HomeShortcutButton(
            title = "الحديث الشريف",
            subtitle = "صحيح السنة",
            icon = Icons.Default.Book,
            badgeColor = Color(0xFF5A4D80),
            modifier = Modifier.weight(1f),
            onClick = { onNavigateToSub(SubScreen.HADITH_LIBRARY) }
          )
          HomeShortcutButton(
            title = "القصص والتاريخ",
            subtitle = "الأنبياء والصحابة",
            icon = Icons.Default.AutoAwesome,
            badgeColor = IslamicGold,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateToSub(SubScreen.STORIES_LIBRARY) }
          )
        }
      }
    }

    // 4. Daily Worship Dashboard (لوحة التحكم الشخصية)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "لوحة متابعة العبادات اليومية",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "احرص على إتمام فرائضك وأورادك لنيل الأجر",
                fontFamily = CairoFontFamily,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Box(contentAlignment = Alignment.Center) {
              CircularProgressIndicator(
                progress = { overallWorshipPercentage / 100f },
                modifier = Modifier.size(54.dp),
                color = IslamicGold,
                trackColor = MaterialTheme.colorScheme.outlineVariant,
                strokeWidth = 6.dp
              )
              Text(
                text = "$overallWorshipPercentage%",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 5 Prayers quick mark row
          val prayerKeys = listOf(
            "fajr" to "الفجر",
            "dhuhr" to "الظهر",
            "asr" to "العصر",
            "maghrib" to "المغرب",
            "isha" to "العشاء"
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            prayerKeys.forEach { (key, label) ->
              val isDone = completedPrayers.contains(key)
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .clickable { viewModel.togglePrayerCompleted(key) }
                  .padding(horizontal = 6.dp, vertical = 4.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                      if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    )
                    .border(
                      1.dp,
                      if (isDone) IslamicGold else MaterialTheme.colorScheme.outline,
                      CircleShape
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  if (isDone) {
                    Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = null,
                      tint = IslamicGold,
                      modifier = Modifier.size(20.dp)
                    )
                  } else {
                    Text(
                      text = label.first().toString(),
                      fontFamily = CairoFontFamily,
                      fontSize = 14.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = label,
                  fontFamily = CairoFontFamily,
                  fontSize = 11.sp,
                  fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal,
                  color = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    // 5. Daily Quran Verse Card (آية اليوم مع التفسير والاستماع والمشاركة)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(IslamicGold)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "آية اليوم المباركة",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
              )
            }

            Text(
              text = "سورة البقرة • آية ٢٥٥",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                RoundedCornerShape(14.dp)
              )
              .padding(16.dp)
          ) {
            Text(
              text = "۞ ${dailyVerse.textUthmani} ۞",
              fontFamily = AmiriFontFamily,
              fontSize = 20.sp,
              lineHeight = 32.sp,
              textAlign = TextAlign.Center,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Tafseer snippet
          Text(
            text = "التفسير الميسر: ${dailyVerse.tafseer}",
            fontFamily = CairoFontFamily,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            IconButton(
              onClick = {
                if (isAudioPlaying) {
                  viewModel.stopAudio()
                } else {
                  viewModel.playSurahAudio(2)
                }
              }
            ) {
              Icon(
                imageVector = if (isAudioPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = "استماع للتلاوة",
                tint = MaterialTheme.colorScheme.primary
              )
            }

            IconButton(
              onClick = {
                shareDialogContent = Triple(
                  "آية اليوم - سورة البقرة: 255",
                  dailyVerse.textUthmani,
                  "تطبيق نور الإسلام"
                )
              }
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "مشاركة الآية",
                tint = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
      }
    }

    // 6. Daily Hadith Card (حديث اليوم)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(EmeraldPrimary)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "حديث اليوم النبوي الشريف",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
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
                text = dailyHadith.authenticityGrade,
                fontFamily = CairoFontFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = dailyHadith.narrator,
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "«${dailyHadith.matnArabic}»",
            fontFamily = AmiriFontFamily,
            fontSize = 18.sp,
            lineHeight = 28.sp,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "الشرح: ${dailyHadith.explanation}",
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${dailyHadith.bookName} • حديث رقم ${dailyHadith.hadithNumber}",
              fontFamily = CairoFontFamily,
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            IconButton(
              onClick = {
                shareDialogContent = Triple(
                  "حديث شريف - ${dailyHadith.bookName}",
                  dailyHadith.matnArabic,
                  "${dailyHadith.narrator} • ${dailyHadith.authenticityGrade}"
                )
              }
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "مشاركة الحديث",
                tint = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
      }
    }

    // 7. Salawat Reminder & Quick Counter Card (الصلاة على النبي ﷺ)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "تذكير بالصلاة على النبي ﷺ",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "«اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَىٰ نَبِيِّنَا مُحَمَّدٍ»",
              fontFamily = AmiriFontFamily,
              fontSize = 16.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "صلواتك اليوم: $homeSalawatCount",
              fontFamily = CairoFontFamily,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = IslamicGold
            )
          }

          Button(
            onClick = { viewModel.incrementHomeSalawat() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = CircleShape,
            modifier = Modifier.size(54.dp),
            contentPadding = PaddingValues(0.dp)
          ) {
            Icon(
              imageVector = Icons.Default.TouchApp,
              contentDescription = "صلِّ على النبي",
              tint = IslamicGoldLight,
              modifier = Modifier.size(24.dp)
            )
          }
        }
      }
    }

    // 8. Islamic Calendar Events Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = IslamicGold,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "المناسبات والتقويم الإسلامي",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          val events = listOf(
            Triple("شهر رمضان المبارك", "١ رمضان ١٤٤٨ هـ", "متبقي حوالي ٤ أشهر"),
            Triple("عيد الفطر السعيد", "١ شوال ١٤٤٨ هـ", "يوم الجائزة وإتمام الصيام"),
            Triple("يوم عرفة وعيد الأضحى", "٩ ذو الحجة ١٤٤٨ هـ", "أعظم أيام الدهر")
          )

          events.forEach { (title, hijri, note) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = title,
                  fontFamily = CairoFontFamily,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 14.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = note,
                  fontFamily = CairoFontFamily,
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Text(
                text = hijri,
                fontFamily = CairoFontFamily,
                fontSize = 12.sp,
                color = IslamicGold,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun HomeShortcutButton(
  title: String,
  subtitle: String,
  icon: ImageVector,
  badgeColor: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  ElevatedCard(
    onClick = onClick,
    modifier = modifier.aspectRatio(0.95f),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(badgeColor.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = badgeColor,
          modifier = Modifier.size(22.dp)
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = title,
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = subtitle,
        fontFamily = CairoFontFamily,
        fontSize = 10.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
