package com.kmp.hango

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.kmp.hango.constant.DEFAULT_BG_COLOR_DARK
import com.kmp.hango.navigation.NavHost
import com.kmp.hango.ui.theme.RoundXTypography
import org.koin.compose.KoinContext

@Composable
fun App() {
    MaterialTheme(
        typography = RoundXTypography(),
    ) {
        KoinContext {
            Scaffold { innerPadding ->
                val isIos = getPlatform().name.contains("iOS")
                val negativePadding = innerPadding.calculateTopPadding() - if (isIos) 8.dp else 0.dp
                val positivePadding = if (isIos) 56.dp else 28.dp

                var bgColor by remember { mutableStateOf(Color(DEFAULT_BG_COLOR_DARK)) }

                Box(
                    Modifier
                        .background(bgColor)
                        .fillMaxSize()
                        .offset(y = negativePadding)
                        .padding(
                            top = positivePadding,
                            bottom = innerPadding.calculateBottomPadding(),
                            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                        )
                ) {
                    NavHost(
                        onChangeColor = { color ->
                            bgColor = Color(color)
                        }
                    )
                }
            }
        }
    }
}




