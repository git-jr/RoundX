package com.kmp.hango.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kmp.hango.components.ConfirmationDialog
import com.kmp.hango.components.LoadScreen
import com.kmp.hango.constant.DEFAULT_BG_COLOR
import com.kmp.hango.constant.DEFAULT_BG_COLOR_DARK
import hango.composeapp.generated.resources.Res
import hango.composeapp.generated.resources.round_x_name
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
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
            )
            .padding(16.dp),
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
        if (!state.load && state.error == null) {
            Column(
                modifier = modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(20))
                        .aspectRatio(1f)
                        .background(Color.White)
                ) {
                    AsyncImage(
                        state.imageProfileUrl,
                        contentDescription = "foto de perfil",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = state.name,
                    color = Color.White,
                    fontSize = 24.sp,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "Pontuação atual: ${state.score}",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )

                Spacer(modifier = Modifier.size(16.dp))

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

                Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = {
                        viewModel.showLogoutDialog(true)
                    },
                    modifier = Modifier.size(200.dp, 56.dp),
                    shape = RoundedCornerShape(25.dp),
                    border = BorderStroke(
                        color = Color(DEFAULT_BG_COLOR_DARK),
                        width = 4.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(DEFAULT_BG_COLOR)
                    ),
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        "Sair",
                        color = Color.White,
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                IconButton(
                    onClick = {
                        viewModel.showPreferencesDialog(true)
                    }
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            }
        }

        if (state.load) {
            LoadScreen()
        }

        state.error?.let {
            Text(
                text = it,
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.size(16.dp))
            if (state.showGoToLogin) {
                Button(
                    onClick = {
                        viewModel.showLogoutDialog(true)
                    },
                    modifier = Modifier.size(200.dp, 56.dp),
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
                        "Ir para login",
                        color = Color.White,
                    )
                }

            }
        }

        ConfirmationDialog(
            showDialog = state.showDeleteDialog,
            onDismiss = { viewModel.showDeleteDialog(false) },
            onConfirm = { viewModel.deleteAccount() },
            title = "Excluir conta",
            message = "Tem certeza que deseja excluir sua conta?"
        )

        ConfirmationDialog(
            showDialog = state.showLogoutDialog,
            onDismiss = { viewModel.showLogoutDialog(false) },
            onConfirm = { viewModel.logout() },
            title = "Sair",
            message = "Tem certeza que deseja sair?"
        )

        if (state.showPreferencesDialog) {
            AlertDialog(
                contentColor = Color.White,
                backgroundColor = Color(DEFAULT_BG_COLOR_DARK),
                onDismissRequest = {
                    viewModel.showPreferencesDialog(false)
                },
                title = {
                    Text("Preferências")
                },
                text = {
                    SettingSwitchItem(
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        text = "Pular contagem regressiva antes do jogo",
                        checked = state.skipCountdown,
                        onCheckedChange = { viewModel.setSkipCountdown(it) }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.showPreferencesDialog(false)
                        },
                    ) {
                        Text("Fechar", color = Color.White)
                    }
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
private fun SettingSwitchItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val contentAlpha = if (enabled) ContentAlpha.high else ContentAlpha.disabled

            Text(
                text = text,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.alpha(contentAlpha)
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled
        )
    }
}
