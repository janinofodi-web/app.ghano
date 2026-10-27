package com.example.ui.screens.stories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ShareCardDialog
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.viewmodel.AppViewModel

@Composable
fun StoryDetailScreen(viewModel: AppViewModel) {
  val story by viewModel.selectedStory.collectAsState()
  var showShareDialog by remember { mutableStateOf(false) }

  if (showShareDialog) {
    ShareCardDialog(
      title = story.titleArabic,
      mainText = story.summary,
      sourceOrSubtitle = story.subtitle,
      onDismiss = { showShareDialog = false }
    )
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag("story_detail_screen"),
    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(IslamicGold.copy(alpha = 0.25f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = story.category.titleArabic,
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = IslamicGoldLight
              )
            }

            IconButton(onClick = { showShareDialog = true }) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "مشاركة القصة",
                tint = Color.White
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = story.titleArabic,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.White
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = story.subtitle,
            fontFamily = CairoFontFamily,
            fontSize = 13.sp,
            color = IslamicGoldLight
          )
        }
      }
    }

    // Story Paragraphs
    items(story.contentParagraphs) { paragraph ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Text(
          text = paragraph,
          fontFamily = CairoFontFamily,
          fontSize = 15.sp,
          lineHeight = 24.sp,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(18.dp)
        )
      }
    }

    // Lessons Learned Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = IslamicGold,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "الدروس والعبر المستفادة من القصة:",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          story.lessonsLearned.forEach { lesson ->
            Text(
              text = "• $lesson",
              fontFamily = CairoFontFamily,
              fontSize = 13.sp,
              lineHeight = 20.sp,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(vertical = 4.dp)
            )
          }
        }
      }
    }
  }
}
