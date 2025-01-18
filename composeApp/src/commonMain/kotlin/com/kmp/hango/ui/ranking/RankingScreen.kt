package com.kmp.hango.ui.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kmp.hango.components.CustomTitle
import com.kmp.hango.constant.DEFAULT_BG_COLOR_DARK
import com.kmp.hango.model.User
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RankingScreen() {
    val viewModel: RankingViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .background(Color(DEFAULT_BG_COLOR_DARK))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.textMessage.isNotEmpty()) {
            CustomTitle(state.textMessage, color = Color.White)
        }

        if (state.searching) {
            Spacer(modifier = Modifier.height(10.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                strokeWidth = 5.dp,
                color = Color.White
            )
        } else if (state.profiles.isNotEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = state.rankingMessage,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )

                LazyColumn {
                    itemsIndexed(state.profiles) { index, profile ->
                        ScoreItemList(profile, index + 1)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreItemList(profile: User, index: Int) {

    val bgRankingColor: Color = when (index) {
        1 -> Color(0XFFc7083b)
        2 -> Color(0XFFe2144b)
        3 -> Color(0XFFfd205b)
        else -> if (index % 2 == 0) Color.Unspecified else Color.LightGray.copy(alpha = 0.2f)
    }

    val (textBgColor, textColor) = if (index <= 3) {
        Pair(Color(0XFFf6c429), Color.Black)
    } else {
        Pair(Color.Transparent, Color.White)
    }

    ListItem(
        headlineContent = {
            Text(
                text = profile.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        },
        supportingContent = {
            Text(
                text = profile.score.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        },
        leadingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(10.dp)
                        .clip(CircleShape)
                        .background(textBgColor)
                        .aspectRatio(1f)
                ) {
                    Text(
                        text = index.toString(),
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20))
                        .aspectRatio(1f)
                        .background(Color.White)
                ) {
                    AsyncImage(
                        profile.imageProfileUrl,
                        contentDescription = "foto de perfil",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        trailingContent = {

        },
        colors = ListItemDefaults.colors(
            containerColor = bgRankingColor,
        ),
        modifier = Modifier
            .height(60.dp)
    )
}