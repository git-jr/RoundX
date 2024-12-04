package com.kmp.hango

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
            val screenshotManager = koinInject<ScreenshotManager>()

            val coroutineScope = rememberCoroutineScope()
            val graphicsLayer = rememberGraphicsLayer()

            Scaffold { innerPadding ->
                val isIos = getPlatform().name.contains("iOS")
                val negativePadding = innerPadding.calculateTopPadding() - if (isIos) 8.dp else 0.dp
                val positivePadding = if (isIos) 56.dp else 28.dp

                Box(
                    Modifier
                        .background(Color.Red)
                        .fillMaxSize()
                        .offset(y = negativePadding)
                        .padding(
                            top = positivePadding,
                            bottom = innerPadding.calculateBottomPadding(),
                            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .drawWithContent {
                                // call record to capture the content in the graphics layer
                                graphicsLayer.record {
                                    // draw the contents of the composable into the graphics layer
                                    this@drawWithContent.drawContent()
                                }
                                // draw the graphics layer on the visible canvas
                                drawLayer(graphicsLayer)
                            }
                            .clickable {
                                coroutineScope.launch {
                                    val bitmap: ImageBitmap = graphicsLayer.toImageBitmap()
                                    screenshotManager.shareImage(bitmap)
                                }
                            }
                    ) {
                        Spacer(modifier = Modifier.size(32.dp))

                        AsyncImage(
                            "https://raw.githubusercontent.com/git-jr/sample-files/refs/heads/main/profile%20pics/profile_pic_emoji_1.png",
                            contentDescription = "Imagem da pergunta",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .border(
                                    width = 4.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .size(150.dp)
                                .clip(shape = RoundedCornerShape(25.dp)),
                        )

                        Spacer(modifier = Modifier.size(32.dp))

                    }

                }
            }

//        HomeScreen()
        }
    }
}


@OptIn(ExperimentalSerializationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
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
            NavHost(
                navController = navController,
                startDestination = Routes.Init,
//                startDestination = Routes.Game("123"),
                modifier = modifier,
                enterTransition = { fadeIn(tween(200)) },
                exitTransition = { fadeOut(tween(200)) },
            ) {
                composable<Routes.Init> {
                    InitScreen(onNavigateCategoryDetail = {
                        navController.navigate(Routes.CategoryDetail(it))
                    })
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

                    CategoryDetailScreen(
                        categoryId = categoryId,
                        onNavigateGame = { navController.navigate(Routes.Game(it)) }
                    )
                }

                composable<Routes.Game> { backStackEntry ->
                    val categoryId = backStackEntry.toRoute<Routes.CategoryDetail>().categoryId
                    GameScreen(
                        categoryId = categoryId,
                        onNavigateHome = { navController.navigate(Routes.Init) }
                    )
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


