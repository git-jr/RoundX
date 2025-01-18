package com.kmp.hango.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomTitle(
    text: String
) {
    Text(
        text = text,
        modifier = Modifier.padding(16.dp),
        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
    )
}