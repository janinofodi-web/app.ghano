package com.example.ui.screens.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.viewmodel.AppViewModel
import com.example.ui.viewmodel.SubScreen

@Composable
fun MoreScreen(
  viewModel: AppViewModel,
  onNavigateToSub: (SubScreen) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag("more_screen"),
    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 80.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text(
        text = "المزيد من الأقسام والخدمات",
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.onBackground
      )
    }

    item {
      MoreItemCard(
        title = "مكتبة الحديث النبوي الشريف",
        subtitle = "صحيح البخاري ومسلم ومصنفات السنة بالأبواب والشروح",
        icon = Icons.Default.Book,
        iconTint = MaterialTheme.colorScheme.primary,
        onClick = { onNavigateToSub(SubScreen.HADITH_LIBRARY) }
      )
    }

    item {
      MoreItemCard(
        title = "القصص والتاريخ الإسلامي",
        subtitle = "قصص الأنبياء، السيرة النبوية، سير الصحابة والتابعين والعلماء",
        icon = Icons.Default.AutoAwesome,
        iconTint = IslamicGold,
        onClick = { onNavigateToSub(SubScreen.STORIES_LIBRARY) }
      )
    }

    item {
      MoreItemCard(
        title = "بوصلة القبلة المشرفة",
        subtitle = "تحديد اتجاه الكعبة والمسافة إلى مكة المكرمة بمستشعرات دقيقة",
        icon = Icons.Default.Explore,
        iconTint = Color(0xFF2EAA84),
        onClick = { onNavigateToSub(SubScreen.QIBLA_COMPASS) }
      )
    }

    item {
      MoreItemCard(
        title = "اختبار ومراجعة حفظ القرآن",
        subtitle = "إخفاء الآيات تدريجيًا لمراجعة الحفظ وتثبيته في الصدور",
        icon = Icons.Default.Psychology,
        iconTint = Color(0xFFC76D2E),
        onClick = { onNavigateToSub(SubScreen.MEMORIZATION_TEST) }
      )
    }

    item {
      MoreItemCard(
        title = "إعدادات التطبيق والمظهر",
        subtitle = "تغيير المدينة، طريقة الحساب، القارئ المفضل، وحجم الخط والوضع الليلي",
        icon = Icons.Default.Settings,
        iconTint = MaterialTheme.colorScheme.primary,
        onClick = { onNavigateToSub(SubScreen.SETTINGS) }
      )
    }
  }
}

@Composable
private fun MoreItemCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  iconTint: Color,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconTint,
          modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column {
          Text(
            text = title,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = subtitle,
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.outline,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
