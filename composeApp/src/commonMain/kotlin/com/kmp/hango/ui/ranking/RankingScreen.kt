package com.kmp.hango.ui.ranking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kmp.hango.components.CustomTitle
import com.kmp.hango.constant.DEFAULT_BG_COLOR
import com.kmp.hango.constant.DEFAULT_BG_COLOR_DARK
import com.kmp.hango.model.User
import hango.composeapp.generated.resources.Res
import hango.composeapp.generated.resources.ic_coin
import hango.composeapp.generated.resources.round_x_name
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RankingScreen() {
    val viewModel: RankingViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()
    val graphicsLayer = rememberGraphicsLayer()
    val coroutineScope = rememberCoroutineScope()

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

            Crossfade(targetState = state.showRankingShare) { showRankingShare ->

                if (!showRankingShare) {
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        ScoreList(
                            state = state,
                            onShowSnackBar = {
                                viewModel.showSnackBar(it)
                            }
                        )
                        this@Column.AnimatedVisibility(
                            visible = state.showSnackBar,
                            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                        ) {
                            Snackbar(
                                action = {
                                    TextButton(onClick = {
                                        coroutineScope.launch {
                                            viewModel.prepareShareRanking()
                                        }
                                    }) {
                                        Text(
                                            "Compartilhar",
                                            color = Color.White,
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(Color(DEFAULT_BG_COLOR_DARK))
                                                .padding(10.dp, 8.dp)
                                        )
                                    }
                                },
                                modifier = Modifier.padding(8.dp),
                                containerColor = Color.White,
                                contentColor = Color.Black,
                                content = { Text(text = state.rankingMessage) },
                            )
                        }
                    }
                } else {
                    Column (
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val borderColor = Color(0XFFC57601)
                        val buttonBgColor = Color(0XFFFFEE00)

                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp),
                            strokeWidth = 5.dp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.size(16.dp))

                        Column(
                            modifier = Modifier
                                .drawWithContent {
                                    graphicsLayer.record {
                                        this@drawWithContent.drawContent()
                                    }
                                    drawLayer(graphicsLayer)
                                }
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(DEFAULT_BG_COLOR))
                                .fillMaxWidth()
                                .padding(32.dp, 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Image(
                                painterResource(Res.drawable.round_x_name),
                                contentDescription = "Logo round x name",
                                modifier = Modifier
                                    .height(56.dp)
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .border(BorderStroke(4.dp, Color.White), RoundedCornerShape(30.dp))
                                    .clip(RoundedCornerShape(30.dp))
                                    .size(200.dp)
                                ,
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.ic_coin),
                                    contentDescription = "Coin image",
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                )

                                AsyncImage(
                                    state.currentUserPhotoUrl,
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                )
                            }

                            Spacer(modifier = Modifier.size(8.dp))

                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(4.dp, borderColor),
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize().background(buttonBgColor),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = state.shareMessage,
                                        color = borderColor,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }

                            Text(
                                text = "Será que você consegue me superar?",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.size(16.dp))

                        }

                        LaunchedEffect(Unit) {
                            delay(500)
                            viewModel.shareRanking(graphicsLayer.toImageBitmap())
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreList(
    state: RankingState,
    onShowSnackBar: (Boolean) -> Unit = {}
) {
    val listState = rememberLazyListState()

    // se o primeiro item não for mais visível, vamos esconder a barra de compartilhamento
    val showShareBar by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }
    LaunchedEffect(showShareBar) {
        onShowSnackBar(showShareBar)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(Color(DEFAULT_BG_COLOR_DARK))
        ) {
            itemsIndexed(state.profiles) { index, profile ->
                ScoreItemList(profile, index + 1)
            }
            item {
                Image(
                    painter = painterResource(
                        Res.drawable.round_x_name
                    ),
                    contentDescription = "Imagem de um X",
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .padding(16.dp)
                )
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