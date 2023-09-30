package com.cucu.cucuapp.presentation.mainscreen.discounts.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cucu.cucuapp.presentation.mainscreen.discounts.viewmodel.DiscountsViewModel
import com.cucu.cucuapp.presentation.mainscreen.profile.profile.view.ProductsLazyRow
import com.cucu.cucuapp.presentation.mainscreen.profile.profile.view.SubtitleText

@Composable
fun DiscountsScreen(
    mainNavController: NavHostController,
    viewModel:DiscountsViewModel = hiltViewModel()
) {

    viewModel.getDiscounts()
    viewModel.discountsList.observeAsState().value?.let { discounts ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            SubtitleText(text = "Descuentos")
            ProductsLazyRow(list = discounts, navController = mainNavController)

            SubtitleText(text = "Combos")
            ProductsLazyRow(list = discounts, navController = mainNavController)

            SubtitleText(text = "Promociones")
            ProductsLazyRow(list = discounts, navController = mainNavController)
        }
    }
}