package com.example.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight

@Composable
fun ShareCardDialog(
  title: String,
  mainText: String,
  sourceOrSubtitle: String,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "مشاركة بطاقة إسلامية",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
          )
          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "إغلاق")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Styled Islamic Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(EmeraldPrimary, EmeraldDark)
              ),
              shape = RoundedCornerShape(16.dp)
            )
            .border(1.5.dp, IslamicGold, RoundedCornerShape(16.dp))
            .padding(20.dp)
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "۞ $title ۞",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = IslamicGoldLight,
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = mainText,
              fontFamily = AmiriFontFamily,
              fontWeight = FontWeight.Normal,
              fontSize = 20.sp,
              color = Color.White,
              textAlign = TextAlign.Center,
              lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = sourceOrSubtitle,
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp,
              color = IslamicGoldLight.copy(alpha = 0.9f),
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "تطبيق نور الإسلام – رفيق المسلم اليومي",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Normal,
              fontSize = 10.sp,
              color = Color.White.copy(alpha = 0.6f)
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = {
            val sendIntent = Intent().apply {
              action = Intent.ACTION_SEND
              putExtra(
                Intent.EXTRA_TEXT,
                "$title\n\n$mainText\n\nالمصدر: $sourceOrSubtitle\n\nعبر تطبيق نور الإسلام"
              )
              type = "text/plain"
            }
            context.startActivity(Intent.createChooser(sendIntent, "مشاركة المحتوى"))
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("confirm_share_button")
        ) {
          Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "مشاركة النص الآن",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }
    }
  }
}
