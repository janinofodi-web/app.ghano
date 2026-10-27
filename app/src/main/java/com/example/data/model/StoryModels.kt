package com.example.data.model

data class IslamicStory(
  val id: String,
  val category: StoryCategory,
  val titleArabic: String,
  val subtitle: String,
  val summary: String,
  val contentParagraphs: List<String>,
  val lessonsLearned: List<String>,
  val readingTimeMinutes: Int,
  val isBookmarked: Boolean = false
)

enum class StoryCategory(val titleArabic: String) {
  PROPHETS("قصص الأنبياء"),
  SEERAH("السيرة النبوية ﷺ"),
  COMPANIONS("سير الصحابة والصحابيات"),
  SCHOLARS("أئمة العلم والتابعين")
}
