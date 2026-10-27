package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.PrayerLogEntity
import com.example.data.local.QuranBookmarkEntity
import com.example.data.local.TasbihCounterEntity
import com.example.data.model.Ayah
import com.example.data.model.CalculationMethod
import com.example.data.model.CityLocation
import com.example.data.model.DhikrItem
import com.example.data.model.Hadith
import com.example.data.model.IslamicStory
import com.example.data.model.JuristicSchool
import com.example.data.model.PrayerSchedule
import com.example.data.model.SurahMeta
import com.example.data.model.TasbihDhikr
import com.example.data.repository.AppPreferencesRepository
import com.example.data.repository.AzkarRepository
import com.example.data.repository.HadithRepository
import com.example.data.repository.PrayerTimesCalculator
import com.example.data.repository.QuranRepository
import com.example.data.repository.StoriesRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AppDestination(val titleArabic: String) {
  HOME("الرئيسية"),
  PRAYER("الصلاة والقبلة"),
  QURAN("القرآن"),
  AZKAR("الأذكار"),
  TASBIH("السبحة"),
  MORE("المزيد")
}

enum class SubScreen {
  NONE,
  SURAH_READER,
  MEMORIZATION_TEST,
  HADITH_LIBRARY,
  STORIES_LIBRARY,
  STORY_DETAIL,
  QIBLA_COMPASS,
  SETTINGS
}

class AppViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {

  private val database = AppDatabase.getDatabase(application)
  val prefs = AppPreferencesRepository(application)

  private val sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
  private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    val vibratorManager = application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
    vibratorManager?.defaultVibrator
  } else {
    @Suppress("DEPRECATION")
    application.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
  }

  // Navigation state
  private val _currentDestination = MutableStateFlow(AppDestination.HOME)
  val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

  private val _currentSubScreen = MutableStateFlow(SubScreen.NONE)
  val currentSubScreen: StateFlow<SubScreen> = _currentSubScreen.asStateFlow()

  // Selected details
  private val _selectedSurah = MutableStateFlow(QuranRepository.SURAHS[0])
  val selectedSurah: StateFlow<SurahMeta> = _selectedSurah.asStateFlow()

  private val _selectedStory = MutableStateFlow(StoriesRepository.STORIES[0])
  val selectedStory: StateFlow<IslamicStory> = _selectedStory.asStateFlow()

  private val _selectedHadith = MutableStateFlow(HadithRepository.ALL_HADITHS[0])
  val selectedHadith: StateFlow<Hadith> = _selectedHadith.asStateFlow()

  // Prayer state
  private val _prayerSchedule = MutableStateFlow<PrayerSchedule?>(null)
  val prayerSchedule: StateFlow<PrayerSchedule?> = _prayerSchedule.asStateFlow()

  private val _completedPrayersToday = MutableStateFlow<Set<String>>(emptySet())
  val completedPrayersToday: StateFlow<Set<String>> = _completedPrayersToday.asStateFlow()

  // Qibla orientation state (degrees)
  private val _compassAzimuth = MutableStateFlow(0f)
  val compassAzimuth: StateFlow<Float> = _compassAzimuth.asStateFlow()

  val qiblaBearing: Double
    get() {
      val city = prefs.selectedCity.value
      return PrayerTimesCalculator.calculateQiblaBearing(city.latitude, city.longitude)
    }

  val distanceToMakkahKm: Int
    get() {
      val city = prefs.selectedCity.value
      return PrayerTimesCalculator.calculateDistanceToMakkahKm(city.latitude, city.longitude)
    }

  // Tasbih state
  val tasbihPresets = listOf(
    TasbihDhikr("subhan_allah", "سُبْحَانَ اللَّهِ", 33),
    TasbihDhikr("alhamdulillah", "الْحَمْدُ لِلَّهِ", 33),
    TasbihDhikr("allahu_akbar", "اللَّهُ أَكْبَرُ", 33),
    TasbihDhikr("la_ilaha_illa_allah", "لَا إِلَٰهَ إِلَّا اللَّهُ", 100),
    TasbihDhikr("astaghfirullah", "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ", 100),
    TasbihDhikr("salawat", "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَىٰ نَبِيِّنَا مُحَمَّدٍ", 100),
    TasbihDhikr("hawqala", "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ", 33),
    TasbihDhikr("subhan_wa_bihamdihi", "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ سُبْحَانَ اللَّهِ الْعَظِيمِ", 100)
  )

  private val _selectedTasbihDhikr = MutableStateFlow(tasbihPresets[0])
  val selectedTasbihDhikr: StateFlow<TasbihDhikr> = _selectedTasbihDhikr.asStateFlow()

  private val _tasbihCurrentCount = MutableStateFlow(0)
  val tasbihCurrentCount: StateFlow<Int> = _tasbihCurrentCount.asStateFlow()

  private val _tasbihTotalCount = MutableStateFlow(142)
  val tasbihTotalCount: StateFlow<Int> = _tasbihTotalCount.asStateFlow()

  private val _tasbihTarget = MutableStateFlow(33)
  val tasbihTarget: StateFlow<Int> = _tasbihTarget.asStateFlow()

  // Azkar counters map (dhikrId to currentCount)
  private val _azkarCounts = MutableStateFlow<Map<Int, Int>>(emptyMap())
  val azkarCounts: StateFlow<Map<Int, Int>> = _azkarCounts.asStateFlow()

  private val _selectedAzkarCategory = MutableStateFlow("morning")
  val selectedAzkarCategory: StateFlow<String> = _selectedAzkarCategory.asStateFlow()

  // Salawat counter on Home Screen
  private val _homeSalawatCount = MutableStateFlow(33)
  val homeSalawatCount: StateFlow<Int> = _homeSalawatCount.asStateFlow()

  // Audio player state
  private var mediaPlayer: MediaPlayer? = null
  private val _isAudioPlaying = MutableStateFlow(false)
  val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying.asStateFlow()

  private var timerJob: Job? = null

  init {
    loadTodayPrayerLogs()
    refreshPrayerSchedule()
    startPrayerTimer()
    registerCompassSensor()
  }

  fun navigateTo(dest: AppDestination) {
    _currentDestination.value = dest
    _currentSubScreen.value = SubScreen.NONE
    stopAudio()
  }

  fun navigateToSubScreen(subScreen: SubScreen) {
    _currentSubScreen.value = subScreen
  }

  fun navigateBack() {
    if (_currentSubScreen.value != SubScreen.NONE) {
      _currentSubScreen.value = SubScreen.NONE
      stopAudio()
    }
  }

  fun openSurah(surah: SurahMeta) {
    _selectedSurah.value = surah
    _currentSubScreen.value = SubScreen.SURAH_READER
  }

  fun openStory(story: IslamicStory) {
    _selectedStory.value = story
    _currentSubScreen.value = SubScreen.STORY_DETAIL
  }

  fun selectAzkarCategory(catId: String) {
    _selectedAzkarCategory.value = catId
  }

  fun selectTasbihDhikr(dhikr: TasbihDhikr) {
    _selectedTasbihDhikr.value = dhikr
    _tasbihTarget.value = dhikr.defaultTarget
    _tasbihCurrentCount.value = 0
  }

  fun incrementTasbih() {
    vibrateFeedback()
    val target = _tasbihTarget.value
    val nextCount = _tasbihCurrentCount.value + 1
    if (target > 0 && nextCount >= target) {
      _tasbihCurrentCount.value = target
      // Stronger vibration on completing lap
      vibrateLapFinished()
    } else {
      _tasbihCurrentCount.value = nextCount
    }
    _tasbihTotalCount.value += 1
  }

  fun resetTasbih() {
    _tasbihCurrentCount.value = 0
  }

  fun setTasbihTarget(newTarget: Int) {
    _tasbihTarget.value = newTarget
    _tasbihCurrentCount.value = 0
  }

  fun incrementHomeSalawat() {
    vibrateFeedback()
    _homeSalawatCount.value += 1
  }

  fun incrementDhikrCount(item: DhikrItem) {
    vibrateFeedback()
    val current = _azkarCounts.value[item.id] ?: 0
    if (current < item.targetCount) {
      val updated = current + 1
      _azkarCounts.value = _azkarCounts.value.toMutableMap().apply {
        put(item.id, updated)
      }
      if (updated >= item.targetCount) {
        vibrateLapFinished()
      }
    }
  }

  fun resetDhikrCount(item: DhikrItem) {
    _azkarCounts.value = _azkarCounts.value.toMutableMap().apply {
      put(item.id, 0)
    }
  }

  fun togglePrayerCompleted(prayerKey: String) {
    val currentSet = _completedPrayersToday.value.toMutableSet()
    val todayDate = getTodayDateString()
    viewModelScope.launch {
      if (currentSet.contains(prayerKey)) {
        currentSet.remove(prayerKey)
        database.prayerLogDao().removePrayer(todayDate, prayerKey)
      } else {
        currentSet.add(prayerKey)
        database.prayerLogDao().insertOrUpdate(
          PrayerLogEntity(date = todayDate, prayerKey = prayerKey, isCompleted = true)
        )
      }
      _completedPrayersToday.value = currentSet
    }
  }

  fun addBookmark(ayah: Ayah, surah: SurahMeta, note: String = "") {
    viewModelScope.launch {
      database.bookmarkDao().insertBookmark(
        QuranBookmarkEntity(
          surahNumber = surah.number,
          surahName = surah.nameArabic,
          ayahNumber = ayah.numberInSurah,
          note = note
        )
      )
    }
  }

  fun playSurahAudio(surahNumber: Int) {
    try {
      stopAudio()
      val reciterId = prefs.selectedReciterId.value
      val url = QuranRepository.getAudioStreamUrl(reciterId, surahNumber)
      mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
          AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        )
        setDataSource(url)
        setOnPreparedListener {
          it.start()
          _isAudioPlaying.value = true
        }
        setOnCompletionListener {
          _isAudioPlaying.value = false
        }
        prepareAsync()
      }
    } catch (e: Exception) {
      _isAudioPlaying.value = false
    }
  }

  fun toggleAudioPlayback() {
    mediaPlayer?.let { player ->
      if (player.isPlaying) {
        player.pause()
        _isAudioPlaying.value = false
      } else {
        player.start()
        _isAudioPlaying.value = true
      }
    }
  }

  fun stopAudio() {
    try {
      mediaPlayer?.stop()
      mediaPlayer?.release()
    } catch (_: Exception) {}
    mediaPlayer = null
    _isAudioPlaying.value = false
  }

  fun setCity(city: CityLocation) {
    prefs.setCity(city)
    refreshPrayerSchedule()
  }

  fun setCalculationMethod(method: CalculationMethod) {
    prefs.setCalculationMethod(method)
    refreshPrayerSchedule()
  }

  fun setJuristicSchool(school: JuristicSchool) {
    prefs.setJuristicSchool(school)
    refreshPrayerSchedule()
  }

  private fun refreshPrayerSchedule() {
    val city = prefs.selectedCity.value
    val method = prefs.calculationMethod.value
    val school = prefs.juristicSchool.value
    _prayerSchedule.value = PrayerTimesCalculator.calculateSchedule(
      latitude = city.latitude,
      longitude = city.longitude,
      timezoneOffset = city.timezoneOffsetHours,
      method = method,
      juristicSchool = school,
      locationName = city.nameArabic
    )
  }

  private fun startPrayerTimer() {
    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      while (isActive) {
        delay(30000L) // every 30 seconds update remaining time
        refreshPrayerSchedule()
      }
    }
  }

  private fun loadTodayPrayerLogs() {
    viewModelScope.launch {
      val today = getTodayDateString()
      database.prayerLogDao().getCompletedPrayersForDate(today).collect { list ->
        _completedPrayersToday.value = list.toSet()
      }
    }
  }

  private fun getTodayDateString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
  }

  private fun vibrateFeedback() {
    if (!prefs.vibrationEnabled.value) return
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE))
      } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(35)
      }
    } catch (_: Exception) {}
  }

  private fun vibrateLapFinished() {
    if (!prefs.vibrationEnabled.value) return
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val pattern = longArrayOf(0, 100, 60, 150)
        vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
      } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(200)
      }
    } catch (_: Exception) {}
  }

  private fun registerCompassSensor() {
    val sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
      ?: sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)
    sensor?.let {
      sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
    }
  }

  override fun onSensorChanged(event: SensorEvent?) {
    if (event == null) return
    if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
      val rotationMatrix = FloatArray(9)
      SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
      val orientation = FloatArray(3)
      SensorManager.getOrientation(rotationMatrix, orientation)
      var azimuthDegrees = Math.toDegrees(orientation[0].toDouble()).toFloat()
      if (azimuthDegrees < 0) azimuthDegrees += 360f
      _compassAzimuth.value = azimuthDegrees
    } else if (event.sensor.type == Sensor.TYPE_ORIENTATION) {
      _compassAzimuth.value = event.values[0]
    }
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

  override fun onCleared() {
    super.onCleared()
    stopAudio()
    sensorManager?.unregisterListener(this)
    timerJob?.cancel()
  }
}
