package com.example.data.repository

import com.example.data.model.CalculationMethod
import com.example.data.model.CityLocation
import com.example.data.model.JuristicSchool
import com.example.data.model.PrayerSchedule
import com.example.data.model.PrayerTime
import java.util.Calendar
import java.util.Locale
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.tan

object PrayerTimesCalculator {

  const val MAKKAH_LATITUDE = 21.422524
  const val MAKKAH_LONGITUDE = 39.826182

  val POPULAR_CITIES = listOf(
    CityLocation("مكة المكرمة", "المملكة العربية السعودية", 21.4225, 39.8262, 3.0),
    CityLocation("المدينة المنورة", "المملكة العربية السعودية", 24.4672, 39.6111, 3.0),
    CityLocation("الرياض", "المملكة العربية السعودية", 24.7136, 46.6753, 3.0),
    CityLocation("جدة", "المملكة العربية السعودية", 21.5433, 39.1728, 3.0),
    CityLocation("القاهرة", "مصر", 30.0444, 31.2357, 2.0),
    CityLocation("الإسكندرية", "مصر", 31.2001, 29.9187, 2.0),
    CityLocation("القدس الشريف", "فلسطين", 31.7683, 35.2137, 2.0),
    CityLocation("عمّان", "الأردن", 31.9454, 35.9284, 3.0),
    CityLocation("دبي", "الإمارات العربية المتحدة", 25.2048, 55.2708, 4.0),
    CityLocation("أبوظبي", "الإمارات العربية المتحدة", 24.4539, 54.3773, 4.0),
    CityLocation("الدوحة", "قطر", 25.2854, 51.5310, 3.0),
    CityLocation("الكويت", "الكويت", 29.3759, 47.9774, 3.0),
    CityLocation("المنامة", "البحرين", 26.2285, 50.5860, 3.0),
    CityLocation("مسقط", "عُمان", 23.5859, 58.4059, 4.0),
    CityLocation("بغداد", "العراق", 33.3152, 44.3661, 3.0),
    CityLocation("دمشق", "سوريا", 33.5138, 36.2765, 3.0),
    CityLocation("بيروت", "لبنان", 33.8938, 35.5018, 2.0),
    CityLocation("الرباط", "المغرب", 34.0209, -6.8416, 1.0),
    CityLocation("الدار البيضاء", "المغرب", 33.5731, -7.5898, 1.0),
    CityLocation("تونس", "تونس", 36.8065, 10.1815, 1.0),
    CityLocation("الجزائر", "الجزائر", 36.7538, 3.0588, 1.0),
    CityLocation("طرابلس", "ليبيا", 32.8872, 13.1913, 2.0),
    CityLocation("الخرطوم", "السودان", 15.5007, 32.5599, 2.0),
    CityLocation("صنعاء", "اليمن", 15.3694, 44.1910, 3.0),
    CityLocation("إسطنبول", "تركيا", 41.0082, 28.9784, 3.0),
    CityLocation("لندن", "المملكة المتحدة", 51.5074, -0.1278, 0.0),
    CityLocation("باريس", "فرنسا", 48.8566, 2.3522, 1.0),
    CityLocation("نيويورك", "الولايات المتحدة", 40.7128, -74.0060, -5.0)
  )

  fun calculateSchedule(
    latitude: Double,
    longitude: Double,
    timezoneOffset: Double,
    calendar: Calendar = Calendar.getInstance(),
    method: CalculationMethod = CalculationMethod.UMM_AL_QURA,
    juristicSchool: JuristicSchool = JuristicSchool.SHAFI_HANBALI_MALIKI,
    locationName: String = "مكة المكرمة"
  ): PrayerSchedule {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val d = julianDate(year, month, day) - 2451545.0

    // Mean anomaly and longitude of the Sun
    val g = fixAngle(357.529 + 0.98560028 * d)
    val q = fixAngle(280.459 + 0.98564736 * d)
    val l = fixAngle(q + 1.915 * sin(Math.toRadians(g)) + 0.020 * sin(Math.toRadians(2 * g)))

    // Obliquity of the ecliptic
    val e = 23.439 - 0.00000036 * d

    // Declination and Right Ascension
    val declination = Math.toDegrees(asin(sin(Math.toRadians(e)) * sin(Math.toRadians(l))))
    var ra = Math.toDegrees(atan2(cos(Math.toRadians(e)) * sin(Math.toRadians(l)), cos(Math.toRadians(l)))) / 15.0
    ra = fixHour(ra)

    // Equation of Time in hours
    val eqTime = q / 15.0 - ra

    // Solar noon (Dhuhr)
    val noon = fixHour(12.0 + timezoneOffset - longitude / 15.0 - eqTime)

    // Sun altitude for sunrise/sunset (refraction + sun radius: 0.833 deg)
    val sunriseHourAngle = hourAngle(-0.833, latitude, declination)
    val sunrise = fixHour(noon - sunriseHourAngle / 15.0)
    val sunset = fixHour(noon + sunriseHourAngle / 15.0)

    // Fajr
    val fajrHourAngle = hourAngle(-method.fajrAngle, latitude, declination)
    val fajr = fixHour(noon - fajrHourAngle / 15.0)

    // Asr
    val shadowFactor = juristicSchool.shadowFactor
    val asrAlt = Math.toDegrees(atan2(1.0, shadowFactor + tan(Math.toRadians(Math.abs(latitude - declination)))))
    val asrHourAngle = hourAngle(asrAlt, latitude, declination)
    val asr = fixHour(noon + asrHourAngle / 15.0)

    // Maghrib (same as sunset or sunset + small buffer)
    val maghrib = sunset

    // Isha
    val isha: Double = if (method == CalculationMethod.UMM_AL_QURA) {
      fixHour(maghrib + 1.5) // 90 minutes after Maghrib
    } else {
      val ishaHourAngle = hourAngle(-method.ishaAngle, latitude, declination)
      fixHour(noon + ishaHourAngle / 15.0)
    }

    // Duha: ~20 minutes after sunrise
    val duha = fixHour(sunrise + (20.0 / 60.0))

    // Qiyam (last third of the night): between Maghrib and Fajr
    val nightDuration = fixHour(fajr - maghrib + 24.0)
    val qiyam = fixHour(maghrib + (nightDuration * (2.0 / 3.0)))

    // Construct prayer objects
    val fajrObj = toPrayerTime("الفجر", "Fajr", fajr)
    val sunriseObj = toPrayerTime("الشروق", "Sunrise", sunrise)
    val dhuhrObj = toPrayerTime("الظهر", "Dhuhr", noon)
    val asrObj = toPrayerTime("العصر", "Asr", asr)
    val maghribObj = toPrayerTime("المغرب", "Maghrib", maghrib)
    val ishaObj = toPrayerTime("العشاء", "Isha", isha)
    val duhaObj = toPrayerTime("الضحى", "Duha", duha)
    val qiyamObj = toPrayerTime("قيام الليل", "Qiyam", qiyam)

    // Find next prayer and remaining time
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)
    val currentTotalMinutes = currentHour * 60 + currentMinute

    val prayerList = listOf(
      "الفجر" to fajrObj,
      "الشروق" to sunriseObj,
      "الظهر" to dhuhrObj,
      "العصر" to asrObj,
      "المغرب" to maghribObj,
      "العشاء" to ishaObj
    )

    var nextPrayerName = "الفجر"
    var remainingMinutes = 0
    var nextFound = false

    for ((name, pTime) in prayerList) {
      val pTotal = pTime.hour * 60 + pTime.minute
      if (pTotal > currentTotalMinutes) {
        nextPrayerName = name
        remainingMinutes = pTotal - currentTotalMinutes
        nextFound = true
        break
      }
    }

    if (!nextFound) {
      // After Isha, next is tomorrow's Fajr
      val fajrTotal = fajrObj.hour * 60 + fajrObj.minute
      remainingMinutes = (24 * 60 - currentTotalMinutes) + fajrTotal
      nextPrayerName = "الفجر"
    }

    val remHours = remainingMinutes / 60
    val remMins = remainingMinutes % 60
    val remFormatted = if (remHours > 0) {
      "$remHours ساعة و $remMins دقيقة"
    } else {
      "$remMins دقيقة"
    }

    val hijriString = approximateHijriDate(calendar)
    val gregorianString = String.format(
      Locale.getDefault(),
      "%d %s %d",
      day,
      arabicMonthName(month),
      year
    )

    return PrayerSchedule(
      fajr = fajrObj.copy(isNext = nextPrayerName == "الفجر"),
      sunrise = sunriseObj.copy(isNext = nextPrayerName == "الشروق"),
      dhuhr = dhuhrObj.copy(isNext = nextPrayerName == "الظهر"),
      asr = asrObj.copy(isNext = nextPrayerName == "العصر"),
      maghrib = maghribObj.copy(isNext = nextPrayerName == "المغرب"),
      isha = ishaObj.copy(isNext = nextPrayerName == "العشاء"),
      qiyam = qiyamObj,
      duha = duhaObj,
      nextPrayerName = nextPrayerName,
      remainingTimeFormatted = remFormatted,
      locationName = locationName,
      hijriDate = hijriString,
      gregorianDate = gregorianString
    )
  }

  fun calculateQiblaBearing(lat: Double, lng: Double): Double {
    val phi1 = Math.toRadians(lat)
    val phi2 = Math.toRadians(MAKKAH_LATITUDE)
    val deltaLambda = Math.toRadians(MAKKAH_LONGITUDE - lng)

    val y = sin(deltaLambda) * cos(phi2)
    val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)
    var bearing = Math.toDegrees(atan2(y, x))
    bearing = (bearing + 360.0) % 360.0
    return bearing
  }

  fun calculateDistanceToMakkahKm(lat: Double, lng: Double): Int {
    val earthRadiusKm = 6371.0
    val dLat = Math.toRadians(MAKKAH_LATITUDE - lat)
    val dLon = Math.toRadians(MAKKAH_LONGITUDE - lng)

    val a = sin(dLat / 2) * sin(dLat / 2) +
      cos(Math.toRadians(lat)) * cos(Math.toRadians(MAKKAH_LATITUDE)) *
      sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(Math.sqrt(a), Math.sqrt(1 - a))
    return (earthRadiusKm * c).toInt()
  }

  private fun toPrayerTime(nameAr: String, nameEn: String, hourDouble: Double): PrayerTime {
    val totalMins = (hourDouble * 60.0).toInt() % (24 * 60)
    val hr = (totalMins / 60)
    val min = totalMins % 60
    val displayHr = if (hr % 12 == 0) 12 else hr % 12
    val amPm = if (hr < 12) "ص" else "م"
    val formatted = String.format(Locale.getDefault(), "%02d:%02d %s", displayHr, min, amPm)
    return PrayerTime(
      nameArabic = nameAr,
      nameEnglish = nameEn,
      timeFormatted = formatted,
      hour = hr,
      minute = min
    )
  }

  private fun julianDate(year: Int, month: Int, day: Int): Double {
    var y = year
    var m = month
    if (m <= 2) {
      y -= 1
      m += 12
    }
    val a = floor(y / 100.0)
    val b = 2 - a + floor(a / 4.0)
    return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
  }

  private fun hourAngle(altitude: Double, latitude: Double, declination: Double): Double {
    val latRad = Math.toRadians(latitude)
    val decRad = Math.toRadians(declination)
    val altRad = Math.toRadians(altitude)

    val cosHA = (sin(altRad) - sin(latRad) * sin(decRad)) / (cos(latRad) * cos(decRad))
    val clamped = cosHA.coerceIn(-1.0, 1.0)
    return Math.toDegrees(acos(clamped))
  }

  private fun fixAngle(angle: Double): Double {
    var a = angle - 360.0 * floor(angle / 360.0)
    if (a < 0) a += 360.0
    return a
  }

  private fun fixHour(hour: Double): Double {
    var h = hour - 24.0 * floor(hour / 24.0)
    if (h < 0) h += 24.0
    return h
  }

  private fun approximateHijriDate(calendar: Calendar): String {
    val gYear = calendar.get(Calendar.YEAR)
    val gMonth = calendar.get(Calendar.MONTH) + 1
    val gDay = calendar.get(Calendar.DAY_OF_MONTH)
    val jd = julianDate(gYear, gMonth, gDay)

    val l = jd - 1948440 + 10632
    val n = floor((l - 1) / 10631.0)
    val l1 = l - 10631 * n + 354
    val j = (floor((10985 - l1) / 5316.0)) * (floor((50 * l1) / 17719.0)) +
      (floor(l1 / 5670.0)) * (floor((43 * l1) / 15238.0))
    val l2 = l1 - (floor((30 - j) / 15.0)) * (floor((17719 * j) / 50.0)) -
      (floor(j / 16.0)) * (floor((15238 * j) / 43.0)) + 29
    val m = floor((24 * l2) / 709.0).toInt()
    val d = (l2 - floor((709.0 * m) / 24.0)).toInt()
    val y = (30 * n + j - 30).toInt()

    val hijriMonths = listOf(
      "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
      "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
      "رمضان", "شوّال", "ذو القعدة", "ذو الحجة"
    )
    val monthIndex = (m - 1).coerceIn(0, 11)
    val monthName = hijriMonths[monthIndex]
    return "$d $monthName $y هـ"
  }

  private fun arabicMonthName(month: Int): String {
    return when (month) {
      1 -> "يناير"
      2 -> "فبراير"
      3 -> "مارس"
      4 -> "أبريل"
      5 -> "مايو"
      6 -> "يونيو"
      7 -> "يوليو"
      8 -> "أغسطس"
      9 -> "سبتمبر"
      10 -> "أكتوبر"
      11 -> "نوفمبر"
      12 -> "ديسمبر"
      else -> ""
    }
  }
}
