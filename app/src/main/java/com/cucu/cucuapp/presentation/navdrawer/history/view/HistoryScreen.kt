package com.cucu.cucuapp.presentation.navdrawer.history.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.cucu.cucuapp.presentation.mainscreen.home.view.ProductsList

@Composable
fun HistoryScreen(mainNavController: NavHostController) {
    ProductsList(mainNavController)
}
