package com.kmp.hango.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kmp.hango.constant.DEFAULT_BG_COLOR
import com.kmp.hango.navigation.Routes


@Composable
fun HomeBottomBar(
    externalIndex: Int,
    onItemSelected: (BottomItem, Int) -> Unit
) {
    NavigationBar(
        modifier = Modifier,
        containerColor = Color(DEFAULT_BG_COLOR).copy(alpha = 0.9f),
        contentColor = Color.White
    ) {
        bottomItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (externalIndex == index) item.enabledIcon else item.disabledIcon,
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp),
                        tint = if (externalIndex == index) Color(DEFAULT_BG_COLOR) else Color.White
                    )
                },
                label = { Text(item.title) },
                selected = externalIndex == index,
                onClick = {
                    onItemSelected(item, index)
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    indicatorColor = Color.White.copy(alpha = 0.5f),
                    unselectedTextColor = Color.White,
                )
            )
        }
    }
}

data class BottomItem(
    val title: String,
    val enabledIcon: ImageVector,
    val disabledIcon: ImageVector,
    val route: Routes
)

val bottomItems = listOf(
    BottomItem("Home", Icons.Filled.Home, Icons.Outlined.Home, Routes.Init),
    BottomItem("Placar", Icons.Filled.Search, Icons.Outlined.Search, Routes.Ranking),
    BottomItem("Profile", Icons.Filled.Person, Icons.Outlined.Person, Routes.Profile)
)