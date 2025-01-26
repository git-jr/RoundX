package com.kmp.hango.di

import com.kmp.hango.data.QuestionService
import com.kmp.hango.data.QuestionServiceImpl
import com.kmp.hango.data.database.AppDatabase
import com.kmp.hango.data.database.getDatabaseBuilder
import com.kmp.hango.network.KtoAPIClient
import com.kmp.hango.respository.QuestionRepository
import com.kmp.hango.respository.QuestionRepositoryImpl
import com.kmp.hango.ui.categoryDetail.CategoryDetailViewModel
import com.kmp.hango.ui.game.GameViewModel
import com.kmp.hango.ui.login.LoginViewModel
import com.kmp.hango.ui.profile.ProfileViewModel
import com.kmp.hango.ui.ranking.RankingViewModel
import com.kmp.hango.ui.register.RegisterViewModel
import com.kmp.hango.ui.splash.SplashViewModel
import io.ktor.client.HttpClient
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val targetModule: Module

val appModule = module {
    viewModel { GameViewModel() }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel() }
    viewModel { ProfileViewModel(get()) }
    viewModel { CategoryDetailViewModel(get()) }
    viewModel { RankingViewModel(get()) }
    viewModel { SplashViewModel(get(), get()) }
}

val dataModule = module {
    single { getDatabaseBuilder(get()) }
    factory { get<AppDatabase>().questionDao() }

    single<QuestionRepository> { QuestionRepositoryImpl(get(), get()) }
}

val networkModule = module {
    single<HttpClient> { KtoAPIClient.httpClient }
    single<QuestionService> { QuestionServiceImpl(get()) }
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(appModule + dataModule + networkModule + targetModule)
    }
}