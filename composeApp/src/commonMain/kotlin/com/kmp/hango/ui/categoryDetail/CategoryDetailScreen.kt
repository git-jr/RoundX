package com.kmp.hango.ui.categoryDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kmp.hango.ui.game.GameViewModel

@Composable
fun CategoryDetailScreen(
    modifier: Modifier = Modifier,
    categoryId: String,
    onNavigateGame: (String) -> Unit
) {
//    val viewModel = viewModel<CategoryDetailViewModel>()
    val viewModel: CategoryDetailViewModel = viewModel { CategoryDetailViewModel() }
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.prepareScreen(categoryId)
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        state.category?.let {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(it.title)
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