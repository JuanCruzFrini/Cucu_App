package com.cucu.cucuapp.presentation.navdrawer.purchases.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cucu.cucuapp.application.Routes
import com.cucu.cucuapp.data.models.cart.CartProduct
import com.cucu.cucuapp.data.models.purchase.Purchase
import com.cucu.cucuapp.presentation.navdrawer.purchases.viewmodel.PurchasesViewModel

@Composable
fun PurchasesScreen(mainNavController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)) {
        PurchasesList(mainNavController)
    }
}

@Composable
fun PurchasesList(
    navController: NavHostController,
    viewModel: PurchasesViewModel = hiltViewModel()
) {

    viewModel.getPurchases()
    viewModel.purchases.observeAsState().value?.let { list ->
        LazyColumn {
            items(list, key = { it.id!! }){
                PurchaseItem(it, navController)
            }
        }
    }
}

@Composable
fun PurchaseItem(purchase: Purchase, navController: NavHostController) {

    var paddingStart = 0.dp
    var listToDraw: List<CartProduct>
    var drawNumber: Boolean

    purchase.products.let { productList ->
        when {
            productList!!.size > 3 -> {
                listToDraw = productList.subList(0,3)
                drawNumber = true
            }

            else -> {
                listToDraw = productList
                drawNumber = false
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    purchase.id?.let { id ->
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "purchaseId",
                            value = id
                        )
                        navController.navigate(Routes.PurchaseDetail.createRoute(id))
                    }
                },
            elevation = CardDefaults.cardElevation(3.dp),
        ) {
            Row(Modifier.padding(8.dp)) {
                Box(Modifier.weight(1.5f)) {
                    listToDraw.forEach { cart ->

                        AsyncImage(
                            modifier = Modifier
                                .size(150.dp, 150.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .padding(paddingStart, 0.dp, 0.dp, 0.dp),
                            model = cart.product.img,
                            contentScale = ContentScale.FillBounds,
                            contentDescription = null
                        )

                        paddingStart += 30.dp
                    }

                    if (drawNumber) {
                        Text(
                            text = "${productList.size - listToDraw.size}+",
                            fontSize = 24.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .border(2.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                                .background(Color.White, RoundedCornerShape(10.dp))
                                .padding(4.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(8.dp, 0.dp, 0.dp, 0.dp)
                        .weight(2f),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "$${purchase.getAmount()}",
                        fontSize = 28.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${productList.size} productos",
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = purchase.state!!,
                        fontSize = 20.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

            }
        }
    }
}
