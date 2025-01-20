package com.kmp.hango.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kmp.hango.components.HomeBottomBar
import com.kmp.hango.components.bottomItems
import com.kmp.hango.constant.DEFAULT_BG_COLOR_DARK
import com.kmp.hango.ui.InitScreen
import com.kmp.hango.ui.categoryDetail.CategoryDetailScreen
import com.kmp.hango.ui.game.GameScreen
import com.kmp.hango.ui.login.LoginScreen
import com.kmp.hango.ui.profile.ProfileScreen
import com.kmp.hango.ui.ranking.RankingScreen
import com.kmp.hango.ui.register.RegisterScreen
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onChangeColor: (Long) -> Unit
) {
    var currentRouteIndex by remember { mutableStateOf(0) }
    val currentRoute by navController.currentBackStackEntryAsState()
    val currentRouteName = currentRoute?.destination?.route

    var showBottomBar by remember { mutableStateOf(true) }

    LaunchedEffect(currentRouteName) {
        currentRouteName?.let {
            val gameRoute = Routes.Game.serializer().descriptor.serialName
            val detailRoute = Routes.CategoryDetail.serializer().descriptor.serialName
            val loginRoute = Routes.Login.serializer().descriptor.serialName
            val route = it.substringBefore("/")
            showBottomBar = route !in listOf(gameRoute, detailRoute, loginRoute)
        }
    }


    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        bottomBar = {
            if (showBottomBar) {
                HomeBottomBar(
                    externalIndex = currentRouteIndex,
                    onItemSelected = { item, index ->
                        currentRouteIndex = index
                        navController.navigate(item.route)
                    }
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SharedTransitionLayout {
                androidx.navigation.compose.NavHost(
                    navController = navController,
                    startDestination = Routes.Login,
                    modifier = modifier,
                ) {

                    composable<Routes.Login> {
                        LoginScreen(
                            onNavigateInit = { navController.navigate(Routes.Profile) },
                            onNavigateRegister = { navController.navigate(Routes.Register) }
                        )
                    }

                    composable<Routes.Register> {
                        RegisterScreen(
                            onNavigateInit = { navController.navigate(Routes.Init) }
                        )
                    }

                    composable<Routes.Init> {
                        onChangeColor(DEFAULT_BG_COLOR_DARK)
                        InitScreen(
                            onNavigateCategoryDetail = { categoryId, categoryColor ->
                                navController.navigate(
                                    Routes.CategoryDetail(
                                        categoryId = categoryId,
                                        categoryColor = categoryColor
                                    )
                                )
                            },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this
                        )
                    }
                    composable<Routes.Ranking> {
                        RankingScreen()
                    }

                    composable<Routes.Profile> {
                        ProfileScreen(onNavigateLogin = {
                            navController.navigateCleanStack(Routes.Login)
                        })
                    }

                    composable<Routes.CategoryDetail> { backStackEntry ->
                        val categoryId = backStackEntry.toRoute<Routes.CategoryDetail>().categoryId
                        val categoryColor =
                            backStackEntry.toRoute<Routes.CategoryDetail>().categoryColor
                        onChangeColor(categoryColor)

                        CategoryDetailScreen(
                            categoryId = categoryId,
                            onNavigateGame = { navController.navigate(Routes.Game(it)) },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this
                        )
                    }

                    composable<Routes.Game> { backStackEntry ->
                        val categoryId = backStackEntry.toRoute<Routes.CategoryDetail>().categoryId
                        onChangeColor(0XFF034d58)
                        GameScreen(
                            categoryId = categoryId,
                            onNavigateHome = { navController.navigate(Routes.Init) },
                            onNavigateRanking = {
                                currentRouteIndex = bottomItems.indexOfFirst { item -> item.route == Routes.Ranking }
                                navController.navigateCleanStack(Routes.Ranking)
                            }
                        )
                    }
                }
            }
        }
    }
}


fun NavHostController.navigateCleanStack(route: Routes) {
    this.navigate(route) {
        popUpTo(this@navigateCleanStack.graph.startDestinationId) {
            inclusive = true
        }
    }
}