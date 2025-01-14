package com.kmp.hango.ui.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kmp.hango.constant.DEFAULT_BG_COLOR_DARK
import hango.composeapp.generated.resources.Res
import hango.composeapp.generated.resources.round_x_name
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onNavigateInit: () -> Unit = {}
) {
    val viewModel: RegisterViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.goToInit) {
        if (state.goToInit) {
            onNavigateInit()
        }
    }

    Column(
        modifier = modifier
            .background(Color(DEFAULT_BG_COLOR_DARK))
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(DEFAULT_BG_COLOR_DARK))
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painterResource(Res.drawable.round_x_name),
                contentDescription = "Logo round x name",
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable {
                    viewModel.selectImage()
                },
            contentAlignment = Alignment.Center
        ) {
            state.imageByteArray?.let {
                AsyncImage(
                    state.imageByteArray,
                    contentDescription = "Logo round x name",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Icone de usuário",
                    tint = Color(0XFF8c1c3a)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.loginError) {
            Text(
                text = "Erro ao cadastrar",
                color = Color.Yellow,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("Nome de Usuário") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                cursorColor = Color.White,
                textColor = Color.White,
                placeholderColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                cursorColor = Color.White,
                textColor = Color.White,
                placeholderColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.updateConfirmPassword(it) },
            label = { Text("Confirmar Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                cursorColor = Color.White,
                textColor = Color.White,
                placeholderColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.register()
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
                "CADASTRAR",
                color = Color.White,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

    }

}