package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_logs")
data class PrayerLogEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val date: String, // e.g. "2026-10-26"
  val prayerKey: String, // "fajr", "dhuhr", "asr", "maghrib", "isha", "duha", "qiyam"
  val isCompleted: Boolean = true,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasbih_counters")
data class TasbihCounterEntity(
  @PrimaryKey val dhikrId: String,
  val titleArabic: String,
  val currentCount: Int,
  val totalLifetimeCount: Int,
  val target: Int = 33,
  val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "quran_bookmarks")
data class QuranBookmarkEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val surahNumber: Int,
  val surahName: String,
  val ayahNumber: Int,
  val timestamp: Long = System.currentTimeMillis(),
  val note: String = ""
)
