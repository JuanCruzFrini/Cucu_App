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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.cucu.cucuapp.data.models.Cart
import com.cucu.cucuapp.data.models.CartProduct
import com.cucu.cucuapp.presentation.navdrawer.cart.viewmodel.CartViewModel
import com.cucu.cucuapp.ui.theme.Purple40
import com.cucu.cucuapp.ui.theme.Purple80
import com.cucu.cucuapp.ui.theme.PurpleGrey80
import kotlin.math.roundToInt

@Composable
fun CartScreen(
    mainNavController: NavHostController,
    viewModel: CartViewModel = hiltViewModel()
) {
    viewModel.getCart()
    viewModel.cart.observeAsState().value?.let { cart ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (list,footer) = createRefs()

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
                        CartProductItem(cartItem) { id -> viewModel.removeProduct(id) }
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
                { viewModel.cancelCart() },
                { cart -> viewModel.buyCart(cart) },
            )
        }
    }
}

@Composable
fun CartFooter(cart: Cart?, modifier: Modifier, cancelCart:() -> Unit, buyCart:(Cart) -> Unit) {
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
                onClick = { cancelCart() } // fixme
            ) {
                Text(text = "Cancelar", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Color.White
                ),
                onClick = { buyCart(cart!!) } // fixme
            ) {
                Text(text = "Comprar", fontSize = 24.sp)
            }
        }

    }
}

@Composable
fun CartProductItem(cart: CartProduct, removeProduct:(Int) -> Unit) {
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
            model = cart.product.img,
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
        Column(modifier = Modifier
            .weight(1.75f)
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
                        // fixme
                        removeProduct(cart.product.id!!)
                        isMenuExpanded = !isMenuExpanded
                    })
            }
        )

    }
}
