package com.kmp.hango

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import com.kmp.hango.components.HomeBottomBar
import com.kmp.hango.navigation.Routes
import com.kmp.hango.ui.InitScreen
import com.kmp.hango.ui.categoryDetail.CategoryDetailScreen
import com.kmp.hango.ui.game.GameScreen
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.serialization.ExperimentalSerializationApi
import network.chaintech.composeMultiplatformScreenCapture.ScreenCaptureComposable
import network.chaintech.composeMultiplatformScreenCapture.rememberScreenCaptureController

@OptIn(ExperimentalCoilApi::class)
@Composable
fun App() {
    MaterialTheme {
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .build()
        }


        val captureController = rememberScreenCaptureController()
        var capturedImage by remember { mutableStateOf<ImageBitmap?>(null) }

        ScreenCaptureComposable(
            modifier = Modifier,
            screenCaptureController = captureController,
            shareImage = true,
            onCaptured = { img: ImageBitmap?, throwable ->
                if (img != null) {
                    capturedImage = img
                }

                // Handle error
                if (throwable != null) {
                    println("Error ScreenCaptureComposable: $throwable")
                }

            }
        ) {

            MaterialTheme {
                Column(
                    modifier = Modifier
                        .background(Color.Red)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.size(32.dp))
                    Text(
                        "Resultado",
                        color = Color.Red,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.size(32.dp))


//                AsyncImage(
//                    "https://raw.githubusercontent.com/git-jr/sample-files/refs/heads/main/profile%20pics/profile_pic_emoji_1.png",
//                    contentDescription = "Imagem da pergunta",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .border(
//                            width = 4.dp,
//                            color = Color.White,
//                            shape = RoundedCornerShape(25.dp)
//                        )
//                        .size(150.dp)
//                        .clip(shape = RoundedCornerShape(25.dp)),
//                )


                    capturedImage?.let { img ->
                        Image(
                            bitmap = img,
                            contentDescription = "Imagem capturada",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .border(
                                    width = 4.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .size(200.dp)
                                .clip(shape = RoundedCornerShape(25.dp)),
                        )
                    }


                    ElevatedButton(
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .widthIn(min = 200.dp).align(Alignment.CenterHorizontally),
                        onClick = {
                           // como chamar a função takeScreenshot() aqui?
                            takeScreenshot()
                        },
                        content = {
                            Text(
                                "Preview ScreenShot",
                                style = TextStyle(fontSize = 16.sp),
                                fontWeight = FontWeight.Bold
                            )
                        },
                    )

                }

            }
        }


//        HomeScreen()
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
        modifier = modifier,
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
//                startDestination = Routes.Init,
                startDestination = Routes.Game("123"),
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

                composable<Routes.CategoryDetail> {
                    CategoryDetailScreen(
                        onNavigateGame = { navController.navigate(Routes.Game(it)) }
                    )
                }

                composable<Routes.Game> {
                    GameScreen()
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


