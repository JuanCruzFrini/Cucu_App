package com.cucu.cucuapp.presentation.notifications.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.cucu.cucuapp.presentation.mainscreen.home.view.ProductsList
import com.cucu.cucuapp.presentation.navdrawer.TopBarNavigateBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavHostController) {

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = { TopBarNavigateBack(navController) }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            ProductsList(navController = navController)
        }
    }
}