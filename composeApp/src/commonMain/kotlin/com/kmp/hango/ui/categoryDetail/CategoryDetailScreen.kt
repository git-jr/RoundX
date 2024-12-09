package com.kmp.hango.ui.categoryDetail

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CategoryDetailScreen(
    modifier: Modifier = Modifier,
    categoryId: String,
    onNavigateGame: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val viewModel: CategoryDetailViewModel = viewModel { CategoryDetailViewModel() }
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.prepareScreen(categoryId)
    }

    with(sharedTransitionScope) {
        state.category?.let {
            Column(
                modifier = modifier
                    .background(Color(it.color))
                    .fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {

                    Image(
                        painter = painterResource(it.icon),
                        contentDescription = it.title,
                        colorFilter = ColorFilter.tint(
                            Color(it.color),
                            blendMode = BlendMode.ColorBurn
                        ),
                        modifier = modifier
                            .size(200.dp)
                            .sharedElement(
                                state = rememberSharedContentState(
                                    key = "image-${it.id}"
                                ),
                                animatedVisibilityScope = animatedContentScope,
                            ),
                    )


                    Text(
                        it.title,
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(
                                    key = "text-$categoryId"
                                ),
                                animatedVisibilityScope = animatedContentScope,
                            ),
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    Text(it.description)
                }

                // botão jogar
                Button(
                    onClick = {
                        state.category?.let {
                            onNavigateGame(it.id)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                ) {
                    Text("Jogar")
                }
            }
        }
    }
}