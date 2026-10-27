package com.example.data.model

data class SurahMeta(
  val number: Int,
  val nameArabic: String,
  val nameEnglish: String,
  val revelationType: String, // "مكية" or "مدنية"
  val ayahCount: Int,
  val page: Int,
  val juz: Int
)

data class Ayah(
  val numberInSurah: Int,
  val textUthmani: String,
  val surahNumber: Int,
  val page: Int,
  val juz: Int,
  val tafseer: String,
  val audioUrl: String? = null
)

data class QuranReciter(
  val id: String,
  val nameArabic: String,
  val subtext: String,
  val serverSubfolder: String
)

data class QuranBookmark(
  val id: Long = 0,
  val surahNumber: Int,
  val surahName: String,
  val ayahNumber: Int,
  val timestamp: Long,
  val note: String = ""
)

data class KhatmaStatus(
  val currentKhatmaId: Long = 1,
  val targetDays: Int = 30,
  val completedPages: Int = 45,
  val totalPages: Int = 604,
  val dailyGoalPages: Int = 20,
  val lastReadSurahNumber: Int = 2,
  val lastReadSurahName: String = "البقرة",
  val lastReadAyahNumber: Int = 255
)
