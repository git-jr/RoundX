package com.kmp.hango.extensions

fun Int.zeroRound(): String {
    return this.toString().padStart(2, '0')
}

fun Int.toTime(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "${minutes.zeroRound()}:${seconds.zeroRound()}"
}