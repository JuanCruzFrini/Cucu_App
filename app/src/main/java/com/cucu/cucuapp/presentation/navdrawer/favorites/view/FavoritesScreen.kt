package com.cucu.cucuapp.presentation.navdrawer.favorites.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cucu.cucuapp.presentation.mainscreen.home.view.ProductItem
import com.cucu.cucuapp.presentation.navdrawer.favorites.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(mainNavController: NavHostController) {
    FavoritesList(mainNavController)
}

@Composable
fun FavoritesList(
    navController: NavHostController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    viewModel.getFavorites()
    viewModel.favList.observeAsState().value?.let { favorites ->
        LazyColumn {
            items(favorites, key = { it.id!! }) {
                ProductItem(it, navController)
            }
        }
    }
}
