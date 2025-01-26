package com.kmp.hango.ui.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kmp.hango.constant.DEFAULT_BG_COLOR
import com.kmp.hango.constant.DEFAULT_BG_COLOR_DARK
import com.kmp.hango.extensions.toTime
import hango.composeapp.generated.resources.Res
import hango.composeapp.generated.resources.ic_coin
import hango.composeapp.generated.resources.ic_o
import hango.composeapp.generated.resources.ic_x
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    categoryId: String,
    onNavigateHome: () -> Unit = {},
    onNavigateRanking: () -> Unit = {}
) {
    val viewModel: GameViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.prepareScreen(categoryId)
    }

    LaunchedEffect(state.goToRanking) {
        if (state.goToRanking) {
            onNavigateRanking()
        }
    }

    val bgColor = Color(DEFAULT_BG_COLOR)
    Column(
        modifier = modifier
            .background(bgColor)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state.isGameFinished) {
            Column(
                modifier = modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val borderColor = Color(0XFFC57601)
                val buttonBgColor = Color(0XFFFFEE00)

                Spacer(modifier = Modifier.size(16.dp))

                var rotationState by remember { mutableStateOf(180f) }
                val rotationCoin by animateFloatAsState(
                    targetValue = rotationState,
                    animationSpec = tween(durationMillis = 500)
                )

                LaunchedEffect(Unit) {
                    delay(500)
                    rotationState += 180f
                }

                Image(
                    painterResource(Res.drawable.ic_coin),
                    contentDescription = "Coin image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .graphicsLayer(rotationY = rotationCoin),
                )

                Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = state.result,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                CorrectAnswer(
                    Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    state.correctAnswers,
                )

                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(80.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(4.dp, borderColor),
                ) {
                    Column(
                        modifier = modifier.fillMaxSize().background(buttonBgColor),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = state.messageText,
                            color = borderColor,
                            fontSize = MaterialTheme.typography.h5.fontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = state.syncMessage,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = {
                        viewModel.synchronizeProgress()
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0XFF68ffa8),
                        contentColor = Color(0XFF1f5b39)
                    ),
                    border = BorderStroke(
                        color = Color(0XFF1f5b39),
                        width = 4.dp
                    ),
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        "SINCRONIZAR", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }


                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.size(16.dp))

                    Button(
                        onClick = { onNavigateHome() },
                        modifier = Modifier.width(150.dp),
                        shape = CircleShape,
                        border = BorderStroke(
                            color = Color(0XFF8c1c3a),
                            width = 4.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0XFFff235e),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            "HOME", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                }

            }
        } else {
            GameInProgress(modifier, state, viewModel)
        }

    }
}

@Composable
private fun GameInProgress(
    modifier: Modifier,
    state: GameUiState,
    viewModel: GameViewModel
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            GameStatus(state)

            if (state.currentQuestion?.image?.isNotEmpty() == true) {
                AsyncImage(
                    "https://raw.githubusercontent.com/git-jr/sample-files/refs/heads/main/profile%20pics/profile_pic_emoji_1.png",
                    contentDescription = "Imagem da pergunta",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .border(
                            width = 4.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(25.dp)),
                )
            }

            Spacer(modifier = Modifier.padding(16.dp))

            Row(
                modifier = Modifier
                    .border(
                        width = 4.dp,
                        color = Color(state.categoryColor),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(Color.White)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    state.currentQuestion?.content.toString(),
                    color = Color(DEFAULT_BG_COLOR_DARK),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        ButtonsTrueFalse(viewModel)
    }
}

@Composable
private fun GameStatus(
    state: GameUiState
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 64.dp, end = 64.dp, top = 16.dp, bottom = 16.dp)
                .clip(shape = RoundedCornerShape(25.dp))
                .border(
                    width = 4.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            Row(
                Modifier
                    .weight(0.5f)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    state.currentCount,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Row(
                Modifier
                    .weight(0.5f)
                    .background(Color.White)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    state.currentTime.toTime(),
                    color = Color(DEFAULT_BG_COLOR),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        CorrectAnswer(
            Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            state.correctAnswers
        )
    }
}

@Composable
private fun CorrectAnswer(
    modifier: Modifier = Modifier,
    answers: List<Boolean?> = emptyList()
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
    ) {
        answers.filterNotNull().forEach { right ->
            val imageRes = if (right) Res.drawable.ic_o else Res.drawable.ic_x
            val contentDescription = if (right) "True" else "False"
            Image(
                painterResource(imageRes),
                contentDescription = contentDescription,
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
private fun ButtonsTrueFalse(viewModel: GameViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        var rotationStateTrue by remember { mutableStateOf(0f) }
        val rotationTrue by animateFloatAsState(
            targetValue = rotationStateTrue,
            animationSpec = tween(durationMillis = 500)
        )

        var rotationStateFalse by remember { mutableStateOf(0f) }
        val rotationFalse by animateFloatAsState(
            targetValue = rotationStateFalse,
            animationSpec = tween(durationMillis = 500)
        )


        Button(
            onClick = {
                viewModel.answerQuestion(true)
                rotationStateTrue += 180f
            },
            modifier = Modifier.size(150.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0XFF678bfc),
                contentColor = Color(0XFF1f5b39)
            ),
            border = BorderStroke(
                color = Color(0XFF0a2579),
                width = 6.dp
            ),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
        ) {
            Image(
                painterResource(Res.drawable.ic_o),
                contentDescription = "True",
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(150.dp)
                    .graphicsLayer(rotationY = rotationTrue),
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Button(
            onClick = {
                viewModel.answerQuestion(false)
                rotationStateFalse += 180f
            },
            modifier = Modifier.size(150.dp),
            shape = RoundedCornerShape(25.dp),
            border = BorderStroke(
                color = Color(0XFF8c1c3a),
                width = 6.dp
            ),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0XFFff235e),
                contentColor = Color(0XFF8c1c3a)
            ),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
        ) {
            Image(
                painterResource(Res.drawable.ic_x),
                contentDescription = "False",
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(150.dp)
                    .graphicsLayer(rotationZ = rotationFalse),
            )
        }
    }
}