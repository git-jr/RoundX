package com.kmp.hango

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import network.chaintech.composeMultiplatformScreenCapture.AppContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppContext.apply { set(this@MainActivity) }

        hideSystemBars()

        setContent {
            App(this)
//            HomeScreen()
        }
    }

    private fun hideSystemBars() {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}