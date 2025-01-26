package com.kmp.hango.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun CustomTitle(
    text: String,
    fontSize: TextUnit = MaterialTheme.typography.headlineSmall.fontSize,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Text(
        text = text,
        modifier = Modifier.padding(16.dp),
        fontSize = fontSize,
        color = color,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}