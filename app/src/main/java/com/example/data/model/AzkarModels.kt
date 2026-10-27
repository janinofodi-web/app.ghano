package com.example.data.model

data class DhikrItem(
  val id: Int,
  val categoryId: String,
  val textArabic: String,
  val referenceOrVirtue: String,
  val targetCount: Int,
  val currentCount: Int = 0,
  val isCompleted: Boolean = false,
  val isFavorite: Boolean = false
)

data class AzkarCategory(
  val id: String,
  val titleArabic: String,
  val subtitle: String,
  val totalItemsCount: Int,
  val completedItemsCount: Int = 0
)

data class TasbihDhikr(
  val id: String,
  val titleArabic: String,
  val defaultTarget: Int = 33,
  val currentCount: Int = 0,
  val totalLifetimeCount: Int = 0
)
