package com.cucu.cucuapp.presentation.navdrawer.purchases.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cucu.cucuapp.application.Routes
import com.cucu.cucuapp.data.models.CartProduct
import com.cucu.cucuapp.data.models.Purchase
import com.cucu.cucuapp.presentation.navdrawer.TopBarNavigateBack
import com.cucu.cucuapp.presentation.navdrawer.purchases.viewmodel.PurchasesViewModel
import com.cucu.cucuapp.ui.theme.Purple40
import com.cucu.cucuapp.ui.theme.Purple80
import kotlin.math.roundToInt

@Composable
fun PurchasesScreen(mainNavController: NavHostController) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)) {
        PurchasesList(mainNavController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseDetail(purchase: Purchase?, mainNavController: NavHostController) {

    Scaffold(
        topBar = { TopBarNavigateBack(mainNavController = mainNavController) }
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val (list,footer) = createRefs()

            LazyColumn(
                modifier =
                Modifier
                    .constrainAs(list) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(0.dp, 0.dp, 0.dp, 140.dp)
            ) {
                purchase?.products?.let { products ->
                    items(products, key = { it.product.id!! }) { purchaseItem ->
                        PurchaseDetailItem(purchaseItem)
                    }
                }
            }

            PurchaseFooter(
                purchase,
                modifier = Modifier.constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
        }
    }

@Composable
fun PurchaseFooter(purchase: Purchase?, modifier: Modifier) {
    Column(
        modifier
            .shadow(3.dp, RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
            .background(Purple40, RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))) {
        Text(
            text = "Estado: ${purchase?.state?.description}",
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .shadow(5.dp, RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
                .background(Purple80, RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
                .padding(32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "${purchase?.getQuantity()} unidades", fontSize = 24.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Total: $${purchase?.getAmount()}", fontSize = 24.sp)
        }
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
fun PurchaseDetailItem(cart: CartProduct) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(4.dp)) {
        AsyncImage(
            modifier = Modifier
                .size(150.dp, 150.dp)
                .weight(1.5f)
                .clip(RoundedCornerShape(10.dp)),
            model = cart.product.img,
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
        Column(modifier = Modifier
            .weight(2f)
            .padding(4.dp)) {
            Text(text = "${cart.product.name}", Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = "Cantidad: ${cart.quantity}", Modifier.weight(1f))
            Text(text = "Precio unitario:\n$${cart.product.newPrice!!.roundToInt()}", Modifier.weight(1f))
        }
        Text(
            modifier = Modifier
                .weight(1.25f)
                .padding(4.dp),
            text = "Total:\n$${cart.product.newPrice!!.roundToInt() * cart.quantity!!.toInt()}",
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@Composable
fun PurchaseItem(purchase: Purchase, navController: NavHostController) {

    var paddingStart = 0.dp
    var listToDraw: List<CartProduct>
    var drawNumber: Boolean

    purchase.products.let { productList ->
        when {
            productList!!.size >= 3 -> {
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
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "purchase",
                        value = purchase
                    )
                    navController.navigate(Routes.PurchaseDetail.createRoute(purchase))
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
                        text = purchase.state?.description!!,
                        fontSize = 20.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

            }
        }
    }
}
