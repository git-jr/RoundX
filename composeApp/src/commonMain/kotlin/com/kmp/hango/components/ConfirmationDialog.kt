package com.kmp.hango.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kmp.hango.constant.DEFAULT_BG_COLOR_DARK

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String
) {
    if (showDialog) {
        AlertDialog(
            contentColor = Color.White,
            backgroundColor = Color(DEFAULT_BG_COLOR_DARK),
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0XFFff235e),
                        contentColor = Color.White
                    ),
                ) {
                    Text("Não")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onConfirm,
                ) {
                    Text("Sim", color = Color.White)
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}