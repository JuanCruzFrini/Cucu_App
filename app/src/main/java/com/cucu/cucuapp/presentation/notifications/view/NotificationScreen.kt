package com.cucu.cucuapp.presentation.notifications.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cucu.cucuapp.presentation.navdrawer.TopBarNavigateBack
import com.cucu.cucuapp.presentation.notifications.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavHostController) {

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = { TopBarNavigateBack(navController) }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            NotificationList(navController = navController)
        }
    }
}

@Composable
fun NotificationList(
    navController: NavHostController,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    viewModel.getNotificationsList()

    viewModel.notifications.observeAsState().value?.let {
        LazyColumn {
            items(it, key = null){
                //NotificationItem(it)
            }
        }
    }
}
