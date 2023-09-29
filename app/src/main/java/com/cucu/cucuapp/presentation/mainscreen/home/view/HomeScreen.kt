package com.cucu.cucuapp.presentation.mainscreen.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cucu.cucuapp.R
import com.cucu.cucuapp.application.Routes
import com.cucu.cucuapp.application.firstCharToUpperCase
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.data.models.items.ItemMenu
import com.cucu.cucuapp.presentation.detail.view.calculateDiscountPercent
import com.cucu.cucuapp.presentation.detail.view.isInDiscount
import com.cucu.cucuapp.presentation.mainscreen.home.viewmodel.HomeViewModel
import com.cucu.cucuapp.ui.theme.Purple40
import com.cucu.cucuapp.ui.theme.Purple80
import kotlinx.coroutines.launch
import kotlin.math.roundToInt



@Composable
fun ProductsList(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    //viewModel.getPurchasesRefs()

    viewModel.getAllProducts()
    viewModel.productsList.observeAsState().value?.let { list ->
        LazyColumn{
            items(list, key = { it.id!! }){
                ProductItem(it, navController)
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, navController: NavHostController? = null) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clickable {
                navController?.currentBackStackEntry?.savedStateHandle?.set(
                    key = "product",
                    value = product
                )
                navController?.navigate(Routes.ProductDetail.createRoute(product))
            }
    ) {
        Row {
            AsyncImage(
                model = product.img,
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                error = painterResource(id = R.drawable.ic_launcher_foreground),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                modifier = Modifier.weight(1.5f)
            )
            Column(
                modifier = Modifier.weight(2f).padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.name?.firstCharToUpperCase()!!,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
                Text(
                    text = product.description?.firstCharToUpperCase()!!,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth().weight(2f),
                    fontWeight = FontWeight.Light
                )
                Row(
                    Modifier.fillMaxWidth().weight(1f),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = "$" + product.newPrice?.roundToInt().toString(),
                        fontSize = 28.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (isInDiscount(product)) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${calculateDiscountPercent(product)}% OFF",
                            color = Color.Green,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawer(drawerState: DrawerState, mainNavController: NavHostController) {
    val scope = rememberCoroutineScope()
    // icons to mimic drawer destinations
    val drawerItems = listOf(
        ItemMenu("Carrito", Icons.Filled.ShoppingCart, Routes.Cart.route),
        ItemMenu("Favoritos", Icons.Filled.Favorite, Routes.Favorites.route),
        ItemMenu("Compras", Icons.Filled.List, Routes.Purchases.route),
        ItemMenu("Historial", Icons.Filled.Refresh, Routes.History.route),
        ItemMenu("Categorias", Icons.Filled.List, Routes.Categories.route)
    )
    val selectedDrawerItem = remember { mutableStateOf(drawerItems[0]) }

    ModalDrawerSheet(drawerContentColor = Purple80) {
        Column(Modifier.width(300.dp)) {
            Image(
                painter = painterResource(id = R.drawable.cucu_logo),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .background(Purple80))
            Spacer(Modifier.height(12.dp))
            drawerItems.forEach { item ->
                NavigationDrawerItem(
                    icon = { Icon(item.icon, contentDescription = null) },
                    label = { Text(item.title) },
                    selected = item == selectedDrawerItem.value,
                    onClick = {
                        scope.launch { drawerState.close() }
                        selectedDrawerItem.value = item
                        mainNavController.navigate(item.route)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = "Version 1.0.0",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    Surface(shadowElevation = 3.dp) {
        TopAppBar(
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                titleContentColor = Purple80,
                navigationIconContentColor = Purple80,
                scrolledContainerColor = Purple40,
            ),
            scrollBehavior = scrollBehavior,
            title = {
                Text(
                    text = "Cucu app",
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    //enlazar la top bar al nav drawer
                    scope.launch { drawerState.open() }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Localized description"
                    )
                }
            },
        )
    }
}
