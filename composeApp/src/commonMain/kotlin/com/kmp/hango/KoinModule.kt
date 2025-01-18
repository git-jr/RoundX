package com.kmp.hango

import com.kmp.hango.ui.game.GameViewModel
import com.kmp.hango.ui.login.LoginViewModel
import com.kmp.hango.ui.profile.ProfileViewModel
import com.kmp.hango.ui.ranking.RankingViewModel
import com.kmp.hango.ui.register.RegisterViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val targetModule: Module

val appModule = module {
    viewModel { GameViewModel(get()) }
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { RankingViewModel() }
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(appModule + targetModule)
    }
}