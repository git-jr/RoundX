package com.kmp.hango.ui.splash

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kmp.hango.constant.DEFAULT_BG_COLOR
import com.kmp.hango.ui.login.LoginViewModel
import hango.composeapp.generated.resources.Res
import hango.composeapp.generated.resources.round_x_name
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onNavigateInit: () -> Unit,
    onNavigateLogin: () -> Unit
) {

    val viewModel: SplashViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.goToInit) {
        if (state.goToInit) {
            onNavigateInit()
        }
    }

    LaunchedEffect(state.goToLogin) {
        if (state.goToLogin) {
            onNavigateLogin()
        }
    }


    Column(
        modifier = modifier
            .background(Color(DEFAULT_BG_COLOR))
            .fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.loading) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(Res.drawable.round_x_name),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(100.dp).align(Alignment.Center)
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (state.error) {
                    Text(text = "Falha ao iniciar o app.")
                    Spacer(modifier = Modifier.size(16.dp))

                    Button(
                        onClick = {
                            viewModel.startChecks()
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
                            "Tenar novamente",
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}