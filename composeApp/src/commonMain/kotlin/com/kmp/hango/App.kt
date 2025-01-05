package com.kmp.hango

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import com.kmp.hango.components.HomeBottomBar
import com.kmp.hango.navigation.Routes
import com.kmp.hango.ui.InitScreen
import com.kmp.hango.ui.categoryDetail.CategoryDetailScreen
import com.kmp.hango.ui.game.GameScreen
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun App() {
    MaterialTheme {
        KoinContext {
            Scaffold { innerPadding ->
                val isIos = getPlatform().name.contains("iOS")
                val negativePadding = innerPadding.calculateTopPadding() - if (isIos) 8.dp else 0.dp
                val positivePadding = if (isIos) 56.dp else 28.dp

                var bgColor by remember { mutableStateOf(Color(0XFF034d58)) }

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
                    HomeScreen(
                        onChangeColor = { color ->
                            bgColor = Color(color)
                            println("Nova cor: $color")
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSerializationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
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
            val route = it.substringBefore("/")
            showBottomBar = route != gameRoute
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
                NavHost(
                    navController = navController,
                    startDestination = Routes.Init,
                    modifier = modifier,
                ) {
                    composable<Routes.Init> {
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
                            animatedContentScope = this@composable
                        )
                    }
                    composable<Routes.Search> {
                        SearchScreen()
                    }
                    composable<Routes.Orders> {
                        OrderScreen()
                    }
                    composable<Routes.Profile> {
                        ProfileScreen()
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
                            animatedContentScope = this@composable
                        )
                    }

                    composable<Routes.Game> { backStackEntry ->
                        val categoryId = backStackEntry.toRoute<Routes.CategoryDetail>().categoryId
                        onChangeColor(0XFF034d58)
                        GameScreen(
                            categoryId = categoryId,
                            onNavigateHome = { navController.navigate(Routes.Init) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Search")
    }
}

@Composable
fun OrderScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Order")
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Profile")
    }
}


