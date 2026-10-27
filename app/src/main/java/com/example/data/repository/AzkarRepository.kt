package com.example.data.repository

import com.example.data.model.AzkarCategory
import com.example.data.model.DhikrItem

object AzkarRepository {

  val CATEGORIES = listOf(
    AzkarCategory("morning", "أذكار الصباح", "حصن المسلم لبداية يوم مبارك", 8),
    AzkarCategory("evening", "أذكار المساء", "طمأنينة وحفظ من الغروب حتى الصباح", 8),
    AzkarCategory("after_prayer", "أذكار بعد الصلاة", "الاستغفار والتسبيح بعد السلام", 6),
    AzkarCategory("sleep", "أذكار النوم والاستيقاظ", "آيات وأذكار قبل المنام وعند الفجر", 6),
    AzkarCategory("quranic_duas", "أدعية من القرآن الكريم", "جوامع الدعاء من كتاب الله", 6),
    AzkarCategory("prophetic_duas", "أدعية نبوية مأثورة", "دعوات النبي ﷺ المستجابة", 6),
    AzkarCategory("home_mosque", "أذكار المسجد والمنزل", "دعاء الدخول والخروج وخطوات المسجد", 5),
    AzkarCategory("distress", "أدعية الكرب والاستغفار", "لتفريج الهموم وزيادة الرزق", 5)
  )

  val ALL_AZKAR: Map<String, List<DhikrItem>> = mapOf(
    "morning" to listOf(
      DhikrItem(
        id = 101,
        categoryId = "morning",
        textArabic = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لاَ إِلَـهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
        referenceOrVirtue = "صحيح مسلم - من قاله حين يصبح أُعطي خير ما في هذا اليوم وخير ما بعده",
        targetCount = 1
      ),
      DhikrItem(
        id = 102,
        categoryId = "morning",
        textArabic = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَـهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ.",
        referenceOrVirtue = "سيد الاستغفار - من قالها موقنًا بها حين يصبح فمات من يومه دخل الجنة (البخاري)",
        targetCount = 1
      ),
      DhikrItem(
        id = 103,
        categoryId = "morning",
        textArabic = "رَضِيتُ بِاللهِ رَبًّا، وَبِالإِسْلاَمِ دِينًا، وَبِمُحَمَّدٍ ﷺ نَبِيًّا.",
        referenceOrVirtue = "من قالها ثلاثًا حين يصبح وثلاثًا حين يمسي كان حقًا على الله أن يرضيه (أبو داود والترمذي)",
        targetCount = 3
      ),
      DhikrItem(
        id = 104,
        categoryId = "morning",
        textArabic = "بِسْمِ اللهِ الَّذِي لاَ يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلاَ فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ.",
        referenceOrVirtue = "ثلاث مرات - لم يضره شيء (أبو داود والترمذي)",
        targetCount = 3
      ),
      DhikrItem(
        id = 105,
        categoryId = "morning",
        textArabic = "سُبْحَانَ اللهِ وَبِحَمْدِهِ: عَدَدَ خَلْقِهِ، وَرِضَا نَفْسِهِ، وَزِنَةَ عَرْشِهِ، وَمِدَادَ كَلِمَاتِهِ.",
        referenceOrVirtue = "ثلاث مرات - تعدل ساعات من الذكر المتواصل (صحيح مسلم)",
        targetCount = 3
      ),
      DhikrItem(
        id = 106,
        categoryId = "morning",
        textArabic = "يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ، وَلاَ تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ.",
        referenceOrVirtue = "الحاكم وصححه الألباني",
        targetCount = 1
      ),
      DhikrItem(
        id = 107,
        categoryId = "morning",
        textArabic = "سُبْحَانَ اللهِ وَبِحَمْدِهِ.",
        referenceOrVirtue = "مائة مرة - حُطّت خطاياه وإن كانت مثل زبد البحر (متفق عليه)",
        targetCount = 100
      ),
      DhikrItem(
        id = 108,
        categoryId = "morning",
        textArabic = "لاَ إِلَـهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
        referenceOrVirtue = "عشر مرات أو مائة مرة - كانت له عدل عشر رقاب وكُتبت له مائة حسنة (متفق عليه)",
        targetCount = 10
      )
    ),
    "evening" to listOf(
      DhikrItem(
        id = 201,
        categoryId = "evening",
        textArabic = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لاَ إِلَـهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
        referenceOrVirtue = "صحيح مسلم - حصن المسلم في المساء",
        targetCount = 1
      ),
      DhikrItem(
        id = 202,
        categoryId = "evening",
        textArabic = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَـهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ.",
        referenceOrVirtue = "سيد الاستغفار - من قالها حين يمسي فمات من ليلته دخل الجنة (البخاري)",
        targetCount = 1
      ),
      DhikrItem(
        id = 203,
        categoryId = "evening",
        textArabic = "أَعُوذُ بِكَلِمَاتِ اللهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ.",
        referenceOrVirtue = "ثلاث مرات - لم تضره حمة أو لدغة عقرب تلك الليلة (صحيح مسلم)",
        targetCount = 3
      ),
      DhikrItem(
        id = 204,
        categoryId = "evening",
        textArabic = "بِسْمِ اللهِ الَّذِي لاَ يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلاَ فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ.",
        referenceOrVirtue = "ثلاث مرات - حرز من كل سوء ومكروه (الترمذي)",
        targetCount = 3
      ),
      DhikrItem(
        id = 205,
        categoryId = "evening",
        textArabic = "رَضِيتُ بِاللهِ رَبًّا، وَبِالإِسْلاَمِ دِينًا، وَبِمُحَمَّدٍ ﷺ نَبِيًّا.",
        referenceOrVirtue = "ثلاث مرات - كان حقًا على الله أن يرضيه يوم القيامة",
        targetCount = 3
      ),
      DhikrItem(
        id = 206,
        categoryId = "evening",
        textArabic = "اللَّهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ الْمَصِيرُ.",
        referenceOrVirtue = "صحيح الترمذي",
        targetCount = 1
      ),
      DhikrItem(
        id = 207,
        categoryId = "evening",
        textArabic = "سُبْحَانَ اللهِ وَبِحَمْدِهِ.",
        referenceOrVirtue = "مائة مرة - لم يأت أحد يوم القيامة بأفضل مما جاء به إلا أحد قال مثل ما قال أو زاد (مسلم)",
        targetCount = 100
      ),
      DhikrItem(
        id = 208,
        categoryId = "evening",
        textArabic = "اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي، لاَ إِلَـهَ إِلاَّ أَنْتَ.",
        referenceOrVirtue = "ثلاث مرات - دعاء العافية والستر (أبو داود)",
        targetCount = 3
      )
    ),
    "after_prayer" to listOf(
      DhikrItem(
        id = 301,
        categoryId = "after_prayer",
        textArabic = "أَسْتَغْفِرُ اللهَ، أَسْتَغْفِرُ اللهَ، أَسْتَغْفِرُ اللهَ.",
        referenceOrVirtue = "ثلاث مرات بعد التسليم مباشرة (صحيح مسلم)",
        targetCount = 3
      ),
      DhikrItem(
        id = 302,
        categoryId = "after_prayer",
        textArabic = "اللَّهُمَّ أَنْتَ السَّلاَمُ وَمِنْكَ السَّلاَمُ، تَبَارَكْتَ يَا ذَا الْجَلاَلِ وَالإِكْرَامِ.",
        referenceOrVirtue = "صحيح مسلم",
        targetCount = 1
      ),
      DhikrItem(
        id = 303,
        categoryId = "after_prayer",
        textArabic = "لاَ إِلَـهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، اللَّهُمَّ لاَ مَانِعَ لِمَا أَعْطَيْتَ، وَلاَ مُعْطِيَ لِمَا مَنَعْتَ، وَلاَ يَنْفَعُ ذَا الْجَدِّ مِنْكَ الْجَدُّ.",
        referenceOrVirtue = "متفق عليه",
        targetCount = 1
      ),
      DhikrItem(
        id = 304,
        categoryId = "after_prayer",
        textArabic = "سُبْحَانَ اللهِ (33)، الْحَمْدُ لِلَّهِ (33)، اللهُ أَكْبَرُ (33).",
        referenceOrVirtue = "التسبيح دبر كل صلاة مكتوبة (صحيح مسلم)",
        targetCount = 33
      ),
      DhikrItem(
        id = 305,
        categoryId = "after_prayer",
        textArabic = "لاَ إِلَـهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ (تمام المائة).",
        referenceOrVirtue = "غُفرت خطاياه وإن كانت مثل زبد البحر (صحيح مسلم)",
        targetCount = 1
      ),
      DhikrItem(
        id = 306,
        categoryId = "after_prayer",
        textArabic = "قراءة آية الكرسي: (اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ...)",
        referenceOrVirtue = "من قرأها دبر كل صلاة مكتوبة لم يمنعه من دخول الجنة إلا أن يموت (النسائي)",
        targetCount = 1
      )
    ),
    "quranic_duas" to listOf(
      DhikrItem(
        id = 401,
        categoryId = "quranic_duas",
        textArabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ.",
        referenceOrVirtue = "سورة البقرة: 201 - أكثر دعاء النبي ﷺ",
        targetCount = 3
      ),
      DhikrItem(
        id = 402,
        categoryId = "quranic_duas",
        textArabic = "رَبَّنَا لاَ تُزِغْ قُلُوبَنَا بَعْدَ إِذْ هَدَيْتَنَا وَهَبْ لَنَا مِن لَّدُنكَ رَحْمَةً إِنَّكَ أَنتَ الْوَهَّابُ.",
        referenceOrVirtue = "سورة آل عمران: 8 - دعاء الثبات على الإيمان",
        targetCount = 1
      ),
      DhikrItem(
        id = 403,
        categoryId = "quranic_duas",
        textArabic = "رَبِّ اشْرَحْ لِي صَدْرِي وَيَسِّرْ لِي أَمْرِي وَاحْلُلْ عُقْدَةً مِّن لِّسَانِي يَفْقَهُوا قَوْلِي.",
        referenceOrVirtue = "سورة طه: 25-28 - دعاء تيسير الأمور والبيان",
        targetCount = 1
      ),
      DhikrItem(
        id = 404,
        categoryId = "quranic_duas",
        textArabic = "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا.",
        referenceOrVirtue = "سورة الإسراء: 24 - دعاء الوالدين",
        targetCount = 3
      ),
      DhikrItem(
        id = 405,
        categoryId = "quranic_duas",
        textArabic = "رَبِّ هَبْ لِي مِن لَّدُنكَ ذُرِّيَّةً طَيِّبَةً إِنَّكَ سَمِيعُ الدُّعَاءِ.",
        referenceOrVirtue = "سورة آل عمران: 38 - دعاء زكريا عليه السلام للذرية الصالحة",
        targetCount = 1
      ),
      DhikrItem(
        id = 406,
        categoryId = "quranic_duas",
        textArabic = "لَّا إِلَـهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ.",
        referenceOrVirtue = "سورة الأنبياء: 87 - دعاء ذي النون، لم يدعُ به مكروب إلا استجيب له",
        targetCount = 3
      )
    )
  )

  fun getAzkarForCategory(categoryId: String): List<DhikrItem> {
    return ALL_AZKAR[categoryId] ?: ALL_AZKAR["morning"] ?: emptyList()
  }
}
