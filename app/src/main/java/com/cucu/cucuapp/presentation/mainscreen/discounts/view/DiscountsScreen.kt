package com.cucu.cucuapp.presentation.mainscreen.discounts.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cucu.cucuapp.presentation.mainscreen.discounts.viewmodel.DiscountsViewModel
import com.cucu.cucuapp.presentation.mainscreen.profile.profile.view.ProductRowItem

@Composable
fun DiscountsScreen(
    mainNavController: NavHostController,
    viewModel:DiscountsViewModel = hiltViewModel()
    ) {

    viewModel.getAllProducts()

    viewModel.productsList.observeAsState().value?.let { list ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Descuentos", fontSize = 20.sp, modifier = Modifier.padding(0.dp, 16.dp))
            Box(modifier = Modifier.weight(1f)){
                LazyRow {
                    items(list, key = { it.id!! }){
                        ProductRowItem(it, mainNavController)
                    }
                }
            }

            Text(text = "Combos", fontSize = 20.sp, modifier = Modifier.padding(0.dp, 16.dp))
            Box(modifier = Modifier.weight(1f)){
                LazyRow{
                    items(list, key = { it.id!! }){
                        ProductRowItem(it, mainNavController)
                    }
                }
            }

            Text(text = "Promociones", fontSize = 20.sp, modifier = Modifier.padding(0.dp, 16.dp))
            Box(modifier = Modifier.weight(1f)){
                LazyRow{
                    items(list, key = { it.id!! }){
                        ProductRowItem(it, mainNavController)
                    }
                }
            }
        }
    }
}