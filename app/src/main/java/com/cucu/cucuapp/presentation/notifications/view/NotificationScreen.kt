package com.cucu.cucuapp.presentation.notifications.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cucu.cucuapp.application.Routes
import com.cucu.cucuapp.data.models.Notification
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
        LazyColumn(Modifier.fillMaxSize()) {
            items(it, key = null){
                NotificationItem(it, navController) { viewModel.updateNotificationState(it.id.toString()) }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    navController: NavHostController,
    updateState: () -> Unit
) {
    var hasBeenOpen by rememberSaveable { mutableStateOf(notification.hasBeenOpen) }
    val background = if (hasBeenOpen == false) { Color.Blue.copy(0.3f) } else Color.Transparent

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .clickable {
                if (hasBeenOpen == false) updateState()
                hasBeenOpen = true
                //navController.navigate(Routes.Purchases.route)
                notification.purchaseId?.let { id ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "purchaseId",
                        value = id
                    )
                    navController.navigate(Routes.PurchaseDetail.createRoute(id))
                }
            }
            .padding(16.dp)
    ) {
        NotificationImg(Modifier.size(80.dp))

        Spacer(modifier = Modifier.width(8.dp))

        Column(Modifier.fillMaxWidth()) {
            NotificationTitle(
                text = notification.title.toString(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            NotificationDescription(
                text = notification.content.toString(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun NotificationTitle(text:String ,modifier: Modifier) {
    Text(text = text, modifier = modifier, maxLines = 1, fontSize = 20.sp, overflow = TextOverflow.Ellipsis)
}

@Composable
fun NotificationDescription(text:String, modifier: Modifier) {
    Text(text = text, modifier = modifier, maxLines = 3, overflow = TextOverflow.Ellipsis)
}

@Composable
fun NotificationImg(modifier: Modifier) {
    Box(modifier = modifier.background(Color.Blue))
}
