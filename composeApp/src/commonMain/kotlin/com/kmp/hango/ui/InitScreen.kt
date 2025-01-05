@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterialApi::class)

package com.kmp.hango.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kmp.hango.model.Category
import com.kmp.hango.respository.categorySample
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
            TopAppBar(
                title = {
                    Text("Buscar")
                },
                navigationIcon = {
                    AsyncImage(
                        "https://raw.githubusercontent.com/git-jr/sample-files/refs/heads/main/profile%20pics/profile_pic_emoji_1.png",
                        contentDescription = "Profile",
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            )
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
            .background(Color(0XFF034d58))
            .fillMaxSize()
    ) {
        LazyColumn {
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
                .height(100.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(15.dp),
        ) {
            ListItem(
                modifier = Modifier
                    .background(Color(category.color))
                    .fillMaxSize(),
                text = {
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
                },
                icon = {
                    Icon(
                        painterResource(category.icon),
                        contentDescription = category.title,
                        Modifier
                            .sharedElement(
                                state = rememberSharedContentState(
                                    key = "image-${category.id}"
                                ),
                                animatedVisibilityScope = animatedContentScope,
                            )
                    )
                }
            )
        }
    }
}

