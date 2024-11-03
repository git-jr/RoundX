package com.kmp.hango

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kmp.hango.components.HomeBottomBar
import com.kmp.hango.components.bottomItems

@Composable
fun App() {
    MaterialTheme {
        HomeScreen()
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    var currentRouteIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            HomeBottomBar(
                externalIndex = currentRouteIndex,
                onItemSelected = { item, index ->
                    currentRouteIndex = index
                    navController.navigate(item.title)
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NavHost(
                navController = navController,
                startDestination = bottomItems[0].title,
                modifier = modifier,
                enterTransition = { fadeIn(tween(200)) },
                exitTransition = { fadeOut(tween(200)) },
            ) {
                composable(bottomItems[0].title) {
                    MainListScreen()
                }
                composable(bottomItems[1].title) {
                    SearchScreen()
                }
                composable(bottomItems[2].title) {
                    OrderScreen()
                }
                composable(bottomItems[3].title) {
                    ProfileScreen()
                }
            }
        }
    }
}


@Composable
fun MainListScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home")
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


