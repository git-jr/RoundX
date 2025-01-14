package com.kmp.hango.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kmp.hango.constant.DEFAULT_BG_COLOR_DARK
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateLogin: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Color(DEFAULT_BG_COLOR_DARK)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val viewModel: ProfileViewModel = koinViewModel()
        val state by viewModel.uiState.collectAsState()

        LaunchedEffect(state.goToLogin) {
            if (state.goToLogin) {
                onNavigateLogin()
            }
        }

        Button(
            onClick = {
                viewModel.showDeleteDialog(true)
            },
            modifier = Modifier.size(300.dp, 56.dp),
            shape = RoundedCornerShape(25.dp),
            border = BorderStroke(
                color = Color(0XFF8c1c3a),
                width = 4.dp
            ),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0XFFff235e),
                contentColor = Color(0XFF8c1c3a)
            ),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
        ) {
            Text(
                "Excluir conta",
                color = Color.White,
            )
        }

        if (state.showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.showDeleteDialog(false)
                },
                title = {
                    Text("Excluir conta")
                },
                text = {
                    Text("Tem certeza que deseja excluir sua conta?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteAccount()
                        }
                    ) {
                        Text("Sim")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            viewModel.showDeleteDialog(false)
                        }
                    ) {
                        Text("Não")
                    }
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}