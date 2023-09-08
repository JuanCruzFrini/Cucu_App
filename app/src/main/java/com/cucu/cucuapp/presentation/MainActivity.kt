package com.cucu.cucuapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cucu.cucuapp.application.Routes
import com.cucu.cucuapp.application.parcelableTypeOf
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.data.models.Purchase
import com.cucu.cucuapp.presentation.detail.view.DetailScreen
import com.cucu.cucuapp.presentation.mainscreen.home.view.MainScreen
import com.cucu.cucuapp.presentation.navdrawer.NavDrawerDestinationsController
import com.cucu.cucuapp.presentation.navdrawer.categories.view.CategoryScreen
import com.cucu.cucuapp.presentation.navdrawer.purchases.view.PurchaseDetail
import com.cucu.cucuapp.presentation.notifications.view.NotificationScreen
import com.cucu.cucuapp.ui.theme.CucuAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val navDrawerRoutes = listOf(
        Routes.Cart.route,Routes.Favorites.route,
        Routes.Purchases.route,
        Routes.History.route,Routes.Categories.route)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CucuAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.Main.route){
                        composable(Routes.Main.route){ MainScreen(navController) }
                        composable(Routes.Notifications.route){ NotificationScreen(navController) }

                        composable(
                            route = Routes.ProductDetail.route,
                            arguments = listOf(navArgument("product"){ type = NavType.parcelableTypeOf<Product>()})
                        ){
                            val product = navController.previousBackStackEntry?.savedStateHandle?.get<Product>("product")
                            DetailScreen(navController, product)
                        }

                        composable(
                            route = Routes.PurchaseDetail.route,
                            arguments = listOf(navArgument("purchase"){ type = NavType.parcelableTypeOf<Purchase>() })
                        ){
                            val purchase = navController.previousBackStackEntry?.savedStateHandle?.get<Purchase>("purchase")
                            PurchaseDetail(purchase, navController)
                        }

                        composable(
                            route = Routes.Category.route,
                            arguments = listOf(navArgument("category"){ type = NavType.StringType })
                        ){
                            val category = navController.previousBackStackEntry?.savedStateHandle?.get<String>("category")
                            CategoryScreen(category, navController)
                        }

                        navDrawerRoutes.forEach {
                            composable(it){ NavDrawerDestinationsController(navController) }
                        }
                    }
                }
            }
        }
    }
}