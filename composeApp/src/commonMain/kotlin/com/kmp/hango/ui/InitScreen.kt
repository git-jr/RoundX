package com.kmp.hango.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kmp.hango.model.Category
import com.kmp.hango.respository.categorySample


@Composable
fun InitScreen(
    modifier: Modifier = Modifier,
    onNavigateGame: (String) -> Unit
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
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CategoryList { onNavigateGame(it) }
            }
        }
    )
}


@Composable
fun CategoryList(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(categorySample) { category ->
                CategoryItem(category = category) { onClick(category.id) }
            }
        }
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    onClick: () -> Unit = {}
) {
    val gradientBg = Brush.horizontalGradient(
        listOf(
            Color(category.secondaryColor),
            Color(category.primaryColor)
        )
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(8.dp, Color(category.secondaryColor)),
    ) {
        Column(
            modifier = modifier.fillMaxSize().background(gradientBg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = category.title,
                color = Color.White,
                fontSize = MaterialTheme.typography.h4.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }

}

