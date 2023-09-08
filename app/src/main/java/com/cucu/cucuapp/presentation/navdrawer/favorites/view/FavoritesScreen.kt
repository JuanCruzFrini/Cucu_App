package com.cucu.cucuapp.presentation.navdrawer.favorites.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.cucu.cucuapp.presentation.mainscreen.home.view.ProductsList

@Composable
fun FavoritesScreen(mainNavController: NavHostController) {
    ProductsList(mainNavController)
}
