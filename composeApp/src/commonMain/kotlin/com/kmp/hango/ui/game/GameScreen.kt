package com.kmp.hango.ui.game

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@Composable
fun GameScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<GameViewModel>()
    val state by viewModel.uiState.collectAsState()

    val bgColor = Color(0XFF19042D)
    Column(
        modifier = modifier
            .background(bgColor)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        if (state.isGameFinished) {
            Text("Fim do jogo", color = Color.White, fontSize = 24.sp)
            return@Column
        } else {
            GameInProgress(modifier, state, bgColor, viewModel)
        }

    }
}

@Composable
private fun GameInProgress(
    modifier: Modifier,
    state: GameUiState,
    bgColor: Color,
    viewModel: GameViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(64.dp, 32.dp)
                .clip(shape = RoundedCornerShape(25.dp))
                .border(
                    width = 4.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            Row(
                modifier
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
                modifier
                    .weight(0.5f)
                    .background(Color.White)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "00:36",
                    color = bgColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

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

        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            state.currentQuestion?.content.toString(),
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { viewModel.answerQuestion(false) },
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
            Text("X", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.size(16.dp))

        Button(
            onClick = {
                viewModel.answerQuestion(true)
            },
            modifier = Modifier.size(150.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0XFF68ffa8),
                contentColor = Color(0XFF1f5b39)
            ),
            border = BorderStroke(
                color = Color(0XFF1f5b39),
                width = 6.dp
            ),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
        ) {
            Text("O", fontSize = 24.sp)
        }

    }
}