package com.example.ui.screens.prayer

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CalculationMethod
import com.example.data.model.CityLocation
import com.example.data.model.JuristicSchool
import com.example.data.model.PrayerTime
import com.example.data.repository.PrayerTimesCalculator
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.EmeraldBorderDark
import com.example.ui.theme.EmeraldCardDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.viewmodel.AppViewModel
import com.example.ui.viewmodel.SubScreen

@Composable
fun PrayerTimesScreen(
  viewModel: AppViewModel,
  onOpenQibla: () -> Unit
) {
  val schedule by viewModel.prayerSchedule.collectAsState()
  val selectedCity by viewModel.prefs.selectedCity.collectAsState()
  val calcMethod by viewModel.prefs.calculationMethod.collectAsState()
  val juristicSchool by viewModel.prefs.juristicSchool.collectAsState()
  val completedPrayers by viewModel.completedPrayersToday.collectAsState()

  var showCityDialog by remember { mutableStateOf(false) }
  var showMethodDialog by remember { mutableStateOf(false) }

  // City Picker Dialog
  if (showCityDialog) {
    AlertDialog(
      onDismissRequest = { showCityDialog = false },
      title = {
        Text(
          text = "اختر مدينتك لمواقيت دقيقة",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp
        )
      },
      text = {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
        ) {
          items(PrayerTimesCalculator.POPULAR_CITIES) { city ->
            val isSelected = city.nameArabic == selectedCity.nameArabic
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  viewModel.setCity(city)
                  showCityDialog = false
                }
                .padding(vertical = 10.dp, horizontal = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(
                  text = city.nameArabic,
                  fontFamily = CairoFontFamily,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 15.sp,
                  color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = city.countryArabic,
                  fontFamily = CairoFontFamily,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              if (isSelected) {
                Icon(
                  Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary
                )
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showCityDialog = false }) {
          Text("إلغاء", fontFamily = CairoFontFamily)
        }
      }
    )
  }

  // Calculation Method Dialog
  if (showMethodDialog) {
    AlertDialog(
      onDismissRequest = { showMethodDialog = false },
      title = {
        Text(
          text = "طريقة الحساب والمذهب",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp
        )
      },
      text = {
        Column {
          Text(
            text = "هيئة الحساب الفلكي:",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
          )
          Spacer(modifier = Modifier.height(6.dp))
          CalculationMethod.values().forEach { method ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.setCalculationMethod(method) }
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = calcMethod == method,
                onClick = { viewModel.setCalculationMethod(method) }
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = method.displayNameArabic,
                fontFamily = CairoFontFamily,
                fontSize = 13.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "مذهب صلاة العصر:",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
          )
          Spacer(modifier = Modifier.height(6.dp))
          JuristicSchool.values().forEach { school ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.setJuristicSchool(school) }
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = juristicSchool == school,
                onClick = { viewModel.setJuristicSchool(school) }
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = school.displayNameArabic,
                fontFamily = CairoFontFamily,
                fontSize = 13.sp
              )
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showMethodDialog = false }) {
          Text("تم وحفظ", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold)
        }
      }
    )
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag("prayer_times_screen_lazy_column"),
    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. City & Qibla Actions Banner
    item {
      ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.LocationOn,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "${selectedCity.nameArabic}، ${selectedCity.countryArabic}",
                  fontFamily = CairoFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 17.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "طريقة الحساب: ${calcMethod.displayNameArabic}",
                fontFamily = CairoFontFamily,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            IconButton(
              onClick = { showCityDialog = true },
              modifier = Modifier.testTag("select_city_button")
            ) {
              Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "تغيير المدينة",
                tint = MaterialTheme.colorScheme.primary
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = onOpenQibla,
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("open_qibla_button")
            ) {
              Icon(Icons.Default.Explore, contentDescription = null, tint = IslamicGoldLight)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "بوصلة القبلة",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.White
              )
            }

            OutlinedButton(
              onClick = { showMethodDialog = true },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "إعدادات الحساب",
                fontFamily = CairoFontFamily,
                fontSize = 12.sp
              )
            }
          }
        }
      }
    }

    // 2. Next Prayer Highlight Header
    item {
      schedule?.let { s ->
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
              Brush.horizontalGradient(
                colors = listOf(EmeraldPrimary, Color(0xFF07241A))
              )
            )
            .border(1.dp, IslamicGold.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .padding(20.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "الصلاة القادمة: صلاة ${s.nextPrayerName}",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = IslamicGoldLight
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "متبقي للأذان: ${s.remainingTimeFormatted}",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.White
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = s.hijriDate,
                fontFamily = CairoFontFamily,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
              )
            }

            Box(
              modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(IslamicGold.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = null,
                tint = IslamicGold,
                modifier = Modifier.size(28.dp)
              )
            }
          }
        }
      }
    }

    // 3. Today's Prayers Table & Tracking Checkboxes
    item {
      schedule?.let { s ->
        val prayers = listOf(
          Triple("fajr", s.fajr, "الفجر"),
          Triple("sunrise", s.sunrise, "الشروق"),
          Triple("dhuhr", s.dhuhr, "الظهر"),
          Triple("asr", s.asr, "العصر"),
          Triple("maghrib", s.maghrib, "المغرب"),
          Triple("isha", s.isha, "العشاء"),
          Triple("duha", s.duha, "الضحى"),
          Triple("qiyam", s.qiyam, "قيام الليل")
        )

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "جدول مواقيت اليوم وسجل الأداء",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = "اضغط على الدائرة لتعليم الصلاة التي أديتها لله تعالى",
              fontFamily = CairoFontFamily,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            prayers.forEach { (key, prayerTime, label) ->
              val isDone = completedPrayers.contains(key)
              val isNext = prayerTime.isNext

              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(
                    if (isNext) IslamicGold.copy(alpha = 0.12f)
                    else Color.Transparent
                  )
                  .clickable {
                    if (key != "sunrise") {
                      viewModel.togglePrayerCompleted(key)
                    }
                  }
                  .padding(vertical = 10.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  if (key != "sunrise") {
                    Icon(
                      imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                      contentDescription = "تمت الصلاة",
                      tint = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                      modifier = Modifier.size(24.dp)
                    )
                  } else {
                    Spacer(modifier = Modifier.size(24.dp))
                  }

                  Spacer(modifier = Modifier.width(12.dp))

                  Column {
                    Text(
                      text = label,
                      fontFamily = CairoFontFamily,
                      fontWeight = if (isNext) FontWeight.Bold else FontWeight.SemiBold,
                      fontSize = 15.sp,
                      color = if (isNext) IslamicGold else MaterialTheme.colorScheme.onSurface
                    )
                    if (isNext) {
                      Text(
                        text = "الصلاة القادمة",
                        fontFamily = CairoFontFamily,
                        fontSize = 11.sp,
                        color = IslamicGold
                      )
                    }
                  }
                }

                Text(
                  text = prayerTime.timeFormatted,
                  fontFamily = CairoFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = if (isNext) IslamicGold else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }
    }
  }
}
