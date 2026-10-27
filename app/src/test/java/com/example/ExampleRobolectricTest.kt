package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.PrayerTimesCalculator
import com.example.data.repository.QuranRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("نور الإسلام", appName)
  }

  @Test
  fun `verify prayer times calculator for Makkah`() {
    val schedule = PrayerTimesCalculator.calculateSchedule(
      latitude = 21.4225,
      longitude = 39.8262,
      timezoneOffset = 3.0,
      locationName = "مكة المكرمة"
    )
    assertNotNull(schedule.fajr)
    assertNotNull(schedule.dhuhr)
    assertNotNull(schedule.asr)
    assertNotNull(schedule.maghrib)
    assertNotNull(schedule.isha)
    assertTrue(schedule.remainingTimeFormatted.isNotEmpty())
  }

  @Test
  fun `verify Quran 114 surahs exist`() {
    assertEquals(114, QuranRepository.SURAHS.size)
    assertEquals("الفاتحة", QuranRepository.SURAHS[0].nameArabic)
    assertEquals("الناس", QuranRepository.SURAHS[113].nameArabic)
  }
}
