package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.CalculationMethod
import com.example.data.model.CityLocation
import com.example.data.model.JuristicSchool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppPreferencesRepository(context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences("noor_al_islam_prefs", Context.MODE_PRIVATE)

  private val _userName = MutableStateFlow(prefs.getString("user_name", "عبد الله") ?: "عبد الله")
  val userName: StateFlow<String> = _userName.asStateFlow()

  private val _selectedCity = MutableStateFlow(
    CityLocation(
      nameArabic = prefs.getString("city_name", "مكة المكرمة") ?: "مكة المكرمة",
      countryArabic = prefs.getString("city_country", "المملكة العربية السعودية") ?: "المملكة العربية السعودية",
      latitude = prefs.getFloat("city_lat", 21.4225f).toDouble(),
      longitude = prefs.getFloat("city_lng", 39.8262f).toDouble(),
      timezoneOffsetHours = prefs.getFloat("city_tz", 3.0f).toDouble()
    )
  )
  val selectedCity: StateFlow<CityLocation> = _selectedCity.asStateFlow()

  private val _calculationMethod = MutableStateFlow(
    CalculationMethod.valueOf(
      prefs.getString("calc_method", CalculationMethod.UMM_AL_QURA.name) ?: CalculationMethod.UMM_AL_QURA.name
    )
  )
  val calculationMethod: StateFlow<CalculationMethod> = _calculationMethod.asStateFlow()

  private val _juristicSchool = MutableStateFlow(
    JuristicSchool.valueOf(
      prefs.getString("juristic_school", JuristicSchool.SHAFI_HANBALI_MALIKI.name) ?: JuristicSchool.SHAFI_HANBALI_MALIKI.name
    )
  )
  val juristicSchool: StateFlow<JuristicSchool> = _juristicSchool.asStateFlow()

  private val _quranFontSizeSp = MutableStateFlow(prefs.getInt("quran_font_size", 24))
  val quranFontSizeSp: StateFlow<Int> = _quranFontSizeSp.asStateFlow()

  private val _isDarkMode = MutableStateFlow(prefs.getBoolean("dark_mode", false))
  val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

  private val _vibrationEnabled = MutableStateFlow(prefs.getBoolean("vibration_enabled", true))
  val vibrationEnabled: StateFlow<Boolean> = _vibrationEnabled.asStateFlow()

  private val _selectedReciterId = MutableStateFlow(prefs.getString("reciter_id", "alafasy") ?: "alafasy")
  val selectedReciterId: StateFlow<String> = _selectedReciterId.asStateFlow()

  fun setUserName(name: String) {
    prefs.edit().putString("user_name", name).apply()
    _userName.value = name
  }

  fun setCity(city: CityLocation) {
    prefs.edit()
      .putString("city_name", city.nameArabic)
      .putString("city_country", city.countryArabic)
      .putFloat("city_lat", city.latitude.toFloat())
      .putFloat("city_lng", city.longitude.toFloat())
      .putFloat("city_tz", city.timezoneOffsetHours.toFloat())
      .apply()
    _selectedCity.value = city
  }

  fun setCalculationMethod(method: CalculationMethod) {
    prefs.edit().putString("calc_method", method.name).apply()
    _calculationMethod.value = method
  }

  fun setJuristicSchool(school: JuristicSchool) {
    prefs.edit().putString("juristic_school", school.name).apply()
    _juristicSchool.value = school
  }

  fun setQuranFontSize(size: Int) {
    prefs.edit().putInt("quran_font_size", size).apply()
    _quranFontSizeSp.value = size
  }

  fun setDarkMode(enabled: Boolean) {
    prefs.edit().putBoolean("dark_mode", enabled).apply()
    _isDarkMode.value = enabled
  }

  fun setVibrationEnabled(enabled: Boolean) {
    prefs.edit().putBoolean("vibration_enabled", enabled).apply()
    _vibrationEnabled.value = enabled
  }

  fun setReciterId(reciterId: String) {
    prefs.edit().putString("reciter_id", reciterId).apply()
    _selectedReciterId.value = reciterId
  }
}
