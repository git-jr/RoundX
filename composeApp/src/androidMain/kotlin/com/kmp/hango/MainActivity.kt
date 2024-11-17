package com.kmp.hango

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import network.chaintech.composeMultiplatformScreenCapture.AppContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppContext.apply { set(this@MainActivity) }

        setContent {
            App(this)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}