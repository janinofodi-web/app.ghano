package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.components.IslamicTopAppBar
import com.example.ui.components.NoorBottomNavigationBar
import com.example.ui.screens.azkar.AzkarScreen
import com.example.ui.screens.hadith.HadithScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.more.MoreScreen
import com.example.ui.screens.prayer.PrayerTimesScreen
import com.example.ui.screens.prayer.QiblaCompassScreen
import com.example.ui.screens.quran.MemorizationTestScreen
import com.example.ui.screens.quran.QuranScreen
import com.example.ui.screens.quran.SurahReaderScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.stories.StoriesScreen
import com.example.ui.screens.stories.StoryDetailScreen
import com.example.ui.screens.tasbih.TasbihScreen
import com.example.ui.theme.NoorAlIslamTheme
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.AppViewModel
import com.example.ui.viewmodel.SubScreen

class MainActivity : ComponentActivity() {

  private val viewModel: AppViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val isDarkMode by viewModel.prefs.isDarkMode.collectAsState()

      NoorAlIslamTheme(darkTheme = isDarkMode) {
        // Enforce Arabic RTL layout for authentic Islamic app experience
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
          NoorAlIslamApp(viewModel = viewModel)
        }
      }
    }
  }
}

@Composable
fun NoorAlIslamApp(viewModel: AppViewModel) {
  val currentDestination by viewModel.currentDestination.collectAsState()
  val currentSubScreen by viewModel.currentSubScreen.collectAsState()
  val isDarkMode by viewModel.prefs.isDarkMode.collectAsState()

  // Handle hardware and gesture back button gracefully
  val hasSubScreen = currentSubScreen != SubScreen.NONE
  BackHandler(enabled = hasSubScreen) {
    viewModel.navigateBack()
  }

  val screenTitle = when {
    currentSubScreen == SubScreen.SURAH_READER -> "تلاوة القرآن الكريم"
    currentSubScreen == SubScreen.MEMORIZATION_TEST -> "اختبار حفظ القرآن"
    currentSubScreen == SubScreen.HADITH_LIBRARY -> "مكتبة الحديث النبوي"
    currentSubScreen == SubScreen.STORIES_LIBRARY -> "القصص والتاريخ الإسلامي"
    currentSubScreen == SubScreen.STORY_DETAIL -> "قراءة القصة"
    currentSubScreen == SubScreen.QIBLA_COMPASS -> "بوصلة القبلة المشرفة"
    currentSubScreen == SubScreen.SETTINGS -> "إعدادات التطبيق"
    currentDestination == AppDestination.HOME -> "نور الإسلام"
    currentDestination == AppDestination.PRAYER -> "مواقيت الصلاة والأذان"
    currentDestination == AppDestination.QURAN -> "المصحف الشريف"
    currentDestination == AppDestination.AZKAR -> "حصن المسلم والأذكار"
    currentDestination == AppDestination.TASBIH -> "السبحة الإلكترونية"
    currentDestination == AppDestination.MORE -> "المزيد من الأقسام"
    else -> "نور الإسلام"
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      IslamicTopAppBar(
        title = screenTitle,
        canNavigateBack = hasSubScreen,
        isDarkMode = isDarkMode,
        onNavigateBack = { viewModel.navigateBack() },
        onToggleDarkMode = { viewModel.prefs.setDarkMode(!isDarkMode) },
        onOpenSettings = { viewModel.navigateToSubScreen(SubScreen.SETTINGS) }
      )
    },
    bottomBar = {
      // Bottom navigation is shown on main destinations
      if (!hasSubScreen) {
        NoorBottomNavigationBar(
          currentDestination = currentDestination,
          onDestinationSelected = { dest -> viewModel.navigateTo(dest) }
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      AnimatedContent(
        targetState = Pair(currentDestination, currentSubScreen),
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screen_transition"
      ) { (_, subScreen) ->
        when (subScreen) {
          SubScreen.SURAH_READER -> SurahReaderScreen(viewModel = viewModel)
          SubScreen.MEMORIZATION_TEST -> MemorizationTestScreen(viewModel = viewModel)
          SubScreen.HADITH_LIBRARY -> HadithScreen(viewModel = viewModel)
          SubScreen.STORIES_LIBRARY -> StoriesScreen(
            viewModel = viewModel,
            onOpenStory = { story -> viewModel.openStory(story) }
          )
          SubScreen.STORY_DETAIL -> StoryDetailScreen(viewModel = viewModel)
          SubScreen.QIBLA_COMPASS -> QiblaCompassScreen(viewModel = viewModel)
          SubScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
          SubScreen.NONE -> {
            when (currentDestination) {
              AppDestination.HOME -> HomeScreen(
                viewModel = viewModel,
                onNavigateTo = { dest -> viewModel.navigateTo(dest) },
                onNavigateToSub = { sub -> viewModel.navigateToSubScreen(sub) }
              )
              AppDestination.PRAYER -> PrayerTimesScreen(
                viewModel = viewModel,
                onOpenQibla = { viewModel.navigateToSubScreen(SubScreen.QIBLA_COMPASS) }
              )
              AppDestination.QURAN -> QuranScreen(
                viewModel = viewModel,
                onOpenSurah = { surah -> viewModel.openSurah(surah) },
                onOpenMemorizationTest = { viewModel.navigateToSubScreen(SubScreen.MEMORIZATION_TEST) }
              )
              AppDestination.AZKAR -> AzkarScreen(viewModel = viewModel)
              AppDestination.TASBIH -> TasbihScreen(viewModel = viewModel)
              AppDestination.MORE -> MoreScreen(
                viewModel = viewModel,
                onNavigateToSub = { sub -> viewModel.navigateToSubScreen(sub) }
              )
            }
          }
        }
      }
    }
  }
}
