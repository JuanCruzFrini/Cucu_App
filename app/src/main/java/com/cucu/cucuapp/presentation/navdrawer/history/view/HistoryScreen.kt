package com.cucu.cucuapp.presentation.navdrawer.history.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cucu.cucuapp.presentation.mainscreen.home.view.ProductItem
import com.cucu.cucuapp.presentation.navdrawer.history.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(mainNavController: NavHostController) {
    HistoryContent(mainNavController)
}

@Composable
fun HistoryContent (
    navController: NavHostController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    viewModel.getUsersHistory()
    viewModel.history.observeAsState().value?.let { list ->
        LazyColumn{
            items(list, key = { it.id!! }){
                ProductItem(it, navController)
            }
        }
    }
}
