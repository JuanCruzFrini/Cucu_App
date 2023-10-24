package com.cucu.cucuapp.presentation.navdrawer.cart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cucu.cucuapp.application.Routes
import com.cucu.cucuapp.data.models.cart.Cart
import com.cucu.cucuapp.data.models.cart.CartProduct
import com.cucu.cucuapp.data.models.purchase.Purchase
import com.cucu.cucuapp.presentation.navdrawer.cart.viewmodel.CartViewModel
import com.cucu.cucuapp.presentation.navdrawer.purchases.view.DialogCancelPurchase
import com.cucu.cucuapp.ui.theme.Purple40
import com.cucu.cucuapp.ui.theme.Purple80
import com.cucu.cucuapp.ui.theme.PurpleGrey80
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    mainNavController: NavHostController,
    viewModel: CartViewModel = hiltViewModel()
) {
    var showCancelDialog by remember { mutableStateOf(false) }
    val cartVal = viewModel.cart.observeAsState().value
    var productList by rememberSaveable { mutableStateOf(cartVal?.products) }

    Scaffold(
        topBar = {
            TopBarCart(mainNavController, productList?.size ?: 0) { showCancelDialog = it } }
    ) { paddingValues ->

        viewModel.getCart()
        viewModel.cart.observeAsState().value?.let { cart ->

            productList = cart.products

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val (list, footer) = createRefs()

                DialogCancelPurchase(
                    show = showCancelDialog,
                    onDismiss = { showCancelDialog = !showCancelDialog },
                    onContinue = {
                        viewModel.clearCart()
                        productList = emptyList()
                        viewModel.getCart()
                        showCancelDialog = !showCancelDialog
                    }
                )

                LazyColumn(
                    modifier = Modifier
                        .constrainAs(list) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(8.dp, 8.dp, 8.dp, 110.dp)
                ) {
                    cart.products?.let { products ->
                        items(products, key = { it.product.id!! }) { cartItem ->
                            CartProductItem(
                                cartItem,
                                removeProduct = { id ->
                                    viewModel.removeProduct(id)
                                    viewModel.getCart()
                                },
                            )
                        }
                    }
                }

                CartFooter(
                    cart,
                    modifier = Modifier.constrainAs(footer) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    navigateBack = { mainNavController.popBackStack() },
                    buyCart = { cart ->
                        val products = mutableListOf<CartProduct>()

                        cart.products?.forEach {
                            products.add(
                                CartProduct(
                                    documentId = it.documentId,
                                    productId = it.productId,
                                    product = it.product,
                                    quantity = it.quantity
                                )
                            )
                        }

                        val purchase = Purchase(products = cart.products)
                        viewModel.buyCart(purchase)
                        mainNavController.navigate(Routes.Purchases.route)
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCart(
    mainNavController: NavHostController,
    cartSize: Int,
    onShowDialog:(Boolean) -> Unit
) {
    Surface(shadowElevation = 3.dp){
        TopAppBar(
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                navigationIconContentColor = Purple80,
                titleContentColor = Purple80,
                scrolledContainerColor = Purple40,
            ),
            title = { Text(text = "Cart")},
            navigationIcon = {
                IconButton(onClick = { mainNavController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                if (cartSize != 0) {
                    IconButton(onClick = {
                        onShowDialog(true)
                    }) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "clear cart",
                            tint = Purple80
                        )
                    }
               }
            }
        )
    }
}

@Composable
fun CartFooter(cart: Cart?, modifier: Modifier, navigateBack:() -> Unit, buyCart:(Cart) -> Unit) {
    Column(
        modifier
            .shadow(3.dp, RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
            .background(Purple40, RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))) {
        Row(
            modifier = Modifier
                .shadow(5.dp, RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
                .background(Purple40, RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
                .padding(16.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "${cart?.getQuantity()} unidades", fontSize = 24.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Total: $${cart?.getAmount()}", fontSize = 24.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
                .background(Purple80, RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
                .padding(16.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleGrey80,
                    contentColor = Purple40
                ),
                onClick = { navigateBack() }
            ) {
                Text(text = "Volver", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                enabled = cart?.products?.isNotEmpty() == true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Color.White
                ),
                onClick = { buyCart(cart!!) }
            ) {
                Text(text = "Comprar", fontSize = 24.sp)
            }
        }

    }
}

@Composable
fun CartProductItem(
    cartProduct: CartProduct,
    removeProduct:(String) -> Unit,
    //editQuantity:(Int) -> Unit,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val density = LocalDensity.current

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
            model = cartProduct.product.img,
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
        Column(modifier = Modifier
            .weight(1.75f)
            .padding(4.dp)) {
            Text(text = "${cartProduct.product.name}", Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = "Cantidad: ${cartProduct.quantity}", Modifier.weight(1f))
            Text(text = "Precio unitario:\n$${cartProduct.product.newPrice!!.roundToInt()}", Modifier.weight(1f))
        }

        Text(
            modifier = Modifier
                .weight(1.25f)
                .padding(4.dp),
            text = "Total:\n$${cartProduct.product.newPrice!!.roundToInt() * cartProduct.quantity!!.toInt()}",
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
        IconButton(
            modifier = Modifier
                .weight(0.25f)
                .onSizeChanged {
                    itemHeight = with(density) { it.height.toDp() }
                },
            onClick = { isMenuExpanded = true }
        ) {
            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "")
        }
        DropdownMenu(
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight,
                x = screenWidth
            ),
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            content = {
                DropdownMenuItem(
                    text = { Text(text = "Remover") },
                    onClick = {
                        removeProduct(cartProduct.documentId!!)
                        isMenuExpanded = !isMenuExpanded
                    })
               /* DropdownMenuItem(
                    text = { Text(text = "Cambiar cantidad") },
                    onClick = {
                        editQuantity(cartProduct.documentId!!*//*, showDialog()*//*)
                        isMenuExpanded = !isMenuExpanded
                    })*/
            }
        )

    }
}
