package com.example.ui.screens.stories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IslamicStory
import com.example.data.model.StoryCategory
import com.example.data.repository.StoriesRepository
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.viewmodel.AppViewModel

@Composable
fun StoriesScreen(
  viewModel: AppViewModel,
  onOpenStory: (IslamicStory) -> Unit
) {
  var selectedCategory by remember { mutableStateOf<StoryCategory?>(null) }

  val filteredStories = remember(selectedCategory) {
    if (selectedCategory == null) StoriesRepository.STORIES
    else StoriesRepository.STORIES.filter { it.category == selectedCategory }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("stories_screen")
  ) {
    // Categories Filter
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp),
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        val isAll = selectedCategory == null
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
              if (isAll) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable { selectedCategory = null }
            .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
          Text(
            text = "الكل",
            fontFamily = CairoFontFamily,
            fontWeight = if (isAll) FontWeight.Bold else FontWeight.Normal,
            fontSize = 12.sp,
            color = if (isAll) Color.White else MaterialTheme.colorScheme.onSurface
          )
        }
      }

      items(StoryCategory.values()) { cat ->
        val isSelected = cat == selectedCategory
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
              if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable { selectedCategory = cat }
            .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
          Text(
            text = cat.titleArabic,
            fontFamily = CairoFontFamily,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 12.sp,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    // Stories List
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 80.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(filteredStories, key = { it.id }) { story ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenStory(story) }
            .testTag("story_card_${story.id}"),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(MaterialTheme.colorScheme.primaryContainer)
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(
                  text = story.category.titleArabic,
                  fontFamily = CairoFontFamily,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
              }

              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.AccessTime,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "${story.readingTimeMinutes} دقائق قراءة",
                  fontFamily = CairoFontFamily,
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = story.titleArabic,
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = story.subtitle,
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Medium,
              fontSize = 13.sp,
              color = IslamicGold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = story.summary,
              fontFamily = CairoFontFamily,
              fontSize = 13.sp,
              lineHeight = 19.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}
