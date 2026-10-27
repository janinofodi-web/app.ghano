package com.example.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CalculationMethod
import com.example.data.model.JuristicSchool
import com.example.data.repository.PrayerTimesCalculator
import com.example.data.repository.QuranRepository
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.viewmodel.AppViewModel

@Composable
fun SettingsScreen(viewModel: AppViewModel) {
  val userName by viewModel.prefs.userName.collectAsState()
  val selectedCity by viewModel.prefs.selectedCity.collectAsState()
  val calcMethod by viewModel.prefs.calculationMethod.collectAsState()
  val juristicSchool by viewModel.prefs.juristicSchool.collectAsState()
  val quranFontSize by viewModel.prefs.quranFontSizeSp.collectAsState()
  val isDarkMode by viewModel.prefs.isDarkMode.collectAsState()
  val vibrationEnabled by viewModel.prefs.vibrationEnabled.collectAsState()
  val selectedReciterId by viewModel.prefs.selectedReciterId.collectAsState()

  var showNameDialog by remember { mutableStateOf(false) }
  var showCityDialog by remember { mutableStateOf(false) }
  var showReciterDialog by remember { mutableStateOf(false) }
  var showCalcMethodDialog by remember { mutableStateOf(false) }
  var newNameText by remember { mutableStateOf(userName) }

  // Edit Name Dialog
  if (showNameDialog) {
    AlertDialog(
      onDismissRequest = { showNameDialog = false },
      title = { Text("تعديل اسم المستخدم", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold) },
      text = {
        OutlinedTextField(
          value = newNameText,
          onValueChange = { newNameText = it },
          label = { Text("الاسم الكريم", fontFamily = CairoFontFamily) },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
      },
      confirmButton = {
        TextButton(
          onClick = {
            if (newNameText.isNotBlank()) {
              viewModel.prefs.setUserName(newNameText.trim())
            }
            showNameDialog = false
          }
        ) {
          Text("حفظ", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showNameDialog = false }) {
          Text("إلغاء", fontFamily = CairoFontFamily)
        }
      }
    )
  }

  // City Picker Dialog
  if (showCityDialog) {
    AlertDialog(
      onDismissRequest = { showCityDialog = false },
      title = { Text("اختيار المدينة", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold) },
      text = {
        LazyColumn(modifier = Modifier.height(300.dp)) {
          items(PrayerTimesCalculator.POPULAR_CITIES.size) { idx ->
            val city = PrayerTimesCalculator.POPULAR_CITIES[idx]
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  viewModel.setCity(city)
                  showCityDialog = false
                }
                .padding(vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = selectedCity.nameArabic == city.nameArabic,
                onClick = {
                  viewModel.setCity(city)
                  showCityDialog = false
                }
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text("${city.nameArabic} (${city.countryArabic})", fontFamily = CairoFontFamily, fontSize = 14.sp)
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showCityDialog = false }) {
          Text("إغلاق", fontFamily = CairoFontFamily)
        }
      }
    )
  }

  // Reciter Picker Dialog
  if (showReciterDialog) {
    AlertDialog(
      onDismissRequest = { showReciterDialog = false },
      title = { Text("اختيار القارئ المفضل", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold) },
      text = {
        Column {
          QuranRepository.RECITERS.forEach { reciter ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  viewModel.prefs.setReciterId(reciter.id)
                  showReciterDialog = false
                }
                .padding(vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = selectedReciterId == reciter.id,
                onClick = {
                  viewModel.prefs.setReciterId(reciter.id)
                  showReciterDialog = false
                }
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(reciter.nameArabic, fontFamily = CairoFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(reciter.subtext, fontFamily = CairoFontFamily, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showReciterDialog = false }) {
          Text("إغلاق", fontFamily = CairoFontFamily)
        }
      }
    )
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag("settings_screen"),
    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 80.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Profile Card
    item {
      SettingsCard(
        title = "الملف الشخصي",
        subtitle = "مرحبًا بك: $userName",
        icon = Icons.Default.Edit,
        onClick = { showNameDialog = true }
      )
    }

    // 2. City & Location
    item {
      SettingsCard(
        title = "الموقع والمدينة الحالية",
        subtitle = "${selectedCity.nameArabic}، ${selectedCity.countryArabic}",
        icon = Icons.Default.LocationOn,
        onClick = { showCityDialog = true }
      )
    }

    // 3. Calculation Method
    item {
      SettingsCard(
        title = "طريقة حساب مواقيت الصلاة",
        subtitle = calcMethod.displayNameArabic,
        icon = Icons.Default.Tune,
        onClick = {
          // Switch to next method
          val next = when (calcMethod) {
            CalculationMethod.UMM_AL_QURA -> CalculationMethod.EGYPTIAN
            CalculationMethod.EGYPTIAN -> CalculationMethod.MUSLIM_WORLD_LEAGUE
            CalculationMethod.MUSLIM_WORLD_LEAGUE -> CalculationMethod.ISNA
            CalculationMethod.ISNA -> CalculationMethod.KARACHI
            CalculationMethod.KARACHI -> CalculationMethod.UMM_AL_QURA
          }
          viewModel.setCalculationMethod(next)
        }
      )
    }

    // 4. Asr School
    item {
      SettingsCard(
        title = "مذهب صلاة العصر",
        subtitle = juristicSchool.displayNameArabic,
        icon = Icons.Default.Tune,
        onClick = {
          val next = if (juristicSchool == JuristicSchool.SHAFI_HANBALI_MALIKI) JuristicSchool.HANAFI
          else JuristicSchool.SHAFI_HANBALI_MALIKI
          viewModel.setJuristicSchool(next)
        }
      )
    }

    // 5. Quran Reciter
    item {
      val reciterName = QuranRepository.RECITERS.find { it.id == selectedReciterId }?.nameArabic ?: "مشاري راشد العفاسي"
      SettingsCard(
        title = "القارئ المفضل للتلاوة",
        subtitle = reciterName,
        icon = Icons.Default.RecordVoiceOver,
        onClick = { showReciterDialog = true }
      )
    }

    // 6. Quran Font Size Slider
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.TextFields, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
              Spacer(modifier = Modifier.width(10.dp))
              Text("حجم خط المصحف", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Text("$quranFontSize نقطة", fontFamily = CairoFontFamily, fontSize = 13.sp, color = IslamicGold)
          }
          Slider(
            value = quranFontSize.toFloat(),
            onValueChange = { viewModel.prefs.setQuranFontSize(it.toInt()) },
            valueRange = 18f..38f
          )
        }
      }
    }

    // 7. Vibration toggle
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Vibration, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("الاهتزاز عند التسبيح والأذكار", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Text("ردود فعل لمسية لطيفة مع كل نقرة", fontFamily = CairoFontFamily, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
          Switch(
            checked = vibrationEnabled,
            onCheckedChange = { viewModel.prefs.setVibrationEnabled(it) },
            colors = SwitchDefaults.colors(checkedThumbColor = IslamicGold)
          )
        }
      }
    }

    // 8. Dark Mode toggle
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("الوضع الداكن (الليلي)", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Text("مريح للعينين أثناء القراءة الليلية", fontFamily = CairoFontFamily, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
          Switch(
            checked = isDarkMode,
            onCheckedChange = { viewModel.prefs.setDarkMode(it) },
            colors = SwitchDefaults.colors(checkedThumbColor = IslamicGold)
          )
        }
      }
    }

    // 9. About Noor Al-Islam App
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("عن تطبيق نور الإسلام", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "تطبيق إسلامي احترافي متكامل وشامل: مصحف إلكتروني، مواقيت صلاة، أذان، قبلة، أذكار، سبحة إلكترونية، مكتبة حديث، وقصص وتاريخ إسلامي.",
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "الإصدار 1.0.0 • تقبل الله منا ومنكم صالح الأعمال",
            fontFamily = CairoFontFamily,
            fontSize = 11.sp,
            color = IslamicGold
          )
        }
      }
    }
  }
}

@Composable
private fun SettingsCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(title, fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Text(subtitle, fontFamily = CairoFontFamily, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
      Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
    }
  }
}
