package com.cucu.cucuapp.presentation.detail.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cucu.cucuapp.R
import com.cucu.cucuapp.application.Routes
import com.cucu.cucuapp.application.textWithLineThrough
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.data.models.cart.CartProduct
import com.cucu.cucuapp.data.models.purchase.Purchase
import com.cucu.cucuapp.presentation.detail.viewmodel.DetailViewModel
import com.cucu.cucuapp.presentation.mainscreen.profile.profile.view.SubtitleText
import com.cucu.cucuapp.presentation.navdrawer.TopBarNavigateBack
import com.cucu.cucuapp.ui.theme.Purple40
import com.cucu.cucuapp.ui.theme.PurpleGrey80
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(mainNavController: NavHostController, product:Product?) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopBarNavigateBack(mainNavController) }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            DetailContent(product = product, mainNavController = mainNavController){
                scope.launch {
                    snackbarHostState.showSnackbar(it)
                }
            }
        }
    }
}

@Composable
fun DetailContent(
    product: Product?,
    viewModel: DetailViewModel = hiltViewModel(),
    mainNavController: NavHostController,
    showSnackBar:(String) -> Unit
) {
    viewModel.authListener()
    product?.let { viewModel.increaseSeenTimes(it) }
    product?.id?.let { id -> viewModel.saveInUserHistory(id) }

    var detailStock by rememberSaveable { mutableStateOf(product?.stock) }

    var showCartDialog by remember { mutableStateOf(false) }
    var showPurchaseDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    DialogSelectQuantity(
        show = showCartDialog,
        stock = product?.stock,
        onDismiss = { showCartDialog = !showCartDialog },
        onContinue = { selectedQuantity ->
            product?.id?.let { id ->
                viewModel.addToCart(id, selectedQuantity)
                showPurchaseDialog = !showPurchaseDialog
                mainNavController.navigate(Routes.Cart.route)
            }
        }
        )

    DialogSelectQuantity(
        show = showPurchaseDialog,
        stock = product?.stock,
        onDismiss = { showPurchaseDialog = !showPurchaseDialog },
        onContinue = { selectedQuantity ->
            product?.let {
                val purchase = Purchase(
                    products = listOf(
                        CartProduct(
                            productId = it.id,
                            product = Product(it.id, it.name, it.newPrice, it.oldPrice, it.stock, it.img, it.description, it.code),
                            quantity = selectedQuantity)
                    )
                )
                viewModel.createPurchase(purchase)
                detailStock = detailStock?.minus(selectedQuantity)
                showPurchaseDialog = !showPurchaseDialog
            }
        }
    )
    viewModel.purchaseId.observeAsState().value?.let { id ->
        mainNavController.currentBackStackEntry?.savedStateHandle?.set(
            key = "purchaseId",
            value = id
        )
        mainNavController.navigate(Routes.PurchaseDetail.createRoute(id))
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {
        Text(text = product?.name.toString(), Modifier.fillMaxWidth().padding(16.dp))
        AsyncImage(
            model = product?.img,
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            error = painterResource(id = R.drawable.ic_launcher_foreground),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            modifier = Modifier.fillMaxWidth().height(300.dp)
        )

        PriceBlock(product)

        Column(Modifier.padding(16.dp)) {

            Text(
                modifier = Modifier.padding(vertical =  8.dp).fillMaxWidth(),
                text = "Stock disponible: " + detailStock.toString() + " unidades" ,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                onClick = {
                    viewModel.user.value?.let {
                    if (product?.stock != 0){
                        showPurchaseDialog = true
                    } else {
                        //showSnackBar("Producto no disponible")
                        Toast.makeText(context, "Producto no disponible", Toast.LENGTH_SHORT).show()
                    }
                    } ?: Toast.makeText(context, "Debes registrarte primero", Toast.LENGTH_SHORT).show()
                }) {
                Text(text = "Comprar ahora")
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleGrey80,
                    contentColor = Purple40
                ),
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                onClick = {
                    viewModel.user.value?.let {
                        if (product?.stock != 0){
                            showCartDialog = true
                        } else {
                            Toast.makeText(context, "Producto no disponible", Toast.LENGTH_SHORT).show()
                        }
                    } ?: Toast.makeText(context, "Debes registrarte primero", Toast.LENGTH_SHORT).show()
                    }) {
                Text(text = "Agregar al carrito")
            }
            SubtitleText(text = "Descripción")
            Text(text = product?.description.toString(), Modifier.fillMaxWidth(), color = Color.Gray)
        }
    }
}

@Composable
fun PriceBlock(product: Product?) {
    if (isInDiscount(product)){
        Text(
            modifier = Modifier.padding(start = 16.dp,top = 8.dp),
            text = textWithLineThrough("$${product?.oldPrice?.roundToInt()}"),
            fontSize = 16.sp,
            color = Color.Gray
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$" + product?.newPrice?.roundToInt().toString(),
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = "${calculateDiscountPercent(product)}% OFF",
                fontSize = 24.sp,
                color = Color.Green
            )
            Spacer(modifier = Modifier.weight(1f))
            CheckIfIsFavorite(productId = product?.id)
        }
    } else {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$" + product?.newPrice?.roundToInt().toString(),
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            CheckIfIsFavorite(productId = product?.id)
        }
    }
}

fun isInDiscount(product: Product?): Boolean {
    val isInDiscount = false
    product?.let {
        return product.newPrice!! < product.oldPrice!!
    }
    return isInDiscount
}

@Composable
fun CheckIfIsFavorite(
    viewModel: DetailViewModel = hiltViewModel(),
    productId: String?
) {
    val context = LocalContext.current.applicationContext

    productId?.let { id ->
        viewModel.checkIfExistInFavList(id)
        viewModel.existInFavList.observeAsState().value?.let { result ->

            var exist by rememberSaveable { mutableStateOf(result) }

            IconButton(onClick = {
                if (viewModel.user.value != null){
                    viewModel.setFav(id)
                    exist = !exist
                } else {
                    Toast.makeText(context, "Debes registrarte primero", Toast.LENGTH_SHORT).show()
                }
            }) {
                Icon(
                    imageVector =
                    if (!exist) Icons.Outlined.FavoriteBorder
                    else Icons.Filled.Favorite,
                    contentDescription = null
                )
            }
        }
    }
}

fun calculateDiscountPercent(product: Product?): String {
    val result = 0
    product?.let {
        val diff = product.oldPrice?.roundToInt()!! - product.newPrice?.roundToInt()!!
        val percent = (diff / product.oldPrice) * 100
        return percent.toInt().toString()
    }
    return result.toString()
}

@Composable
fun DialogSelectQuantity(
    show: Boolean,
    stock: Int?,
    onDismiss:()-> Unit,
    onContinue:(Int) -> Unit
){
    var selectedQuantity by remember { mutableIntStateOf(1) }
    var stockAvailable by remember { mutableStateOf(stock?.minus(selectedQuantity)) }

    if (show){
        Dialog(onDismissRequest = { onDismiss() }) {
            Card(
                colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray
            )) {
                Column(
                    Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "¿Cuantas unidades desea agregar?", textAlign = TextAlign.Center)
                    Text(text = "(${stockAvailable} unidades disponibles)")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "$selectedQuantity", Modifier.background(Color.Transparent), fontSize = 24.sp)

                    Row {
                        IconButton(
                            onClick = {
                                stock?.let {
                                    if (stockAvailable in 0 until it && selectedQuantity > 1) {
                                        selectedQuantity = selectedQuantity.minus(1)
                                        stockAvailable = it.minus(selectedQuantity)
                                    }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = null, tint = Color.Red)
                        }
                        IconButton(
                            onClick = {
                                stock?.let {
                                    if (stockAvailable != 0) {
                                        selectedQuantity = selectedQuantity.plus(1)
                                        stockAvailable = it.minus(selectedQuantity)
                                    }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null, tint = Color.Green)
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PurpleGrey80,
                                contentColor = Purple40
                            ),
                            onClick = { onDismiss() }) {
                            Text(text = "Cancelar")
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Purple40,
                                contentColor = Color.White
                            ),
                            onClick = { onContinue(selectedQuantity) }) {
                            Text(text = "Continuar")
                        }
                    }
                }
            }
        }
    }
}





















