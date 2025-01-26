package com.kmp.hango.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import hango.composeapp.generated.resources.Res
import hango.composeapp.generated.resources.nunito_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun RoundXTypography() = Typography(
    defaultFontFamily = FontFamily(
        Font(resource = Res.font.nunito_semibold, FontWeight.Normal, FontStyle.Normal),
    )
)
