@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterialApi::class)

package com.kmp.hango.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.kmp.hango.constant.DEFAULT_BG_COLOR_DARK
import com.kmp.hango.model.Category
import com.kmp.hango.respository.categorySample
import hango.composeapp.generated.resources.Res
import hango.composeapp.generated.resources.round_x_name
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun InitScreen(
    modifier: Modifier = Modifier,
    onNavigateCategoryDetail: (String, Long) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    Scaffold(
        topBar = {
            AppBarInitScreen()
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CategoryList(
                    modifier = modifier.fillMaxSize(),
                    onClick = { id, color ->
                        onNavigateCategoryDetail(id, color)
                    },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope
                )
            }
        }
    )
}

@Composable
private fun AppBarInitScreen() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(DEFAULT_BG_COLOR_DARK))
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painterResource(Res.drawable.round_x_name),
            contentDescription = "Profile",
        )
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CategoryList(
    modifier: Modifier = Modifier,
    onClick: (String, Long) -> Unit = { _, _ -> },
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {

    Column(
        modifier = modifier
            .background(Color(DEFAULT_BG_COLOR_DARK))
            .fillMaxSize()
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(categorySample) { category ->
                CategoryItem(
                    category = category,
                    modifier = modifier,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope
                ) {
                    onClick(
                        category.id,
                        category.color
                    )
                }
            }
        }

//        LazyColumn {
//            items(categorySample) { category ->
//                CategoryItem(
//                    category = category,
//                    modifier = modifier,
//                    sharedTransitionScope = sharedTransitionScope,
//                    animatedContentScope = animatedContentScope
//                ) {
//                    onClick(
//                        category.id,
//                        category.color
//                    )
//                }
//            }
//        }
    }

}


@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onClick: () -> Unit = {}
) {
    with(sharedTransitionScope) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(150.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(15.dp),
        ) {
            Column(
                modifier = Modifier
                    .background(Color(category.color)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally
            ) {

                Image(
                    painter = painterResource(category.icon),
                    contentDescription = category.title,
                    colorFilter = ColorFilter.tint(
                        Color(category.color),
                        blendMode = BlendMode.ColorBurn
                    ),
                    modifier = Modifier
                        .size(50.dp)
                        .sharedElement(
                            state = rememberSharedContentState(
                                key = "image-${category.id}"
                            ),
                            animatedVisibilityScope = animatedContentScope,
                        ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = category.title,
                    modifier = Modifier
                        .sharedElement(
                            state = rememberSharedContentState(
                                key = "text-${category.id}"
                            ),
                            animatedVisibilityScope = animatedContentScope,
                        )
                )
            }
        }
    }
}

