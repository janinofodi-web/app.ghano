package com.example.data.model

data class PrayerTime(
  val nameArabic: String,
  val nameEnglish: String,
  val timeFormatted: String, // e.g. "04:52 ص"
  val hour: Int,
  val minute: Int,
  val isNext: Boolean = false,
  val isCompleted: Boolean = false
)

data class PrayerSchedule(
  val fajr: PrayerTime,
  val sunrise: PrayerTime,
  val dhuhr: PrayerTime,
  val asr: PrayerTime,
  val maghrib: PrayerTime,
  val isha: PrayerTime,
  val qiyam: PrayerTime,
  val duha: PrayerTime,
  val nextPrayerName: String,
  val remainingTimeFormatted: String,
  val locationName: String,
  val hijriDate: String,
  val gregorianDate: String
)

enum class CalculationMethod(val displayNameArabic: String, val fajrAngle: Double, val ishaAngle: Double) {
  UMM_AL_QURA("أم القرى (مكة المكرمة)", 18.5, 90.0), // 90 min after Maghrib
  EGYPTIAN("الهيئة العامة المصرية للمساحة", 19.5, 17.5),
  MUSLIM_WORLD_LEAGUE("رابطة العالم الإسلامي", 18.0, 17.0),
  ISNA("الجمعية الإسلامية لأمريكا الشمالية (ISNA)", 15.0, 15.0),
  KARACHI("جامعة العلوم الإسلامية بكراتشي", 18.0, 18.0)
}

enum class JuristicSchool(val displayNameArabic: String, val shadowFactor: Double) {
  SHAFI_HANBALI_MALIKI("الجمهور (الشافعي، المالكي، الحنبلي)", 1.0),
  HANAFI("المذهب الحنفي", 2.0)
}

data class CityLocation(
  val nameArabic: String,
  val countryArabic: String,
  val latitude: Double,
  val longitude: Double,
  val timezoneOffsetHours: Double
)
