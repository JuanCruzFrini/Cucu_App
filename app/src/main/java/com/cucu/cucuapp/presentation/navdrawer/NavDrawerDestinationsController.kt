package com.cucu.cucuapp.presentation.navdrawer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cucu.cucuapp.application.Routes
import com.cucu.cucuapp.application.firstCharToUpperCase
import com.cucu.cucuapp.presentation.navdrawer.cart.view.CartScreen
import com.cucu.cucuapp.presentation.navdrawer.categories.view.CategoriesScreen
import com.cucu.cucuapp.presentation.navdrawer.favorites.view.FavoritesScreen
import com.cucu.cucuapp.presentation.navdrawer.history.view.HistoryScreen
import com.cucu.cucuapp.presentation.navdrawer.purchases.view.PurchasesScreen
import com.cucu.cucuapp.ui.theme.Purple40
import com.cucu.cucuapp.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawerDestinationsController(mainNavController: NavHostController) {

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = { TopBarNavigateBack(mainNavController) }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            SetContent(mainNavController)
        }
    }

}

@Composable
fun SetContent(mainNavController: NavHostController) {
    when(mainNavController.currentDestination?.route){
        Routes.Cart.route -> CartScreen(mainNavController)
        Routes.Favorites.route -> FavoritesScreen(mainNavController)
        Routes.Purchases.route -> PurchasesScreen(mainNavController)
        Routes.History.route -> HistoryScreen(mainNavController)
        Routes.Categories.route -> CategoriesScreen(mainNavController)
        else -> { }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarNavigateBack(mainNavController: NavHostController) {

    val checkRoute = when (mainNavController.currentDestination?.route){
        Routes.ProductDetail.route -> "Detail"
        Routes.PurchaseDetail.route -> "Purchase"
        Routes.Category.route -> "Category"
        else -> { mainNavController.currentDestination?.route?.firstCharToUpperCase() }
    }

    Surface(shadowElevation = 3.dp){
        TopAppBar(
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                navigationIconContentColor = Purple80,
                titleContentColor = Purple80,
                scrolledContainerColor = Purple40,
            ),
            title = { Text(text = "$checkRoute")},
            navigationIcon = {
                IconButton(onClick = { mainNavController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            }
        )
    }
}
