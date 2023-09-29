package com.cucu.cucuapp.presentation.mainscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cucu.cucuapp.R
import com.cucu.cucuapp.application.Routes
import com.cucu.cucuapp.presentation.mainscreen.discounts.view.DiscountsScreen
import com.cucu.cucuapp.presentation.mainscreen.home.view.NavDrawer
import com.cucu.cucuapp.presentation.mainscreen.home.view.ProductsList
import com.cucu.cucuapp.presentation.mainscreen.home.view.TopBar
import com.cucu.cucuapp.presentation.mainscreen.profile.profile.view.ProfileScreenController
import com.cucu.cucuapp.presentation.mainscreen.profile.profile.view.TopBarProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainNavController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        modifier = Modifier.wrapContentWidth(),
        drawerState = drawerState,
        drawerContent = { NavDrawer(drawerState, mainNavController) }
    ) {
        MainContent(drawerState, mainNavController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(drawerState: DrawerState, mainNavController: NavHostController) {
    //Expandir topappbar al scrollear
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val navController = rememberNavController()

    val navBackStackEntryState = navController.currentBackStackEntryAsState()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = { BottomBar(navController, navBackStackEntryState) },
        topBar = { TopAppBarController(scrollBehavior, drawerState, navBackStackEntryState, mainNavController) }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Routes.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Routes.Home.route) { ProductsList(mainNavController) }
            composable(Routes.Discounts.route) { DiscountsScreen(mainNavController) }
            composable(Routes.Profile.route) { ProfileScreenController(mainNavController) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarController(
    scrollBehavior: TopAppBarScrollBehavior,
    drawerState: DrawerState,
    navBackStackEntryState: State<NavBackStackEntry?>,
    mainNavController: NavHostController
) {
    return when (navBackStackEntryState.value?.destination?.route){
        Routes.Profile.route -> TopBarProfile(scrollBehavior, drawerState, mainNavController)
        Routes.PurchaseDetail.route -> { }
        //discounts and home
        else -> TopBar(scrollBehavior = scrollBehavior, drawerState = drawerState)
    }
}


@Composable
fun BottomBar(navController: NavHostController, navBackStackEntryState: State<NavBackStackEntry?>) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    when (navBackStackEntryState.value?.destination?.route){
        Routes.Home.route -> { selectedItem = 0 }
        Routes.Discounts.route -> { selectedItem = 1 }
        Routes.Profile.route -> { selectedItem = 2 }
    }

    val items = listOf(Routes.Home.route, Routes.Discounts.route, Routes.Profile.route)

    Surface(shadowElevation = 3.dp) {
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        when (index) {
                            0 -> { Icon(Icons.Filled.Home, contentDescription = item) }
                            1 -> { Icon(painter = painterResource(id = R.drawable.sell) , contentDescription = item) }
                            2 -> { Icon(Icons.Filled.Person, contentDescription = item) }
                            else -> {}
                        }
                    },
                    label = { Text(text = item) },
                    selected = selectedItem == index,
                    onClick = {
                        when(index){
                            0 -> navController.popBackStack(item,false)
                            else -> navController.navigate(item)
                        }

                        selectedItem = index
                    }
                )
            }
        }
    }
}
