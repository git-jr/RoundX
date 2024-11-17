package com.kmp.hango

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun takeScreenshot()