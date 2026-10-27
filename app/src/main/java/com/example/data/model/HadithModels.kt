package com.example.data.model

data class Hadith(
  val id: Int,
  val category: String,
  val bookName: String,
  val chapterName: String,
  val narrator: String,
  val matnArabic: String,
  val explanation: String,
  val practicalBenefits: List<String>,
  val authenticityGrade: String, // "صحيح - متفق عليه", "صحيح البخاري", "صحيح مسلم"
  val hadithNumber: String,
  val isFavorite: Boolean = false
)

enum class HadithCategory(val titleArabic: String, val iconName: String) {
  ALL("الكل", "all"),
  ETHICS("الأخلاق والآداب", "ethics"),
  WORSHIP("الصلاة والعبادة", "worship"),
  PARENTS("بر الوالدين والصلة", "parents"),
  CHARITY("الصدقة والإنفاق", "charity"),
  REPENTANCE("التوبة والاستغفار", "repentance"),
  PARADISE("الجنة ونعيمها", "paradise"),
  PATIENCE("الصبر واليقين", "patience")
}
