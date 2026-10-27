package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerLogDao {
  @Query("SELECT prayerKey FROM prayer_logs WHERE date = :date AND isCompleted = 1")
  fun getCompletedPrayersForDate(date: String): Flow<List<String>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdate(log: PrayerLogEntity)

  @Query("DELETE FROM prayer_logs WHERE date = :date AND prayerKey = :prayerKey")
  suspend fun removePrayer(date: String, prayerKey: String)
}

@Dao
interface TasbihDao {
  @Query("SELECT * FROM tasbih_counters")
  fun getAllTasbihCounters(): Flow<List<TasbihCounterEntity>>

  @Query("SELECT * FROM tasbih_counters WHERE dhikrId = :id")
  suspend fun getTasbihCounterById(id: String): TasbihCounterEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdate(counter: TasbihCounterEntity)
}

@Dao
interface BookmarkDao {
  @Query("SELECT * FROM quran_bookmarks ORDER BY timestamp DESC")
  fun getAllBookmarks(): Flow<List<QuranBookmarkEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBookmark(bookmark: QuranBookmarkEntity)

  @Query("DELETE FROM quran_bookmarks WHERE id = :id")
  suspend fun deleteBookmark(id: Long)
}
