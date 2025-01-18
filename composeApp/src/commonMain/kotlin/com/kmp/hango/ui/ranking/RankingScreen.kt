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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kmp.hango.components.CustomTitle
import com.kmp.hango.model.User
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RankingScreen() {
    val viewModel: RankingViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()

    if (state.searching) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = state.textMessage)
            Spacer(modifier = Modifier.height(10.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                strokeWidth = 5.dp,
                color = MaterialTheme.colorScheme.primary
            )

        }
    } else {
        if (state.profiles.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CustomTitle("Sem resultados")
            }
        } else {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CustomTitle("Ranking ${if (state.currentUserPositionOnRanking > 0) "(${state.currentUserPositionOnRanking}º)" else ""}")

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
        1 -> Color.Red
        2 -> Color.Blue
        3 -> Color(0xFF14B306)
        else -> if (index % 2 == 0) Color.Gray else Color.LightGray
    }

    ListItem(
        headlineContent = {
            Text(
                text = profile.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )
        },
        supportingContent = {
            Text(
                text = profile.score.toString(),
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
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
                ) {
                    Text(
                        text = index.toString(),
                        color = if (index in 0..3) bgRankingColor else Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                AsyncImage(
                    profile.imageProfileUrl,
                    contentDescription = "foto de perfil",
                    modifier = Modifier
                        .clip(RoundedCornerShape(20))
                        .aspectRatio(1f)
                )
            }
        },
        trailingContent = {

        },
        colors = ListItemDefaults.colors(
            containerColor = bgRankingColor.copy(alpha = 0.2f),
        ),
        modifier = Modifier
            .height(60.dp)
    )
}