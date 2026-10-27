package com.example.data.model

data class UserProfile(
  val userName: String = "عبد الله",
  val avatarId: Int = 1,
  val streakDays: Int = 12,
  val totalSalawatCount: Int = 450,
  val notificationEnabled: Boolean = true,
  val vibrationEnabled: Boolean = true
)

data class DailyWorshipStats(
  val date: String,
  val completedPrayers: List<String> = emptyList(), // ["fajr", "dhuhr", "asr", "maghrib", "isha", "duha", "qiyam"]
  val morningAzkarCompleted: Boolean = false,
  val eveningAzkarCompleted: Boolean = false,
  val quranPagesReadToday: Int = 4,
  val dailyWirdGoalPages: Int = 10,
  val tasbihCountToday: Int = 132
) {
  val overallPercentage: Int
    get() {
      var score = 0
      val prayerScore = (completedPrayers.size.coerceAtMost(5) * 12) // max 60%
      val azkarScore = (if (morningAzkarCompleted) 10 else 0) + (if (eveningAzkarCompleted) 10 else 0) // 20%
      val quranScore = ((quranPagesReadToday.toFloat() / dailyWirdGoalPages.coerceAtLeast(1)) * 20).toInt().coerceAtMost(20)
      score = prayerScore + azkarScore + quranScore
      return score.coerceIn(0, 100)
    }
}

data class IslamicEvent(
  val titleArabic: String,
  val hijriDate: String,
  val description: String,
  val daysRemaining: Int
)
